package com.moneymaker;

import com.moneymaker.client.BinanceWebSocketClient;
import com.moneymaker.model.PriceUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application entry point for MoneyMaker bot.
 */
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws InterruptedException {
        log.info("Starting MoneyMaker bot...");

        // Functional approach: pass a lambda to handle updates
        BinanceWebSocketClient client = new BinanceWebSocketClient("btcusdt", App::onPriceUpdate);

        client.connect();

        // Keep the main thread alive to see updates (in a real app, this would be managed differently)
        // Just for testing purpose.
        Thread.sleep(60_000);
        log.info("Shutting down bot.");
    }

    private static void onPriceUpdate(PriceUpdate update) {
        log.info("Market Update [{}]: BID: {} | ASK: {}", 
            update.symbol(), update.bestBidPrice(), update.bestAskPrice());
    }
}
