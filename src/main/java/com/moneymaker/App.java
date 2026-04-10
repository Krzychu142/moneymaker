package com.moneymaker;

import com.moneymaker.client.BinanceWebSocketClient;
import com.moneymaker.simulation.PaperExchange;
import com.moneymaker.strategy.GridStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Main application entry point for MoneyMaker bot.
 */
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws InterruptedException {
        log.info("Starting MoneyMaker bot in Simulation Mode...");

        // Initialize Simulation Exchange
        PaperExchange exchange = new PaperExchange(result -> {
            log.info("TRADING UPDATE: Trade filled at {} for {}", result.order().price(), result.order().symbol());
        });

        // Initialize Strategy (Grid: 0.5% size, 0.001 BTC per order)
        BinanceWebSocketClient client = getBinanceWebSocketClient(exchange);

        client.connect();

        // Keep the main thread alive
        Thread.sleep(120_000); // 2-minute simulation
        log.info("Final Balance: {} USDT / {} Asset",
                exchange.getQuoteBalance(), exchange.getBaseBalance());
        log.info("Shutting down bot.");
    }

    private static BinanceWebSocketClient getBinanceWebSocketClient(PaperExchange exchange) {
        GridStrategy strategy = new GridStrategy(
                exchange,
                new BigDecimal("0.0001"),
                new BigDecimal("0.001")
        );

        // Connect to Market Data
        return new BinanceWebSocketClient("btcusdt", update -> {
            // First, update exchange to see if any orders were filled
            exchange.onPriceUpdate(update);
            // Then, let strategy process the new market state
            strategy.processUpdate(update);
        });
    }
}
