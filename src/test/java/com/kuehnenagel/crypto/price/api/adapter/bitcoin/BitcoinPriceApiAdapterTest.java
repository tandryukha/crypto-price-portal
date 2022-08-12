package com.kuehnenagel.crypto.price.api.adapter.bitcoin;

import com.kuehnenagel.crypto.context.InstanceFactory;
import com.kuehnenagel.crypto.date.DateService;
import com.kuehnenagel.crypto.exception.CurrencyNotSupportedException;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import com.kuehnenagel.crypto.price.api.bitcoin.BitcoinPriceApiAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;

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
        priceApiAdapter = new BitcoinPriceApiAdapter(restTemplate, dateService, InstanceFactory.CONFIG);
    }

    @Test
    void shouldReturnPriceFromApi() throws CurrencyNotSupportedException {
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice/EUR.json", String.class))
                .thenReturn(new ResponseEntity<>(getCurrentPriceResponse(), HttpStatus.OK));
        Double price = priceApiAdapter.getCurrentPrice("EUR").orElse(null);
        assertEquals(23383.2758, price);
    }

    @Test
    void shouldReturnEmptyResult_GivenNoMatchingCurrency() throws CurrencyNotSupportedException {
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice/XYZ.json", String.class))
                .thenReturn(new ResponseEntity<>(getCurrentPriceResponse(), HttpStatus.OK));
        assertNull(priceApiAdapter.getCurrentPrice("XYZ").orElse(null));
    }

    @Test
    void shouldReturnEmptyResult_GivenEmptyBody() throws CurrencyNotSupportedException {
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice/ZZZ.json", String.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        assertNull(priceApiAdapter.getCurrentPrice("ZZZ").orElse(null));
    }

    @Test
    void shouldReturnHistoricalPrice() throws CurrencyNotSupportedException {
        when(dateService.getCurrentDate()).thenReturn(Instant.parse("2013-09-06T10:15:30.00Z"));
        when(dateService.getDateForDaysBack(4)).thenReturn(Instant.parse("2013-09-02T10:15:30.00Z"));
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/historical/close.json?start=2013-09-02&end=2013-09-06&currency=EUR", String.class))
                .thenReturn(new ResponseEntity<>(getHistoricalBPI(), HttpStatus.OK));

        List<Double> historicalPrices = priceApiAdapter.getHistoricalPrice("EUR", 5);
        assertEquals(List.of(1043.0618, 1036.1931, 1038.4625, 981.6364, 985.9107), historicalPrices);
    }

    @Test
    void shouldReturnEmptyListIfHistoricalPriceNotAvailable() throws CurrencyNotSupportedException {
        when(dateService.getCurrentDate()).thenReturn(Instant.parse("2013-09-06T10:15:30.00Z"));
        when(dateService.getDateForDaysBack(4)).thenReturn(Instant.parse("2013-09-02T10:15:30.00Z"));
        when(restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/historical/close.json?start=2013-09-02&end=2013-09-06&currency=ZZZ", String.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        assertEquals(emptyList(), priceApiAdapter.getHistoricalPrice("ZZZ", 5));
    }

    private static String getHistoricalBPI() {
        return """
                {
                  "bpi": {
                    "2013-09-01": 1043.0618,
                    "2013-09-02": 1036.1931,
                    "2013-09-03": 1038.4625,
                    "2013-09-04": 981.6364,
                    "2013-09-05": 985.9107
                  },
                  "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index. BPI value data returned as UAH.",
                  "time": {
                    "updated": "Sep 6, 2013 00:03:00 UTC",
                    "updatedISO": "2013-09-06T00:03:00+00:00"
                  }
                }""";
    }

    private String getCurrentPriceResponse() {
        return """
                {
                  "time": {
                    "updated": "Aug 12, 2022 18:18:00 UTC",
                    "updatedISO": "2022-08-12T18:18:00+00:00",
                    "updateduk": "Aug 12, 2022 at 19:18 BST"
                  },
                  "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
                  "bpi": {
                    "USD": {
                      "code": "USD",
                      "rate": "24,003.8719",
                      "description": "United States Dollar",
                      "rate_float": 24003.8719
                    },
                    "EUR": {
                      "code": "EUR",
                      "rate": "23,383.2758",
                      "description": "Euro",
                      "rate_float": 23383.2758
                    }
                  }
                }""";
    }
}