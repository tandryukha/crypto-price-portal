package com.kuehnenagel.crypto.price.api.bitcoin;

import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
public class BitcoinPriceApiAdapter implements PriceApiAdapter {
    private final RestTemplate restTemplate;


    @Override
    public Optional<Double> getCurrentPrice(String currency) {
        ResponseEntity<CurrentBPI> responseEntity = restTemplate.getForEntity(URI.create("https://api.coindesk.com/v1/bpi/currentprice/EUR.json"), CurrentBPI.class);
        return Optional.ofNullable(requireNonNull(responseEntity.getBody()).getBpi().getOrDefault(currency, new Price()).getRate());
    }

    @Override
    public List<Double> getHistoricalPrice(String currency, int days) {
        return null;
    }
}
