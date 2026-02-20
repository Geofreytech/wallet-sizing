package com.example.walletsizing.api.controller;

import com.example.walletsizing.application.usecase.WalletSizingService;
import com.example.walletsizing.application.usecase.dto.WalletSizingResponse;
import com.example.walletsizing.domain.model.Customer;
import com.example.walletsizing.domain.model.WalletSizing;
import com.example.walletsizing.domain.model.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class WalletSizingController {

    private final WalletSizingService walletSizingService;
    private final CustomerRepository customerRepository;

    // The Constructor that injects your dependencies
    public WalletSizingController(WalletSizingService walletSizingService, CustomerRepository customerRepository) {
        this.walletSizingService = walletSizingService;
        this.customerRepository = customerRepository;
    }

    /**
     * POST: Calculate and Save a new Wallet Sizing
     * URL: http://localhost:8080/api/v1/customers/{customerId}/wallet-sizings
     */

    /**
     * GET: Retrieve the history of calculations for a customer
     * URL: http://localhost:8080/api/v1/customers/{customerId}/wallet-sizings
     */
    @GetMapping("/{customerId}/wallet-sizings")
    public ResponseEntity<List<WalletSizing>> getHistory(@PathVariable Long customerId) {
        List<WalletSizing> history = walletSizingService.getCustomerHistory(customerId);
        return ResponseEntity.ok(history);
    }
}