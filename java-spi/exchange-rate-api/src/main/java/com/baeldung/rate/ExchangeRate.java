package com.baeldung.rate;

import com.baeldung.rate.exception.ProviderNotFoundException;
import com.baeldung.rate.spi.ExchangeRateProvider;

import java.util.List;
import java.util.ServiceLoader;

import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toList;

public final class ExchangeRate {

    private static final String DEFAULT_PROVIDER = "com.baeldung.rate.impl.YahooFinanceExchangeRateProvider";

    //All providers
    public static List<ExchangeRateProvider> providers() {
        return load(ExchangeRateProvider.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    //Default provider
    public static ExchangeRateProvider provider() {
        return provider(DEFAULT_PROVIDER);
    }

    //provider by name
    public static ExchangeRateProvider provider(String providerName) {
        return load(ExchangeRateProvider.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(p -> providerName.equals(p.getClass().getName()))
                .findFirst()
                .orElseThrow(() -> new ProviderNotFoundException("Exchange Rate provider " + providerName + " not found"));
    }
}
