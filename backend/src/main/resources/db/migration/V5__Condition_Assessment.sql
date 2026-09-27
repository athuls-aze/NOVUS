-- V5__Condition_Assessment.sql
-- Table for device condition assessments

CREATE TABLE condition_assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_device_id BIGINT NOT NULL,
    battery_health INT NOT NULL,
    display_condition VARCHAR(10) NOT NULL,
    body_condition VARCHAR(10) NOT NULL,
    camera_condition VARCHAR(10) NOT NULL,
    charging_condition VARCHAR(10) NOT NULL,
    overall_score INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_condition_user_device FOREIGN KEY (user_device_id) REFERENCES user_devices(id) ON DELETE CASCADE
);
