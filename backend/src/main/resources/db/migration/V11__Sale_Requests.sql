-- V11__Sale_Requests.sql
CREATE TABLE sale_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_device_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    selected_offer_id BIGINT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_sale_user_device FOREIGN KEY (user_device_id) REFERENCES user_devices(id) ON DELETE CASCADE,
    CONSTRAINT fk_sale_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_sale_offer FOREIGN KEY (selected_offer_id) REFERENCES offers(id) ON DELETE SET NULL
);
