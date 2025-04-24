-- Таблица пользователей
CREATE TABLE IF NOT EXISTS USERS (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);

-- Таблица ролей (authorities)
CREATE TABLE IF NOT EXISTS AUTHORITIES (
    username VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES USERS(username)
);

-- Индекс на таблицу ролей
CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username ON AUTHORITIES (username, authority);

-- Для пользователя admin
INSERT INTO users (username, password, enabled) VALUES ('admin', '{noop}admin', true);
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');

-- Для пользователя user
INSERT INTO users (username, password, enabled) VALUES ('user', '{noop}user', true);
INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');

