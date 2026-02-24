package com.example.walletsizing.api.controller;

import com.example.walletsizing.application.usecase.WalletSizingService;
import com.example.walletsizing.application.usecase.dto.WalletCalculationRequest;
import com.example.walletsizing.application.usecase.dto.WalletSizingResponse;
import com.example.walletsizing.domain.model.Customer;
import com.example.walletsizing.domain.model.WalletSizing;
import com.example.walletsizing.domain.model.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/api/v1")
public class WalletSizingController {

    private final WalletSizingService walletSizingService;

    public WalletSizingController(WalletSizingService walletSizingService) {
        this.walletSizingService = walletSizingService;
    }

    // GET /api/v1/customers/2/wallet-sizings (History)
    @GetMapping("/customers/{customerId}/wallet-sizings")
    public ResponseEntity<List<WalletSizing>> getHistory(@PathVariable Long customerId) {
        return ResponseEntity.ok(walletSizingService.getHistoryByCustomer(customerId));
    }

    // POST /api/v1/customers/2/wallet-sizings (Create New)
    @PostMapping("/customers/{customerId}/wallet-sizings")
    public ResponseEntity<WalletSizing> createSizing(@PathVariable Long customerId, @RequestBody WalletCalculationRequest request) {
        return ResponseEntity.ok(walletSizingService.saveNewSizing(customerId, request));
    }

    // GET /api/v1/wallet-sizings/5 (View specific draft)
    @GetMapping("/wallet-sizings/{id}")
    public ResponseEntity<WalletSizing> getById(@PathVariable Long id) {
        return ResponseEntity.ok(walletSizingService.getById(id));
    }

    // POST /api/v1/wallet-sizings/5/submit (Finalize and Lock)
    @PostMapping("/wallet-sizings/{id}/submit")
    public ResponseEntity<Void> submitSizing(@PathVariable Long id) {
        walletSizingService.submit(id);
        return ResponseEntity.accepted().build();
    }
}