package com.example.walletsizing.application.usecase.dto;

import java.math.BigDecimal;

public class WalletCalculationRequest {
    private BigDecimal externalDebt;

    // Default constructor
    public WalletCalculationRequest() {}

    // Getter and Setter
    public BigDecimal getExternalDebt() { return externalDebt; }
    public void setExternalDebt(BigDecimal externalDebt) { this.externalDebt = externalDebt; }
}