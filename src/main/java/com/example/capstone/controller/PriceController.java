package com.example.capstone.controller;

import com.example.capstone.dto.StockPriceDTO;
import com.example.capstone.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class PriceController {

    private final PriceService stockPriceService;

    @Autowired
    public PriceController(PriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    @GetMapping("/stock-price")
    public Mono<List<StockPriceDTO>> getStockPrice(
            @RequestParam String stockCode,
            @RequestParam String periodType) {
        return stockPriceService.getStockPrice(stockCode, periodType);
    }
}