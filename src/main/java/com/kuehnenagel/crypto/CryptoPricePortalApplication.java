package com.kuehnenagel.crypto;


import com.kuehnenagel.crypto.context.InstanceFactory;
import com.kuehnenagel.crypto.output.PriceStatsToStringAdapter;
import com.kuehnenagel.crypto.price.stats.PriceStatsService;
import com.kuehnenagel.crypto.price.stats.dto.PriceStats;

import java.util.Scanner;

public class CryptoPricePortalApplication {
    private static final PriceStatsService bitcoinPriceStatsService = InstanceFactory.createBitcoinPriceStatsService();
    private static final PriceStatsToStringAdapter adapter = InstanceFactory.createFaultTolerantPriceAdapter();

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter the currency, for example EUR, USD");
            String currency = userInput.nextLine();
            if (!currency.isBlank()) {
                PriceStats priceStats = bitcoinPriceStatsService.getPriceStats(currency);
                System.out.println(adapter.toString(priceStats));
            }
        }
    }

}
