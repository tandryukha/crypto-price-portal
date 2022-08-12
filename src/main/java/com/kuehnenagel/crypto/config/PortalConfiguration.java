package com.kuehnenagel.crypto.config;


import lombok.Data;

import java.io.IOException;
import java.util.Properties;

@Data
public class PortalConfiguration {
    private int historicalDays;
    private String bitcoinCurrentPriceBaseUrl;
    private String bitcoinHistoricalPriceBaseUrl;

    public PortalConfiguration() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));
        historicalDays = Integer.parseInt(properties.getProperty("historicalDays", "30"));
        bitcoinCurrentPriceBaseUrl = properties.getProperty("bitcoinCurrentPriceBaseUrl", "https://api.coindesk.com/v1/bpi/currentprice/");
        bitcoinHistoricalPriceBaseUrl = properties.getProperty("bitcoinHistoricalPriceBaseUrl", "https://api.coindesk.com/v1/bpi/historical/close.json");

    }
}
