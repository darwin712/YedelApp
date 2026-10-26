package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateAdministrador;
import mx.desarrollo.entity.Administrador;

import java.util.List;

public class FacadeAdministrador {

    private final DelegateAdministrador delegateAdministrador;

    public FacadeAdministrador() {
        this.delegateAdministrador = new DelegateAdministrador();
    }

    public Administrador login(String password, String usuario){
        return delegateAdministrador.login(password, usuario);
    }

    public void saveAdministrador(Administrador administrador){
        delegateAdministrador.saveAdministrador(administrador);
    }

    public List<Administrador> findAll(){
        return delegateAdministrador.findAll();
    }

}
