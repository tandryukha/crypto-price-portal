package com.kuehnenagel.crypto.price.stats;

import com.kuehnenagel.crypto.price.stats.dto.PriceStats;

import java.util.Optional;

public interface PriceStatsService {
    Optional<PriceStats> getPriceStats(String currency);
}
