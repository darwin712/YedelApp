package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Estilista;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;

public class EstilistaDAO extends AbstractDAO<Estilista> {
    private final EntityManager entityManager;

    public EstilistaDAO(EntityManager em){
        super(Estilista.class);
        this.entityManager = em;
    }

    public List<Estilista> findAll(){
        entityManager.clear();
        return entityManager
                .createQuery("SELECT e FROM Estilista e", Estilista.class)
                .getResultList();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }


}
