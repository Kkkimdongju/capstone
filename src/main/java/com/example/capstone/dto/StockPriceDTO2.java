package com.example.capstone.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * setDate(node.get("stck_bsop_date").asText());  // 거래일
 *                     stockPrice.setClosePrice(node.get("stck_clpr").asText());  // 종가
 *                     stockPrice.setOpenPrice(node.get("stck_oprc").asText());   // 시가
 *                     stockPrice.setHighPrice(node.get("stck_hgpr").asText());   // 고가
 *                     stockPrice.setLowPrice(node.get("stck_lwpr").asText());    // 저가
 *                     stockPrice.setVolume
 */
@Getter
@Setter
public class StockPriceDTO2 {
    private List<DailyStockData> output2;

    // 일별 데이터 객체 (output2 내부 배열)
    @Getter @Setter
    public static class DailyStockData {
        private String date;  // 주식 영업 일자
        private String closePrice;      // 주식 종가
        private String openPrice;
        private String highPrice;
        private String lowPrice;
        private String volume;
        }
}
