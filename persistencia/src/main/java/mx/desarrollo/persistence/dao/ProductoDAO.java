package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;

public class ProductoDAO extends AbstractDAO<Producto> {
    private final EntityManager entityManager;

    public ProductoDAO(EntityManager em) {
        super(Producto.class);
        this.entityManager = em;
    }

    public List<Producto> obtenerTodos() {
        entityManager.clear();
        return entityManager
                .createQuery("SELECT p FROM Producto p ORDER BY p.nombre", Producto.class)
                .getResultList();
    }

    public void eliminar(Integer idProducto) {
        execute(em -> {
            Producto producto = em.find(Producto.class, idProducto);
            if (producto == null) {
                throw new IllegalArgumentException("No se encontró el producto seleccionado.");
            }
            em.createNativeQuery("DELETE FROM historial_inventario WHERE id_producto = ?")
                    .setParameter(1, idProducto)
                    .executeUpdate();
            em.createNativeQuery("DELETE FROM inventario_estilista WHERE id_producto = ?")
                    .setParameter(1, idProducto)
                    .executeUpdate();
            em.remove(producto);
            return null;
        });
    }

    @Override
      public EntityManager getEntityManager() {
        return entityManager;
    }
}

