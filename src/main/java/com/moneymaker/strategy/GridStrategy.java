package com.moneymaker.strategy;

import com.moneymaker.model.OrderType;
import com.moneymaker.model.PriceUpdate;
import com.moneymaker.simulation.PaperExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Basic Grid Trading Strategy implementation.
 */
public class GridStrategy {
    private static final Logger log = LoggerFactory.getLogger(GridStrategy.class);

    private final PaperExchange exchange;
    private final BigDecimal gridSize; // Percentage interval, e.g., 0.01 for 1%
    private final BigDecimal orderAmount;

    private BigDecimal lastBasePrice;
    private boolean initialized = false;

    public GridStrategy(PaperExchange exchange, BigDecimal gridSize, BigDecimal orderAmount) {
        this.exchange = exchange;
        this.gridSize = gridSize;
        this.orderAmount = orderAmount;
    }

    public void processUpdate(PriceUpdate update) {
        if (!initialized) {
            initializeGrid(update);
            return;
        }

        BigDecimal currentPrice = update.bestBidPrice();
        BigDecimal priceMove = currentPrice.subtract(lastBasePrice).divide(lastBasePrice, 4, RoundingMode.HALF_UP).abs();

        // If price moved more than the grid size, we "shift" the grid
        if (priceMove.compareTo(gridSize) >= 0) {
            log.info("Price moved significantly. Re-adjusting grid. New Base: {}", currentPrice);
            this.lastBasePrice = currentPrice;

            deployGridOrders(update);
        }
    }

    private void initializeGrid(PriceUpdate update) {
        this.lastBasePrice = update.bestBidPrice();
        this.initialized = true;

        log.info("Initializing Grid at Base Price: {}", lastBasePrice);

        // Place initial grid: one BUY below and one SELL above for demonstration
        deployGridOrders(update);
    }

    private void deployGridOrders(PriceUpdate update) {
        BigDecimal buyPrice = lastBasePrice.multiply(BigDecimal.ONE.subtract(gridSize)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal sellPrice = lastBasePrice.multiply(BigDecimal.ONE.add(gridSize)).setScale(2, RoundingMode.HALF_UP);

        exchange.placeOrder(update.symbol(), OrderType.BUY, buyPrice, orderAmount);
        exchange.placeOrder(update.symbol(), OrderType.SELL, sellPrice, orderAmount);
    }
}
