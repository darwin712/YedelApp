package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;

import mx.desarrollo.entity.Servicio;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

import java.util.List;

public class ServicioDAO extends AbstractDAO<Servicio> {
    private final EntityManager entityManager;

    public ServicioDAO(EntityManager em) {
        super(Servicio.class);
        this.entityManager = em;
    }

    public List<Servicio> obtenerTodos(){
        return entityManager
                .createQuery("SELECT a FROM Servicio a", Servicio.class)
                .getResultList();
    }


    public List<Servicio> obtenerServiciosPorEstilista(Integer idEstilista) {
        return entityManager.createNativeQuery("SELECT s.* FROM servicio s " +
                        "INNER JOIN estilista_servicio es ON s.id_servicio = es.id_servicio " +
                        "WHERE es.id_estilista = :idEstilista", Servicio.class)
                .setParameter("idEstilista", idEstilista)
                .getResultList();
    }


    @Override
    public EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();

    }
}
