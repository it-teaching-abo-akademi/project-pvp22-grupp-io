CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL
);