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
        return ServiceLocator.getInstanceServicioDAO().obtenerTodos();
    }

    public List<Servicio> obtenerServiciosPorEstilista(Integer idEstilista){
        return ServiceLocator.getInstanceServicioDAO().obtenerServiciosPorEstilista(idEstilista);
    }

    public void asignarServicioEstilista(Integer idEstilista, Integer idServicio){
        ServiceLocator.getInstanceServicioDAO().asignarServicioEstilista(idEstilista, idServicio);
    }
}
