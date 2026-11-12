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
import mx.desarrollo.entity.Administrador;
import mx.desarrollo.entity.Cliente;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.IOException;
import java.io.Serializable;

@Named("loginUI")
@SessionScoped
public class LoginBeanUI implements Serializable{
    private LoginHelper loginHelper;
    private Administrador usuario;
    private Cliente cliente;
    private String correo;
    private String contrasena;
    private String rol;
    
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
