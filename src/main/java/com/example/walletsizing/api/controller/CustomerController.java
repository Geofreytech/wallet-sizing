package com.example.walletsizing.api.controller;

import com.example.walletsizing.application.usecase.CustomerService;
import com.example.walletsizing.application.usecase.EligibilityService;
import com.example.walletsizing.application.usecase.WalletSizingService;
import com.example.walletsizing.application.usecase.dto.CustomerSearchResponse;
import com.example.walletsizing.application.usecase.dto.WalletSizingResponse;
import com.example.walletsizing.application.usecase.dto.WalletCalculationRequest;
import com.example.walletsizing.domain.model.Customer;
import com.example.walletsizing.domain.model.CustomerRepository;
import com.example.walletsizing.domain.model.FinancialData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final EligibilityService eligibilityService;
    private final WalletSizingService walletSizingService;
    private final CustomerRepository customerRepository;

    public CustomerController(
            CustomerService customerService,
            WalletSizingService walletSizingService,
            EligibilityService eligibilityService,
            CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.walletSizingService = walletSizingService;
        this.eligibilityService = eligibilityService;
        this.customerRepository = customerRepository;
    }

    // 1. Unified Search: Search by name or CIF
    @GetMapping("/search")
    public ResponseEntity<List<CustomerSearchResponse>> search(@RequestParam String query) {
        return ResponseEntity.ok(customerService.search(query));
    }

    // 2. Get Single Customer Details
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Eligibility Check
    @GetMapping("/{id}/eligibility")
    public ResponseEntity<Map<String, Object>> checkEligibility(@PathVariable Long id) {
        boolean isEligible = customerService.isEligibleForSizing(id);
        return ResponseEntity.ok(Map.of(
                "customerId", id,
                "isEligible", isEligible,
                "status", isEligible ? "Eligible" : "Ineligible",
                "reason", isEligible ? "Criteria met" : "Missing required financial data (Turnover/Industry)"
        ));
    }

    // 4. Wallet Sizing Calculation
    @PostMapping("/{id}/calculate")
    public ResponseEntity<WalletSizingResponse> calculate(
            @PathVariable Long id,
            @RequestBody WalletCalculationRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));

        WalletSizingResponse response = walletSizingService.calculateDetailedSizing(
                customer,
                request.getExternalDebt()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}/loans")
    public ResponseEntity<List<FinancialData>> getLoans(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getFinancialsByCategory(customerId, "LOAN_BALANCE"));
    }

    @GetMapping("/{customerId}/deposits")
    public ResponseEntity<List<FinancialData>> getDeposits(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getFinancialsByCategory(customerId, "DEPOSIT_BALANCE"));
    }

    @GetMapping("/{customerId}/im-products")
    public ResponseEntity<List<FinancialData>> getInvestmentProducts(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getFinancialsByCategory(customerId, "IM_PRODUCT"));
    }

    @GetMapping("/{customerId}/im-capital")
    public ResponseEntity<List<FinancialData>> getCapitalProducts(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getFinancialsByCategory(customerId, "IM_CAPITAL"));
    }

    @GetMapping("/{customerId}/external-facilities")
    public ResponseEntity<List<FinancialData>> getExternalFacilities(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getFinancialsByCategory(customerId, "EXTERNAL"));
    }
}