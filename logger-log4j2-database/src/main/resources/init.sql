CREATE
USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'password';
CREATE
DATABASE IF NOT EXISTS test_db;
USE
test_db;

CREATE TABLE LOG_TABLE
(
    ID         INT PRIMARY KEY AUTO_INCREMENT,
    EVENT_DATE TIMESTAMP,
    LEVEL      VARCHAR(10),
    LOGGER     VARCHAR(255),
    MESSAGE    VARCHAR(4000)
);
