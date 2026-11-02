package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateCliente;
import mx.desarrollo.entity.Administrador;
import mx.desarrollo.entity.Cliente;

import java.util.List;

public class FacadeCliente {

    private final DelegateCliente delegateCliente;

    public FacadeCliente() {
        this.delegateCliente = new DelegateCliente();
    }

    public Cliente login(String password, String correo){
        return delegateCliente.login(password, correo);
    }

    public void guardarCliente(Cliente cliente){
        delegateCliente.saveCliente(cliente);
    }

    public void actualizarCliente(Cliente Cliente){
        delegateCliente.updateCliente(Cliente);
    }

    public void eliminarCliente(Cliente cliente){
        delegateCliente.deleteCliente(cliente);
    }

    public List<Cliente> findAll(){
        return delegateCliente.findAll();
    }

}