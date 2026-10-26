package mx.desarrollo.delegate;

import mx.desarrollo.entity.Administrador;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateAdministrador {
    public Administrador login(String password, String usuario){
        Administrador administrador = new Administrador();
        List<Administrador> administradores = ServiceLocator.getInstanceAdministradorDAO().findAll();

        for(Administrador ad:administradores){
            if(ad.getContrasena().equals(password) && ad.getUsuario().equalsIgnoreCase(usuario)){
                administrador = ad;
            }
        }
        return administrador;
    }

    public void saveAdministrador(Administrador administrador){
        ServiceLocator.getInstanceAdministradorDAO().save(administrador);
    }

    public List<Administrador> findAll(){
        return ServiceLocator.getInstanceAdministradorDAO().findAll();
    }

}
