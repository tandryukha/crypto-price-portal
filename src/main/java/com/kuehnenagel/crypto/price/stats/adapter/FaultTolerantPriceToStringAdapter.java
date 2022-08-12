package com.kuehnenagel.crypto.price.stats.adapter;

import com.kuehnenagel.crypto.output.PriceStatsToStringAdapter;
import com.kuehnenagel.crypto.price.stats.dto.PriceStats;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

public class FaultTolerantPriceToStringAdapter implements PriceStatsToStringAdapter {
    @Override
    public String toString(PriceStats priceStats) {
        String currency = priceStats.getCurrency();
        Double currentPrice = priceStats.getCurrentPrice();
        int days = priceStats.getPeriodDays();
        Double maxPrice = priceStats.getHighestPeriodPrice();
        Double minPrice = priceStats.getLowestPeriodPrice();
        if (nonNull(currentPrice) && nonNull(minPrice) && nonNull(maxPrice)) {
            return format("""
                            Current price: %.2f %s
                            Max price for the last %s days: %.2f %s
                            Min price for the last %s days: %.2f %s""",
                    currentPrice, currency,
                    days, maxPrice, currency,
                    days, minPrice, currency
            );
        } else if (nonNull(minPrice) && nonNull(maxPrice)) {
            return format("""
                            Current price: N/A in %s
                            Max price for the last %s days: %.2f %s
                            Min price for the last %s days: %.2f %s""",
                    currency,
                    days, maxPrice, currency,
                    days, minPrice, currency
            );

        } else if (nonNull(currentPrice)) {
            return format("""
                            Current price: %.2f %s
                            Max/Min price for the last %s days: N/A in %s""",
                    currentPrice, currency,
                    days, currency);
        } else {
            return format("No data is available for the %s currency for the last %s days", currency, days);
        }

    }
}
