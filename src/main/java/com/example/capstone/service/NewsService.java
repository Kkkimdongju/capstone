package com.example.capstone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NewsService {

    @Value("${clientid}")
    private String clientId;

    @Value("${clientsecret}")
    private String clientSecret;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public NewsService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public Mono<JsonNode> searchNews(String query) {
        String uri = "https://openapi.naver.com/v1/search/news.json?query=" + query;

        return webClient.get()
                .uri(uri)
                .headers(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.set("X-Naver-Client-Id", clientId);
                    httpHeaders.set("X-Naver-Client-Secret", clientSecret);
                })
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> parseResponse(response));
    }

    private Mono<JsonNode> parseResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            return Mono.just(rootNode);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

}