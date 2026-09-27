# Smart Assistant Architecture

**CRITICAL CONSTRAINT**: The Smart Assistant is an internal Java decision-support system. It MUST NOT integrate external LLMs (e.g., Gemini, OpenAI).

## Architecture Flow
1. **Input**: User submits a message (e.g., "Should I sell my iPhone 13 now?").
2. **Intent Detection**: Simple deterministic parsing (Regex/Keyword matching) in Java to identify intent (e.g., `Intent.SELL_DECISION`).
3. **Data Retrieval**: Fetch the user's specific device data, current valuation, and historical market trend from the database.
4. **Business Rule Engine**: Apply conditional logic based on the intent.
   - Example Rule: *If device age > 2 years and trend is downwards, recommend "Sell Now".*
5. **Response Generation**: Construct a parameterized natural language response string using the retrieved data.

## Supported Intents (Planned)
- Device current value lookup
- Valuation explanation
- Warranty status/remaining
- Condition/Battery impact analysis
- Market trend summary
- Sell now or wait decision
- Local offer queries
