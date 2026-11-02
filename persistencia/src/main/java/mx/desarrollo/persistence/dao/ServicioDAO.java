package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Servicio;
import mx.desarrollo.persistence.persistence.AbstractDAO;


import java.util.List;


public class ServicioDAO extends AbstractDAO<Servicio> {
    private final EntityManager entityManager;

    public ServicioDAO(EntityManager em) {
        super(Servicio.class);
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}