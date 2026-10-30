package mx.desarrollo.persistence.integration;

import jakarta.persistence.EntityManager;

import mx.desarrollo.persistence.dao.*;
import mx.desarrollo.persistence.persistence.HibernateUtil;


public class ServiceLocator {

    private static AdministradorDAO administradorDAO;
    private static UsuarioDAO usuarioDAO;
    private static ClienteDAO clienteDAO;

    private static EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    public static AdministradorDAO getInstanceAdministradorDAO(){
        if(administradorDAO == null){
            administradorDAO = new AdministradorDAO(getEntityManager());
            return administradorDAO;
        } else{
            return administradorDAO;
        }
    }

    public static UsuarioDAO getInstanceUsuarioDAO() {
        if(usuarioDAO == null){
            usuarioDAO = new UsuarioDAO(getEntityManager());
            return usuarioDAO;
        } else{
            return usuarioDAO;
        }
    }

    public static ClienteDAO getInstanceClienteDAO() {
        if(clienteDAO == null){
            clienteDAO = new ClienteDAO(getEntityManager());
            return clienteDAO;
        } else{
            return clienteDAO;
        }
    }
}
