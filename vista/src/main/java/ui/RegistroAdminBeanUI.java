package ui;

import helper.RegistroAdminHelper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("registroAdminUI")
@RequestScoped
public class RegistroAdminBeanUI implements Serializable {
    private RegistroAdminHelper registroAdminHelper;

    @Inject
    private LoginBeanUI loginUI;

    public RegistroAdminBeanUI() {
        registroAdminHelper = new RegistroAdminHelper();
    }

    public String verificarAcceso(){
        if(loginUI.getRol() == null){
            return "login?faces-redirect=true";
        }
        if("CLIENTE".equals(loginUI.getRol())){
            return "indexCliente?faces-redirect=true";
        }
        if(!"ADMINISTRADOR".equals(loginUI.getRol()) || loginUI.getUsuario() == null || loginUI.getUsuario().getId() == null){
            return "index?faces-redirect=true";
        }
        if(!registroAdminHelper.esAdministradorPrincipal(loginUI.getUsuario().getId())){
            return "index?faces-redirect=true";
        }
        return null;
    }

}
