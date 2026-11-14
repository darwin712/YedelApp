package mx.desarrollo.persistence.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase utilitaria para inicializar y obtener el {@link EntityManagerFactory}.
 */

public class HibernateUtil {
    private static EntityManagerFactory emf;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("persistencePU");
            } catch (Throwable ex) {
                ex.printStackTrace();   // muestra toda la cadena de "Caused by"
                throw new IllegalStateException("No se pudo crear el EntityManagerFactory", ex);
            }
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
