package com.example.walletsizing.application.usecase;

import com.example.walletsizing.application.usecase.dto.WalletCalculationRequest;
import com.example.walletsizing.application.usecase.dto.WalletSizingResponse;
import com.example.walletsizing.domain.model.Customer;
import com.example.walletsizing.domain.model.CustomerRepository;
import com.example.walletsizing.domain.model.WalletSizing;
import com.example.walletsizing.domain.model.WalletSizingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletSizingService {

    private final WalletSizingRepository walletSizingRepository;
    private final CustomerRepository customerRepository;
    private final WalletCalculationService calculationService;
    // Assuming you have a separate math service

    public WalletSizingService(WalletSizingRepository walletSizingRepository,
                               CustomerRepository customerRepository,
                               WalletCalculationService calculationService) {
        this.walletSizingRepository = walletSizingRepository;
        this.customerRepository = customerRepository;
        this.calculationService = calculationService;
    }

    // Fix for: GET /history
    public List<WalletSizing> getHistoryByCustomer(Long customerId) {
        return walletSizingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    // Fix for: GET by ID
    public WalletSizing getById(Long id) {
        return walletSizingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sizing record not found with ID: " + id));
    }

    @Transactional
    public WalletSizing saveNewSizing(Long customerId, WalletCalculationRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        WalletSizingResponse result = calculationService.calculateDetailedSizing(customer, request.getExternalDebt());

        WalletSizing entity = new WalletSizing();
        entity.setCustomer(customer);
        entity.setExternalDebt(request.getExternalDebt());

        // Use the getters from your Record to fill the Entity
        entity.setInternalDebt(result.internalBalance());
        entity.setTotalWallet(result.totalMarketSize()); // Maps result to Entity
        entity.setWalletShare(result.walletShare());     // Maps result to Entity

        entity.setStatus("DRAFT");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setFinancialPeriod("2025-2026"); // Or dynamic from request

        return walletSizingRepository.save(entity);
    }

    // Fix for: POST /submit
    @Transactional
    public void submit(Long id) {
        WalletSizing sizing = getById(id);

        if ("SUBMITTED".equals(sizing.getStatus())) {
            throw new IllegalStateException("This sizing has already been submitted and cannot be modified.");
        }

        sizing.setStatus("SUBMITTED");
        // sizing.setSubmittedAt(LocalDateTime.now()); // Ensure this field exists in your Entity
        walletSizingRepository.save(sizing);
    }
}