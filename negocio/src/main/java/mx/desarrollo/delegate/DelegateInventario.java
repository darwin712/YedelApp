package mx.desarrollo.delegate;

import mx.desarrollo.entity.InventarioEstilista;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.math.BigDecimal;
import java.util.List;

public class DelegateInventario {
    public List<Producto> obtenerProductos() {
        return ServiceLocator.getInstanceProductoDAO().obtenerTodos();
    }

    public void guardarProducto(Producto producto) {
        ServiceLocator.getInstanceProductoDAO().save(producto);
    }

    public void actualizarProducto(Producto producto) {
        ServiceLocator.getInstanceProductoDAO().update(producto);
    }

    public void eliminarProducto(Integer idProducto) {
        ServiceLocator.getInstanceProductoDAO().eliminar(idProducto);
    }

    public List<InventarioEstilista> obtenerInventarios() {
        return ServiceLocator.getInstanceInventarioDAO().obtenerTodos();
    }

    public List<InventarioEstilista> obtenerPorEmpleado(Integer idEmpleado) {
        return ServiceLocator.getInstanceInventarioDAO().obtenerPorEmpleado(idEmpleado);
    }

    public void guardarCantidad(Integer idEmpleado, Integer idProducto, Integer cantidad, BigDecimal precio, Integer stockMinimo, Integer idAdministrador) {
        ServiceLocator.getInstanceInventarioDAO().guardarCantidad(idEmpleado, idProducto, cantidad, precio, stockMinimo, idAdministrador);
    }

    public void eliminarInventario(Integer idEmpleado, Integer idProducto, Integer idAdministrador) {
        ServiceLocator.getInstanceInventarioDAO().eliminarInventario(idEmpleado, idProducto, idAdministrador);
    }
}
