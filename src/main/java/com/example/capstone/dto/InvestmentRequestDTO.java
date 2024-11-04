package com.example.capstone.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class InvestmentRequestDTO {
    private String stockName;
    private String stockCode;
    private String buyDate;
    private int buyQuantity;
    private String sellDate;
    private int sellQuantity;
}
