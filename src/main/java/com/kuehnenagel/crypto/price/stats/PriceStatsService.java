package com.kuehnenagel.crypto.price.stats;

import com.kuehnenagel.crypto.price.stats.dto.PriceStats;

public interface PriceStatsService {
    PriceStats getPriceStats(String currency);
}
