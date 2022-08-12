package com.kuehnenagel.crypto.price.api.bitcoin;

import com.google.gson.Gson;
import com.kuehnenagel.crypto.date.DateService;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class BitcoinPriceApiAdapter implements PriceApiAdapter {
    private final RestTemplate restTemplate;
    private final DateService dateService;
    private final String baseUrl = "https://api.coindesk.com/v1/bpi/currentprice/";//todo move to config
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());


    @Override
    public Optional<Double> getCurrentPrice(String currency) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getCurrentPriceUrl(currency), String.class);
        String responseBody = responseEntity.getBody();
        Optional<CurrentBPI> currentBPI = getFromJson(responseBody, CurrentBPI.class);
        return currentBPI.map(CurrentBPI::getBpi).map(bpi -> bpi.get(currency)).map(Price::getRateFloat);
    }

    @Override
    public List<Double> getHistoricalPrice(String currency, int days) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getHistoricalPriceUrl(currency, days), String.class);
        String responseBody = responseEntity.getBody();
        Optional<HistoricalBPI> historicalBPI = getFromJson(responseBody, HistoricalBPI.class);
        return historicalBPI.map(HistoricalBPI::getBpi).map(TreeMap::values).map(collection -> collection.stream().toList()).orElse(emptyList());
    }

    private <T> Optional<T> getFromJson(String responseBody, Class<T> clazz) {
        if (responseBody == null) return Optional.empty();
        return Optional.of(new Gson().fromJson(responseBody, clazz));
    }

    private String getCurrentPriceUrl(String currency) {
        return format(baseUrl + "%s.json", currency);
    }

    private String getHistoricalPriceUrl(String currency, int days) {
        String startDate = formatter.format(dateService.getDateForDaysBack(days - 1));
        String endDate = formatter.format(dateService.getCurrentDate());
        return UriComponentsBuilder.fromHttpUrl("https://api.coindesk.com/v1/bpi/historical/close.json")
                .queryParam("start", startDate)
                .queryParam("end", endDate)
                .queryParam("currency", currency)
                .encode()
                .toUriString();
    }
}
