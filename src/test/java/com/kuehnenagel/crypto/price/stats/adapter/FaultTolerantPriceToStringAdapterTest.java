package com.kuehnenagel.crypto.price.stats.adapter;

import com.kuehnenagel.crypto.output.PriceStatsToStringAdapter;
import com.kuehnenagel.crypto.price.stats.dto.PriceStats;
import org.junit.jupiter.api.DisplayName;
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
                Current bitcoin price: 12345.88 EUR
                Max price available for the last 30 days: 100000000.00 EUR
                Min price available for the last 30 days: 1111.11 EUR""", output);
    }

    @DisplayName("Should convert to user-friendly string even if historical data is missing")
    @Test
    void shouldConvertToUserFriendlyStringEvenIfHistoricalDataIsMissing() {
        PriceStats priceStats = PriceStats.builder()
                .periodDays(30)
                .currency("EUR")
                .currentPrice(12345.876)
                .lowestPeriodPrice(null)
                .highestPeriodPrice(null)
                .build();
        String output = adapter.toString(priceStats);
        assertEquals("""
                Current bitcoin price: 12345.88 EUR
                Max/Min price for the last 30 days: N/A in EUR""", output);
    }


    @Test
    void shouldConvertToUserFriendlyStringWhenCurrentPriceIsMissing() {
        PriceStats priceStats = PriceStats.builder()
                .periodDays(30)
                .currency("EUR")
                .currentPrice(null)
                .lowestPeriodPrice(1111.1111)
                .highestPeriodPrice(99999999.99999)
                .build();
        String output = adapter.toString(priceStats);
        assertEquals("""
                Current bitcoin price: N/A in EUR
                Max price available for the last 30 days: 100000000.00 EUR
                Min price available for the last 30 days: 1111.11 EUR""", output);
    }

    @DisplayName("Should say no data available for this currency if no data is fetched from API")
    @Test
    void shouldSayNoDataAvailableForThisCurrency() {
        PriceStats priceStats = PriceStats.builder()
                .periodDays(30)
                .currency("EUR")
                .currentPrice(null)
                .lowestPeriodPrice(null)
                .highestPeriodPrice(null)
                .build();
        String output = adapter.toString(priceStats);
        assertEquals("No data is available for the EUR currency for the last 30 days", output);
    }
}