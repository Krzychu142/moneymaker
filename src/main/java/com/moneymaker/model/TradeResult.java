package com.moneymaker.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents the final result of a filled simulated trade.
 *
 * @param order    The original order that was filled
 * @param filledAt When the order was filled
 * @param fee      The fee charged for the trade (in quote currency)
 * @param netValue The total value of the trade after fees (price * amount - fee for BUY, price * amount - fee for SELL)
 */
public record TradeResult(
        SimulatedOrder order,
        Instant filledAt,
        BigDecimal fee,
        BigDecimal netValue
) {
}
