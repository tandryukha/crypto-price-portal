package com.kuehnenagel.crypto.price.stats;

import com.kuehnenagel.crypto.exception.CurrencyNotSupportedException;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import com.kuehnenagel.crypto.price.stats.dto.PriceStats;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class PriceStatsServiceImpl implements PriceStatsService {
    private final PriceApiAdapter priceAPI;
    private final int historicalDays;

    @Override
    public PriceStats getPriceStats(String currency) {
        List<Double> historicalPrice;
        Optional<Double> currentPrice;
        boolean currencyNotSupported = false;
        try {
            historicalPrice = priceAPI.getHistoricalPrice(currency, historicalDays);
            currentPrice = priceAPI.getCurrentPrice(currency);
        } catch (CurrencyNotSupportedException e) {
            currencyNotSupported = true;
            historicalPrice = emptyList();
            currentPrice = Optional.empty();
        }
        Optional<Double> highestPrice = historicalPrice.stream().max(Double::compareTo);
        Optional<Double> lowestPrice = historicalPrice.stream().min(Double::compareTo);
        return PriceStats.builder()
                .currency(currency)
                .currencyNotSupported(currencyNotSupported)
                .currentPrice(currentPrice.orElse(null))
                .highestPeriodPrice(highestPrice.orElse(null))
                .lowestPeriodPrice(lowestPrice.orElse(null))
                .historicalDays(historicalDays).build();
    }
}
