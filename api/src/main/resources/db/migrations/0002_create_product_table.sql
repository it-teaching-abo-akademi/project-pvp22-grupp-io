DROP TABLE IF EXISTS product CASCADE;
CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    name VARCHAR(100),
    old_id INT,
    keywords TEXT
);
