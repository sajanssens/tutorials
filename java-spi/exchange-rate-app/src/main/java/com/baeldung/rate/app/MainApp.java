package com.baeldung.rate.app;

import com.baeldung.rate.ExchangeRate;
import com.baeldung.rate.api.Quote;
import com.baeldung.rate.spi.ExchangeRateProvider;

import java.time.LocalDate;
import java.util.List;

// Run with mvn install from the java-spi directory:
// 1) without implementing classes: default
// 2) with implementing classes: uncomment the impl dependency in the pom of exchange-rate-app

public class MainApp {
    public static void main(String... args) {
        System.out.println("Running MainApp...");

        ExchangeRate.providers().forEach(MainApp::process);
        // process(ExchangeRate.provider()); // default provider

        System.out.println("Done.");
        System.exit(0);
    }

    private static void process(ExchangeRateProvider provider) {
        System.out.println("Retreiving USD quotes from provider :" + provider);
        List<Quote> quotes = provider.create().getQuotes("USD", LocalDate.now());
        System.out.println(String.format("%14s%12s|%12s", "", "Ask", "Bid"));
        System.out.println("----------------------------------------");
        quotes.forEach(quote -> {
            System.out.println("USD --> " + quote.getCurrency() + " : " + String.format("%12f|%12f", quote.getAsk(), quote.getBid()));
        });
    }
}
