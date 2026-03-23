# MoneyMaker 💰

A crypto trading bot for automated investment on cryptocurrency exchanges.

## Requirements
- Java 21
- Maven 3.9+

## How to Run

### 1. Compile and Run
The fastest way to run the application using Maven:
```bash
mvn compile exec:java -Dexec.mainClass="com.moneymaker.App"
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
