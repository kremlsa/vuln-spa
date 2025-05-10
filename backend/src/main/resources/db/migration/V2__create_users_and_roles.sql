-- Таблица пользователей
CREATE TABLE IF NOT EXISTS USERS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,        -- добавили id
    username VARCHAR(255) NOT NULL UNIQUE,       -- теперь уникальный, но не PK
    password VARCHAR(255) NOT NULL,
    is_vip BOOLEAN NOT NULL DEFAULT FALSE,       -- булев тип и snake_case
    enabled BOOLEAN NOT NULL
);

-- Таблица ролей (authorities) остаётся, как есть:
CREATE TABLE IF NOT EXISTS AUTHORITIES (
    username VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    CONSTRAINT fk_authorities_users
      FOREIGN KEY(username) REFERENCES USERS(username)
);

-- Индекс на таблицу ролей
CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username
  ON AUTHORITIES (username, authority);

-- Данные для admin
INSERT INTO users (username, password, is_vip, enabled)
VALUES ('admin', '5ebe2294ecd0e0f08eab7690d2a6ee69', TRUE, TRUE);

INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_ADMIN');

-- Для user
INSERT INTO users (username, password, is_vip, enabled)
VALUES ('user', 'ee11cbb19052e40b07aac0ca060c23ee', FALSE, TRUE);

INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER');
