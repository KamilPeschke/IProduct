CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

INSERT INTO users (username, password, email, created_at, updated_at, deleted_at) VALUES
('user1', 'User1', 'admin@example.com', CURRENT_TIMESTAMP, NULL, NULL),
('user2', 'user2', 'user1@example.com', CURRENT_TIMESTAMP, NULL, NULL),
('user3', 'user3', 'admin1@example.com', CURRENT_TIMESTAMP, NULL, NULL),
('user4', 'user4', 'user12@example.com', CURRENT_TIMESTAMP, NULL, NULL);