package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateInventario;
import mx.desarrollo.entity.InventarioEstilista;
import mx.desarrollo.entity.Producto;

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

    public List<InventarioEstilista> obtenerInventarios() {
        return delegateInventario.obtenerInventarios();
    }

    public List<InventarioEstilista> obtenerPorEmpleado(Integer idEmpleado) {
        return delegateInventario.obtenerPorEmpleado(idEmpleado);
    }

    public void guardarCantidad(Integer idEmpleado, Integer idProducto, Integer cantidad, Integer idAdministrador) {
        delegateInventario.guardarCantidad(idEmpleado, idProducto, cantidad, idAdministrador);
    }
}
