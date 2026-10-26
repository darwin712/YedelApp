/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;


import mx.desarrollo.integration.ServiceFacadeLocator;
import mx.desarrollo.entity.Administrador;

import java.io.Serializable;

public class LoginHelper implements Serializable {
    

    /**
     * Metodo para hacer login llamara a la instancia de administradorFacade
     *
     * @param usuario
     * @param password
     * @return
     */
    public mx.desarrollo.entity.Administrador Login(String usuario, String password){
        return ServiceFacadeLocator.getInstanceFacadeAdministrador().login(password, usuario);
    }
    
    
    
}
