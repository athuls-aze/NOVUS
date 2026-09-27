# Database Design

## Database Constraints
- **Engine**: MySQL
- **ORM**: Hibernate (via Spring Data JPA)
- **Schema Management**: Flyway. No destructive DDL-auto features (`create`/`create-drop`) in production code. 
  - *Environment Separation*: Core migrations are located in `classpath:db/migration`. Development-only seed data (e.g., `V3__Dev_Seed_Data.sql`) is stored in `classpath:db/migration/dev`. The `application-local.yml` profile configures `spring.flyway.locations` to run both, whereas production profiles will only run the core folder.

## Core Entities & Relationships

### Master Data (Smartphone Catalog)
- **brands**: `id`, `name`, timestamps
- **series**: `id`, `brand_id`, `name`, timestamps
- **models**: `id`, `series_id`, `name`, `release_date`, timestamps
- **variants**: `id`, `model_id`, `storage`, `ram`, `color`, `base_price`, timestamps

### User Data
- **users**: `id`, `email`, `password_hash`, `role`, timestamps
- **user_devices**: `id`, `user_id`, `variant_id`, `purchase_date`, `purchase_price`, `battery_health`, `device_condition_id`, timestamps (References master data variants without duplicating them).

### Business Modules
- **warranties**: `id`, `device_id`, `start_date`, `end_date`, `type`, `provider`, timestamps
- **condition_assessments**: `id`, `user_device_id`, `battery_health`, `display_condition`, `body_condition`, `camera_condition`, `charging_condition`, `overall_score`, timestamps.
  - *Design Choice*: ONE-TO-MANY (multiple assessments per device, GET returns the latest). This allows tracking condition degradation over time.
  - *Scoring Weights*: Battery (30%), Display (25%), Body (20%), Camera (15%), Charging (10%).
- **market_data**: `id`, `variant_id`, `recorded_date`, `average_price`, `trend_indicator`
- **valuations**: `id`, `user_device_id`, `estimated_value`, `lower_range`, `upper_range`, `confidence_score`, `condition_score`, `age_months`, `trend_multiplier`, `explanation`, timestamps.
  - *Formula Source Variables*: Base price (Variant), Condition Score (ConditionAssessment), Age (UserDevice purchase date), Trend Multiplier (MarketData).
- **offers** (Demo Data): `id`, `shop_name`, `price`, `location`, `distance`, `verification_method`, `payment_method`
- **sale_requests**: `id`, `device_id`, `user_id`, `status` (DRAFT, SUBMITTED, VERIFICATION_PENDING, VERIFIED, PAYMENT_PENDING, COMPLETED, REJECTED), `selected_offer_id`, timestamps

## Design Principles
- Use sensible primary keys (UUIDs or auto-incrementing BigInts depending on access patterns).
- Define foreign keys to ensure referential integrity.
- Apply unique constraints (e.g., user email).
- Include standard auditing fields (`created_at`, `updated_at`).
