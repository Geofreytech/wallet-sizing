package com.example.walletsizing.api.controller;

import com.example.walletsizing.application.usecase.CustomerService;
import com.example.walletsizing.application.usecase.EligibilityService;
import com.example.walletsizing.domain.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final EligibilityService eligibilityService; // 2. Add this field

    // 3. Update the constructor to include both
    public CustomerController(CustomerService customerService, EligibilityService eligibilityService) {
        this.customerService = customerService;
        this.eligibilityService = eligibilityService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByCif(@RequestParam String cif) {
        return customerService.findByCif(cif)
                .map(customer -> {
                    // Now this won't be red!
                    boolean eligible = eligibilityService.isEligible(customer);

                    return ResponseEntity.ok(new Object() {
                        public Object data = customer;
                        public boolean isEligible = eligible;
                        public String status = eligible ? "Eligible" : "Ineligible";
                    });
                })
                .orElse(ResponseEntity.notFound().build());
    }
}