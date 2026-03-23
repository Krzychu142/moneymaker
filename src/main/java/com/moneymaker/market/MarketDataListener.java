package com.moneymaker.market;

import com.moneymaker.model.PriceUpdate;
import java.util.function.Consumer;

/**
 * Abstraction for market data updates.
 */
@FunctionalInterface
public interface MarketDataListener extends Consumer<PriceUpdate> {
    // A functional interface that acts as a subscriber to price updates.
}
