# NOVUS Architecture

## Pattern
**Modular Monolith**
The application will be built as a single deployable artifact (Spring Boot JAR for backend, static files for frontend) with clear internal logical boundaries. Do NOT create multiple real microservices.

## Backend Architecture (Clean Layered)
- **Controllers**: Handle HTTP requests/responses, delegate to services.
- **Services**: Contain core business logic, handle transactions.
- **Repositories**: Interface with the database via Spring Data JPA.
- **Entities**: JPA domain models mapping directly to database tables.
- **DTOs**: Data Transfer Objects used to transfer data between Controller and Service layers, ensuring internal Entity models are not leaked to API clients.

## Conceptual Modules
- Authentication & User Management
- Smartphone Catalog (Master Data)
- User Devices (Ownership Records)
- Warranty Tracking
- Condition Assessment
- Valuation Engine
- Market Data
- Local Offers
- Smart Assistant
- Sale Workflow

## Technology Stack
- **Backend**: Java 21, Spring Boot (Web, JPA, Security, Validation), Maven
- **Database**: MySQL, Flyway (Migration)
- **Frontend**: React, TypeScript, Vite, Tailwind CSS, Lucide React, Recharts
