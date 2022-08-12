package com.kuehnenagel.crypto.price.stats;

import com.kuehnenagel.crypto.price.dto.PriceStats;
import com.kuehnenagel.crypto.price.facade.PriceFacade;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PriceStatsServiceImpl implements PriceStatsService {
    private final PriceFacade priceFacade;
    private final int statDays;

    @Override
    public PriceStats getPriceStats(String currency) {
        List<Double> historicalPrice = priceFacade.getHistoricalPrice(currency, statDays);
        Double highestPrice = historicalPrice.stream().max(Double::compareTo).orElse(null);
        Double lowestPrice = historicalPrice.stream().min(Double::compareTo).orElse(null);
        Double currentPrice = priceFacade.getCurrentPrice(currency).orElse(null);
        return PriceStats.builder()
                .currentPrice(currentPrice)
                .highestPeriodPrice(highestPrice)
                .lowestPeriodPrice(lowestPrice)
                .periodDays(statDays).build();
    }
}
