package com.kuehnenagel.crypto;

import com.kuehnenagel.crypto.price.dto.PriceStats;
import com.kuehnenagel.crypto.price.api.adapter.PriceApiAdapter;
import com.kuehnenagel.crypto.price.stats.PriceStatsService;
import com.kuehnenagel.crypto.price.stats.PriceStatsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BitcoinPriceStatsServiceTest {
    @Mock
    private PriceApiAdapter bitcoinPriceAPI;

    private PriceStatsService bitcoinPriceStatsService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        bitcoinPriceStatsService = new PriceStatsServiceImpl(bitcoinPriceAPI,5);
    }

    @Test
    void shouldReturnPriceInformation() {
        when(bitcoinPriceAPI.getCurrentPrice("EUR")).thenReturn(Optional.of(101d));
        when(bitcoinPriceAPI.getHistoricalPrice("EUR", 5)).thenReturn(List.of(50.0, 60.1, 40.2, 60.3, 90.4));
        PriceStats expectedPriceStats = PriceStats.builder().currentPrice(101.0).periodDays(5).highestPeriodPrice(90.4).lowestPeriodPrice(40.2).build();

        PriceStats bitcoinPriceStats = bitcoinPriceStatsService.getPriceStats("EUR");
        assertEquals(expectedPriceStats, bitcoinPriceStats);
    }

    @Test
    void shouldReturnEmptyPriceIfNoInfoFetched() {
        when(bitcoinPriceAPI.getCurrentPrice("EUR")).thenReturn(Optional.empty());
        when(bitcoinPriceAPI.getHistoricalPrice("EUR", 5)).thenReturn(emptyList());
        PriceStats emptyPriceStats = PriceStats.builder().periodDays(5).build();

        PriceStats bitcoinPriceStats = bitcoinPriceStatsService.getPriceStats("EUR");
        assertEquals(emptyPriceStats, bitcoinPriceStats);
    }
}
