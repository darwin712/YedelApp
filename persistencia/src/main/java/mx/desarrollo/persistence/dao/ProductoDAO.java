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

    @Override
      public EntityManager getEntityManager() {
        return entityManager;
    }
}

