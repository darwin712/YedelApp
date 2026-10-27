CREATE DATABASE yedelApp;

USE yedelApp;

CREATE TABLE administrador (
    id_administrador INT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido_p VARCHAR(100),
    apellido_m VARCHAR(100),
    correo VARCHAR(150),
    contrasena VARCHAR(255),
    telefono VARCHAR(20)
);

CREATE TABLE cliente (
    id_cliente INT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido_p VARCHAR(100),
    apellido_m VARCHAR(100),
    correo VARCHAR(150),
    contrasena VARCHAR(255),
    telefono VARCHAR(20)
);

CREATE TABLE estilista (
    id_estilista INT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido_p VARCHAR(100),
    apellido_m VARCHAR(100),
    especialidad VARCHAR(100),
    activo BOOLEAN
);

CREATE TABLE servicio (
    id_servicio INT PRIMARY KEY,
    nombre VARCHAR(100),
    duracion_min INT,
    precio DECIMAL(10,2)
);

CREATE TABLE estilista_servicio (
    id_estilista INT,
    id_servicio INT,
    PRIMARY KEY (id_estilista, id_servicio)
);

CREATE TABLE horario (
    id_horario INT PRIMARY KEY,
    id_estilista INT,
    dia_semana VARCHAR(10),
    hora_inicio TIME,
    hora_fin TIME
);

CREATE TABLE cita (
    id_cita INT PRIMARY KEY,
    id_cliente INT,
    id_administrador INT,
    id_estilista INT,
    id_servicio INT,
    fecha DATE,
    hora_inicio TIME,
    estado VARCHAR(20)
);

CREATE TABLE producto (
    id_producto INT PRIMARY KEY,
    nombre VARCHAR(100),
    categoria VARCHAR(50),
    precio DECIMAL(10,2),
    stock_bodega INT,
    stock_minimo INT
);

CREATE TABLE inventario_estilista (
    id_estilista INT,
    id_producto INT,
    cantidad INT,
    PRIMARY KEY (id_estilista, id_producto)
);

CREATE TABLE historial_inventario (
    id_historial INT PRIMARY KEY,
    id_producto INT,
    id_estilista INT,
    id_administrador INT,
    tipo VARCHAR(20),
    cantidad INT,
    fecha DATETIME
);
