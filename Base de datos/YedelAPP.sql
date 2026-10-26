CREATE DATABASE IF NOT EXISTS YedelAPP
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE YedelAPP;

CREATE TABLE administrador (
    id_administrador INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(20)
) ENGINE=InnoDB;

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(20)
) ENGINE=InnoDB;

CREATE TABLE estilista (
    id_estilista INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE servicio (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    duracion_min INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    CONSTRAINT chk_servicio_duracion CHECK (duracion_min > 0),
    CONSTRAINT chk_servicio_precio CHECK (precio >= 0)
) ENGINE=InnoDB;

CREATE TABLE estilista_servicio (
    id_estilista INT NOT NULL,
    id_servicio INT NOT NULL,
    PRIMARY KEY (id_estilista, id_servicio),
    CONSTRAINT fk_es_estilista FOREIGN KEY (id_estilista)
        REFERENCES estilista (id_estilista)
        ON DELETE CASCADE,
    CONSTRAINT fk_es_servicio FOREIGN KEY (id_servicio)
        REFERENCES servicio (id_servicio)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE horario (
    id_horario INT AUTO_INCREMENT PRIMARY KEY,
    id_estilista INT NOT NULL,
    dia_semana ENUM('lunes','martes','miercoles','jueves','viernes','sabado','domingo') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    CONSTRAINT chk_horario_horas CHECK (hora_fin > hora_inicio),
    CONSTRAINT fk_horario_estilista FOREIGN KEY (id_estilista)
        REFERENCES estilista (id_estilista)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE cita (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_administrador INT NULL,
    id_estilista INT NOT NULL,
    id_servicio INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    estado ENUM('pendiente','confirmada','completada','cancelada') NOT NULL DEFAULT 'pendiente',
    INDEX idx_cita_estilista_fecha (id_estilista, fecha, hora_inicio),
    CONSTRAINT fk_cita_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
        ON DELETE RESTRICT,
    CONSTRAINT fk_cita_administrador FOREIGN KEY (id_administrador)
        REFERENCES administrador (id_administrador)
        ON DELETE SET NULL,
    CONSTRAINT fk_cita_estilista FOREIGN KEY (id_estilista)
        REFERENCES estilista (id_estilista)
        ON DELETE RESTRICT,
    CONSTRAINT fk_cita_servicio FOREIGN KEY (id_servicio)
        REFERENCES servicio (id_servicio)
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    precio DECIMAL(10,2) NOT NULL,
    stock_bodega INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 0,
    CONSTRAINT chk_producto_precio CHECK (precio >= 0),
    CONSTRAINT chk_producto_stock CHECK (stock_bodega >= 0),
    CONSTRAINT chk_producto_minimo CHECK (stock_minimo >= 0)
) ENGINE=InnoDB;

CREATE TABLE inventario_estilista (
    id_estilista INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id_estilista, id_producto),
    CONSTRAINT chk_inventario_cantidad CHECK (cantidad >= 0),
    CONSTRAINT fk_inv_estilista FOREIGN KEY (id_estilista)
        REFERENCES estilista (id_estilista)
        ON DELETE RESTRICT,
    CONSTRAINT fk_inv_producto FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE historial_inventario (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_estilista INT NULL,
    id_administrador INT NOT NULL,
    tipo ENUM('entrada','asignacion','salida','ajuste') NOT NULL,
    cantidad INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_historial_cantidad CHECK (cantidad > 0),
    CONSTRAINT fk_hist_producto FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
        ON DELETE RESTRICT,
    CONSTRAINT fk_hist_estilista FOREIGN KEY (id_estilista)
        REFERENCES estilista (id_estilista)
        ON DELETE RESTRICT,
    CONSTRAINT fk_hist_administrador FOREIGN KEY (id_administrador)
        REFERENCES administrador (id_administrador)
        ON DELETE RESTRICT
) ENGINE=InnoDB;