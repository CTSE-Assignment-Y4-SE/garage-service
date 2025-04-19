-- File: ddl-script-v1.sql

DROP TABLE IF EXISTS garages;

CREATE TABLE garages (
     garage_id BIGSERIAL PRIMARY KEY,
     garage_name VARCHAR(255) NOT NULL,
     website VARCHAR(255),
     phone_number VARCHAR(30),
     address VARCHAR(255),
     social_media VARCHAR(255),
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
