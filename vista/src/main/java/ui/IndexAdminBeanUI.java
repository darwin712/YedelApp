package ui;

import helper.IndexAdminHelper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("indexAdminUI")
@RequestScoped
public class IndexAdminBeanUI implements Serializable {
    private IndexAdminHelper indexAdminHelper;
    private Boolean principal;

    @Inject
    private LoginBeanUI loginUI;

    public IndexAdminBeanUI() {
        indexAdminHelper = new IndexAdminHelper();
    }

    public String verificarAcceso(){
        if(loginUI.getRol() == null){
            return "login?faces-redirect=true";
        }
        if("CLIENTE".equals(loginUI.getRol())){
            return "indexCliente?faces-redirect=true";
        }
        return null;
    }

    public boolean isPrincipal(){
        if(principal == null){
            principal = loginUI.getUsuario() != null
                    && loginUI.getUsuario().getId() != null
                    && indexAdminHelper.esAdministradorPrincipal(loginUI.getUsuario().getId());
        }
        return principal;
    }

}
