-- File: ddl-script-v2.sql

DROP TABLE IF EXISTS service_slots;

CREATE TABLE service_slots (
    slot_id SERIAL PRIMARY KEY,
    service_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    total_slots INT NOT NULL,
    available_slots INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
