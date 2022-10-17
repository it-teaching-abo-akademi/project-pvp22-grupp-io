DROP TABLE IF EXISTS order_line CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS payment_type CASCADE;
DROP TABLE IF EXISTS "order" CASCADE;

CREATE TABLE IF NOT EXISTS "order" (
    id SERIAL PRIMARY KEY,
    user_id INT,
    total_price INT,
    completed BOOLEAN,
    CONSTRAINT user_fk FOREIGN KEY(user_id) REFERENCES "user"(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS order_line (
    id SERIAL PRIMARY KEY,
    total_price INT NOT NULL,
    unit_price INT,
    quantity INT NOT NULL,
    order_id INT NOT NULL,
    product_id INT,
    CONSTRAINT product_fk FOREIGN KEY(product_id) REFERENCES product(id) ON DELETE RESTRICT,
    CONSTRAINT order_fk FOREIGN KEY(order_id) REFERENCES "order"(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS payment_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS payment (
    id SERIAL PRIMARY KEY,
    amount INT NOT NULL,
    payment_type_id INT NOT NULL,
    order_id INT NOT NULL,
    CONSTRAINT payment_type_fk FOREIGN KEY(payment_type_id) REFERENCES payment_type(id) ON DELETE RESTRICT,
    CONSTRAINT payment_line_fk FOREIGN KEY(order_id) REFERENCES "order"(id) ON DELETE RESTRICT
);
