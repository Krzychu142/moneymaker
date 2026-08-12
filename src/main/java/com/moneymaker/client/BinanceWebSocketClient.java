package com.moneymaker.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneymaker.market.MarketDataListener;
import com.moneymaker.model.PriceUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

/**
 * Binance implementation of a real-time price tracker.
 */
public class BinanceWebSocketClient {
    private static final Logger log = LoggerFactory.getLogger(BinanceWebSocketClient.class);
    private static final String BINANCE_WS_URL = "wss://stream.binance.com:9443/ws/%s@bookTicker";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String symbol;
    private final MarketDataListener listener;

    public BinanceWebSocketClient(String symbol, MarketDataListener listener) {
        this.symbol = symbol.toLowerCase();
        this.listener = listener;
    }

    public void connect() {
        String url = String.format(BINANCE_WS_URL, symbol);
        log.info("Connecting to Binance WebSocket: {}", url);

        HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(url), new BinanceWebSocketListener());
    }

    private class BinanceWebSocketListener implements WebSocket.Listener {
        private final StringBuilder messageBuffer = new StringBuilder();

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            messageBuffer.append(data);
            if (last) {
                processMessage(messageBuffer.toString());
                messageBuffer.setLength(0);
            }
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        private void processMessage(String message) {
            try {
                JsonNode node = objectMapper.readTree(message);

                // Fields from Binance bookTicker:
                // "s": symbol, "b": best bid price, "a": best ask price
                PriceUpdate update = new PriceUpdate(
                        node.get("s").asText(),
                        new BigDecimal(node.get("b").asText()),
                        new BigDecimal(node.get("a").asText())
                );

                listener.accept(update);
            } catch (Exception e) {
                log.error("Error parsing message: {}", message, e);
            }
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            log.error("WebSocket error", error);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            log.info("WebSocket closed: {} - {}", statusCode, reason);
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        }
    }
}
