package com.example.capstone.controller;

import com.example.capstone.dto.InvestmentRequestDTO;
import com.example.capstone.dto.InvestmentResultDTO;
import com.example.capstone.service.InvestmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
public class InvestmentController {

    private final InvestmentService investmentService;

    @Autowired
    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping(value = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<InvestmentResultDTO>> calculateReturns(@RequestBody List<InvestmentRequestDTO> investments) {
        investments.forEach(investment -> log.debug("Received Investment: {}", investment));
        return investmentService.calculateInvestmentReturns(investments);
    }

}

