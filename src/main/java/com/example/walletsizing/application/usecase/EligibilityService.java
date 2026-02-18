package com.example.walletsizing.application.usecase;


import com.example.walletsizing.domain.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class EligibilityService {

    public boolean isEligible(Customer customer) {
        // Business Rule: Only Corporate/SME segments are eligible
        if (customer.getSegment() == null) return false;

        String segment = customer.getSegment().toUpperCase();
        return segment.equals("LARGE_ENTERPRISE") || segment.equals("SME");
    }
}