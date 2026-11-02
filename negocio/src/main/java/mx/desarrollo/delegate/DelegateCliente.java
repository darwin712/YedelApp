package mx.desarrollo.delegate;

import mx.desarrollo.entity.Cliente;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateCliente {
    public Cliente login(String password, String correo){
        Cliente cliente = new Cliente();
        List<Cliente> clientes = ServiceLocator.getInstanceClienteDAO().findAll();

        for(Cliente ad:clientes){
            if(password != null && password.equals(ad.getContrasena()) && correo != null && correo.equalsIgnoreCase(ad.getCorreo())){
                cliente = ad;
            }
        }
        return cliente;
    }
    
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
        return ServiceLocator.getInstanceClienteDAO().findAll();
    }

}