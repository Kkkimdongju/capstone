package com.example.capstone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockPriceDTO {
    private String date;
    private String closePrice;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String volume;
}
