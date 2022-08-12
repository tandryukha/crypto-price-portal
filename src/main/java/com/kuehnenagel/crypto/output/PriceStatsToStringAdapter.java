package com.kuehnenagel.crypto.output;

import com.kuehnenagel.crypto.price.stats.dto.PriceStats;

public interface PriceStatsToStringAdapter {
    String toString(PriceStats priceStats);
}
