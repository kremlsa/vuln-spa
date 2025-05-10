CREATE TABLE NOTE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    content VARCHAR(5000),
    author VARCHAR(255)
);

INSERT INTO note (author, title, content)
VALUES ('Чак', '1', 'Чак Норрис не спит. Он ждёт.');
INSERT INTO note (author, title, content)
VALUES ('Чак', '2', 'У Чака Норриса нет клавиши Backspace — он не ошибается.');
INSERT INTO note (author, title, content)
VALUES ('Чак', '3', 'Чак Норрис может открыть закрытую скобку.');
INSERT INTO note (author, title, content)
VALUES ('Чак', '4', 'Компилятор не предупреждает Чака Норриса. Он извиняется.');
INSERT INTO note (author, title, content)
VALUES ('Чак', '5', 'Чак Норрис может сделать SELECT * FROM users WHERE password="password" — и это будет безопасно.');
INSERT INTO note (author, title, content)
VALUES ('Чак', '6', 'IDE Чака Норриса — это cat >.');
INSERT INTO note (author, title, content)
VALUES ('Чак', '7', 'curl отправляет заголовок X-Chuck-Norris: true, если понимает, что его вызвал Чак.');