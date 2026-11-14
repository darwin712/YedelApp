/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import helper.LoginHelper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarrollo.entity.*;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("loginUI")
@SessionScoped
public class LoginBeanUI implements Serializable{
    private LoginHelper loginHelper;
    private Administrador usuario;
    private Cliente cliente;
    private String correo;
    private String contrasena;
    private String rol;

    private String alertaStockPendiente;

    
    public LoginBeanUI() {
        loginHelper = new LoginHelper();
    }
    
    /**
     * Metodo postconstructor todo lo que este dentro de este metodo
     * sera la primero que haga cuando cargue la pagina
     */
    @PostConstruct
    public void init(){
        usuario= new Administrador();
        cliente = new Cliente();
    }

     public void login() throws IOException{
        String appURL = "/index.xhtml";
        // los atributos de usuario vienen del xhtml 
        if(correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.isEmpty()){
            contrasena = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos incompletos:", "Ingrese su correo y contraseña"));
            return;
        }
        Administrador ad= new Administrador();
        ad.setId(0);
        ad = loginHelper.Login(correo.trim(), contrasena);
          if(ad != null && ad.getId()!=null){
            // asigno el administrador encontrado al usuario de esta clase para que 
            // se muestre correctamente en la pagina de informacion
            usuario=ad;
            rol = "ADMINISTRADOR";
            contrasena = null;

            cargarAlertasStock();

            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + appURL);
            return;
        }
        Cliente cl = loginHelper.LoginCliente(correo.trim(), contrasena);
        if(cl != null && cl.getId()!=null){
            contrasena = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Acceso no disponible:", "Esta cuenta no tiene acceso al panel de empleados"));
            return;
        }
        contrasena = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Correo o contraseña incorrecta:", "Intente de nuevo"));
    }

    
    public void logout() throws IOException{
        String appURL = "/login.xhtml";
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + appURL);
    }

    private void cargarAlertasStock() {
        alertaStockPendiente = null;
        boolean verTodos = isPrincipal();
        Integer miEstilista = getIdEstilista();

        List<Producto> listaProductos = ServiceFacadeLocator.getInstanceFacadeInventario().obtenerProductos();
        List<InventarioEstilista> listaInventarios = ServiceFacadeLocator.getInstanceFacadeInventario().obtenerInventarios();
        List<Estilista> listaEstilistas = ServiceFacadeLocator.getInstanceFacadeEstilista().findAll();

        StringBuilder mensaje = new StringBuilder();

        //revisa el stock general de la bodega si es administrador principal
        if (verTodos) {
            StringBuilder general = new StringBuilder();
            for (Producto prod : listaProductos) {
                int stock = prod.getStockBodega() == null ? 0 : prod.getStockBodega();
                int minimo = prod.getStockMinimo() == null ? 0 : prod.getStockMinimo();

                if (stock <= minimo) {
                    if (general.length() > 0) {
                        general.append("\n");
                    }
                    general.append(prod.getNombre()).append(" (quedan ").append(stock).append(")");
                }
            }
            if (general.length() > 0) {
                mensaje.append("general:\n").append(general);
            }
        }

        //se arma el StringBuilder para la notificacion verificando si la cantidad actual es igual o menor a la minima
        for (Estilista est : listaEstilistas) {
            if (!Boolean.TRUE.equals(est.getActivo())) {
                continue;
            }
            if (!verTodos && (miEstilista == null || !miEstilista.equals(est.getId()))) {
                continue;
            }

            StringBuilder faltantes = new StringBuilder();
            for (InventarioEstilista inv : listaInventarios) {
                if (est.getId().equals(inv.getId().getIdEstilista())) {
                    Producto prod = buscarProductoPorId(listaProductos, inv.getId().getIdProducto());
                    if (prod != null) {

                        int cantidad = inv.getCantidad() == null ? 0 : inv.getCantidad();
                        int minimo = prod.getStockMinimo() == null ? 0 : prod.getStockMinimo();

                        if (cantidad <= minimo) {
                            if (faltantes.length() > 0) {
                                faltantes.append("\n");
                            }
                            faltantes.append(prod.getNombre()).append(" (quedan ").append(cantidad).append(")");
                        }
                    }
                }
            }

            if (faltantes.length() > 0) {
                if (mensaje.length() > 0) {
                    mensaje.append("\n\n");
                }
                mensaje.append(est.getNombre()).append(":\n").append(faltantes);
            }
        }

        //se cierra
        if (mensaje.length() > 0) {
            alertaStockPendiente = mensaje.toString();
        }
    }

    private Producto buscarProductoPorId(List<Producto> listaProductos, Integer idProducto) {
        if (idProducto == null) {
            return null;
        }
        for (Producto prod : listaProductos) {
            if (idProducto.equals(prod.getId())) {
                return prod;
            }
        }
        return null;
    }

    public void mostrarAlertas() {
        if (alertaStockPendiente != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Alerta: Falta de Stock", alertaStockPendiente));
            alertaStockPendiente = null;
        }
    }

    
    /* getters y setters*/

    public Administrador getUsuario() {
        return usuario;
    }

    public void setUsuario(Administrador usuario) {
        this.usuario = usuario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public boolean isPrincipal() {
        return usuario != null && usuario.getId() != null
                && ServiceFacadeLocator.getInstanceFacadeAdministrador().esPrincipal(usuario.getId());
    }

    public Integer getIdEstilista() {
        return usuario == null ? null : usuario.getIdEstilista();
    }
    
    
    
    
    
    
    
    

    

    
}
