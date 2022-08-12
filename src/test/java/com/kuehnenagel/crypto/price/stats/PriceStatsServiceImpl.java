package com.kuehnenagel.crypto.price.stats;

import com.kuehnenagel.crypto.price.dto.PriceStats;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PriceStatsServiceImpl implements PriceStatsService {
    private final PriceStatsService priceStatsService;
    private final int statDays;

    @Override
    public PriceStats getPriceStats(String currency) {
        return PriceStats.builder().build();
    }
}
