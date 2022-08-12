package com.kuehnenagel.crypto.price.stats;

import com.kuehnenagel.crypto.price.dto.PriceStats;
import com.kuehnenagel.crypto.price.api.adapter.PriceApiAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PriceStatsServiceImpl implements PriceStatsService {
    private final PriceApiAdapter priceAPI;
    private final int statDays;

    @Override
    public PriceStats getPriceStats(String currency) {
        List<Double> historicalPrice = priceAPI.getHistoricalPrice(currency, statDays);
        Double highestPrice = historicalPrice.stream().max(Double::compareTo).orElse(null);
        Double lowestPrice = historicalPrice.stream().min(Double::compareTo).orElse(null);
        Double currentPrice = priceAPI.getCurrentPrice(currency).orElse(null);
        return PriceStats.builder()
                .currentPrice(currentPrice)
                .highestPeriodPrice(highestPrice)
                .lowestPeriodPrice(lowestPrice)
                .periodDays(statDays).build();
    }
}
