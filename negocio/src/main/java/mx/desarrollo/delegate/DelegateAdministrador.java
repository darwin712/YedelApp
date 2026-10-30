package mx.desarrollo.delegate;

import mx.desarrollo.entity.Administrador;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateAdministrador {
    public Administrador login(String password, String correo){
        Administrador administrador = new Administrador();
        List<Administrador> administradores = ServiceLocator.getInstanceAdministradorDAO().findAll();

        for(Administrador ad:administradores){
            if(password != null && password.equals(ad.getContrasena()) && correo != null && correo.equalsIgnoreCase(ad.getCorreo())){
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

    public boolean esPrincipal(Integer id){
        if(id == null){
            return false;
        }
        Integer menorId = null;
        for(Administrador ad : ServiceLocator.getInstanceAdministradorDAO().findAll()){
            if(ad.getId() != null && (menorId == null || ad.getId() < menorId)){
                menorId = ad.getId();
            }
        }
        return menorId != null && menorId.equals(id);
    }

}
