package com.kuehnenagel.crypto.price.api.adapter.bitcoin;

import com.kuehnenagel.crypto.date.DateService;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import com.kuehnenagel.crypto.price.api.bitcoin.BitcoinPriceApiAdapter;
import com.kuehnenagel.crypto.price.api.bitcoin.CurrentBPI;
import com.kuehnenagel.crypto.price.api.bitcoin.HistoricalBPI;
import com.kuehnenagel.crypto.price.api.bitcoin.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class BitcoinPriceApiAdapterTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private DateService dateService;
    private PriceApiAdapter priceApiAdapter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        priceApiAdapter = new BitcoinPriceApiAdapter(restTemplate, dateService);
    }

    @Test
    void shouldReturnPriceFromApi() {
        CurrentBPI bpi = CurrentBPI.builder().bpi(Map.of("EUR", new Price(23841.7739))).build();
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice/EUR.json", CurrentBPI.class))
                .thenReturn(new ResponseEntity<>(bpi, HttpStatus.OK));
        Double price = priceApiAdapter.getCurrentPrice("EUR").orElse(null);
        assertEquals(23841.7739, price);
    }

    @Test
    void shouldReturnEmptyResult_GivenNoMatchingCurrency() {
        CurrentBPI bpi = CurrentBPI.builder().bpi(Map.of("USD", new Price(23841.7739))).build();
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice/EUR.json", CurrentBPI.class))
                .thenReturn(new ResponseEntity<>(bpi, HttpStatus.OK));
        assertNull(priceApiAdapter.getCurrentPrice("EUR").orElse(null));
    }

    @Test
    void shouldReturnEmptyResult_GivenEmptyBody() {
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice/ZZZ.json", CurrentBPI.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        assertNull(priceApiAdapter.getCurrentPrice("ZZZ").orElse(null));
    }

    @Test
    void shouldReturnHistoricalPrice() {
        when(dateService.getCurrentDate()).thenReturn(Instant.parse("2013-09-06T10:15:30.00Z"));
        when(dateService.getDateForDaysBack(4)).thenReturn(Instant.parse("2013-09-02T10:15:30.00Z"));
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/historical/close.json?start=2013-09-02&end=2013-09-06&currency=EUR", HistoricalBPI.class))
                .thenReturn(new ResponseEntity<>(getHistoricalBPI(), HttpStatus.OK));

        List<Double> historicalPrices = priceApiAdapter.getHistoricalPrice("EUR", 5);
        assertEquals(List.of(1043.0618, 1036.1931, 1038.4625, 981.6364, 985.9107), historicalPrices);
    }

    @Test
    void shouldReturnEmptyListIfHistoricalPriceNotAvailable() {
        when(dateService.getCurrentDate()).thenReturn(Instant.parse("2013-09-06T10:15:30.00Z"));
        when(dateService.getDateForDaysBack(4)).thenReturn(Instant.parse("2013-09-02T10:15:30.00Z"));
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/historical/close.json?start=2013-09-02&end=2013-09-06&currency=ZZZ", HistoricalBPI.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        assertEquals(emptyList(), priceApiAdapter.getHistoricalPrice("ZZZ", 5));
    }

    private static HistoricalBPI getHistoricalBPI() {
        return HistoricalBPI.builder().bpi(new TreeMap<>(
                Map.of(
                        "2013-09-01", 1043.0618,
                        "2013-09-02", 1036.1931,
                        "2013-09-03", 1038.4625,
                        "2013-09-04", 981.6364,
                        "2013-09-05", 985.9107
                ))).build();
    }
}