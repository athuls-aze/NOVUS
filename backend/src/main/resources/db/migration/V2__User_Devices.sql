-- V2__User_Devices.sql
-- Table for registering user devices

CREATE TABLE user_devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    variant_id BIGINT NOT NULL,
    purchase_date DATE,
    purchase_price DECIMAL(10, 2),
    storage_override VARCHAR(50),
    color_override VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_devices_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_devices_variant FOREIGN KEY (variant_id) REFERENCES variants(id) ON DELETE CASCADE
);
