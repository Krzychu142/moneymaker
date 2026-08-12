package com.moneymaker.model;

import java.math.BigDecimal;

/**
 * Immutable price update from the exchange.
 *
 * @param symbol       The trading pair (e.g., BTCUSDT)
 * @param bestBidPrice The highest price a buyer is willing to pay
 * @param bestAskPrice The lowest price a seller is willing to accept
 */
public record PriceUpdate(
        String symbol,
        BigDecimal bestBidPrice,
        BigDecimal bestAskPrice
) {
}
