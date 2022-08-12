package com.kuehnenagel.crypto.price.api.bitcoin;

import com.kuehnenagel.crypto.price.api.PriceApiAdapter;

import java.util.List;
import java.util.Optional;

public class BitcoinPriceApiAdapter implements PriceApiAdapter {
    @Override
    public Optional<Double> getCurrentPrice(String currency) {
        return Optional.empty();
    }

    @Override
    public List<Double> getHistoricalPrice(String currency, int days) {
        return null;
    }
}
