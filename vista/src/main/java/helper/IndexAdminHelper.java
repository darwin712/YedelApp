package helper;

import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.Serializable;

public class IndexAdminHelper implements Serializable {

    public boolean esAdministradorPrincipal(Integer id){
        return ServiceFacadeLocator.getInstanceFacadeAdministrador().esPrincipal(id);
    }

}
