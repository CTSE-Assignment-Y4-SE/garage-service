-- File: ddl-script-v2.sql

DROP TABLE IF EXISTS booking_requests;

CREATE TABLE booking_requests (
    booking_id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    booking_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_slot FOREIGN KEY (slot_id) REFERENCES service_slots (slot_id) ON DELETE CASCADE
);
