package mx.desarrollo.persistence.integration;

import jakarta.persistence.EntityManager;

import mx.desarrollo.persistence.dao.*;
import mx.desarrollo.persistence.persistence.HibernateUtil;


public class ServiceLocator {

    private static AdministradorDAO administradorDAO;
    private static UsuarioDAO usuarioDAO;
    private static CitaDAO citaDAO;
    private static ClienteDAO clienteDAO;
    private static ServicioDAO servicioDAO;
    private static EstilistaDAO estilistaDAO;

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

    public static CitaDAO getInstanceCitaDAO(){
        if(citaDAO == null){
            citaDAO = new CitaDAO(getEntityManager());
            return citaDAO;
        } else{
            return citaDAO;
        }
    }

    public static ClienteDAO getInstanceClienteDAO(){
        if(clienteDAO == null){
            clienteDAO = new ClienteDAO(getEntityManager());
            return clienteDAO;
        } else{
            return clienteDAO;
        }
    }

    public static ServicioDAO getInstanceServicioDAO(){
        if(servicioDAO == null){
            servicioDAO = new ServicioDAO(getEntityManager());
            return servicioDAO;
        } else{
            return servicioDAO;
        }
    }

    public static EstilistaDAO getInstanceEstilistaDAO(){
        if(estilistaDAO == null){
            estilistaDAO = new EstilistaDAO(getEntityManager());
            return estilistaDAO;
        } else{
            return estilistaDAO;
        }
    }
}
