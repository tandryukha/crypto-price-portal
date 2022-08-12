package com.kuehnenagel.crypto.price.api.bitcoin;

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

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class BitcoinPriceApiAdapter implements PriceApiAdapter {
    private final RestTemplate restTemplate;
    private final DateService dateService;
    private final String baseUrl = "https://api.coindesk.com/v1/bpi/currentprice/";//todo move to config
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());


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
        ResponseEntity<HistoricalBPI> responseEntity = restTemplate.getForEntity(getHistoricalPriceUrl(currency, days), HistoricalBPI.class);
        HistoricalBPI responseBody = responseEntity.getBody();
        if (isNull(responseBody)) return emptyList();
        return responseBody.getBpi().values().stream().toList();
    }

    private String getHistoricalPriceUrl(String currency, int days) {
        String startDate = formatter.format(dateService.getDateForDaysBack(days));
        String endDate = formatter.format(dateService.getCurrentDate());
        return UriComponentsBuilder.fromHttpUrl("https://api.coindesk.com/v1/bpi/historical/close.json")
                .queryParam("start", startDate)
                .queryParam("end", endDate)
                .queryParam("currency", currency)
                .encode()
                .toUriString();
    }
}
