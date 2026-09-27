-- V8__Valuations.sql
-- Table for storing calculated valuations

CREATE TABLE valuations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_device_id BIGINT NOT NULL,
    estimated_value DECIMAL(10,2) NOT NULL,
    lower_range DECIMAL(10,2) NOT NULL,
    upper_range DECIMAL(10,2) NOT NULL,
    confidence_score INT NOT NULL,
    condition_score INT NOT NULL,
    age_months INT NOT NULL,
    trend_multiplier DECIMAL(5,4) NOT NULL,
    explanation TEXT,
    calculated_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_valuations_user_device FOREIGN KEY (user_device_id) REFERENCES user_devices(id) ON DELETE CASCADE
);
