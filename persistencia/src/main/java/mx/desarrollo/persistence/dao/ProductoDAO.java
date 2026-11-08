package mx.desarrollo.persistence.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import mx.desarrollo.persistence.persistence.HibernateUtil;
import src.main.java.mx.desarrollo.entity.Producto;

public class ProductoDAO {

    // Método para dar de baja lógica a un producto
    public void darDeBaja(Long idProducto) {
        EntityManager em = HibernateUtil.getEntityManager(); // O la forma en que obtengas tu EntityManager
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Producto producto = em.find(Producto.class, idProducto);
            if (producto != null) {
                em.remove(producto); // Esto lo borra físicamente de la tabla
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al dar de baja el producto en la base de datos", e);
        } finally {
            em.close();
        }
    }
}
