-- V3__Dev_Seed_Data.sql
-- Seed data for local testing. This file should only be run via the 'local' profile.

-- Insert Test User
-- Password is 'password123' (bcrypt hash: $2a$10$daHFe3VDJGDjiYDD6UQcl.YQVhLUsVRfDsRTbhLIgmhDS83m4fCGm)
INSERT INTO users (email, password_hash, role) 
VALUES ('test@example.com', '$2a$10$daHFe3VDJGDjiYDD6UQcl.YQVhLUsVRfDsRTbhLIgmhDS83m4fCGm', 'USER');

-- Insert Catalog Data
INSERT INTO brands (name) VALUES ('Apple');
SET @brand_id = LAST_INSERT_ID();

INSERT INTO series (brand_id, name) VALUES (@brand_id, 'iPhone 13 Series');
SET @series_id = LAST_INSERT_ID();

INSERT INTO models (series_id, name, release_date) VALUES (@series_id, 'iPhone 13', '2021-09-24');
SET @model_id = LAST_INSERT_ID();

INSERT INTO variants (model_id, storage, ram, color, base_price) 
VALUES (@model_id, '128GB', '4GB', 'Midnight', 799.00);

INSERT INTO variants (model_id, storage, ram, color, base_price) 
VALUES (@model_id, '256GB', '4GB', 'Starlight', 899.00);
