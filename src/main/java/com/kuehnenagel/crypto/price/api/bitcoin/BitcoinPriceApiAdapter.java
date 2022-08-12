package com.kuehnenagel.crypto.price.api.bitcoin;

import com.kuehnenagel.crypto.date.DateService;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class BitcoinPriceApiAdapter implements PriceApiAdapter {
    private final RestTemplate restTemplate;
    private final DateService dateService;
    private final String baseUrl = "https://api.coindesk.com/v1/bpi/currentprice/";//todo move to config


    @Override
    public Optional<Double> getCurrentPrice(String currency) {
        ResponseEntity<CurrentBPI> responseEntity = restTemplate.getForEntity(getCurrentPriceUrl(currency), CurrentBPI.class);
        CurrentBPI responseBody = responseEntity.getBody();
        if (isNull(responseBody)) return Optional.empty();
        return Optional.ofNullable(responseBody.getBpi().getOrDefault(currency, new Price()).getRate());
    }

    private String getCurrentPriceUrl(String currency) {
        return format(baseUrl + "%s.json", currency);
    }

    @Override
    public List<Double> getHistoricalPrice(String currency, int days) {
        return null;
    }
}
