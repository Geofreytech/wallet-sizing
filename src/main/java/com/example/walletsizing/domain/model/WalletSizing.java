package com.example.walletsizing.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_sizings")
@Data // This generates setExternalDebt() automatically
@NoArgsConstructor
@AllArgsConstructor
public class WalletSizing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String financialPeriod;

    private BigDecimal annualTurnover;

    // THE NEW FIELDS
    private BigDecimal internalDebt;
    private BigDecimal externalDebt; // This creates setExternalDebt()
    private BigDecimal totalMarketDebt;
    private BigDecimal walletSharePercentage;

    @Column(nullable = false)
    private String status;

    private String createdBy;
    private LocalDateTime createdAt;
}