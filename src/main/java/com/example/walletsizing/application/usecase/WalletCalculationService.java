package com.example.walletsizing.application.usecase;

import com.example.walletsizing.application.usecase.dto.WalletSizingResponse;
import com.example.walletsizing.domain.model.Customer;
import com.example.walletsizing.domain.model.FinancialData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletCalculationService {

    public WalletSizingResponse calculateDetailedSizing(Customer customer, BigDecimal externalDebt) {

        // 1. Calculate Internal Balance (Sum of Loans)
        BigDecimal internalBalance = customer.getFinancialData().stream()
                .filter(fd -> fd.getCategory().contains("LOAN"))
                .map(FinancialData::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Calculate Total Market Size (Turnover * Multiplier, e.g., 30%)
        BigDecimal turnover = customer.getFinancialData().stream()
                .filter(fd -> fd.getCategory().contains("TURNOVER") || fd.getCategory().contains("REVENUE"))
                .map(FinancialData::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMarketSize = turnover.multiply(new BigDecimal("0.30"));

        // 3. Calculate Wallet Share using your Record's static helper
        BigDecimal walletShare = WalletSizingResponse.calculateShare(internalBalance, totalMarketSize);

        // 4. Calculate Opportunity Gap
        BigDecimal opportunityGap = totalMarketSize.subtract(internalBalance);

        // 5. Generate Recommendation
        String recommendation = (walletShare.compareTo(new BigDecimal("20")) < 0)
                ? "Aggressive Acquisition: High potential to buy out external debt."
                : "Defensive Play: Maintain relationship and cross-sell IM products.";

        // Return matching your Record exactly
        return new WalletSizingResponse(
                internalBalance,
                externalDebt,
                totalMarketSize,
                walletShare,
                opportunityGap,
                recommendation
        );
    }
}