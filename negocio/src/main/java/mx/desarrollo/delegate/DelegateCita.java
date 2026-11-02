package mx.desarrollo.delegate;

import mx.desarrollo.entity.Cita;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateCita {
    public void saveCita(Cita cita){
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
}