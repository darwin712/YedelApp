package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Administrador;
import mx.desarrollo.entity.Estilista;
import mx.desarrollo.persistence.persistence.AbstractDAO;


import java.util.List;


public class AdministradorDAO extends AbstractDAO<Administrador> {
    private final EntityManager entityManager;

    public AdministradorDAO(EntityManager em) {
        super(Administrador.class);
        this.entityManager = em;
    }

    public List<Administrador> obtenerTodos(){
        return entityManager
                .createQuery("SELECT a FROM Administrador a", Administrador.class)
                .getResultList();
    }

    public void actualizarAdministradorYEmpleado(Administrador administrador){
        execute(em -> {
            em.merge(administrador);
            if(administrador.getIdEstilista() != null){
                Estilista estilista = em.find(Estilista.class, administrador.getIdEstilista());
                if(estilista != null){
                    estilista.setNombre(administrador.getNombre());
                    estilista.setApellidoP(administrador.getApellidoP());
                    estilista.setApellidoM(administrador.getApellidoM());
                }
            }
            return null;
        });
    }

    public void eliminarAdministradorYDesactivarEmpleado(Administrador administrador){
        execute(em -> {
            Administrador actual = em.find(Administrador.class, administrador.getId());
            if(actual == null){
                return null;
            }
            if(actual.getIdEstilista() != null){
                Estilista estilista = em.find(Estilista.class, actual.getIdEstilista());
                if(estilista != null){
                    estilista.setActivo(false);
                }
            }
            em.remove(actual);
            return null;
        });
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
