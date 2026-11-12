-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 05-11-2026 a las 18:26:54
-- Versión del servidor: 8.0.17
-- Versión de PHP: 7.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `yedelapp`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administrador`
--

CREATE DATABASE yedelapp;
use yedelapp;

CREATE TABLE `administrador` (
                                 `id_administrador` int(11) NOT NULL,
                                 `nombre` varchar(100) DEFAULT NULL,
                                 `apellido_p` varchar(100) DEFAULT NULL,
                                 `apellido_m` varchar(100) DEFAULT NULL,
                                 `correo` varchar(150) DEFAULT NULL,
                                 `contrasena` varchar(255) DEFAULT NULL,
                                 `telefono` varchar(20) DEFAULT NULL,
                                 `id_estilista` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `administrador`
--

INSERT INTO `administrador` (`id_administrador`, `nombre`, `apellido_p`, `apellido_m`, `correo`, `contrasena`, `telefono`, `id_estilista`) VALUES
                                                                                                                                               (1, 'Admin', 'Yedel', 'Salon', 'admin@yedel.com', 'admin123', '0000000000', 1),
                                                                                                                                               (3, 'Ernesto', 'Cota', 'Cazares', 'ernesto1@gmail.com', '12345678', '6867504136', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita`
--

CREATE TABLE `cita` (
                        `id_cita` int(11) NOT NULL,
                        `id_cliente` int(11) DEFAULT NULL,
                        `id_administrador` int(11) DEFAULT NULL,
                        `id_estilista` int(11) DEFAULT NULL,
                        `id_servicio` int(11) DEFAULT NULL,
                        `fecha` date DEFAULT NULL,
                        `hora_inicio` time DEFAULT NULL,
                        `estado` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cita`
--

INSERT INTO `cita` (`id_cita`, `id_cliente`, `id_administrador`, `id_estilista`, `id_servicio`, `fecha`, `hora_inicio`, `estado`) VALUES
                                                                                                                                      (4, 1, 3, 2, NULL, '2026-11-05', '17:00:00', 'Activa'),
                                                                                                                                      (8, 1, 1, 1, NULL, '2026-11-05', '09:30:00', 'Activa');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita_servicio`
--

CREATE TABLE `cita_servicio` (
                                 `id_cita` int(11) NOT NULL,
                                 `id_servicio` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cita_servicio`
--

INSERT INTO `cita_servicio` (`id_cita`, `id_servicio`) VALUES
                                                           (8, 1),
                                                           (8, 2),
                                                           (8, 3),
                                                           (8, 4),
                                                           (8, 5),
                                                           (4, 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
                           `id_cliente` int(11) NOT NULL,
                           `nombre` varchar(100) DEFAULT NULL,
                           `apellido_p` varchar(100) DEFAULT NULL,
                           `apellido_m` varchar(100) DEFAULT NULL,
                           `correo` varchar(150) DEFAULT NULL,
                           `contrasena` varchar(255) DEFAULT NULL,
                           `telefono` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id_cliente`, `nombre`, `apellido_p`, `apellido_m`, `correo`, `contrasena`, `telefono`) VALUES
    (1, 'Cliente', 'Prueba', 'Uno', 'cliente@yedel.com', 'cliente123', '0000000000');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estilista`
--

CREATE TABLE `estilista` (
                             `id_estilista` int(11) NOT NULL,
                             `nombre` varchar(100) DEFAULT NULL,
                             `apellido_p` varchar(100) DEFAULT NULL,
                             `apellido_m` varchar(100) DEFAULT NULL,
                             `especialidad` varchar(100) DEFAULT NULL,
                             `activo` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estilista`
--

INSERT INTO `estilista` (`id_estilista`, `nombre`, `apellido_p`, `apellido_m`, `especialidad`, `activo`) VALUES
                                                                                                             (1, 'Yedel', '', '', 'Belleza', 1),
                                                                                                             (2, 'Ernesto', 'Cota', 'Cazares', 'Barberia', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estilista_servicio`
--

CREATE TABLE `estilista_servicio` (
                                      `id_estilista` int(11) NOT NULL,
                                      `id_servicio` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estilista_servicio`
--

INSERT INTO `estilista_servicio` (`id_estilista`, `id_servicio`) VALUES
                                                                     (1, 1),
                                                                     (1, 2),
                                                                     (1, 3),
                                                                     (1, 4),
                                                                     (1, 5),
                                                                     (1, 6),
                                                                     (1, 7),
                                                                     (1, 8),
                                                                     (2, 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_inventario`
--

CREATE TABLE `historial_inventario` (
                                        `id_historial` int(11) NOT NULL,
                                        `id_producto` int(11) DEFAULT NULL,
                                        `id_estilista` int(11) DEFAULT NULL,
                                        `id_administrador` int(11) DEFAULT NULL,
                                        `tipo` varchar(20) DEFAULT NULL,
                                        `cantidad` int(11) DEFAULT NULL,
                                        `fecha` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `horario`
--

CREATE TABLE `horario` (
                           `id_horario` int(11) NOT NULL,
                           `id_estilista` int(11) DEFAULT NULL,
                           `dia_semana` varchar(10) DEFAULT NULL,
                           `hora_inicio` time DEFAULT NULL,
                           `hora_fin` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `horario`
--

INSERT INTO `horario` (`id_horario`, `id_estilista`, `dia_semana`, `hora_inicio`, `hora_fin`) VALUES
                                                                                                  (1, 1, 'Lunes', '08:00:00', '20:00:00'),
                                                                                                  (2, 1, 'Martes', '08:00:00', '20:00:00'),
                                                                                                  (3, 1, 'Miércoles', '08:00:00', '20:00:00'),
                                                                                                  (4, 1, 'Jueves', '08:00:00', '20:00:00'),
                                                                                                  (5, 1, 'Viernes', '08:00:00', '20:00:00'),
                                                                                                  (6, 1, 'Sábado', '08:00:00', '20:00:00'),
                                                                                                  (7, 1, 'Domingo', '08:00:00', '20:00:00'),
                                                                                                  (8, 2, 'Lunes', '08:00:00', '20:00:00'),
                                                                                                  (9, 2, 'Martes', '08:00:00', '20:00:00'),
                                                                                                  (10, 2, 'Miércoles', '08:00:00', '20:00:00'),
                                                                                                  (11, 2, 'Jueves', '08:00:00', '20:00:00'),
                                                                                                  (12, 2, 'Viernes', '08:00:00', '20:00:00'),
                                                                                                  (13, 2, 'Sábado', '08:00:00', '20:00:00'),
                                                                                                  (14, 2, 'Domingo', '08:00:00', '20:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario_estilista`
--

CREATE TABLE `inventario_estilista` (
                                        `id_estilista` int(11) NOT NULL,
                                        `id_producto` int(11) NOT NULL,
                                        `cantidad` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
                            `id_producto` int(11) NOT NULL AUTO_INCREMENT,
                            `nombre` varchar(100) DEFAULT NULL,
                            `categoria` varchar(50) DEFAULT NULL,
                            `precio` decimal(10,2) DEFAULT NULL,
                            `stock_bodega` int(11) DEFAULT NULL,
                            `stock_minimo` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicio`
--

CREATE TABLE `servicio` (
                            `id_servicio` int(11) NOT NULL,
                            `nombre` varchar(100) DEFAULT NULL,
                            `duracion_min` int(11) DEFAULT NULL,
                            `precio` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `servicio`
--

INSERT INTO `servicio` (`id_servicio`, `nombre`, `duracion_min`, `precio`) VALUES
                                                                               (1, 'Color', 60, '500.00'),
                                                                               (2, 'Alaseado', 60, '500.00'),
                                                                               (3, 'Maquillaje', 60, '800.00'),
                                                                               (4, 'Peinado', 60, '400.00'),
                                                                               (5, 'Cortes', 60, '200.00'),
                                                                               (6, 'Uñas', 60, '1200.00'),
                                                                               (7, 'Pedicure', 60, '900.00'),
                                                                               (8, 'Manicure', 60, '800.00'),
                                                                               (9, 'Corte de pelo', 60, '200.00');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administrador`
--
ALTER TABLE `administrador`
    ADD PRIMARY KEY (`id_administrador`);

--
-- Indices de la tabla `cita`
--
ALTER TABLE `cita`
    ADD PRIMARY KEY (`id_cita`);

--
-- Indices de la tabla `cita_servicio`
--
ALTER TABLE `cita_servicio`
    ADD PRIMARY KEY (`id_cita`,`id_servicio`),
  ADD KEY `fk_citaservicio_servicio` (`id_servicio`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
    ADD PRIMARY KEY (`id_cliente`);

--
-- Indices de la tabla `estilista`
--
ALTER TABLE `estilista`
    ADD PRIMARY KEY (`id_estilista`);

--
-- Indices de la tabla `estilista_servicio`
--
ALTER TABLE `estilista_servicio`
    ADD PRIMARY KEY (`id_estilista`,`id_servicio`);

--
-- Indices de la tabla `historial_inventario`
--
ALTER TABLE `historial_inventario`
    ADD PRIMARY KEY (`id_historial`);

--
-- Indices de la tabla `horario`
--
ALTER TABLE `horario`
    ADD PRIMARY KEY (`id_horario`);

--
-- Indices de la tabla `inventario_estilista`
--
ALTER TABLE `inventario_estilista`
    ADD PRIMARY KEY (`id_estilista`,`id_producto`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
    ADD PRIMARY KEY (`id_producto`);

--
-- Indices de la tabla `servicio`
--
ALTER TABLE `servicio`
    ADD PRIMARY KEY (`id_servicio`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `administrador`
--
ALTER TABLE `administrador`
    MODIFY `id_administrador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `cita`
--
ALTER TABLE `cita`
    MODIFY `id_cita` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
    MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `estilista`
--
ALTER TABLE `estilista`
    MODIFY `id_estilista` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `horario`
--
ALTER TABLE `horario`
    MODIFY `id_horario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `servicio`
--
ALTER TABLE `servicio`
    MODIFY `id_servicio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cita_servicio`
--
ALTER TABLE `cita_servicio`
    ADD CONSTRAINT `fk_citaservicio_cita` FOREIGN KEY (`id_cita`) REFERENCES `cita` (`id_cita`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_citaservicio_servicio` FOREIGN KEY (`id_servicio`) REFERENCES `servicio` (`id_servicio`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
