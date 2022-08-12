package com.kuehnenagel.crypto.price.stats.adapter;

import com.kuehnenagel.crypto.output.PriceStatsToStringAdapter;
import com.kuehnenagel.crypto.price.stats.dto.PriceStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FaultTolerantPriceToStringAdapterTest {

    private final PriceStatsToStringAdapter adapter = new FaultTolerantPriceToStringAdapter();

    @Test
    void shouldConvertToUserFriendlyString() {
        PriceStats priceStats = PriceStats.builder()
                .periodDays(30)
                .currency("EUR")
                .currentPrice(12345.876)
                .lowestPeriodPrice(1111.1111)
                .highestPeriodPrice(99999999.99999)
                .build();
        String output = adapter.toString(priceStats);
        assertEquals("""
                Current price: 12345.88 EUR
                Max price for the last 30 days: 100000000.00 EUR
                Min price for the last 30 days: 1111.11 EUR""", output);
    }
    //todo unhappy path when no property present - say currency not supported
    //todo unhappy path when some properties are missing -say some data was not fetched from API and say when not all data was present from API - like not all days
}