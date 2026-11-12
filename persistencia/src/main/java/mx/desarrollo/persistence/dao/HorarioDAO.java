package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Horario;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;

public class HorarioDAO extends AbstractDAO<Horario> {
    private final EntityManager entityManager;

    public HorarioDAO(EntityManager em) {
        super(Horario.class);
        this.entityManager = em;
    }

    public List<Horario> obtenerPorEstilista(Integer idEstilista) {
        return entityManager.createQuery("SELECT h FROM Horario h WHERE h.idEstilista = :idEstilista ORDER BY h.id", Horario.class)
                .setParameter("idEstilista", idEstilista)
                .getResultList();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
