-- V7__Dev_Market_Seed.sql
-- DEMO DATA for development profile only. 
-- Seeded 6 months of demo market data for Apple iPhone 13 variants (variant_id 1 and 2).

-- variant 1 (iPhone 13 128GB)
INSERT INTO market_data (variant_id, record_date, average_market_price, trend_indicator) VALUES
(1, '2024-01-01', 50000.00, 'STABLE'),
(1, '2024-02-01', 49500.00, 'STABLE'),
(1, '2024-03-01', 48000.00, 'FALLING'),
(1, '2024-04-01', 47500.00, 'STABLE'),
(1, '2024-05-01', 46000.00, 'FALLING'),
(1, '2024-06-01', 45000.00, 'FALLING');

-- variant 2 (iPhone 13 256GB)
INSERT INTO market_data (variant_id, record_date, average_market_price, trend_indicator) VALUES
(2, '2024-01-01', 60000.00, 'STABLE'),
(2, '2024-02-01', 59500.00, 'STABLE'),
(2, '2024-03-01', 58500.00, 'FALLING'),
(2, '2024-04-01', 57000.00, 'FALLING'),
(2, '2024-05-01', 56000.00, 'FALLING'),
(2, '2024-06-01', 55000.00, 'FALLING');
