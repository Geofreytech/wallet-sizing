package com.example.walletsizing.domain.model;


import com.example.walletsizing.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // findById is inherited automatically
}