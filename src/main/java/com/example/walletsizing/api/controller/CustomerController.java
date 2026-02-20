package com.example.walletsizing.api.controller;

import com.example.walletsizing.application.usecase.CustomerService;
import com.example.walletsizing.application.usecase.EligibilityService;
import com.example.walletsizing.application.usecase.WalletSizingService;
import com.example.walletsizing.application.usecase.dto.WalletSizingResponse;
import com.example.walletsizing.application.usecase.dto.WalletCalculationRequest;
import com.example.walletsizing.domain.model.Customer;
import com.example.walletsizing.domain.model.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final EligibilityService eligibilityService;
    private final WalletSizingService walletSizingService;
    private final CustomerRepository customerRepository;

    // Fixed Constructor: Now includes CustomerRepository in the parameters
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

    @GetMapping("/search")
    public ResponseEntity<?> searchByCif(@RequestParam String cif) {
        return customerService.findByCif(cif)
                .map(customer -> {
                    boolean eligible = eligibilityService.isEligible(customer);
                    return ResponseEntity.ok(new Object() {
                        public Object data = customer;
                        public boolean isEligible = eligible;
                        public String status = eligible ? "Eligible" : "Ineligible";
                    });
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint for Postman: POST http://localhost:8080/api/v1/customers/1/calculate
    @PostMapping("/{id}/calculate")
    public ResponseEntity<WalletSizingResponse> calculate(
            @PathVariable Long id,
            @RequestBody WalletCalculationRequest request) {

        // 1. Fetch the actual customer object from DB
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));

        // 2. Pass the object and the debt from the request body to the service
        WalletSizingResponse response = walletSizingService.calculateDetailedSizing(
                customer,
                request.getExternalDebt()
        );

        return ResponseEntity.ok(response);
    }

    // Older endpoint (optional: you can keep or delete this)
    @PostMapping("/{customerId}/wallet-sizings")
    public ResponseEntity<WalletSizingResponse> createSizing(
            @PathVariable Long customerId,
            @RequestBody BigDecimal externalDebt) {

        return customerService.findById(customerId)
                .map(customer -> ResponseEntity.ok(walletSizingService.calculateDetailedSizing(customer, externalDebt)))
                .orElse(ResponseEntity.notFound().build());
    }
}