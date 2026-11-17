package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.InventarioEstilista;
import mx.desarrollo.entity.InventarioEstilistaId;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.math.BigDecimal;
import java.util.List;

public class InventarioDAO extends AbstractDAO<InventarioEstilista> {
    private final EntityManager entityManager;

    public InventarioDAO(EntityManager em) {
        super(InventarioEstilista.class);
        this.entityManager = em;
    }

    public List<InventarioEstilista> obtenerTodos() {
        entityManager.clear();
        return entityManager
                .createQuery("SELECT i FROM InventarioEstilista i ORDER BY i.id.idEstilista, i.id.idProducto", InventarioEstilista.class)
                .getResultList();
    }

    public List<InventarioEstilista> obtenerPorEmpleado(Integer idEmpleado) {
        entityManager.clear();
        return entityManager
                .createQuery("SELECT i FROM InventarioEstilista i WHERE i.id.idEstilista = :idEmpleado ORDER BY i.id.idProducto", InventarioEstilista.class)
                .setParameter("idEmpleado", idEmpleado)
                .getResultList();
    }

    public void guardarCantidad(Integer idEmpleado, Integer idProducto, Integer nuevaCantidad, BigDecimal precio, Integer stockMinimo, Integer idAdministrador) {
        execute(em -> {
            Producto producto = em.find(Producto.class, idProducto);
            if (producto == null) {
                throw new IllegalArgumentException("No se encontró el producto seleccionado.");
            }

            InventarioEstilistaId id = new InventarioEstilistaId();
            id.setIdEstilista(idEmpleado);
            id.setIdProducto(idProducto);
            InventarioEstilista inventario = em.find(InventarioEstilista.class, id);
            int cantidadAnterior = inventario == null || inventario.getCantidad() == null ? 0 : inventario.getCantidad();
            int diferencia = nuevaCantidad - cantidadAnterior;

            if (inventario == null) {
                inventario = new InventarioEstilista();
                inventario.setId(id);
                inventario.setCantidad(nuevaCantidad);
                inventario.setPrecio(precio);
                inventario.setStockMinimo(stockMinimo);
                em.persist(inventario);
            } else {
                inventario.setCantidad(nuevaCantidad);
                inventario.setPrecio(precio);
                inventario.setStockMinimo(stockMinimo);
            }

            if (diferencia != 0) {
                String tipo = diferencia > 0 ? "ENTRADA" : "SALIDA";
                em.createNativeQuery("INSERT INTO historial_inventario (id_producto, id_estilista, id_administrador, tipo, cantidad, fecha) VALUES (?, ?, ?, ?, ?, NOW())")
                        .setParameter(1, idProducto)
                        .setParameter(2, idEmpleado)
                        .setParameter(3, idAdministrador)
                        .setParameter(4, tipo)
                        .setParameter(5, Math.abs(diferencia))
                        .executeUpdate();
            }
            return null;
        });
    }

    public void eliminarInventario(Integer idEmpleado, Integer idProducto, Integer idAdministrador) {
        execute(em -> {
            InventarioEstilistaId id = new InventarioEstilistaId();
            id.setIdEstilista(idEmpleado);
            id.setIdProducto(idProducto);
            InventarioEstilista inventario = em.find(InventarioEstilista.class, id);
            if (inventario == null) {
                throw new IllegalArgumentException("No se encontró el producto en el inventario del empleado.");
            }
            int cantidad = inventario.getCantidad() == null ? 0 : inventario.getCantidad();
            if (cantidad > 0) {
                em.createNativeQuery("INSERT INTO historial_inventario (id_producto, id_estilista, id_administrador, tipo, cantidad, fecha) VALUES (?, ?, ?, ?, ?, NOW())")
                        .setParameter(1, idProducto)
                        .setParameter(2, idEmpleado)
                        .setParameter(3, idAdministrador)
                        .setParameter(4, "ELIMINACION")
                        .setParameter(5, cantidad)
                        .executeUpdate();
            }
            em.remove(inventario);
            return null;
        });
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
