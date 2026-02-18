package com.example.walletsizing.application.usecase;

import com.example.walletsizing.domain.model.Customer;
import com.example.walletsizing.infrastructure.persistence.JpaCustomerRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomerService {
    private final JpaCustomerRepository repository;

    public CustomerService(JpaCustomerRepository repository) {
        this.repository = repository;
    }

    public Optional<Customer> findByCif(String cif) {
        return repository.findByCif(cif);
    }
}
