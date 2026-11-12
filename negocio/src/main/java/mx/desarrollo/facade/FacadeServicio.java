package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateServicio;
import mx.desarrollo.entity.Servicio;

import java.util.List;

public class FacadeServicio {

    private final DelegateServicio delegateServicio;

    public FacadeServicio() {
        this.delegateServicio = new DelegateServicio();
    }

    public void guardarServicio(Servicio servicio) {
        delegateServicio.saveServicio(servicio);
    }

    public void actualizarServicio(Servicio servicio) {
        delegateServicio.updateServicio(servicio);
    }

    public void eliminarServicio(Servicio servicio) {
        delegateServicio.deleteServicio(servicio);
    }

    public List<Servicio> findAll() {
        return delegateServicio.findAll();
    }

    public List<Servicio> obtenerServiciosPorEstilista (Integer idEstilista){
        return delegateServicio.obtenerServiciosPorEstilista(idEstilista);
    }

    public void asignarServicioEstilista(Integer idEstilista, Integer idServicio){
        delegateServicio.asignarServicioEstilista(idEstilista, idServicio);
    }

}
