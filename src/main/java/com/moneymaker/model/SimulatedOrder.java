package com.moneymaker.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents a simulated order in the system.
 *
 * @param symbol    The trading pair (e.g., BTCUSDT)
 * @param type      BUY or SELL
 * @param price     The target price for the order
 * @param amount    The quantity of the asset to trade
 * @param createdAt When the order was placed
 */
public record SimulatedOrder(
        String symbol,
        OrderType type,
        BigDecimal price,
        BigDecimal amount,
        Instant createdAt
) {
}
