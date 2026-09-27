# Java OOP Mapping

This document demonstrates how NOVUS implements core Object-Oriented Programming principles for academic evaluation.

## Key Components

### 1. Interfaces & Polymorphism
- **`MarketDataProvider` (Interface)**: Defines the contract for fetching market data.
  - Method: `MarketData getMarketData(Variant variant)`
- **`DemoMarketDataProvider` (Implementation)**: Implements the interface to provide curated, static demo data.
  - *Academic Value*: Demonstrates abstraction. If a live API is ever added, we only need to implement `LiveMarketDataProvider` without changing the Valuation Engine.

### 2. Encapsulation
- **Entities & DTOs**: Fields are marked `private` with appropriate getters/setters. Internal database identifiers and sensitive fields (e.g., password hashes) are hidden from the presentation layer via DTO mapping.

### 3. Separation of Concerns (Layered Architecture)
- **`ValuationEngine`**: A dedicated service class responsible solely for calculating the device value based on inputs. It does not know about HTTP requests (Controller) or database SQL (Repository).
- **`ConditionAssessment`**: Encapsulates the logic for weighting and scoring device condition.

### 4. Dependency Injection
- Spring's DI is used to inject repositories into services, and services into controllers, demonstrating loose coupling.

### 5. Exception Handling
- Custom exceptions (e.g., `DeviceNotFoundException`, `InvalidConditionScoreException`) handled globally via `@ControllerAdvice`.
