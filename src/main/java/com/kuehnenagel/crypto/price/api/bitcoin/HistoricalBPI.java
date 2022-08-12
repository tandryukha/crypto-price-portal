package com.kuehnenagel.crypto.price.api.bitcoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalBPI {
    Map<String, Double> bpi;
}
