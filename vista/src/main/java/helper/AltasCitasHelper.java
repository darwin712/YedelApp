package helper;

import mx.desarrollo.delegate.DelegateCliente;
import mx.desarrollo.delegate.DelegateEstilista;
import mx.desarrollo.delegate.DelegateServicio;
import mx.desarrollo.entity.Cita;
import mx.desarrollo.entity.Cliente;
import mx.desarrollo.entity.Estilista;
import mx.desarrollo.entity.Horario;
import mx.desarrollo.entity.Servicio;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AltasCitasHelper implements Serializable {

    public void registrarCita(Cita cita, List<Integer> idsServicios) {
        ServiceFacadeLocator.getInstanceFacadeCita().guardarCitaConServicios(cita, idsServicios);
    }



    public List<Servicio> ObtenerlistaServicios() {
        return new DelegateServicio().findAll();
    }

    public List<Cita> obtenerCitasPorEstilista(Integer idEstilista) {
        return ServiceFacadeLocator.getInstanceFacadeCita().ObtenerPorEstilista(idEstilista);
    }

    public boolean traslapeEstilista(Integer idEstilista, LocalDate fecha, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin){
        return ServiceFacadeLocator.getInstanceFacadeCita().traslapeEstilista(idEstilista, fecha, nuevaHoraInicio, nuevaHoraFin, null);
    }

    public boolean traslapeCliente(Integer idCliente, LocalDate fecha, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin){
        return ServiceFacadeLocator.getInstanceFacadeCita().traslapeCliente(idCliente, fecha, nuevaHoraInicio, nuevaHoraFin, null);
    }

    public List<Cliente> obtenerListaClientes (){
        return new DelegateCliente().findAll();
    }

    public List<Servicio> ObtenerServicioPorEstilista(Integer idEstilista){
        return ServiceFacadeLocator.getInstanceFacadeServicio().obtenerServiciosPorEstilista(idEstilista);

    }
    public List<Estilista> obtenerListaEstilistas() {
        return new DelegateEstilista().findAll();
    }

    public List<Horario> obtenerHorariosPorEstilista(Integer idEstilista) {
        return ServiceFacadeLocator.getInstanceFacadeHorario().obtenerPorEstilista(idEstilista);
    }
}
