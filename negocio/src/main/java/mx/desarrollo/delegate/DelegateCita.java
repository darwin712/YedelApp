package mx.desarrollo.delegate;

import mx.desarrollo.persistence.dao.CitaDAO;
import mx.desarrollo.persistence.integration.ServiceLocator;

public class DelegateCita {
    private CitaDAO citaDAO;

    public DelegateCita() {
        this.citaDAO = ServiceLocator.getInstanceCitaDAO();
    }

    public void darDeBajaCita(Integer idCita) {
        citaDAO.eliminarCita(idCita);
    }
}