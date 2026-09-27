# Testing Strategy

Testing is a core component of the B.Tech project evaluation.

## Frameworks
- JUnit 5
- Mockito
- Spring Boot Test

## Key Areas to Test
1. **Valuation Engine**: Ensure the formula `(Base Price × Trend) − (Condition Penalty + Age Depreciation)` calculates correctly under various edge cases (e.g., extreme age, perfect condition, negative trend).
2. **Condition Scoring**: Verify weighted sums for battery, display, and body.
3. **Smart Assistant Intent**: Test the keyword/regex parsing logic to ensure correct intent mapping.
4. **State Transitions**: Test the `SaleWorkflow` entity to ensure it correctly transitions between valid states (e.g., DRAFT → SUBMITTED) and rejects invalid ones.

## Test Philosophy
Focus on business logic and edge cases (invalid dates, negative values, missing data) early. Do not leave testing as an afterthought.
