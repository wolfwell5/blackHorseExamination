CREATE TABLE IF NOT EXISTS payment_response
(
    id         VARCHAR   NOT NULL UNIQUE PRIMARY KEY,
    code       VARCHAR,
    created_at TIMESTAMP NULL default current_timestamp,
    updated_at TIMESTAMP NULL default current_timestamp
)
