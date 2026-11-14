package helper;

import mx.desarrollo.entity.Cliente;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.Serializable;

public class RegistroClienteHelper implements Serializable {

    //public boolean esAdministradorPrincipal(Integer id){
    //    return ServiceFacadeLocator.getInstanceFacadeAdministrador().esPrincipal(id);
    //}

    public boolean existeCorreo(String correo){
        for(Cliente cliente : ServiceFacadeLocator.getInstanceFacadeCliente().findAll()){
            if(cliente.getCorreo() != null && cliente.getCorreo().equalsIgnoreCase(correo)){
                return true;
            }
        }
        return false;
    }

    public void guardarCliente(Cliente cliente){
        ServiceFacadeLocator.getInstanceFacadeCliente().guardarCliente(cliente);
    }

}
