CREATE TABLE IF NOT EXISTS orders
(
    id         VARCHAR PRIMARY KEY NOT NULL,
    user_id    VARCHAR             NULL,
    account_id VARCHAR             NULL,
    order_name VARCHAR             NULL,
    pay_status VARCHAR             NULL,
    created_at TIMESTAMP           default current_timestamp,
    updated_at TIMESTAMP           default current_timestamp
);
