package helper;

import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.Serializable;

public class RegistroAdminHelper implements Serializable {

    public boolean esAdministradorPrincipal(Integer id){
        return ServiceFacadeLocator.getInstanceFacadeAdministrador().esPrincipal(id);
    }

}
