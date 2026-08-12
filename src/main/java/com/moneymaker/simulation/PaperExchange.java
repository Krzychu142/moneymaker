package com.moneymaker.simulation;

import com.moneymaker.model.OrderType;
import com.moneymaker.model.PriceUpdate;
import com.moneymaker.model.SimulatedOrder;
import com.moneymaker.model.TradeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Simulates an exchange for paper trading.
 * Tracks open orders, balance, and calculates fees.
 */
public class PaperExchange {
    private static final Logger log = LoggerFactory.getLogger(PaperExchange.class);
    private static final BigDecimal FEE_PERCENTAGE = new BigDecimal("0.001"); // 0.1%

    private final List<SimulatedOrder> openOrders = new ArrayList<>();
    private final List<TradeResult> tradeHistory = new ArrayList<>();
    private final Consumer<TradeResult> onTradeFilled;

    private BigDecimal quoteBalance = new BigDecimal("1000.0"); // Starting with 1000 USDT
    private BigDecimal baseBalance = BigDecimal.ZERO;

    public PaperExchange(Consumer<TradeResult> onTradeFilled) {
        this.onTradeFilled = onTradeFilled;
    }

    public synchronized void placeOrder(String symbol, OrderType type, BigDecimal price, BigDecimal amount) {
        SimulatedOrder order = new SimulatedOrder(symbol, type, price, amount, Instant.now());
        openOrders.add(order);
        log.info("Order Placed: {} {} at {} (Amount: {})", type, symbol, price, amount);
    }

    public synchronized void onPriceUpdate(PriceUpdate update) {
        List<SimulatedOrder> toRemove = new ArrayList<>();

        for (SimulatedOrder order : openOrders) {
            if (shouldFill(order, update)) {
                TradeResult result = fillOrder(order);
                tradeHistory.add(result);
                toRemove.add(order);
                onTradeFilled.accept(result);
            }
        }
        openOrders.removeAll(toRemove);
    }

    private boolean shouldFill(SimulatedOrder order, PriceUpdate update) {
        if (order.type() == OrderType.BUY) {
            // Fill BUY order if market ASK price is lower or equal to target price
            return update.bestAskPrice().compareTo(order.price()) <= 0;
        } else {
            // Fill SELL order if market BID price is higher or equal to target price
            return update.bestBidPrice().compareTo(order.price()) >= 0;
        }
    }

    private TradeResult fillOrder(SimulatedOrder order) {
        BigDecimal totalValue = order.price().multiply(order.amount());
        BigDecimal fee = totalValue.multiply(FEE_PERCENTAGE);

        if (order.type() == OrderType.BUY) {
            quoteBalance = quoteBalance.subtract(totalValue.add(fee));
            baseBalance = baseBalance.add(order.amount());
        } else {
            quoteBalance = quoteBalance.add(totalValue.subtract(fee));
            baseBalance = baseBalance.subtract(order.amount());
        }

        log.info("Order FILLED: {} | Fee: {} | New Balance: {} USDT / {} Asset",
                order.type(), fee.setScale(4, RoundingMode.HALF_UP),
                quoteBalance.setScale(2, RoundingMode.HALF_UP), baseBalance);

        return new TradeResult(order, Instant.now(), fee, totalValue);
    }

    public List<TradeResult> getTradeHistory() {
        return List.copyOf(tradeHistory);
    }

    public BigDecimal getQuoteBalance() {
        return quoteBalance;
    }

    public BigDecimal getBaseBalance() {
        return baseBalance;
    }
}
