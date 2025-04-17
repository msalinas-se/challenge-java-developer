-- Script de creación de la BD en H2

DROP TABLE IF EXISTS USER_PHONES;
DROP TABLE IF EXISTS PHONE;
DROP TABLE IF EXISTS "USER";

-- Tabla de usuarios
CREATE TABLE "USER" (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created TIMESTAMP,
    modified TIMESTAMP,
    last_login TIMESTAMP,
    token VARCHAR(512),
    isactive BOOLEAN
);

-- Tabla de teléfonos
CREATE TABLE PHONE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(20),
    citycode VARCHAR(10),
    contrycode VARCHAR(10)
);

-- Join table para relación unidireccional OneToMany
CREATE TABLE USER_PHONES (
    USER_ID UUID NOT NULL,
    PHONES_ID BIGINT NOT NULL,
    PRIMARY KEY (USER_ID, PHONES_ID),
    FOREIGN KEY (USER_ID) REFERENCES "USER"(id),
    FOREIGN KEY (PHONES_ID) REFERENCES PHONE(id)
);
