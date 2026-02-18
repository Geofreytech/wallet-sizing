package com.example.walletsizing.domain.model;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

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