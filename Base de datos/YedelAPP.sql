SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS yedelapp CHARACTER SET utf8mb4;
USE yedelApp;

CREATE TABLE estilista (
    id_estilista INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido_p VARCHAR(100),
    apellido_m VARCHAR(100),
    especialidad VARCHAR(100),
    activo BOOLEAN
);

CREATE TABLE administrador (
    id_administrador INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido_p VARCHAR(100),
    apellido_m VARCHAR(100),
    correo VARCHAR(150),
    contrasena VARCHAR(255),
    telefono VARCHAR(20),
    id_estilista INT,
    CONSTRAINT fk_administrador_estilista
        FOREIGN KEY (id_estilista) REFERENCES estilista(id_estilista)
            ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido_p VARCHAR(100),
    apellido_m VARCHAR(100),
    correo VARCHAR(150),
    contrasena VARCHAR(255),
    telefono VARCHAR(20)
);

CREATE TABLE servicio (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    duracion_min INT,
    precio DECIMAL(10,2)
);

CREATE TABLE estilista_servicio (
    id_estilista INT,
    id_servicio INT,
    PRIMARY KEY (id_estilista, id_servicio),
    CONSTRAINT fk_estilista_servicio_estilista
        FOREIGN KEY (id_estilista) REFERENCES estilista(id_estilista)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_estilista_servicio_servicio
        FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio)
            ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE horario (
    id_horario INT AUTO_INCREMENT PRIMARY KEY,
    id_estilista INT,
    dia_semana VARCHAR(10),
    hora_inicio TIME,
    hora_fin TIME,
    CONSTRAINT fk_horario_estilista
        FOREIGN KEY (id_estilista) REFERENCES estilista(id_estilista)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE cita (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    id_administrador INT,
    id_estilista INT,
    fecha DATE,
    hora_inicio TIME,
    estado VARCHAR(20)
);

CREATE TABLE cita_servicio (
    id_cita INT NOT NULL,
    id_servicio INT NOT NULL,
    PRIMARY KEY (id_cita, id_servicio),
    CONSTRAINT fk_citaservicio_cita
        FOREIGN KEY (id_cita) REFERENCES cita(id_cita)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_citaservicio_servicio
        FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio)
            ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE producto (
    id_producto  INT AUTO_INCREMENT PRIMARY KEY,
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
    PRIMARY KEY (id_estilista, id_producto),
    CONSTRAINT fk_inventario_estilista
        FOREIGN KEY (id_estilista) REFERENCES estilista(id_estilista)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_inventario_producto
        FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
            ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE historial_inventario (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    id_estilista INT,
    id_administrador INT,
    tipo VARCHAR(20),
    cantidad INT,
    fecha DATETIME,
    CONSTRAINT fk_historial_producto
        FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_historial_estilista
        FOREIGN KEY (id_estilista) REFERENCES estilista(id_estilista)
            ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_historial_administrador
        FOREIGN KEY (id_administrador) REFERENCES administrador(id_administrador)
            ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO estilista (id_estilista, nombre, apellido_p, apellido_m, especialidad, activo) VALUES
    (1, 'Yedel',   '',      '',        'Belleza',  1),
    (2, 'Ernesto', 'Cota',  'Cazares', 'Barberia', 1);

INSERT INTO administrador (id_administrador, nombre, apellido_p, apellido_m, correo, contrasena, telefono, id_estilista) VALUES
    (1, 'Admin', 'Yedel', 'Salon','admin@yedel.com','admin123', '0000000000', 1),
    (3, 'Ernesto', 'Cota',  'Cazares',  'ernesto1@gmail.com', '12345678', '6867504136', 2);

INSERT INTO cliente (id_cliente, nombre, apellido_p, apellido_m, correo, contrasena, telefono) VALUES
    (1, 'Cliente', 'Prueba', 'Uno', 'cliente@yedel.com', 'cliente123', '0000000000');

INSERT INTO servicio (id_servicio, nombre, duracion_min, precio) VALUES
    (1, 'Color',         60,  500.00),
    (2, 'Alaseado',      60,  500.00),
    (3, 'Maquillaje',    60,  800.00),
    (4, 'Peinado',       60,  400.00),
    (5, 'Cortes',        60,  200.00),
    (6, 'Uñas',          60, 1200.00),
    (7, 'Pedicure',      60,  900.00),
    (8, 'Manicure',      60,  800.00),
    (9, 'Corte de pelo', 60,  200.00);

INSERT INTO estilista_servicio (id_estilista, id_servicio) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7),
    (1, 8),
    (2, 9);

INSERT INTO horario (id_horario, id_estilista, dia_semana, hora_inicio, hora_fin) VALUES
    (1,  1, 'Lunes',     '08:00:00', '20:00:00'),
    (2,  1, 'Martes',    '08:00:00', '20:00:00'),
    (3,  1, 'Miércoles', '08:00:00', '20:00:00'),
    (4,  1, 'Jueves',    '08:00:00', '20:00:00'),
    (5,  1, 'Viernes',   '08:00:00', '20:00:00'),
    (6,  1, 'Sábado',    '08:00:00', '20:00:00'),
    (7,  1, 'Domingo',   '08:00:00', '20:00:00'),
    (8,  2, 'Lunes',     '08:00:00', '20:00:00'),
    (9,  2, 'Martes',    '08:00:00', '20:00:00'),
    (10, 2, 'Miércoles', '08:00:00', '20:00:00'),
    (11, 2, 'Jueves',    '08:00:00', '20:00:00'),
    (12, 2, 'Viernes',   '08:00:00', '20:00:00'),
    (13, 2, 'Sábado',    '08:00:00', '20:00:00'),
    (14, 2, 'Domingo',   '08:00:00', '20:00:00');

INSERT INTO cita (id_cita, id_cliente, id_administrador, id_estilista, fecha, hora_inicio, estado) VALUES
    (4, 1, 3, 2, '2026-11-05', '17:00:00', 'Activa'),
    (8, 1, 1, 1, '2026-11-05', '09:30:00', 'Activa');

INSERT INTO cita_servicio (id_cita, id_servicio) VALUES
    (8, 1),
    (8, 2),
    (8, 3),
    (8, 4),
    (8, 5),
    (4, 9);