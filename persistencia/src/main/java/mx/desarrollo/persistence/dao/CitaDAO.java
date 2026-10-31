package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Cita;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import java.util.Optional;

public class CitaDAO extends AbstractDAO<Cita> {
    private final EntityManager entityManager;

    public CitaDAO(EntityManager entityManager) {
        super(Cita.class);
        this.entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }


    public void eliminarCita(Integer idCita) {
        Optional<Cita> citaOpt = find(idCita);
        citaOpt.ifPresent(this::delete);
    }
}
