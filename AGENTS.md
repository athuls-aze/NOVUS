# AI Agent Rules for NOVUS

Future AI coding sessions MUST obey the following rules to ensure the project remains an explainable, academic B.Tech CSE project.

## Core Rules
1. **Read Documentation First**: Always read this `AGENTS.md` file, the relevant `/docs/*.md` files, `CURRENT_STATUS.md`, and `TODO.md` before making any code modifications.
2. **Preserve Architecture**: Never rewrite unrelated working code. Maintain the Modular Monolith architecture.
3. **Keep Commits Small**: Make logically separated commits. Stop after completing the explicitly requested milestone. Do not proceed to the next phase without user approval.
4. **Update Status**: Update `CURRENT_STATUS.md` and `TODO.md` after completing tasks.

## Technology Stack Constraints
- **Backend**: Java 21, Spring Boot, Spring Web, Spring Data JPA, Hibernate, Spring Security, Jakarta Validation.
- **Database**: MySQL. Database migrations MUST be handled by Flyway. Do NOT use `spring.jpa.hibernate.ddl-auto=create` or `create-drop`.
- **Frontend**: React, TypeScript, Vite, Tailwind CSS, Lucide React, Recharts (where necessary).
- **Testing**: JUnit 5, Mockito, Spring Boot Test.

## Project-Specific Constraints
- **Smart Assistant**: MUST use internal Java logic only. Do NOT integrate Gemini, OpenAI, or any external LLM APIs.
- **Market Data**: Use curated/demo data via `DemoMarketDataProvider`. Do NOT implement external live-market scraping.
- **Valuation**: Must be deterministic and explainable using Java OOP principles. No random numbers.
- **Security**: Do NOT expose secrets (passwords, DB credentials, JWT secrets, API keys) in source code. Use environment variables.
- **Entity Exposure**: Do not expose JPA entities directly from REST endpoints. Always map to/from DTOs.
