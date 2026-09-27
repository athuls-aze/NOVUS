-- V4__Warranty.sql
-- Table for warranty tracking

CREATE TABLE warranties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_device_id BIGINT NOT NULL,
    warranty_months INT NOT NULL,
    starts_at DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_warranties_user_device FOREIGN KEY (user_device_id) REFERENCES user_devices(id) ON DELETE CASCADE,
    CONSTRAINT uq_user_device_warranty UNIQUE (user_device_id)
);
