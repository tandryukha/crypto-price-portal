package com.kuehnenagel.crypto.context;

import com.kuehnenagel.crypto.config.PortalConfiguration;
import com.kuehnenagel.crypto.date.DateService;
import com.kuehnenagel.crypto.output.PriceStatsToStringAdapter;
import com.kuehnenagel.crypto.price.api.PriceApiAdapter;
import com.kuehnenagel.crypto.price.api.bitcoin.BitcoinPriceApiAdapter;
import com.kuehnenagel.crypto.price.stats.PriceStatsService;
import com.kuehnenagel.crypto.price.stats.PriceStatsServiceImpl;
import com.kuehnenagel.crypto.price.stats.adapter.FaultTolerantPriceToStringAdapter;
import lombok.SneakyThrows;
import org.springframework.web.client.RestTemplate;

public class InstanceFactory {

    public static final PortalConfiguration configuration = getPortalConfiguration();

    public static PriceStatsService createBitcoinPriceStatsService() {
        return new PriceStatsServiceImpl(getBitcoinPriceApiAdapter(), configuration.getHistoricalDays());
    }

    private static PriceApiAdapter getBitcoinPriceApiAdapter() {
        return new BitcoinPriceApiAdapter(new RestTemplate(), new DateService(), configuration);
    }

    public static PriceStatsToStringAdapter createFaultTolerantPriceAdapter() {
        return new FaultTolerantPriceToStringAdapter();
    }

    @SneakyThrows
    private static PortalConfiguration getPortalConfiguration() {
        return new PortalConfiguration();
    }
}
