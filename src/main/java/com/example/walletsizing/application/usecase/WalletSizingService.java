package com.example.walletsizing.application.usecase;

import com.example.walletsizing.domain.model.Customer;
import com.example.walletsizing.domain.model.FinancialData;
import com.example.walletsizing.application.usecase.dto.WalletSizingResponse;
import com.example.walletsizing.domain.model.WalletSizing;
import com.example.walletsizing.domain.model.WalletSizingRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletSizingService {

    private final WalletSizingRepository walletSizingRepository;

    public WalletSizingService(WalletSizingRepository walletSizingRepository) {
        this.walletSizingRepository = walletSizingRepository;
    }

    public List<WalletSizing> getCustomerHistory(Long customerId) {
        // Basic history fetch - in a later step we can add a security check here too
        return walletSizingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public WalletSizingResponse calculateDetailedSizing(Customer customer, BigDecimal externalDebt) {

        // 1. JWT Security: Get the username from the authenticated token
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("=== SECURITY DEBUG START ===");
        System.out.println("TOKEN USERNAME: [" + currentUsername + "]");
        System.out.println("DB ASSIGNED RM: [" + customer.getAssignedRm() + "]");
        System.out.println("=== SECURITY DEBUG END ===");
        System.out.println("LOGGED IN: [" + currentUsername + "]");
        System.out.println("DB OWNER: [" + customer.getAssignedRm() + "]");


        // 2. RBAC Check (Section 6.2): Ensure the RM owns this customer
        if (customer.getAssignedRm() == null || !customer.getAssignedRm().equals(currentUsername)) {
            throw new AccessDeniedException("Security Alert: Customer " + customer.getCif() + " is assigned to another RM.");
        }

        // 3. Calculation Logic
        List<FinancialData> internalData = customer.getFinancialData();
        BigDecimal internalTotal = internalData.stream()
                .filter(data -> "LOAN_BALANCE".equals(data.getCategory()))
                .map(FinancialData::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMarket = internalTotal.add(externalDebt);
        BigDecimal share = WalletSizingResponse.calculateShare(internalTotal, totalMarket);
        BigDecimal gap = totalMarket.subtract(internalTotal);

        String recommendation = (share.compareTo(new BigDecimal("30")) < 0)
                ? "High Opportunity: Increase limit"
                : "Maintain: Dominant share";

        // 4. Persistence with Audit Trail (Section 6.1)
        WalletSizing sizing = new WalletSizing();
        sizing.setCustomer(customer);
        sizing.setExternalDebt(externalDebt);
        sizing.setInternalDebt(internalTotal);
        sizing.setTotalMarketDebt(totalMarket);
        sizing.setWalletSharePercentage(share);
        sizing.setStatus("SUBMITTED");
        sizing.setFinancialPeriod("FY2026");

        // Plotted currentUsername instead of SYSTEM_RM
        sizing.setCreatedBy(currentUsername);
        sizing.setCreatedAt(LocalDateTime.now());

        walletSizingRepository.save(sizing);

        // 5. Build Response (Matches your 6-argument Record)
        return new WalletSizingResponse(
                internalTotal,
                externalDebt,
                totalMarket,
                share,
                gap,
                recommendation
        );
    }
}