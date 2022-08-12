package com.kuehnenagel.crypto.price.api.adapter.bitcoin;

import com.kuehnenagel.crypto.price.api.adapter.PriceApiAdapter;

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
