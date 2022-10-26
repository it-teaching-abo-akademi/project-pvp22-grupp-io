DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS bonus_card CASCADE;

CREATE TABLE IF NOT EXISTS "user" (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_day VARCHAR(32),
    sex VARCHAR(50),
    bonus_points INT
);

CREATE TABLE IF NOT EXISTS bonus_card (
   id SERIAL PRIMARY KEY,
   number VARCHAR(100),
   good_thru_month INT,
   good_thru_year INT,
   blocked BOOLEAN,
   expired BOOLEAN,
   holderName VARCHAR(100),
   user_id INT,
   CONSTRAINT user_fk FOREIGN KEY(user_id) REFERENCES "user"(id) ON DELETE CASCADE
);
