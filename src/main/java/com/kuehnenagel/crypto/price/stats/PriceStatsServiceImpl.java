package com.kuehnenagel.crypto.price.stats;

import com.kuehnenagel.crypto.price.stats.dto.PriceStats;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class PriceStatsServiceImpl implements PriceStatsService {
    private final PriceApiAdapter priceAPI;
    private final int statDays;

    @Override
    public Optional<PriceStats> getPriceStats(String currency) {
        List<Double> historicalPrice = priceAPI.getHistoricalPrice(currency, statDays);
        Optional<Double> highestPrice = historicalPrice.stream().max(Double::compareTo);
        Optional<Double> lowestPrice = historicalPrice.stream().min(Double::compareTo);
        Optional<Double> currentPrice = priceAPI.getCurrentPrice(currency);
        if (historicalPrice.isEmpty() || currentPrice.isEmpty()) return Optional.empty();
        return Optional.of(PriceStats.builder()
                .currentPrice(currentPrice.get())
                .highestPeriodPrice(highestPrice.get())
                .lowestPeriodPrice(lowestPrice.get())
                .periodDays(statDays).build());
    }
}
