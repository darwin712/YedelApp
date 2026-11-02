package mx.desarrollo.delegate;

import mx.desarrollo.entity.Administrador;
import mx.desarrollo.entity.Cliente;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateCliente {
    public void saveCliente(Cliente cliente){
        ServiceLocator.getInstanceClienteDAO().save(cliente);
    }

    public void updateCliente(Cliente cliente){
        ServiceLocator.getInstanceClienteDAO().update(cliente);
    }

    public void deleteCliente(Cliente cliente){
        ServiceLocator.getInstanceClienteDAO().delete(cliente);
    }

    public List<Cliente> findAll(){
        return ServiceLocator.getInstanceClienteDAO().obtenerTodos();
    }

    public Cliente loginCliente(String password, String correo){
        Cliente cliente = new Cliente();
        List<Cliente> clientes = ServiceLocator.getInstanceClienteDAO().findAll();

        for(Cliente cl: clientes){
            if(password != null && password.equals(cl.getContrasena()) && correo != null && correo.equalsIgnoreCase(cl.getCorreo())){
                cliente = cl;
            }
        }
        return cliente;
    }
}