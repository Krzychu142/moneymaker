# MoneyMaker 💰

A crypto trading bot for automated investment on cryptocurrency exchanges.

## Strategy: Grid Trading

MoneyMaker utilizes a **Grid Trading** strategy, designed to profit from market volatility in "sideways" markets. It
places a "grid" of buy and sell orders at regular intervals around a base price.

- **Buy Low, Sell High:** Automatically places sell orders when a buy order is filled, capturing the spread.
- **Systematic Profit:** Focuses on small, consistent gains from price fluctuations rather than long-term trends.

## Simulation Mode

The bot currently operates in **Simulation Mode** (Paper Trading).

- **Realistic Tracking:** Tracks potential profit/loss without real financial risk.
- **Fee Awareness:** All simulated trades account for standard exchange fees (e.g., Binance's 0.1% taker/maker fee) to
  provide accurate net profit calculations.

## Requirements

- Java 21
- Maven 3.9+

## How to Run

### 1. Compile and Run

The fastest way to run the application using Maven:

```bash
mvn compile exec:java -Dexec.mainClass=com.moneymaker.App
```

### 2. Build Executable (JAR)

To create a package that can be moved and run independently:

```bash
mvn package
java -jar target/moneymaker-1.0-SNAPSHOT.jar
```

### 3. Clean Project

To remove all built files and the `target/` directory:

```bash
mvn clean
```
