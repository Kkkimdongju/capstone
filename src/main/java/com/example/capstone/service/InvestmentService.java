package com.example.capstone.service;

import com.example.capstone.dto.InvestmentRequestDTO;
import com.example.capstone.dto.InvestmentResultDTO;
import com.example.capstone.dto.StockPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestmentService {

    private final PriceService priceService;

    @Autowired
    public InvestmentService(PriceService priceService) {
        this.priceService = priceService;
    }

    // 수익률 계산 메소드
    public Mono<List<InvestmentResultDTO>> calculateInvestmentReturns(List<InvestmentRequestDTO> investments) {
        return Flux.fromIterable(investments)
                .flatMap(this::calculateReturnForInvestment)
                .collectList();
    }

    // 각 투자 정보를 받아 수익률을 계산
    private Mono<InvestmentResultDTO> calculateReturnForInvestment(InvestmentRequestDTO investment) {
        Mono<List<StockPriceDTO>> buyPriceMono = priceService.getStockPrice2(investment.getStockCode(), investment.getBuyDate());
        Mono<List<StockPriceDTO>> sellPriceMono = priceService.getStockPrice2(investment.getStockCode(), investment.getSellDate());

        return Mono.zip(buyPriceMono, sellPriceMono)
                .map(tuple -> {
                    List<StockPriceDTO> buyPrices = tuple.getT1();
                    List<StockPriceDTO> sellPrices = tuple.getT2();

                    double buyPrice = buyPrices.isEmpty() ? 0.0 : Double.parseDouble(buyPrices.get(0).getClosePrice());
                    double sellPrice = sellPrices.isEmpty() ? 0.0 : Double.parseDouble(sellPrices.get(0).getClosePrice());

                    double investmentAmount = buyPrice * investment.getBuyQuantity();
                    double currentAmount = sellPrice * investment.getSellQuantity();
                    double profitLoss = currentAmount - investmentAmount;
                    double profitLossPercentage = (profitLoss / investmentAmount) * 100;

                    InvestmentResultDTO result = new InvestmentResultDTO();
                    result.setStockName(investment.getStockName());
                    result.setProfitLoss(profitLoss);
                    result.setProfitLossPercentage(profitLossPercentage);

                    return result;
                });
    }
}
