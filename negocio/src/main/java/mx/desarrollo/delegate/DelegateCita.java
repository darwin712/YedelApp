package mx.desarrollo.delegate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import mx.desarrollo.entity.Cita;
import mx.desarrollo.entity.Estilista;
import mx.desarrollo.persistence.dao.CitaDAO;
import mx.desarrollo.persistence.integration.ServiceLocator;
import mx.desarrollo.persistence.persistence.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DelegateCita {


    public void saveCita(Cita cita) {
        ServiceLocator.getInstanceCitaDAO().save(cita);
    }

    public void updateCita(Cita cita){
        ServiceLocator.getInstanceCitaDAO().update(cita);
    }

    public void deleteCita(Cita cita){
        ServiceLocator.getInstanceCitaDAO().delete(cita);
    }

    public List<Cita> findAll(){
        return ServiceLocator.getInstanceCitaDAO().findAll();
    }

    public List<Cita> ObtenerCitasIndividuales(Integer idEstilista){
        return ServiceLocator.getInstanceCitaDAO().findFromWhere("idEstilista","id",String.valueOf(idEstilista));
    }

    public void guardarCitaConServicios(Cita cita, List<Integer> idsServicios) {
        EntityManager em = HibernateUtil.getEntityManager();

        try {
            CitaDAO citaDAO = new CitaDAO(em);
            citaDAO.guardarCitaConServicios(cita, idsServicios);
        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public boolean traslapeEstilista(Integer idEstilista, LocalDate fecha, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CitaDAO citaDAO = new CitaDAO(em);
            return citaDAO.traslapeEstilista(idEstilista, fecha, nuevaHoraInicio, nuevaHoraFin);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
