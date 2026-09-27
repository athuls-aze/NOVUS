-- V6__Market_Data.sql

CREATE TABLE market_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    variant_id BIGINT NOT NULL,
    record_date DATE NOT NULL,
    average_market_price DECIMAL(10,2) NOT NULL,
    trend_indicator VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_market_data_variant FOREIGN KEY (variant_id) REFERENCES variants(id) ON DELETE CASCADE,
    CONSTRAINT uq_variant_record_date UNIQUE (variant_id, record_date)
);
