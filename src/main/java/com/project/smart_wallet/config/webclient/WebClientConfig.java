package com.project.smart_wallet.config.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3";

    private static final String BRAPI_API_URL = "https://brapi.dev/api/v2";

    private final String BRAPI_API_KEY;

    public WebClientConfig(@Value("${BRAPI_STOCK_API_KEY}") String BRAPI_API_KEY) {
        this.BRAPI_API_KEY = BRAPI_API_KEY;
    }

    @Bean
    public WebClient coingeckoWebClient() {
        return WebClient.builder().baseUrl(COINGECKO_API_URL).build();
    }

    @Bean
    public WebClient brapiWebClient() {
        return WebClient.builder()
                .baseUrl(BRAPI_API_URL)
                .defaultHeader("Authorization", "Bearer " + BRAPI_API_KEY)
                .build();
    }
}
