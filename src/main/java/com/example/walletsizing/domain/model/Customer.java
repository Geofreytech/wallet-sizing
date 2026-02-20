package com.example.walletsizing.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cif;

    @Column(nullable = false)
    private String name;

    @Column(name = "kra_pin", unique = true)
    private String kraPin;

    private String industry;

    private String segment;

    // Only one declaration needed!
    @Column(name = "assigned_rm")
    private String assignedRm;

    // Note: @Data (Lombok) automatically creates getAssignedRm and setAssignedRm for you!

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WalletSizing> walletSizings;

    @OneToMany(mappedBy = "customerId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FinancialData> financialData;
}