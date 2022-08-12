package com.kuehnenagel.crypto.price.api;

import com.kuehnenagel.crypto.exception.CurrencyNotSupportedException;

import java.util.List;
import java.util.Optional;

public interface PriceApiAdapter {
    Optional<Double> getCurrentPrice(String currency) throws CurrencyNotSupportedException;

    List<Double> getHistoricalPrice(String currency, int historicalDays) throws CurrencyNotSupportedException;
}
