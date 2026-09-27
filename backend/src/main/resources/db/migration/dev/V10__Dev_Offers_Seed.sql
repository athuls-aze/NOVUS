-- V10__Dev_Offers_Seed.sql
-- DEMO DATA ONLY

INSERT INTO offers (variant_id, shop_name, price, location, distance, verification_method, payment_method) VALUES
(1, 'Tech Zone', 42000.00, 'Downtown', 2.5, 'IN_STORE', 'CASH_OR_UPI'),
(1, 'Mobile Connect', 40500.00, 'North Mall', 5.0, 'AT_HOME', 'UPI_ONLY'),
(2, 'Tech Zone', 52000.00, 'Downtown', 2.5, 'IN_STORE', 'CASH_OR_UPI');
