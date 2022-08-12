package com.kuehnenagel.crypto.price.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceStats {
    String currency;
    boolean currencyNotSupported;
    Double currentPrice;
    int historicalDays;
    Double highestPeriodPrice;
    Double lowestPeriodPrice;
}
