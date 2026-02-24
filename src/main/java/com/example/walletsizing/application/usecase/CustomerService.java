package com.example.walletsizing.application.usecase;

import com.example.walletsizing.application.usecase.dto.CustomerSearchResponse;
import com.example.walletsizing.domain.model.Customer;
import com.example.walletsizing.domain.model.CustomerRepository;
import com.example.walletsizing.domain.model.FinancialData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Optimized search using the Repository's JPQL query.
     * This avoids loading full Customer entities into memory.
     */
    public List<CustomerSearchResponse> search(String query) {
        return customerRepository.searchCustomers(query);
    }

    /**
     * Business Logic Gate: Ensures the customer is ready for a wallet sizing calculation.
     * Checks for:
     * 1. Existence of the customer
     * 2. Defined Industry (used for Sector multipliers)
     * 3. Existing Financial Data (Turnover/Revenue)
     */
    @Transactional(readOnly = true)
    public boolean isEligibleForSizing(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    // Rule 1: Must have an industry assigned (required for sector-specific logic)
                    boolean hasIndustry = customer.getIndustry() != null && !customer.getIndustry().trim().isEmpty();

                    // Rule 2: Must have at least one financial record with a positive amount
                    boolean hasFinancialData = customer.getFinancialData() != null &&
                            customer.getFinancialData().stream()
                                    .anyMatch(fd -> fd.getAmount() != null && fd.getAmount().doubleValue() > 0);

                    return hasIndustry && hasFinancialData;
                })
                .orElse(false);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByCif(String cif) {
        // Using the built-in findByCif from your Repository
        return customerRepository.findByCif(cif);
    }
    /**
     * Fetches specific financial components for a customer by category.
     */
    public List<FinancialData> getFinancialsByCategory(Long customerId, String category) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    List<FinancialData> allData = customer.getFinancialData();
                    System.out.println("DEBUG: Found " + allData.size() + " total financial records for customer " + customerId);


                    List<FinancialData> filtered = allData.stream()
                            .filter(fd -> fd.getCategory() != null &&
                                    fd.getCategory().toUpperCase().contains(category.toUpperCase()))
                            .collect(Collectors.toList());



                    return filtered;
                })
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}