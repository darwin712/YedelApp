package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateInventario;
import mx.desarrollo.entity.InventarioEstilista;
import mx.desarrollo.entity.Producto;

import java.math.BigDecimal;
import java.util.List;

public class FacadeInventario {
    private final DelegateInventario delegateInventario;

    public FacadeInventario() {
        delegateInventario = new DelegateInventario();
    }

    public List<Producto> obtenerProductos() {
        return delegateInventario.obtenerProductos();
    }

    public void guardarProducto(Producto producto) {
        delegateInventario.guardarProducto(producto);
    }

    public void actualizarProducto(Producto producto) {
        delegateInventario.actualizarProducto(producto);
    }

    public void eliminarProducto(Integer idProducto) {
        delegateInventario.eliminarProducto(idProducto);
    }

    public List<InventarioEstilista> obtenerInventarios() {
        return delegateInventario.obtenerInventarios();
    }

    public List<InventarioEstilista> obtenerPorEmpleado(Integer idEmpleado) {
        return delegateInventario.obtenerPorEmpleado(idEmpleado);
    }

    public void guardarCantidad(Integer idEmpleado, Integer idProducto, Integer cantidad, BigDecimal precio, Integer stockMinimo, Integer idAdministrador) {
        delegateInventario.guardarCantidad(idEmpleado, idProducto, cantidad, precio, stockMinimo, idAdministrador);
    }

    public void eliminarInventario(Integer idEmpleado, Integer idProducto, Integer idAdministrador) {
        delegateInventario.eliminarInventario(idEmpleado, idProducto, idAdministrador);
    }
}
