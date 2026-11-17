package mx.desarrollo.persistence.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HibernateUtil {
    private static EntityManagerFactory entityManagerFactory;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            try {
                prepararEsquema();
                entityManagerFactory = Persistence.createEntityManagerFactory("persistencePU");
            } catch (Exception e) {
                throw new IllegalStateException("No se pudo crear el EntityManagerFactory", e);
            }
        }
        return entityManagerFactory;
    }

    private static void prepararEsquema() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontró el controlador de MySQL", e);
        }
        try (Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/yedelapp?useSSL=false&serverTimezone=UTC",
                "root",
                "12345678")) {
            DatabaseMetaData datos = conexion.getMetaData();
            boolean existeColumna;
            try (ResultSet columnas = datos.getColumns("yedelapp", null, "producto", "id_administrador")) {
                existeColumna = columnas.next();
            }
            try (Statement consulta = conexion.createStatement()) {
                if (!existeColumna) {
                    consulta.executeUpdate("ALTER TABLE producto ADD COLUMN id_administrador INT NULL AFTER id_producto");
                }
                consulta.executeUpdate("UPDATE producto SET id_administrador = 1 WHERE id_administrador IS NULL");
            }
            boolean existePrecioInventario;
            try (ResultSet columnas = datos.getColumns("yedelapp", null, "inventario_estilista", "precio")) {
                existePrecioInventario = columnas.next();
            }
            boolean existeMinimoInventario;
            try (ResultSet columnas = datos.getColumns("yedelapp", null, "inventario_estilista", "stock_minimo")) {
                existeMinimoInventario = columnas.next();
            }
            try (Statement consulta = conexion.createStatement()) {
                if (!existePrecioInventario) {
                    consulta.executeUpdate("ALTER TABLE inventario_estilista ADD COLUMN precio DECIMAL(10,2) NULL AFTER cantidad");
                }
                if (!existeMinimoInventario) {
                    consulta.executeUpdate("ALTER TABLE inventario_estilista ADD COLUMN stock_minimo INT NULL AFTER precio");
                }
                consulta.executeUpdate("UPDATE inventario_estilista i INNER JOIN producto p ON p.id_producto = i.id_producto SET i.precio = COALESCE(i.precio, p.precio, 0), i.stock_minimo = COALESCE(i.stock_minimo, p.stock_minimo, 0)");
            }
            boolean existeLlave = false;
            try (ResultSet llaves = datos.getImportedKeys("yedelapp", null, "producto")) {
                while (llaves.next()) {
                    if ("fk_producto_administrador".equalsIgnoreCase(llaves.getString("FK_NAME"))) {
                        existeLlave = true;
                        break;
                    }
                }
            }
            if (!existeLlave) {
                try (Statement consulta = conexion.createStatement()) {
                    consulta.executeUpdate("ALTER TABLE producto ADD CONSTRAINT fk_producto_administrador FOREIGN KEY (id_administrador) REFERENCES administrador(id_administrador) ON DELETE SET NULL ON UPDATE CASCADE");
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo preparar la base de datos", e);
        }
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    public static void close() {
        shutdown();
    }
}
