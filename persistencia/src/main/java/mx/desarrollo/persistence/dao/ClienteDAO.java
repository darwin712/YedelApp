package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Cliente;
import mx.desarrollo.persistence.persistence.AbstractDAO;


import java.util.List;


public class ClienteDAO extends AbstractDAO<Cliente> {
    private final EntityManager entityManager;

    public ClienteDAO(EntityManager em) {
        super(Cliente.class);
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}