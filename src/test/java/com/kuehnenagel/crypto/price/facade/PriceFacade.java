package com.kuehnenagel.crypto.price.facade;

import java.time.Period;
import java.util.List;
import java.util.Optional;

public interface PriceFacade {
    Optional<Double> getCurrentPrice(String currency);

    List<Double> getHistoricalPrice(String currency, int days);
}
