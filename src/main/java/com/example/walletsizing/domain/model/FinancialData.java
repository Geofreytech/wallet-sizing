package com.example.walletsizing.domain.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Entity
@Table(name = "financial_data")
@Data
public class FinancialData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    private String category; // e.g., LOAN_BALANCE, DEPOSIT_BALANCE

    private BigDecimal amount;

    private String currency; // Always 'KES' for our current scope
}


