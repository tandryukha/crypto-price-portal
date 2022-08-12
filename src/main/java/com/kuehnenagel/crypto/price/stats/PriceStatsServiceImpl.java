package com.kuehnenagel.crypto.price.stats;

import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import com.kuehnenagel.crypto.price.stats.dto.PriceStats;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PriceStatsServiceImpl implements PriceStatsService {
    private final PriceApiAdapter priceAPI;
    private final int statDays;

    @Override
    public PriceStats getPriceStats(String currency) {
        List<Double> historicalPrice = priceAPI.getHistoricalPrice(currency, statDays);
        Optional<Double> highestPrice = historicalPrice.stream().max(Double::compareTo);
        Optional<Double> lowestPrice = historicalPrice.stream().min(Double::compareTo);
        Optional<Double> currentPrice = priceAPI.getCurrentPrice(currency);
        return PriceStats.builder()
                .currency(currency)
                .currentPrice(currentPrice.orElse(null))
                .highestPeriodPrice(highestPrice.orElse(null))
                .lowestPeriodPrice(lowestPrice.orElse(null))
                .periodDays(statDays).build();
    }
}
