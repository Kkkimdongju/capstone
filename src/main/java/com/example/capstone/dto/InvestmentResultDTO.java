package com.example.capstone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InvestmentResultDTO {
    private String stockName;
    private double profitLoss;
    private double profitLossPercentage;

    public InvestmentResultDTO() {
    }

    public InvestmentResultDTO(String stockName, double profitLoss, double profitLossPercentage) {
        this.stockName = stockName;
        this.profitLoss = profitLoss;
        this.profitLossPercentage = profitLossPercentage;
    }
}
