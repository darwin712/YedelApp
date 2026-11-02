package helper;


import mx.desarrollo.integration.ServiceFacadeLocator;
import mx.desarrollo.entity.Administrador;
import mx.desarrollo.entity.Cliente;

import java.io.Serializable;

public class LoginHelper implements Serializable {


    /**
     * Metodo para hacer login llamara a la instancia de administradorFacade
     *
     * @param correo
     * @param password
     * @return
     */
    public Administrador Login(String correo, String password){
        return ServiceFacadeLocator.getInstanceFacadeAdministrador().login(password, correo);
    }

    public Cliente LoginCliente(String correo, String password){
        return ServiceFacadeLocator.getInstanceFacadeCliente().login(password, correo);
    }



}