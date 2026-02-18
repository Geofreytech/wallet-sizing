package com.example.walletsizing.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "customers")
@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cif; // Customer Identification File

    @Column(nullable = false)
    private String name;

    @Column(name = "kra_pin", unique = true)
    private String kraPin;

    private String industry;

    private String segment; // e.g., SME, SACCO, LARGE_ENTERPRISE

    @OneToMany(mappedBy = "customerId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FinancialData> financialData;
}