package mx.desarrollo.delegate;

import mx.desarrollo.entity.Servicio;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateServicio {
    public void saveServicio(Servicio servicio){
        ServiceLocator.getInstanceServicioDAO().save(servicio);
    }

    public void updateServicio(Servicio servicio){
        ServiceLocator.getInstanceServicioDAO().update(servicio);
    }

    public void deleteServicio(Servicio servicio){
        ServiceLocator.getInstanceServicioDAO().delete(servicio);
    }

    public List<Servicio> findAll(){
        return ServiceLocator.getInstanceServicioDAO().findAll();
    }

}