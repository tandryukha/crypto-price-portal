package com.kuehnenagel.crypto.price.api.bitcoin;

import com.google.gson.Gson;
import com.kuehnenagel.crypto.config.PortalConfiguration;
import com.kuehnenagel.crypto.date.DateService;
import com.kuehnenagel.crypto.exception.CurrencyNotSupportedException;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
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
    private final String currentPriceBaseUrl;
    private final String historicalPriceBaseUrl;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

    public BitcoinPriceApiAdapter(RestTemplate restTemplate, DateService dateService, PortalConfiguration configuration) {
        this.restTemplate = restTemplate;
        this.dateService = dateService;
        currentPriceBaseUrl = configuration.getBitcoinCurrentPriceBaseUrl();
        historicalPriceBaseUrl = configuration.getBitcoinHistoricalPriceBaseUrl();
    }


    @Override
    public Optional<Double> getCurrentPrice(String currency) throws CurrencyNotSupportedException {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(getCurrentPriceUrl(currency), String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) throw new CurrencyNotSupportedException();
            return Optional.empty();
        }
        String responseBody = responseEntity.getBody();
        Optional<CurrentBPI> currentBPI = getFromJson(responseBody, CurrentBPI.class);
        return currentBPI.map(CurrentBPI::getBpi).map(bpi -> bpi.get(currency.toUpperCase())).map(Price::getRateFloat);
    }

    @Override
    public List<Double> getHistoricalPrice(String currency, int days) throws CurrencyNotSupportedException {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(getHistoricalPriceUrl(currency, days), String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) throw new CurrencyNotSupportedException();
            return emptyList();
        }
        String responseBody = responseEntity.getBody();
        Optional<HistoricalBPI> historicalBPI = getFromJson(responseBody, HistoricalBPI.class);
        return historicalBPI.map(HistoricalBPI::getBpi).map(TreeMap::values).map(collection -> collection.stream().toList()).orElse(emptyList());
    }

    private <T> Optional<T> getFromJson(String responseBody, Class<T> clazz) {
        if (responseBody == null) return Optional.empty();
        return Optional.of(new Gson().fromJson(responseBody, clazz));
    }

    private String getCurrentPriceUrl(String currency) {
        return format(currentPriceBaseUrl + "%s.json", currency);
    }

    private String getHistoricalPriceUrl(String currency, int days) {
        String startDate = formatter.format(dateService.getDateForDaysBack(days - 1));
        String endDate = formatter.format(dateService.getCurrentDate());
        return UriComponentsBuilder.fromHttpUrl(historicalPriceBaseUrl)
                .queryParam("start", startDate)
                .queryParam("end", endDate)
                .queryParam("currency", currency)
                .encode()
                .toUriString();
    }
}
