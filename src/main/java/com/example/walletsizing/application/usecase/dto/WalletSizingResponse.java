package com.example.walletsizing.application.usecase.dto;


import java.math.BigDecimal;
import java.math.RoundingMode;

public record WalletSizingResponse(
        BigDecimal internalBalance,
        BigDecimal externalBalance,
        BigDecimal totalMarketSize,
        BigDecimal walletShare,
        BigDecimal opportunityGap,
        String recommendation
) {
    // Helper to calculate percentage without wildcards or complex utils
    public static BigDecimal calculateShare(BigDecimal internal, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;
        return internal.divide(total, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }
}