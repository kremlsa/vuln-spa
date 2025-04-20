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

-- Вставка тестового пользователя admin/admin с ролью ROLE_ADMIN
INSERT INTO USERS (username, password, enabled) VALUES ('admin', '{noop}admin', true);
INSERT INTO AUTHORITIES (username, authority) VALUES ('admin', 'ROLE_ADMIN');
