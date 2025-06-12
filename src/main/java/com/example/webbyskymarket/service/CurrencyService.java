package com.example.webbyskymarket.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyService {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/2ba59bfffdbc8662e369941d/latest/USD";
    private BigDecimal usdToRub = BigDecimal.ONE;
    private long lastUpdate = 0;

    public BigDecimal getUsdToRub() {
        if (System.currentTimeMillis() - lastUpdate > 60 * 60 * 1000) { // 1 час
            updateRates();
        }
        return usdToRub;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void updateRates() {
        RestTemplate restTemplate = new RestTemplate();
        Map response = restTemplate.getForObject(API_URL, Map.class);
        Map<String, Object> rates = (Map<String, Object>) response.get("conversion_rates");
        usdToRub = new BigDecimal(rates.get("RUB").toString());
        lastUpdate = System.currentTimeMillis();
    }
} 