package com.kuehnenagel.crypto;


import com.kuehnenagel.crypto.context.InstanceFactory;
import com.kuehnenagel.crypto.price.stats.PriceStatsService;
import com.kuehnenagel.crypto.price.stats.dto.PriceStats;

import java.util.Optional;
import java.util.Scanner;

import static java.lang.String.format;

public class CryptoPricePortalApplication {
    private static final PriceStatsService bitcointPriceStatsService = InstanceFactory.createBitcoinPriceStatsService();

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter the currency, for example EUR, USD");
            String currency = userInput.nextLine();
            if (!currency.isBlank()) {
                Optional<PriceStats> priceStats = bitcointPriceStatsService.getPriceStats(currency);
                String output = priceStats.map(Object::toString).orElse(format("No data found for currency %s, try another one", currency));
                System.out.println(output);
            }
        }
    }

}
