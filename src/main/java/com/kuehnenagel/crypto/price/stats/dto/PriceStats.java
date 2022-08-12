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
    Double currentPrice;
    int periodDays;
    Double highestPeriodPrice;
    Double lowestPeriodPrice;
}
