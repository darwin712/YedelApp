package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

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

    public void asignarServicioEstilista(Integer idEstilista, Integer idServicio) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNativeQuery("INSERT IGNORE INTO estilista_servicio (id_estilista, id_servicio) VALUES (:idEstilista, :idServicio)")
                    .setParameter("idEstilista", idEstilista)
                    .setParameter("idServicio", idServicio)
                    .executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }


    @Override
    public EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();

    }
}
