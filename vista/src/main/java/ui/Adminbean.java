package ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.desarrollo.integration.ServiceFacadeLocator;
import mx.desarrollo.entity.Administrador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("adminBean")
@ViewScoped
public class Adminbean implements Serializable {

    private List<Administrador> administradores;
    private String filtroBusqueda;
    private Administrador adminSeleccionado;

    @Inject
    private LoginBeanUI loginUI;

    @PostConstruct
    public void init(){
        administradores = ServiceFacadeLocator.getInstanceFacadeAdministrador().findAll();
    }

    public void buscar(){

    }

    public String verificarAcceso(){
        if(loginUI == null || loginUI.getRol() == null){
            return "login?faces-redirect=true";
        }
        if(!"ADMINISTRADOR".equals(loginUI.getRol()) || loginUI.getUsuario() == null
                || !ServiceFacadeLocator.getInstanceFacadeAdministrador().esPrincipal(loginUI.getUsuario().getId())){
            return "index?faces-redirect=true";
        }
        return null;
    }

    public void guardarEdicion(){
        try{
            if(adminSeleccionado == null || adminSeleccionado.getCorreo() == null
                    || adminSeleccionado.getCorreo().trim().isEmpty()
                    || adminSeleccionado.getContrasena() == null
                    || adminSeleccionado.getContrasena().trim().isEmpty()){
                mensajeError("Debe ingresar el correo y la contraseña.");
                return;
            }
            for(Administrador administrador : administradores){
                if(!administrador.getId().equals(adminSeleccionado.getId())
                        && administrador.getCorreo() != null
                        && administrador.getCorreo().equalsIgnoreCase(adminSeleccionado.getCorreo().trim())){
                    mensajeError("Ya existe un administrador con ese correo.");
                    return;
                }
            }
            adminSeleccionado.setCorreo(adminSeleccionado.getCorreo().trim().toLowerCase());
            ServiceFacadeLocator.getInstanceFacadeAdministrador().actualizarAdministradorYEmpleado(adminSeleccionado);
            adminSeleccionado = null;
            administradores = ServiceFacadeLocator.getInstanceFacadeAdministrador().findAll();
            mensajeExito("El administrador se modificó correctamente.");
        } catch(Exception ex){
            mensajeError("No se pudo modificar al administrador.");
        }
    }

    public void cancelarEdicion(){
        adminSeleccionado = null;
    }

    public void elim(Administrador administrador){
        try{
            if(administrador == null || administrador.getId() == null){
                mensajeError("No se encontró el administrador.");
                return;
            }
            if(ServiceFacadeLocator.getInstanceFacadeAdministrador().esPrincipal(administrador.getId())){
                mensajeError("No se puede eliminar al administrador principal.");
                return;
            }
            if(loginUI.getUsuario() != null && administrador.getId().equals(loginUI.getUsuario().getId())){
                mensajeError("No puede eliminar su propia cuenta.");
                return;
            }
            ServiceFacadeLocator.getInstanceFacadeAdministrador().eliminarAdministradorYDesactivarEmpleado(administrador);
            administradores = ServiceFacadeLocator.getInstanceFacadeAdministrador().findAll();
            mensajeExito("El administrador se eliminó correctamente.");
        } catch (Exception ex){
            mensajeError("No se pudo eliminar al administrador.");
        }
    }

    private void mensajeError(String textoMensaje){
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", textoMensaje));
    }

    public List<Administrador> getListaAdmin(){
        if(filtroBusqueda == null || filtroBusqueda.trim().isEmpty()){
            return administradores;
        }
        List<Administrador> filtrados = new ArrayList<>();
        String criterio = filtroBusqueda.toLowerCase();
        for(Administrador admin : administradores){
            if(contiene(admin.getNombre(), criterio)
                    || contiene(admin.getApellidoP(), criterio)
                    || contiene(admin.getApellidoM(), criterio)
                    || contiene(admin.getCorreo(), criterio)){
                filtrados.add(admin);
            }
        }
        return filtrados;
    }

    private boolean contiene(String valor, String criterio){
        return valor != null && valor.toLowerCase().contains(criterio);
    }

    private void mensajeExito(String textoMensaje){
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", textoMensaje));
    }

    public String getFiltroBusqueda() {
        return filtroBusqueda;
    }

    public void setFiltroBusqueda(String filtroBusqueda) {
        this.filtroBusqueda = filtroBusqueda;
    }

    public Administrador getAdminSeleccionado() {
        return adminSeleccionado;
    }

    public void setAdminSeleccionado(Administrador adminSeleccionado) {
        this.adminSeleccionado = adminSeleccionado;
    }

}
 
