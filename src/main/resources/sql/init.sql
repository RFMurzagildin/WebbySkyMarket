CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    firstname VARCHAR(64) NOT NULL,
    lastname VARCHAR(64) NOT NULL,
    role VARCHAR(32) NOT NULL,
    password VARCHAR(128) NOT NULL,
    email VARCHAR(64),
    phone_number VARCHAR(64),
    status VARCHAR(32) NOT NULL,
    gander VARCHAR(16) NOT NULL
);

CREATE TABLE IF NOT EXISTS products(
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(64) NOT NULL,
   description VARCHAR(2048) NOT NULL ,
   price DECIMAL(10, 2) NOT NULL,
   image_path_1 VARCHAR(256) NOT NULL,
   image_path_2 VARCHAR(256),
   image_path_3 VARCHAR(256),
   category VARCHAR(64) NOT NULL,
   condition VARCHAR(16) NOT NULL,
   status VARCHAR(16) NOT NULL,
   user_id INTEGER REFERENCES users(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS feedbacks(
    id BIGSERIAL PRIMARY KEY,
    sender_id INTEGER REFERENCES users(id),
    recipient_id INTEGER REFERENCES users(id),
    description VARCHAR(1024),
    stars INT CHECK (stars BETWEEN 1 AND 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
