-- Script de creación de la BD en H2

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
    is_active BOOLEAN
);

-- Tabla de teléfonos
CREATE TABLE PHONE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(20),
    citycode VARCHAR(10),
    contrycode VARCHAR(10)
    , user_id UUID NOT NULL
    , FOREIGN KEY (user_id) REFERENCES "USER"(id)
);
