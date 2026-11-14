package ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.desarrollo.entity.Estilista;
import mx.desarrollo.entity.InventarioEstilista;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.integration.ServiceFacadeLocator;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("inventarioUI")
@ViewScoped
public class InventarioBeanUI implements Serializable {
    @Inject
    private LoginBeanUI loginUI;

    private List<Producto> productos = new ArrayList<>();
    private List<InventarioEstilista> inventarios = new ArrayList<>();
    private List<Estilista> empleados = new ArrayList<>();
    private List<ProductoVista> inventarioGeneral = new ArrayList<>();
    private List<InventarioEmpleadoVista> inventarioEmpleado = new ArrayList<>();
    private Producto productoNuevo;
    private Producto productoSeleccionado;
    private ProductoVista productoFilaSeleccionado;
    private InventarioEmpleadoVista inventarioFilaSeleccionado;
    private Integer idEmpleadoSeleccionado;
    private Integer idProductoEmpleado;
    private Integer cantidadEmpleado;
    private boolean principal;
    private boolean inventarioNuevo;
    private boolean productoNuevoAbierto;
    private String vistaInventario = "EMPLEADO";

    @PostConstruct
    public void init() {
        productoNuevo = nuevoProducto();
        if (loginUI == null || loginUI.getUsuario() == null || loginUI.getUsuario().getId() == null) {
            return;
        }
        principal = loginUI.isPrincipal();
        cargarEmpleados();
        idEmpleadoSeleccionado = loginUI.getIdEstilista();
        if (idEmpleadoSeleccionado == null && !empleados.isEmpty()) {
            idEmpleadoSeleccionado = empleados.get(0).getId();
        }
        cargarDatos();
    }

    public String verificarAcceso() {
        if (loginUI == null || loginUI.getRol() == null) {
            return "login?faces-redirect=true";
        }
        if (!"ADMINISTRADOR".equals(loginUI.getRol())) {
            return "login?faces-redirect=true";
        }
        return null;
    }

    private Producto nuevoProducto() {
        Producto producto = new Producto();
        producto.setPrecio(BigDecimal.ZERO);
        producto.setStockBodega(0);
        producto.setStockMinimo(0);
        return producto;
    }

    private void cargarEmpleados() {
        empleados = new ArrayList<>();
        for (Estilista empleado : ServiceFacadeLocator.getInstanceFacadeEstilista().findAll()) {
            if (Boolean.TRUE.equals(empleado.getActivo())) {
                empleados.add(empleado);
            }
        }
    }

    private void cargarDatos() {
        productos = ServiceFacadeLocator.getInstanceFacadeInventario().obtenerProductos();
        inventarios = ServiceFacadeLocator.getInstanceFacadeInventario().obtenerInventarios();
        cargarInventarioGeneral();
        cargarInventarioEmpleado();
    }

    private void cargarInventarioGeneral() {
        Map<Integer, Integer> cantidades = new HashMap<>();
        for (InventarioEstilista inventario : inventarios) {
            Integer idProducto = inventario.getId().getIdProducto();
            int cantidad = inventario.getCantidad() == null ? 0 : inventario.getCantidad();
            cantidades.put(idProducto, cantidades.getOrDefault(idProducto, 0) + cantidad);
        }
        inventarioGeneral = new ArrayList<>();
        for (Producto producto : productos) {
            inventarioGeneral.add(new ProductoVista(producto, cantidades.getOrDefault(producto.getId(), 0)));
        }
    }

