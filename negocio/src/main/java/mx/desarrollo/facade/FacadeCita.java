package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateCita;
import mx.desarrollo.entity.Cita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class FacadeCita {

    private final DelegateCita delegateCita;

    public FacadeCita() {
        this.delegateCita = new DelegateCita();
    }

    public void guardarCita(Cita cita) {
        delegateCita.saveCita(cita);
    }

    public void actualizarCita(Cita cita) {
        delegateCita.updateCita(cita);
    }

    public void eliminarCita(Cita cita) {
        delegateCita.deleteCita(cita);
    }

    public List<Cita> findAll() {
        return delegateCita.findAll();
    }

    public List<Cita> ObtenerPorEstilista (Integer idEstilista){
return delegateCita.ObtenerCitasIndividuales(idEstilista);
    }

    public void guardarCitaConServicios(Cita cita, List<Integer> idsServicios) {
        delegateCita.guardarCitaConServicios(cita, idsServicios);
    }

    public void darDeBajaCita(Integer idCita) {
        delegateCita.darDeBajaCita(idCita);
    }

    public boolean traslapeEstilista(Integer idEstilista, LocalDate fecha, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin, Integer idCitaIgnorar) {
        return delegateCita.traslapeEstilista(idEstilista, fecha, nuevaHoraInicio, nuevaHoraFin, idCitaIgnorar);
    }

    public void actualizarCitaConServicios(Integer idCita, Integer idCliente, Integer idEstilista, LocalDate fecha, LocalTime hora, List<Integer> idsServicios) {
        delegateCita.actualizarCitaConServicios(idCita, idCliente, idEstilista, fecha, hora, idsServicios);
    }

}