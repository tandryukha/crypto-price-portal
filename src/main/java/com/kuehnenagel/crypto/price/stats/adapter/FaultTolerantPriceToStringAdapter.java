package com.kuehnenagel.crypto.price.stats.adapter;

import com.kuehnenagel.crypto.output.PriceStatsToStringAdapter;
import com.kuehnenagel.crypto.price.stats.dto.PriceStats;

import static java.lang.String.format;

public class FaultTolerantPriceToStringAdapter implements PriceStatsToStringAdapter {
    @Override
    public String toString(PriceStats priceStats) {
        String currency = priceStats.getCurrency();
        Double currentPrice = priceStats.getCurrentPrice();
        int days = priceStats.getPeriodDays();
        Double maxPrice = priceStats.getHighestPeriodPrice();
        Double minPrice = priceStats.getLowestPeriodPrice();
        return format("""
                        Current price: %.2f %s
                        Max price for the last %s days: %.2f %s
                        Min price for the last %s days: %.2f %s""",
                currentPrice, currency,
                days, maxPrice, currency,
                days, minPrice, currency

        );
    }
}
