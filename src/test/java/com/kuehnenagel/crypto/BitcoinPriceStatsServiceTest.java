package com.kuehnenagel.crypto;

import com.kuehnenagel.crypto.price.dto.PriceStats;
import com.kuehnenagel.crypto.price.facade.PriceFacade;
import com.kuehnenagel.crypto.price.stats.PriceStatsService;
import com.kuehnenagel.crypto.price.stats.PriceStatsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BitcoinPriceStatsServiceTest {
    @Mock
    private PriceFacade bitcoinPriceFacade;

    private PriceStatsService bitcoinPriceStatsService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        bitcoinPriceStatsService = new PriceStatsServiceImpl(bitcoinPriceFacade,5);
    }

    @Test
    void shouldReturnPriceInformation() {
        when(bitcoinPriceFacade.getCurrentPrice("EUR")).thenReturn(Optional.of(101d));
        when(bitcoinPriceFacade.getHistoricalPrice("EUR", 5)).thenReturn(List.of(50.0, 60.1, 40.2, 60.3, 90.4));
        PriceStats expectedPriceStats = PriceStats.builder().currentPrice(101.0).periodDays(5).highestPeriodPrice(90.4).lowestPeriodPrice(40.2).build();

        PriceStats bitcoinPriceStats = bitcoinPriceStatsService.getPriceStats("EUR");
        assertEquals(expectedPriceStats, bitcoinPriceStats);
    }
    //todo handle if currency not present
    //todo handle if no data fetched
}
