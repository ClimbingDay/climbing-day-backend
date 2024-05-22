CREATE TABLE member
(
    id                      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    email                   VARCHAR(30)     NOT NULL,
    password                VARCHAR(80)     NOT NULL,
    name                    VARCHAR(20)     NOT NULL,
    birthDate               DATE            NOT NULL,
    phoneNumber             VARCHAR(20)     NOT NULL,
    status                  VARCHAR(20)     NOT NULL,
    roles                   VARCHAR(20)     NOT NULL
);