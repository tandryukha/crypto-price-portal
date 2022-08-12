package com.kuehnenagel.crypto.price.api.adapter;

import java.util.List;
import java.util.Optional;

public interface PriceApiAdapter {
    Optional<Double> getCurrentPrice(String currency);

    List<Double> getHistoricalPrice(String currency, int days);
}
