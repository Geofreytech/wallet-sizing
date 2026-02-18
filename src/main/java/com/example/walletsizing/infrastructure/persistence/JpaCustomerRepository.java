package com.example.walletsizing.infrastructure.persistence;


import com.example.walletsizing.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaCustomerRepository extends JpaRepository<Customer, Long> {
    // SDD 7.2: Requirement for Customer Search
    Optional<Customer> findByCif(String cif);
}