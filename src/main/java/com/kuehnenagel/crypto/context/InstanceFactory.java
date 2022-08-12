package com.kuehnenagel.crypto.context;

import com.kuehnenagel.crypto.date.DateService;
import com.kuehnenagel.crypto.output.PriceStatsToStringAdapter;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import com.kuehnenagel.crypto.price.api.bitcoin.BitcoinPriceApiAdapter;
import com.kuehnenagel.crypto.price.stats.PriceStatsService;
import com.kuehnenagel.crypto.price.stats.PriceStatsServiceImpl;
import com.kuehnenagel.crypto.price.stats.adapter.FaultTolerantPriceToStringAdapter;
import org.springframework.web.client.RestTemplate;

public class InstanceFactory {
    public static PriceStatsService createBitcoinPriceStatsService() {
        return new PriceStatsServiceImpl(getBitcoinPriceApiAdapter(), 90);//todo move days to config
    }

    private static PriceApiAdapter getBitcoinPriceApiAdapter() {
        return new BitcoinPriceApiAdapter(new RestTemplate(), new DateService());//todo urls to config
    }

    public static PriceStatsToStringAdapter createFaultTolerantPriceAdapter() {
        return new FaultTolerantPriceToStringAdapter();
    }
}
