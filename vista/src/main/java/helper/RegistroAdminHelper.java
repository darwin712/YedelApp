package helper;

import mx.desarrollo.integration.ServiceFacadeLocator;
import mx.desarrollo.entity.Administrador;

import java.io.Serializable;

public class RegistroAdminHelper implements Serializable {

    public boolean esAdministradorPrincipal(Integer id){
        return ServiceFacadeLocator.getInstanceFacadeAdministrador().esPrincipal(id);
    }

    public boolean existeCorreo(String correo){
        for(Administrador administrador : ServiceFacadeLocator.getInstanceFacadeAdministrador().findAll()){
            if(administrador.getCorreo() != null && administrador.getCorreo().equalsIgnoreCase(correo)){
                return true;
            }
        }
        return false;
    }

    public void guardarAdministrador(Administrador administrador){
        ServiceFacadeLocator.getInstanceFacadeAdministrador().saveAdministrador(administrador);
    }

}