    private void verificarStock(Integer idEstilista, Integer idProducto) {
        if (idEstilista == null || idProducto == null) {
            return;
        }
        if (!principal && !idEstilista.equals(loginUI.getIdEstilista())) {
            return;
        }

        for (InventarioEstilista inv : inventarios) {
            if (idEstilista.equals(inv.getId().getIdEstilista())
                    && idProducto.equals(inv.getId().getIdProducto())) {
                for (Producto p : productos) {
                    if (idProducto.equals(p.getId())) {
                        int cantidad = inv.getCantidad() == null ? 0 : inv.getCantidad();
                        int minimo = p.getStockMinimo() == null ? 0 : p.getStockMinimo();
                        if (cantidad <= minimo) {
                            String sujeto = principal ? getNombreEmpleadoSeleccionado().trim() + " tiene" : "Tienes";
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                                            "Alerta de Stock",
                                            sujeto + " poco \"" + p.getNombre() + "\" (Quedan: " + cantidad + " / Mín: " + minimo + ")."));
                        }
                        return;
                    }
                }
            }
        }
    }

    private void cargarInventarioEmpleado() {
        inventarioEmpleado = new ArrayList<>();
        if (idEmpleadoSeleccionado == null) {
            return;
        }
        Map<Integer, Producto> productosPorId = new HashMap<>();
        for (Producto producto : productos) {
            productosPorId.put(producto.getId(), producto);
        }
        for (InventarioEstilista inventario : inventarios) {
            if (idEmpleadoSeleccionado.equals(inventario.getId().getIdEstilista())) {
                Producto producto = productosPorId.get(inventario.getId().getIdProducto());
                if (producto != null) {
                    inventarioEmpleado.add(new InventarioEmpleadoVista(producto, inventario.getCantidad()));
                }
            }
        }
    }

    public void cambiarEmpleado() {
        if (!principal) {
            idEmpleadoSeleccionado = loginUI.getIdEstilista();
        }
        cargarInventarioEmpleado();
    }

    public void guardarProductoNuevo() {
        try {
            if (!productoValido(productoNuevo, null)) {
                return;
            }
            prepararProducto(productoNuevo);
            ServiceFacadeLocator.getInstanceFacadeInventario().guardarProducto(productoNuevo);
            productoNuevo = nuevoProducto();
            productoNuevoAbierto = false;
            cargarDatos();
            mensajeExito("El producto se registró correctamente.");
        } catch (Exception e) {
            mensajeError(obtenerDetalle(e, "No se pudo registrar el producto."));
        }
    }

    public void prepararProductoNuevo() {
        productoNuevo = nuevoProducto();
        productoNuevoAbierto = true;
    }

    public void cancelarProductoNuevo() {
        productoNuevo = nuevoProducto();
        productoNuevoAbierto = false;
    }

    public void cambiarVista() {
        cancelarProducto();
        cancelarInventarioEmpleado();
        cancelarProductoNuevo();
        cargarInventarioEmpleado();
    }

    public void prepararAgregar() {
        if ("GENERAL".equals(vistaInventario)) {
            prepararProductoNuevo();
        } else {
            prepararInventarioNuevo();
        }
    }

    public void seleccionarProducto(SelectEvent<ProductoVista> evento) {
        Producto producto = evento.getObject().getProducto();
        productoSeleccionado = copiarProducto(producto);
    }

    public void guardarProductoSeleccionado() {
        try {
            if (!productoValido(productoSeleccionado, productoSeleccionado.getId())) {
                return;
            }
            prepararProducto(productoSeleccionado);
            ServiceFacadeLocator.getInstanceFacadeInventario().actualizarProducto(productoSeleccionado);

            cargarDatos();


            productoSeleccionado = null;
            productoFilaSeleccionado = null;

            mensajeExito("El producto se modificó correctamente.");



        } catch (Exception e) {
            mensajeError(obtenerDetalle(e, "No se pudo modificar el producto."));
        }
    }

    public void cancelarProducto() {
        productoSeleccionado = null;
        productoFilaSeleccionado = null;
    }

    public void prepararInventarioNuevo() {
        inventarioNuevo = true;
        inventarioFilaSeleccionado = null;
        idProductoEmpleado = null;
        cantidadEmpleado = 0;
    }

    public void seleccionarInventario(SelectEvent<InventarioEmpleadoVista> evento) {
        inventarioNuevo = false;
        InventarioEmpleadoVista inventario = evento.getObject();
        idProductoEmpleado = inventario.getProducto().getId();
        cantidadEmpleado = inventario.getCantidad();
    }

    public void guardarInventarioEmpleado() {
        try {
            if (!principal) {
                idEmpleadoSeleccionado = loginUI.getIdEstilista();
            }
            if (idEmpleadoSeleccionado == null) {
                mensajeError("Debe seleccionar un empleado.");
                return;
            }
            if (idProductoEmpleado == null) {
                mensajeError("Debe seleccionar un producto.");
                return;
            }
            if (cantidadEmpleado == null || cantidadEmpleado < 0) {
                mensajeError("La cantidad debe ser igual o mayor a cero.");
                return;
            }
            ServiceFacadeLocator.getInstanceFacadeInventario().guardarCantidad(
                    idEmpleadoSeleccionado,
                    idProductoEmpleado,
                    cantidadEmpleado,
                    loginUI.getUsuario().getId());

            cargarDatos();
            verificarStock(idEmpleadoSeleccionado, idProductoEmpleado);
            cancelarInventarioEmpleado();

            mensajeExito("El inventario del empleado se guardó correctamente.");


        } catch (Exception e) {
            mensajeError(obtenerDetalle(e, "No se pudo guardar el inventario del empleado."));
        }
    }

    public void cancelarInventarioEmpleado() {
        inventarioNuevo = false;
        inventarioFilaSeleccionado = null;
        idProductoEmpleado = null;
        cantidadEmpleado = null;
    }

    private boolean productoValido(Producto producto, Integer idIgnorar) {
        if (producto == null || producto.getNombre() == null || producto.getNombre().trim().isEmpty()
                || producto.getCategoria() == null || producto.getCategoria().trim().isEmpty()) {
            mensajeError("Debe ingresar el nombre y la categoría del producto.");
            return false;
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0
                || producto.getStockBodega() == null || producto.getStockBodega() < 0
                || producto.getStockMinimo() == null || producto.getStockMinimo() < 0) {
            mensajeError("El precio y las cantidades deben ser iguales o mayores a cero.");
            return false;
        }
        for (Producto item : productos) {
            if ((idIgnorar == null || !item.getId().equals(idIgnorar))
                    && item.getNombre() != null
                    && item.getNombre().equalsIgnoreCase(producto.getNombre().trim())) {
                mensajeError("Ya existe un producto con ese nombre.");
                return false;
            }
        }
        return true;
    }

    private void prepararProducto(Producto producto) {
        producto.setNombre(producto.getNombre().trim());
        producto.setCategoria(producto.getCategoria().trim());
    }

    private Producto copiarProducto(Producto original) {
        Producto copia = new Producto();
        copia.setId(original.getId());
        copia.setNombre(original.getNombre());
        copia.setCategoria(original.getCategoria());
        copia.setPrecio(original.getPrecio());
        copia.setStockBodega(original.getStockBodega());
        copia.setStockMinimo(original.getStockMinimo());
        return copia;
    }

    private String obtenerDetalle(Throwable error, String mensaje) {
        Throwable causa = error;
        while (causa.getCause() != null) {
            causa = causa.getCause();
        }
        if (causa instanceof IllegalArgumentException && causa.getMessage() != null) {
            return causa.getMessage();
        }
        return mensaje;
    }

    private void mensajeError(String detalle) {
        FacesContext.getCurrentInstance().validationFailed();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", detalle));
    }

    private void mensajeExito(String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", detalle));
    }

    public String getNombreEmpleadoSeleccionado() {
        for (Estilista empleado : empleados) {
            if (empleado.getId().equals(idEmpleadoSeleccionado)) {
                return empleado.getNombre() + " "
                        + (empleado.getApellidoP() == null ? "" : empleado.getApellidoP()) + " "
                        + (empleado.getApellidoM() == null ? "" : empleado.getApellidoM());
            }
        }
        return "Sin empleado asignado";
    }

    public Integer getStockDisponibleProductoEmpleado() {
        for (Producto producto : productos) {
            if (producto.getId().equals(idProductoEmpleado)) {
                return producto.getStockBodega();
            }
        }
        return 0;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Estilista> getEmpleados() {
        return empleados;
    }

    public List<ProductoVista> getInventarioGeneral() {
        return inventarioGeneral;
    }

    public List<InventarioEmpleadoVista> getInventarioEmpleado() {
        return inventarioEmpleado;
    }

    public Producto getProductoNuevo() {
        return productoNuevo;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public ProductoVista getProductoFilaSeleccionado() {
        return productoFilaSeleccionado;
    }

    public void setProductoFilaSeleccionado(ProductoVista productoFilaSeleccionado) {
        this.productoFilaSeleccionado = productoFilaSeleccionado;
    }

    public InventarioEmpleadoVista getInventarioFilaSeleccionado() {
        return inventarioFilaSeleccionado;
    }

    public void setInventarioFilaSeleccionado(InventarioEmpleadoVista inventarioFilaSeleccionado) {
        this.inventarioFilaSeleccionado = inventarioFilaSeleccionado;
    }

    public Integer getIdEmpleadoSeleccionado() {
        return idEmpleadoSeleccionado;
    }

    public void setIdEmpleadoSeleccionado(Integer idEmpleadoSeleccionado) {
        this.idEmpleadoSeleccionado = idEmpleadoSeleccionado;
    }

    public Integer getIdProductoEmpleado() {
        return idProductoEmpleado;
    }

    public void setIdProductoEmpleado(Integer idProductoEmpleado) {
        this.idProductoEmpleado = idProductoEmpleado;
    }

    public Integer getCantidadEmpleado() {
        return cantidadEmpleado;
    }

    public void setCantidadEmpleado(Integer cantidadEmpleado) {
        this.cantidadEmpleado = cantidadEmpleado;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public boolean isInventarioNuevo() {
        return inventarioNuevo;
    }

    public boolean isModalInventarioAbierto() {
        return inventarioNuevo || idProductoEmpleado != null;
    }

    public boolean isProductoNuevoAbierto() {
        return productoNuevoAbierto;
    }

    public String getVistaInventario() {
        return vistaInventario;
    }

    public void setVistaInventario(String vistaInventario) {
        this.vistaInventario = vistaInventario;
    }

    public static class ProductoVista implements Serializable {
        private final Producto producto;
        private final Integer cantidadEmpleados;

        public ProductoVista(Producto producto, Integer cantidadEmpleados) {
            this.producto = producto;
            this.cantidadEmpleados = cantidadEmpleados;
        }

        public Producto getProducto() {
            return producto;
        }

        public Integer getCantidadEmpleados() {
            return cantidadEmpleados;
        }

        public Integer getCantidadTotal() {
            int bodega = producto.getStockBodega() == null ? 0 : producto.getStockBodega();
            return bodega + cantidadEmpleados;
        }

        public boolean isStockBajo() {
            int stock = producto.getStockBodega() == null ? 0 : producto.getStockBodega();
            int minimo = producto.getStockMinimo() == null ? 0 : producto.getStockMinimo();
            return stock <= minimo;
        }
    }

    public static class InventarioEmpleadoVista implements Serializable {
        private final Producto producto;
        private final Integer cantidad;

        public InventarioEmpleadoVista(Producto producto, Integer cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public Producto getProducto() {
            return producto;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public boolean isStockBajo() {
            int minimo = producto.getStockMinimo() == null ? 0 : producto.getStockMinimo();
            return cantidad != null && cantidad <= minimo;
        }
    }
}
