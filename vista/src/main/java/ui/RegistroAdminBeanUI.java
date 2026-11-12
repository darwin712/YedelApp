package ui;

import helper.RegistroAdminHelper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.desarrollo.entity.Administrador;

import java.io.Serializable;
import java.util.regex.Pattern;

@Named("registroAdminUI")
@RequestScoped
public class RegistroAdminBeanUI implements Serializable {
    private RegistroAdminHelper registroAdminHelper;
    private Administrador administrador;

    private static final Pattern CORREO_VALIDO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Inject
    private LoginBeanUI loginUI;

    public RegistroAdminBeanUI() {
        registroAdminHelper = new RegistroAdminHelper();
        administrador = new Administrador();
    }

    public String verificarAcceso(){
        if(loginUI.getRol() == null){
            return "login?faces-redirect=true";
        }
        if("CLIENTE".equals(loginUI.getRol())){
            return "login?faces-redirect=true";
        }
        if(!"ADMINISTRADOR".equals(loginUI.getRol()) || loginUI.getUsuario() == null || loginUI.getUsuario().getId() == null){
            return "index?faces-redirect=true";
        }
        if(!registroAdminHelper.esAdministradorPrincipal(loginUI.getUsuario().getId())){
            return "index?faces-redirect=true";
        }
        return null;
    }

    public void registrar(){
        try{
            String correo = administrador.getCorreo() == null ? "" : administrador.getCorreo().trim().toLowerCase();
            String contrasena = administrador.getContrasena() == null ? "" : administrador.getContrasena().trim();

            if(correo.isEmpty() || contrasena.isEmpty()){
                mensajeError("Ingrese el correo y la contraseña.");
                return;
            }
            if(!CORREO_VALIDO.matcher(correo).matches()){
                mensajeError("Ingrese un correo electrónico válido.");
                return;
            }
            if(contrasena.length() < 8){
                mensajeError("La contraseña debe tener al menos 8 caracteres.");
                return;
            }
            if(registroAdminHelper.existeCorreo(correo)){
                mensajeError("Ya existe un administrador con ese correo.");
                return;
            }

            administrador.setCorreo(correo);
            administrador.setContrasena(contrasena);
            registroAdminHelper.guardarAdministrador(administrador);
            administrador = new Administrador();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "El administrador se registró correctamente."));
        } catch(Exception ex){
            mensajeError("No se pudo registrar al administrador.");
        }
    }

    private void mensajeError(String detalle){
        FacesContext.getCurrentInstance().validationFailed();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", detalle));
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

}
