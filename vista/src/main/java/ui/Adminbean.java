package ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import mx.desarrollo.integration.ServiceFacadeLocator;
import mx.desarrollo.entity.Administrador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {

    private List<Administrador> administradores;
    private String filtroBusqueda;
    private Administrador adminSeleccionado;

    @PostConstruct
    public void init(){
        administradores = ServiceFacadeLocator.getInstanceFacadeAdministrador().findAll();
    }

    public void buscar(){

    }

    public void guardarEdicion(){
        ServiceFacadeLocator.getInstanceFacadeAdministrador().actualizarAdministrador(adminSeleccionado);
        adminSeleccionado = null;
        administradores = ServiceFacadeLocator.getInstanceFacadeAdministrador().findAll();
    }

    public void cancelarEdicion(){
        adminSeleccionado = null;
    }

    public void elim(Administrador administrador){
        try{
            ServiceFacadeLocator.getInstanceFacadeAdministrador().eliminarAdministrador(administrador);
            administradores = ServiceFacadeLocator.getInstanceFacadeAdministrador().findAll();
        } catch (Exception ex){
            mensajeError("No se pudo eliminar al administrador. Verifique que no tenga registros relacionados.");
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
            if(admin.getNombre().toLowerCase().contains(criterio)
                    || admin.getAppaterno().toLowerCase().contains(criterio)
                    || admin.getApmaterno().toLowerCase().contains(criterio)
                    || admin.getUsuario().toLowerCase().contains(criterio)){
                filtrados.add(admin);
            }
        }
        return filtrados;
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
 