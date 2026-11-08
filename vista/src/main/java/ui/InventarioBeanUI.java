package ui;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import mx.desarrollo.delegate.DelegateProducto;
import mx.desarrollo.entity.Producto;

import java.io.Serializable;
import java.util.List;

@Named(value = "inventarioBeanUI")
@ViewScoped
public class InventarioBeanUI implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Producto> listaProductos;
    private DelegateProducto delegateProducto;

    public InventarioBeanUI() {
        this.delegateProducto = new DelegateProducto();
        cargarInventario();
    }

    public void cargarInventario() {
        // Lógica para obtener la lista de productos activos y mostrarlos en la tabla
        // listaProductos = delegateProducto.obtenerProductosActivos();
    }

    // Método que se ejecuta desde el botón de la interfaz
    public String darDeBajaSeleccionado(Producto productoSeleccionado) {
        try {
            if (productoSeleccionado != null && productoSeleccionado.getId() != null) {
                delegateProducto.darDeBajaProducto(productoSeleccionado.getId().longValue());

                // Actualizamos la lista en pantalla
                cargarInventario();

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "El producto se ha dado de baja correctamente."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo dar de baja el producto: " + e.getMessage()));
            e.printStackTrace();
        }
        return null; // Mantiene al usuario en la misma página
    }

    // Getters y Setters
    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
}