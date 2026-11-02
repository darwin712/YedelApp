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

    public void guardarCliente(Cliente cliente) {
        delegateCliente.saveCliente(cliente);
    }

    public void actualizarCliente(Cliente cliente) {
        delegateCliente.updateCliente(cliente);
    }

    public void eliminarCliente(Cliente cliente) {
        delegateCliente.deleteCliente(cliente);
    }

    public List<Cliente> findAll() {
        return delegateCliente.findAll();
    }


    public Cliente login(String password, String correo) {
        return delegateCliente.loginCliente(password, correo);
    }
}