# Gemini Instructions

- **Context Optimization:** Always ignore the `target/` directory during file listing and search operations.
- **Language Preference:** Project documentation (`README.md`) should be maintained in English.
- **Project Scope:** Crypto trading bot developed with Java 21 and Maven.
- **Project Goals:**
    - **Simulation-First:** The bot should operate in a simulation mode by default, tracking potential profits/losses
      without executing real trades.
    - **Grid Trading Strategy:** Implement a grid-based approach to capture profits from market volatility through
      automated buy/sell orders at predefined price levels.
    - **Fee Calculation:** Every simulated transaction must account for exchange fees (e.g., 0.1% for Binance) to ensure
      realistic profit tracking.
- **Coding Standards:**
    - Adhere to **Clean Code** principles and **SOLID** design patterns.
    - Prioritize **Testability** (use dependency injection, mockable interfaces).
    - Favor **Functional Programming** patterns (Streams, Lambdas, Immutability).
    - Use modern Java 21 features (Records, Sealed Classes, Pattern Matching, Virtual Threads).
