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

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void getCurrentPrice() {
        CurrentBPI bpi = CurrentBPI.builder().bpi(Map.of("EUR", new Price(23841.7739))).build();
        when(restTemplate.getForEntity(URI.create("https://api.coindesk.com/v1/bpi/currentprice/EUR.json"), CurrentBPI.class))
                .thenReturn(new ResponseEntity<>(bpi, HttpStatus.OK));
        Double price = priceApiAdapter.getCurrentPrice("EUR").orElse(null);
        assertEquals(23841.7739, price);
    }

    @Test
    void getHistoricalPrice() {
        //todo
    }
    //todo unhappy scenarios when there is no data or currency is not supported
}