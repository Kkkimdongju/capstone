package com.example.capstone.service;

import com.example.capstone.dto.StockPriceDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceService {

    @Value("${appkey}")
    private String appkey;

    @Value("${appsecret}")
    private String appSecret;

    @Value("${access_token}")
    private String accessToken;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public PriceService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://openapi.koreainvestment.com:9443").build();
        this.objectMapper = objectMapper;
    }

    // 헤더 생성 메소드 (기간별 시세 API 호출용)
    private HttpHeaders createStockPriceHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.set("appkey", appkey);
        headers.set("appSecret", appSecret);
        headers.set("tr_id", "FHKST03010100");  // '국내주식기간별시세' API의 트랜잭션 ID
        headers.set("custtype", "P");
        return headers;
    }

    // API 응답을 처리하여 원하는 형식으로 변환하는 메소드
    private Mono<List<StockPriceDTO>> parseStockPriceResponse(String response) {
        try {
            List<StockPriceDTO> priceDataList = new ArrayList<>();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode outputNode = rootNode.get("output2");

            if (outputNode != null) {
                for (JsonNode node : outputNode) {
                    StockPriceDTO stockPrice = new StockPriceDTO();
                    stockPrice.setDate(node.get("stck_bsop_date").asText());  // 거래일
                    stockPrice.setClosePrice(node.get("stck_clpr").asText());  // 종가
                    stockPrice.setOpenPrice(node.get("stck_oprc").asText());   // 시가
                    stockPrice.setHighPrice(node.get("stck_hgpr").asText());   // 고가
                    stockPrice.setLowPrice(node.get("stck_lwpr").asText());    // 저가
                    stockPrice.setVolume(node.get("acml_vol").asText());       // 거래량
                    priceDataList.add(stockPrice);
                }
            }

            return Mono.just(priceDataList);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    // 기간별 시세 데이터를 가져오는 메소드
    public Mono<List<StockPriceDTO>> getStockPrice(String stockCode, String periodType) {
        HttpHeaders headers = createStockPriceHttpHeaders();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate today = LocalDate.now();
        LocalDate since;
        if (periodType.equals("M")) {
            since = today.minusMonths(24);
        } else if (periodType.equals("Y")) {
            since = today.minusYears(10);
        } else {
            since = today.minusDays(60);
        }


        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice")
                        .queryParam("FID_COND_MRKT_DIV_CODE", "J")  // 시장 구분 코드
                        .queryParam("FID_INPUT_ISCD", stockCode)     // 종목 코드
                        .queryParam("FID_INPUT_DATE_1", since.format(formatter))
                        .queryParam("FID_INPUT_DATE_2", today.format(formatter))
                        .queryParam("FID_PERIOD_DIV_CODE", periodType) // 기간 코드 (D:일, W:주, M:월, Y:년)
                        .queryParam("FID_ORG_ADJ_PRC", "0")          // 수정 주가 미반영
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> parseStockPriceResponse(response));
    }
}

