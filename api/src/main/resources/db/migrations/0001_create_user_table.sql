CREATE TABLE IF NOT EXISTS "user" (
    id SERIAL PRIMARY KEY,
    customer_reference UUID NOT NULL,
    name VARCHAR(100) NOT NULL
);
