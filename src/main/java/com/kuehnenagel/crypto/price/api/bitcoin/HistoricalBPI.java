package com.kuehnenagel.crypto.price.api.bitcoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.TreeMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalBPI {
    TreeMap<String, Double> bpi;//keeping records sorted by key
}
