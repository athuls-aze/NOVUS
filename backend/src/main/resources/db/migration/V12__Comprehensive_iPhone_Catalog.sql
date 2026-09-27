-- V12__Comprehensive_iPhone_Catalog.sql
-- Extensive smartphone catalog seed: iPhone 11, 12, 13, 14, 15, 16, and SE series

-- Ensure Apple brand exists
INSERT IGNORE INTO brands (name) VALUES ('Apple');

-- Helper procedure to safely populate series, models, and variants
DROP PROCEDURE IF EXISTS populate_iphone_catalog;
DELIMITER $$
CREATE PROCEDURE populate_iphone_catalog()
BEGIN
    DECLARE brand_id_val BIGINT;
    DECLARE s_id BIGINT;
    DECLARE m_id BIGINT;

    SELECT id INTO brand_id_val FROM brands WHERE name = 'Apple' LIMIT 1;

    -- ==========================================
    -- 1. iPhone 11 Series
    -- ==========================================
    INSERT IGNORE INTO series (brand_id, name) VALUES (brand_id_val, 'iPhone 11 Series');
    SELECT id INTO s_id FROM series WHERE brand_id = brand_id_val AND name = 'iPhone 11 Series';

    -- iPhone 11
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 11', '2019-09-20');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 11';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '64GB', '4GB', 'Black', 49900.00),
        (m_id, '64GB', '4GB', 'White', 49900.00),
        (m_id, '64GB', '4GB', 'Green', 49900.00),
        (m_id, '64GB', '4GB', 'Purple', 49900.00),
        (m_id, '128GB', '4GB', 'Black', 54900.00),
        (m_id, '128GB', '4GB', 'White', 54900.00),
        (m_id, '128GB', '4GB', 'Green', 54900.00),
        (m_id, '256GB', '4GB', 'Black', 64900.00);

    -- iPhone 11 Pro
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 11 Pro', '2019-09-20');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 11 Pro';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '64GB', '4GB', 'Midnight Green', 89900.00),
        (m_id, '64GB', '4GB', 'Space Gray', 89900.00),
        (m_id, '256GB', '4GB', 'Midnight Green', 103900.00),
        (m_id, '256GB', '4GB', 'Silver', 103900.00),
        (m_id, '512GB', '4GB', 'Space Gray', 121900.00);

    -- iPhone 11 Pro Max
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 11 Pro Max', '2019-09-20');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 11 Pro Max';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '64GB', '4GB', 'Midnight Green', 99900.00),
        (m_id, '64GB', '4GB', 'Space Gray', 99900.00),
        (m_id, '256GB', '4GB', 'Midnight Green', 113900.00),
        (m_id, '512GB', '4GB', 'Gold', 131900.00);

    -- ==========================================
    -- 2. iPhone 12 Series
    -- ==========================================
    INSERT IGNORE INTO series (brand_id, name) VALUES (brand_id_val, 'iPhone 12 Series');
    SELECT id INTO s_id FROM series WHERE brand_id = brand_id_val AND name = 'iPhone 12 Series';

    -- iPhone 12 mini
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 12 mini', '2020-11-13');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 12 mini';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '64GB', '4GB', 'Black', 59900.00),
        (m_id, '64GB', '4GB', 'Blue', 59900.00),
        (m_id, '128GB', '4GB', 'White', 64900.00),
        (m_id, '128GB', '4GB', 'Black', 64900.00),
        (m_id, '256GB', '4GB', 'Green', 74900.00);

    -- iPhone 12
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 12', '2020-10-23');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 12';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '64GB', '4GB', 'Black', 65900.00),
        (m_id, '64GB', '4GB', 'White', 65900.00),
        (m_id, '64GB', '4GB', 'Blue', 65900.00),
        (m_id, '128GB', '4GB', 'Black', 70900.00),
        (m_id, '128GB', '4GB', 'Blue', 70900.00),
        (m_id, '128GB', '4GB', 'Purple', 70900.00),
        (m_id, '256GB', '4GB', 'White', 80900.00);

    -- iPhone 12 Pro
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 12 Pro', '2020-10-23');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 12 Pro';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Pacific Blue', 99900.00),
        (m_id, '128GB', '6GB', 'Graphite', 99900.00),
        (m_id, '256GB', '6GB', 'Pacific Blue', 109900.00),
        (m_id, '512GB', '6GB', 'Graphite', 129900.00);

    -- iPhone 12 Pro Max
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 12 Pro Max', '2020-11-13');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 12 Pro Max';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Pacific Blue', 109900.00),
        (m_id, '128GB', '6GB', 'Graphite', 109900.00),
        (m_id, '256GB', '6GB', 'Pacific Blue', 119900.00),
        (m_id, '512GB', '6GB', 'Gold', 139900.00);

    -- ==========================================
    -- 3. iPhone 13 Series additions
    -- ==========================================
    INSERT IGNORE INTO series (brand_id, name) VALUES (brand_id_val, 'iPhone 13 Series');
    SELECT id INTO s_id FROM series WHERE brand_id = brand_id_val AND name = 'iPhone 13 Series';

    -- iPhone 13 mini
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 13 mini', '2021-09-24');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 13 mini';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '4GB', 'Midnight', 64900.00),
        (m_id, '128GB', '4GB', 'Blue', 64900.00),
        (m_id, '256GB', '4GB', 'Pink', 74900.00);

    -- iPhone 13 (more variants)
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 13';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '4GB', 'Blue', 69900.00),
        (m_id, '128GB', '4GB', 'Pink', 69900.00),
        (m_id, '128GB', '4GB', 'Green', 69900.00),
        (m_id, '256GB', '4GB', 'Midnight', 79900.00),
        (m_id, '256GB', '4GB', 'Blue', 79900.00),
        (m_id, '512GB', '4GB', 'Midnight', 99900.00);

    -- iPhone 13 Pro
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 13 Pro', '2021-09-24');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 13 Pro';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Sierra Blue', 119900.00),
        (m_id, '128GB', '6GB', 'Graphite', 119900.00),
        (m_id, '256GB', '6GB', 'Sierra Blue', 129900.00),
        (m_id, '256GB', '6GB', 'Alpine Green', 129900.00),
        (m_id, '512GB', '6GB', 'Graphite', 149900.00),
        (m_id, '1TB', '6GB', 'Sierra Blue', 169900.00);

    -- iPhone 13 Pro Max
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 13 Pro Max', '2021-09-24');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 13 Pro Max';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Sierra Blue', 129900.00),
        (m_id, '128GB', '6GB', 'Graphite', 129900.00),
        (m_id, '256GB', '6GB', 'Sierra Blue', 139900.00),
        (m_id, '512GB', '6GB', 'Alpine Green', 159900.00),
        (m_id, '1TB', '6GB', 'Graphite', 179900.00);

    -- ==========================================
    -- 4. iPhone 14 Series
    -- ==========================================
    INSERT IGNORE INTO series (brand_id, name) VALUES (brand_id_val, 'iPhone 14 Series');
    SELECT id INTO s_id FROM series WHERE brand_id = brand_id_val AND name = 'iPhone 14 Series';

    -- iPhone 14
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 14', '2022-09-16');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 14';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Midnight', 69900.00),
        (m_id, '128GB', '6GB', 'Starlight', 69900.00),
        (m_id, '128GB', '6GB', 'Blue', 69900.00),
        (m_id, '128GB', '6GB', 'Purple', 69900.00),
        (m_id, '256GB', '6GB', 'Midnight', 79900.00),
        (m_id, '256GB', '6GB', 'Blue', 79900.00),
        (m_id, '512GB', '6GB', 'Purple', 99900.00);

    -- iPhone 14 Plus
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 14 Plus', '2022-10-07');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 14 Plus';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Midnight', 79900.00),
        (m_id, '128GB', '6GB', 'Blue', 79900.00),
        (m_id, '128GB', '6GB', 'Purple', 79900.00),
        (m_id, '256GB', '6GB', 'Starlight', 89900.00),
        (m_id, '512GB', '6GB', 'Midnight', 109900.00);

    -- iPhone 14 Pro
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 14 Pro', '2022-09-16');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 14 Pro';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Deep Purple', 129900.00),
        (m_id, '128GB', '6GB', 'Space Black', 129900.00),
        (m_id, '256GB', '6GB', 'Deep Purple', 139900.00),
        (m_id, '256GB', '6GB', 'Gold', 139900.00),
        (m_id, '512GB', '6GB', 'Space Black', 159900.00),
        (m_id, '1TB', '6GB', 'Deep Purple', 179900.00);

    -- iPhone 14 Pro Max
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 14 Pro Max', '2022-09-16');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 14 Pro Max';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Deep Purple', 139900.00),
        (m_id, '128GB', '6GB', 'Space Black', 139900.00),
        (m_id, '256GB', '6GB', 'Deep Purple', 149900.00),
        (m_id, '256GB', '6GB', 'Silver', 149900.00),
        (m_id, '512GB', '6GB', 'Deep Purple', 169900.00),
        (m_id, '1TB', '6GB', 'Space Black', 189900.00);

    -- ==========================================
    -- 5. iPhone 15 Series
    -- ==========================================
    INSERT IGNORE INTO series (brand_id, name) VALUES (brand_id_val, 'iPhone 15 Series');
    SELECT id INTO s_id FROM series WHERE brand_id = brand_id_val AND name = 'iPhone 15 Series';

    -- iPhone 15
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 15', '2023-09-22');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 15';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Black', 79900.00),
        (m_id, '128GB', '6GB', 'Blue', 79900.00),
        (m_id, '128GB', '6GB', 'Green', 79900.00),
        (m_id, '128GB', '6GB', 'Pink', 79900.00),
        (m_id, '256GB', '6GB', 'Black', 89900.00),
        (m_id, '256GB', '6GB', 'Blue', 89900.00),
        (m_id, '512GB', '6GB', 'Black', 109900.00);

    -- iPhone 15 Plus
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 15 Plus', '2023-09-22');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 15 Plus';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '6GB', 'Black', 89900.00),
        (m_id, '128GB', '6GB', 'Pink', 89900.00),
        (m_id, '256GB', '6GB', 'Black', 99900.00),
        (m_id, '256GB', '6GB', 'Green', 99900.00),
        (m_id, '512GB', '6GB', 'Blue', 119900.00);

    -- iPhone 15 Pro
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 15 Pro', '2023-09-22');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 15 Pro';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '8GB', 'Natural Titanium', 134900.00),
        (m_id, '128GB', '8GB', 'Black Titanium', 134900.00),
        (m_id, '128GB', '8GB', 'Blue Titanium', 134900.00),
        (m_id, '128GB', '8GB', 'White Titanium', 134900.00),
        (m_id, '256GB', '8GB', 'Natural Titanium', 144900.00),
        (m_id, '256GB', '8GB', 'Black Titanium', 144900.00),
        (m_id, '512GB', '8GB', 'Natural Titanium', 164900.00),
        (m_id, '1TB', '8GB', 'Natural Titanium', 184900.00);

    -- iPhone 15 Pro Max
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 15 Pro Max', '2023-09-22');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 15 Pro Max';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '256GB', '8GB', 'Natural Titanium', 159900.00),
        (m_id, '256GB', '8GB', 'Black Titanium', 159900.00),
        (m_id, '256GB', '8GB', 'Blue Titanium', 159900.00),
        (m_id, '256GB', '8GB', 'White Titanium', 159900.00),
        (m_id, '512GB', '8GB', 'Natural Titanium', 179900.00),
        (m_id, '1TB', '8GB', 'Black Titanium', 199900.00);

    -- ==========================================
    -- 6. iPhone 16 Series
    -- ==========================================
    INSERT IGNORE INTO series (brand_id, name) VALUES (brand_id_val, 'iPhone 16 Series');
    SELECT id INTO s_id FROM series WHERE brand_id = brand_id_val AND name = 'iPhone 16 Series';

    -- iPhone 16
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 16', '2024-09-20');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 16';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '8GB', 'Black', 79900.00),
        (m_id, '128GB', '8GB', 'White', 79900.00),
        (m_id, '128GB', '8GB', 'Pink', 79900.00),
        (m_id, '128GB', '8GB', 'Teal', 79900.00),
        (m_id, '128GB', '8GB', 'Ultramarine', 79900.00),
        (m_id, '256GB', '8GB', 'Black', 89900.00),
        (m_id, '256GB', '8GB', 'Ultramarine', 89900.00),
        (m_id, '512GB', '8GB', 'Teal', 109900.00);

    -- iPhone 16 Plus
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 16 Plus', '2024-09-20');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 16 Plus';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '8GB', 'Black', 89900.00),
        (m_id, '128GB', '8GB', 'Ultramarine', 89900.00),
        (m_id, '256GB', '8GB', 'Pink', 99900.00),
        (m_id, '256GB', '8GB', 'Teal', 99900.00),
        (m_id, '512GB', '8GB', 'Black', 119900.00);

    -- iPhone 16 Pro
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 16 Pro', '2024-09-20');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 16 Pro';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '128GB', '8GB', 'Desert Titanium', 119900.00),
        (m_id, '128GB', '8GB', 'Natural Titanium', 119900.00),
        (m_id, '128GB', '8GB', 'White Titanium', 119900.00),
        (m_id, '128GB', '8GB', 'Black Titanium', 119900.00),
        (m_id, '256GB', '8GB', 'Desert Titanium', 129900.00),
        (m_id, '256GB', '8GB', 'Natural Titanium', 129900.00),
        (m_id, '512GB', '8GB', 'Black Titanium', 149900.00),
        (m_id, '1TB', '8GB', 'Desert Titanium', 169900.00);

    -- iPhone 16 Pro Max
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone 16 Pro Max', '2024-09-20');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone 16 Pro Max';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '256GB', '8GB', 'Desert Titanium', 144900.00),
        (m_id, '256GB', '8GB', 'Natural Titanium', 144900.00),
        (m_id, '256GB', '8GB', 'White Titanium', 144900.00),
        (m_id, '256GB', '8GB', 'Black Titanium', 144900.00),
        (m_id, '512GB', '8GB', 'Desert Titanium', 164900.00),
        (m_id, '512GB', '8GB', 'Natural Titanium', 164900.00),
        (m_id, '1TB', '8GB', 'Desert Titanium', 184900.00);

    -- ==========================================
    -- 7. iPhone SE Series
    -- ==========================================
    INSERT IGNORE INTO series (brand_id, name) VALUES (brand_id_val, 'iPhone SE Series');
    SELECT id INTO s_id FROM series WHERE brand_id = brand_id_val AND name = 'iPhone SE Series';

    -- iPhone SE (2nd Generation)
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone SE (2nd Gen)', '2020-04-24');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone SE (2nd Gen)';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '64GB', '3GB', 'Black', 42500.00),
        (m_id, '64GB', '3GB', 'White', 42500.00),
        (m_id, '128GB', '3GB', 'Black', 47800.00),
        (m_id, '256GB', '3GB', 'White', 58300.00);

    -- iPhone SE (3rd Generation)
    INSERT IGNORE INTO models (series_id, name, release_date) VALUES (s_id, 'iPhone SE (3rd Gen)', '2022-03-18');
    SELECT id INTO m_id FROM models WHERE series_id = s_id AND name = 'iPhone SE (3rd Gen)';
    INSERT IGNORE INTO variants (model_id, storage, ram, color, base_price) VALUES
        (m_id, '64GB', '4GB', 'Midnight', 43900.00),
        (m_id, '64GB', '4GB', 'Starlight', 43900.00),
        (m_id, '128GB', '4GB', 'Midnight', 48900.00),
        (m_id, '128GB', '4GB', 'Starlight', 48900.00),
        (m_id, '256GB', '4GB', 'Midnight', 58900.00);

END$$
DELIMITER ;

CALL populate_iphone_catalog();
DROP PROCEDURE IF EXISTS populate_iphone_catalog;
