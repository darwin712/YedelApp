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
    private Producto productoEmpleadoSeleccionado;
    private ProductoVista productoFilaSeleccionado;
    private InventarioEmpleadoVista inventarioFilaSeleccionado;
    private Integer idEmpleadoSeleccionado;
    private Integer idProductoEmpleado;
    private Integer cantidadEmpleado;
    private BigDecimal precioEmpleado = BigDecimal.ZERO;
    private Integer stockMinimoEmpleado = 0;
    private Integer cantidadProductoNuevo = 0;
    private boolean principal;
    private boolean inventarioNuevo;
    private boolean productoNuevoAbierto;
    private boolean confirmacionEliminarProducto;
    private boolean confirmacionEliminarInventario;
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
        Map<Integer, Integer> empleadosPorProducto = new HashMap<>();
        for (InventarioEstilista inventario : inventarios) {
            Integer idProducto = inventario.getId().getIdProducto();
            int cantidad = inventario.getCantidad() == null ? 0 : inventario.getCantidad();
            cantidades.put(idProducto, cantidades.getOrDefault(idProducto, 0) + cantidad);
            empleadosPorProducto.put(idProducto, empleadosPorProducto.getOrDefault(idProducto, 0) + 1);
        }
        inventarioGeneral = new ArrayList<>();
        for (Producto producto : productos) {
            inventarioGeneral.add(new ProductoVista(
                    producto,
                    cantidades.getOrDefault(producto.getId(), 0),
                    empleadosPorProducto.getOrDefault(producto.getId(), 0)));
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
                        int minimo = inv.getStockMinimo() == null ? 0 : inv.getStockMinimo();
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
                    inventarioEmpleado.add(new InventarioEmpleadoVista(
                            producto,
                            inventario.getCantidad(),
                            inventario.getPrecio(),
                            inventario.getStockMinimo()));
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
            if (!principal) {
                idEmpleadoSeleccionado = loginUI.getIdEstilista();
            }
            if (idEmpleadoSeleccionado == null) {
                mensajeError("Debe seleccionar un empleado.");
                return;
            }
            if (productoNuevo == null || productoNuevo.getNombre() == null || productoNuevo.getNombre().trim().isEmpty()) {
                mensajeError("Debe ingresar el nombre del producto.");
                return;
            }
            if (cantidadProductoNuevo == null || cantidadProductoNuevo < 0) {
                mensajeError("La cantidad debe ser igual o mayor a cero.");
                return;
            }
            if (productoNuevo.getPrecio() == null || productoNuevo.getPrecio().compareTo(BigDecimal.ZERO) < 0
                    || productoNuevo.getStockMinimo() == null || productoNuevo.getStockMinimo() < 0) {
                mensajeError("El precio y el mínimo deben ser iguales o mayores a cero.");
                return;
            }
            Producto productoExistente = buscarProductoPorNombre(productoNuevo.getNombre());
            if (productoExistente != null) {
                if (productoAsignadoEmpleado(productoExistente.getId())) {
                    mensajeError("Este producto ya está en el inventario del empleado. Selecciónelo en la tabla para modificar su cantidad.");
                    return;
                }
                ServiceFacadeLocator.getInstanceFacadeInventario().guardarCantidad(
                        idEmpleadoSeleccionado,
                        productoExistente.getId(),
                        cantidadProductoNuevo,
                        productoNuevo.getPrecio(),
                        productoNuevo.getStockMinimo(),
                        loginUI.getUsuario().getId());
                productoNuevo = nuevoProducto();
                cantidadProductoNuevo = 0;
                productoNuevoAbierto = false;
                cargarDatos();
                mensajeExito("El producto existente se agregó al inventario del empleado correctamente.");
                return;
            }
            if (!productoValido(productoNuevo, null)) {
                return;
            }
            Integer cantidadInicial = cantidadProductoNuevo;
            productoNuevo.setStockBodega(0);
            productoNuevo.setIdAdministrador(loginUI.getUsuario().getId());
            prepararProducto(productoNuevo);
            ServiceFacadeLocator.getInstanceFacadeInventario().guardarProducto(productoNuevo);
            ServiceFacadeLocator.getInstanceFacadeInventario().guardarCantidad(
                    idEmpleadoSeleccionado,
                    productoNuevo.getId(),
                    cantidadInicial,
                    productoNuevo.getPrecio(),
                    productoNuevo.getStockMinimo(),
                    loginUI.getUsuario().getId());
            productoNuevo = nuevoProducto();
            cantidadProductoNuevo = 0;
            productoNuevoAbierto = false;
            cargarDatos();
            mensajeExito("El producto se agregó al inventario del empleado correctamente.");
        } catch (Exception e) {
            mensajeError(obtenerDetalle(e, "No se pudo registrar el producto."));
        }
    }

    public void prepararProductoNuevo() {
        productoNuevo = nuevoProducto();
        cantidadProductoNuevo = 0;
        productoNuevoAbierto = true;
    }

    public void cancelarProductoNuevo() {
        productoNuevo = nuevoProducto();
        cantidadProductoNuevo = 0;
        productoNuevoAbierto = false;
    }

    public List<String> completarProductos(String consulta) {
        List<String> coincidencias = new ArrayList<>();
        String texto = consulta == null ? "" : consulta.trim().toLowerCase();
        for (Producto producto : productos) {
            if (producto.getNombre() != null && producto.getNombre().toLowerCase().contains(texto)) {
                coincidencias.add(producto.getNombre());
            }
        }
        return coincidencias;
    }

    public void seleccionarProductoExistente(SelectEvent<String> evento) {
        Producto existente = buscarProductoPorNombre(evento.getObject());
        if (existente != null) {
            productoNuevo = copiarProducto(existente);
        }
    }

    public void cambiarNombreProductoNuevo() {
        if (productoNuevo == null || productoNuevo.getNombre() == null) {
            return;
        }
        String nombre = productoNuevo.getNombre();
        Producto existente = buscarProductoPorNombre(nombre);
        if (existente != null) {
            productoNuevo = copiarProducto(existente);
        } else if (productoNuevo.getId() != null) {
            productoNuevo = nuevoProducto();
            productoNuevo.setNombre(nombre);
        }
    }

    private Producto buscarProductoPorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (Producto producto : productos) {
            if (producto.getNombre() != null && producto.getNombre().equalsIgnoreCase(nombre.trim())) {
                return producto;
            }
        }
        return null;
    }

    private boolean productoAsignadoEmpleado(Integer idProducto) {
        for (InventarioEstilista inventario : inventarios) {
            if (idEmpleadoSeleccionado.equals(inventario.getId().getIdEstilista())
                    && idProducto.equals(inventario.getId().getIdProducto())) {
                return true;
            }
        }
        return false;
    }

    public void cambiarVista() {
        cancelarProducto();
        cancelarInventarioEmpleado();
        cancelarProductoNuevo();
        cargarInventarioEmpleado();
    }

    public void prepararAgregar() {
        prepararProductoNuevo();
    }

    public void seleccionarProducto(SelectEvent<ProductoVista> evento) {
        if (!principal) {
            productoFilaSeleccionado = null;
            mensajeError("Solamente el administrador principal puede modificar productos del inventario general.");
            return;
        }
        Producto producto = evento.getObject().getProducto();
        productoSeleccionado = copiarProducto(producto);
        confirmacionEliminarProducto = false;
    }

    public void guardarProductoSeleccionado() {
        try {
            if (!principal) {
                mensajeError("Solamente el administrador principal puede modificar productos del inventario general.");
                productoSeleccionado = null;
                productoFilaSeleccionado = null;
                return;
            }
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

    public void eliminarProductoSeleccionado() {
        try {
            if (!principal) {
                mensajeError("Solamente el administrador principal puede eliminar productos del inventario general.");
                confirmacionEliminarProducto = false;
                return;
            }
            if (productoSeleccionado == null || productoSeleccionado.getId() == null) {
                mensajeError("Debe seleccionar un producto.");
                return;
            }
            ServiceFacadeLocator.getInstanceFacadeInventario().eliminarProducto(productoSeleccionado.getId());
            productoSeleccionado = null;
            productoFilaSeleccionado = null;
            confirmacionEliminarProducto = false;
            cargarDatos();
            mensajeExito("El producto se eliminó correctamente.");
        } catch (Exception e) {
            mensajeError(obtenerDetalle(e, "No se pudo eliminar el producto."));
        }
    }

    public void cancelarProducto() {
        productoSeleccionado = null;
        productoFilaSeleccionado = null;
        confirmacionEliminarProducto = false;
    }

    public void solicitarEliminarProducto() {
        if (!principal) {
            mensajeError("Solamente el administrador principal puede eliminar productos del inventario general.");
            return;
        }
        confirmacionEliminarProducto = true;
    }

    public void cancelarEliminarProducto() {
        confirmacionEliminarProducto = false;
    }

    public void prepararInventarioNuevo() {
        inventarioNuevo = true;
        inventarioFilaSeleccionado = null;
        productoEmpleadoSeleccionado = null;
        idProductoEmpleado = null;
        cantidadEmpleado = 0;
        precioEmpleado = BigDecimal.ZERO;
        stockMinimoEmpleado = 0;
    }

    public void seleccionarInventario(SelectEvent<InventarioEmpleadoVista> evento) {
        inventarioNuevo = false;
        confirmacionEliminarInventario = false;
        InventarioEmpleadoVista inventario = evento.getObject();
        productoEmpleadoSeleccionado = copiarProducto(inventario.getProducto());
        idProductoEmpleado = inventario.getProducto().getId();
        cantidadEmpleado = inventario.getCantidad();
        precioEmpleado = inventario.getPrecio();
        stockMinimoEmpleado = inventario.getStockMinimo();
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
            if (precioEmpleado == null || precioEmpleado.compareTo(BigDecimal.ZERO) < 0
                    || stockMinimoEmpleado == null || stockMinimoEmpleado < 0) {
                mensajeError("El precio y el mínimo deben ser iguales o mayores a cero.");
                return;
            }
            if (!inventarioNuevo && isPuedeModificarProductoEmpleado()) {
                if (!productoValido(productoEmpleadoSeleccionado, idProductoEmpleado)) {
                    return;
                }
                prepararProducto(productoEmpleadoSeleccionado);
                ServiceFacadeLocator.getInstanceFacadeInventario().actualizarProducto(productoEmpleadoSeleccionado);
            }
            ServiceFacadeLocator.getInstanceFacadeInventario().guardarCantidad(
                    idEmpleadoSeleccionado,
                    idProductoEmpleado,
                    cantidadEmpleado,
                    precioEmpleado,
                    stockMinimoEmpleado,
                    loginUI.getUsuario().getId());

            cargarDatos();
            verificarStock(idEmpleadoSeleccionado, idProductoEmpleado);
            cancelarInventarioEmpleado();

            mensajeExito("El producto y el inventario del empleado se guardaron correctamente.");


        } catch (Exception e) {
            mensajeError(obtenerDetalle(e, "No se pudo guardar el inventario del empleado."));
        }
    }

    public void cancelarInventarioEmpleado() {
        inventarioNuevo = false;
        inventarioFilaSeleccionado = null;
        productoEmpleadoSeleccionado = null;
        idProductoEmpleado = null;
        cantidadEmpleado = null;
        precioEmpleado = BigDecimal.ZERO;
        stockMinimoEmpleado = 0;
        confirmacionEliminarInventario = false;
    }

    public void solicitarEliminarInventario() {
        if (idProductoEmpleado == null) {
            mensajeError("Debe seleccionar un producto.");
            return;
        }
        confirmacionEliminarInventario = true;
    }

    public void cancelarEliminarInventario() {
        confirmacionEliminarInventario = false;
    }

    public void eliminarInventarioEmpleado() {
        try {
            if (!principal) {
                idEmpleadoSeleccionado = loginUI.getIdEstilista();
            }
            if (idEmpleadoSeleccionado == null || idProductoEmpleado == null) {
                mensajeError("Debe seleccionar un producto del empleado.");
                return;
            }
            ServiceFacadeLocator.getInstanceFacadeInventario().eliminarInventario(
                    idEmpleadoSeleccionado,
                    idProductoEmpleado,
                    loginUI.getUsuario().getId());
            cancelarInventarioEmpleado();
            cargarDatos();
            mensajeExito("El producto se quitó del inventario del empleado correctamente.");
        } catch (Exception e) {
            mensajeError(obtenerDetalle(e, "No se pudo quitar el producto del inventario del empleado."));
        }
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
        copia.setIdAdministrador(original.getIdAdministrador());
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

    public Integer getCantidadProductoNuevo() {
        return cantidadProductoNuevo;
    }

    public void setCantidadProductoNuevo(Integer cantidadProductoNuevo) {
        this.cantidadProductoNuevo = cantidadProductoNuevo;
    }

    public boolean isProductoNuevoExistente() {
        return productoNuevo != null && productoNuevo.getId() != null;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public Producto getProductoEmpleadoSeleccionado() {
        return productoEmpleadoSeleccionado;
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

    public BigDecimal getPrecioEmpleado() {
        return precioEmpleado;
    }

    public void setPrecioEmpleado(BigDecimal precioEmpleado) {
        this.precioEmpleado = precioEmpleado;
    }

    public Integer getStockMinimoEmpleado() {
        return stockMinimoEmpleado;
    }

    public void setStockMinimoEmpleado(Integer stockMinimoEmpleado) {
        this.stockMinimoEmpleado = stockMinimoEmpleado;
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

    public boolean isConfirmacionEliminarProducto() {
        return confirmacionEliminarProducto;
    }

    public boolean isConfirmacionEliminarInventario() {
        return confirmacionEliminarInventario;
    }

    public boolean isPuedeModificarProductoEmpleado() {
        if (principal) {
            return true;
        }
        return productoEmpleadoSeleccionado != null
                && productoEmpleadoSeleccionado.getIdAdministrador() != null
                && loginUI != null
                && loginUI.getUsuario() != null
                && productoEmpleadoSeleccionado.getIdAdministrador().equals(loginUI.getUsuario().getId());
    }

    public String getVistaInventario() {
        return vistaInventario;
    }

    public void setVistaInventario(String vistaInventario) {
        this.vistaInventario = vistaInventario;
    }

    public static class ProductoVista implements Serializable {
        private final Producto producto;
        private final Integer cantidadTotal;
        private final Integer empleados;

        public ProductoVista(Producto producto, Integer cantidadTotal, Integer empleados) {
            this.producto = producto;
            this.cantidadTotal = cantidadTotal;
            this.empleados = empleados;
        }

        public Producto getProducto() {
            return producto;
        }

        public Integer getEmpleados() {
            return empleados;
        }

        public Integer getCantidadTotal() {
            return cantidadTotal;
        }
    }

    public static class InventarioEmpleadoVista implements Serializable {
        private final Producto producto;
        private final Integer cantidad;
        private final BigDecimal precio;
        private final Integer stockMinimo;

        public InventarioEmpleadoVista(Producto producto, Integer cantidad, BigDecimal precio, Integer stockMinimo) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.precio = precio;
            this.stockMinimo = stockMinimo;
        }

        public Producto getProducto() {
            return producto;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public BigDecimal getPrecio() {
            return precio;
        }

        public Integer getStockMinimo() {
            return stockMinimo;
        }

        public boolean isStockBajo() {
            int minimo = stockMinimo == null ? 0 : stockMinimo;
            return cantidad != null && cantidad <= minimo;
        }
    }
}
