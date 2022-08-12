package com.kuehnenagel.crypto.context;

import com.kuehnenagel.crypto.date.DateService;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import com.kuehnenagel.crypto.price.api.bitcoin.BitcoinPriceApiAdapter;
import com.kuehnenagel.crypto.price.stats.PriceStatsService;
import com.kuehnenagel.crypto.price.stats.PriceStatsServiceImpl;
import org.springframework.web.client.RestTemplate;

public class InstanceFactory {
    public static PriceStatsService createBitcoinPriceStatsService() {
        return new PriceStatsServiceImpl(getBitcoinPriceApiAdapter(), 30);//todo move days to config
    }

    private static PriceApiAdapter getBitcoinPriceApiAdapter() {
        return new BitcoinPriceApiAdapter(new RestTemplate(), new DateService());//todo urls to config
    }
}
