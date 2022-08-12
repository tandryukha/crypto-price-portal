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
        return convertToString(currency, currentPrice, days, maxPrice, minPrice);

    }

    private static String convertToString(String currency, Double currentPrice, int days, Double maxPrice, Double minPrice) {
        if (nonNull(currentPrice) && nonNull(minPrice) && nonNull(maxPrice)) {
            return toString(currency, currentPrice, days, maxPrice, minPrice);
        } else if (nonNull(minPrice) && nonNull(maxPrice)) {
            return toString(currency, days, maxPrice, minPrice);
        } else if (nonNull(currentPrice)) {
            return toString(currency, currentPrice, days);
        } else {
            return toString(currency, days);
        }
    }

    private static String toString(String currency, int days) {
        return format("No data is available for the %s currency for the last %s days", currency, days);
    }

    private static String toString(String currency, Double currentPrice, int days) {
        return format("""
                        Current price: %.2f %s
                        Max/Min price for the last %s days: N/A in %s""",
                currentPrice, currency,
                days, currency);
    }

    private static String toString(String currency, int days, Double maxPrice, Double minPrice) {
        return format("""
                        Current price: N/A in %s
                        Max price available for the last %s days: %.2f %s
                        Min price available for the last %s days: %.2f %s""",
                currency,
                days, maxPrice, currency,
                days, minPrice, currency
        );
    }

    private static String toString(String currency, Double currentPrice, int days, Double maxPrice, Double minPrice) {
        return format("""
                        Current price: %.2f %s
                        Max price available for the last %s days: %.2f %s
                        Min price available for the last %s days: %.2f %s""",
                currentPrice, currency,
                days, maxPrice, currency,
                days, minPrice, currency
        );
    }
}
