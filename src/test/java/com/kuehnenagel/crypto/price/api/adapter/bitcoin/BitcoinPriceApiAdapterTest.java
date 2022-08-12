package com.kuehnenagel.crypto.price.api.adapter.bitcoin;

import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import com.kuehnenagel.crypto.price.api.bitcoin.BitcoinPriceApiAdapter;
import com.kuehnenagel.crypto.price.api.bitcoin.CurrentBPI;
import com.kuehnenagel.crypto.price.api.bitcoin.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class BitcoinPriceApiAdapterTest {

    @Mock
    private RestTemplate restTemplate;
    private PriceApiAdapter priceApiAdapter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        priceApiAdapter = new BitcoinPriceApiAdapter(restTemplate);
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
    void getHistoricalPrice() {
        //todo
    }
    //todo unhappy scenarios when there is no data or currency is not supported
}