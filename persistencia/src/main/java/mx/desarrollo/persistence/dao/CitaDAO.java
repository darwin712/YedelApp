package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import mx.desarrollo.entity.Cita;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class CitaDAO extends AbstractDAO<Cita> {
    private final EntityManager entityManager;

    public CitaDAO(EntityManager em){
        super(Cita.class);
        this.entityManager = em;
    }

    public List<Cita> obtenerTodos(){
        return entityManager
                .createQuery("SELECT c FROM Cita c", Cita.class)
                .getResultList();
    }

    public void guardarCitaConServicios(Cita cita, List<Integer> idsServicios) {
        execute(em -> {

            em.persist(cita);
            em.flush();


            if (idsServicios != null && !idsServicios.isEmpty()) {
                String query = "INSERT INTO cita_servicio (id_cita, id_servicio) VALUES (?, ?)";
                for (Integer idServicio : idsServicios) {
                    em.createNativeQuery(query)
                            .setParameter(1, cita.getId())
                            .setParameter(2, idServicio)
                            .executeUpdate();
                }
            }
            return null;
        });
    }

    public boolean traslapeEstilista(Integer idEstilista, LocalDate fecha, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin){
        return execute(em ->{
            String query = "SELECT cita.hora_inicio, COALESCE(SUM(serv.duracion_min), 30) AS duracion_total " +
                    "FROM cita cita " +
                    "LEFT JOIN cita_servicio cServicio ON cita.id_cita = cServicio.id_cita " +
                    "LEFT JOIN servicio serv ON cServicio.id_servicio = serv.id_servicio " +
                    "WHERE cita.id_estilista = :idEstilista " +
                    "  AND cita.fecha = :fecha " +
                    "  AND cita.estado <> 'Cancelada' " +
                    "GROUP BY cita.id_cita, cita.hora_inicio";

            @SuppressWarnings("unchecked")
            List<Object[]> resultados= em.createNativeQuery(query).setParameter("idEstilista", idEstilista)
                    .setParameter("fecha",fecha).getResultList();

            for (Object[] row : resultados) {
                java.sql.Time timeSql = (java.sql.Time) row[0];
                LocalTime existInicio = timeSql.toLocalTime();
                int duracionMinutos = ((Number) row[1]).intValue();
                LocalTime existFin = existInicio.plusMinutes(duracionMinutos);

                if (nuevaHoraInicio.isBefore(existFin) && nuevaHoraFin.isAfter(existInicio)) {
                    return true;
                }
            }
            return false;
        });

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
