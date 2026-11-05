package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateHorario;
import mx.desarrollo.entity.Horario;

import java.util.List;

public class FacadeHorario {
    private final DelegateHorario delegateHorario;

    public FacadeHorario() {
        this.delegateHorario = new DelegateHorario();
    }

    public void guardarHorario(Horario horario) {
        delegateHorario.guardarHorario(horario);
    }

    public void actualizarHorario(Horario horario) {
        delegateHorario.actualizarHorario(horario);
    }

    public List<Horario> obtenerPorEstilista(Integer idEstilista) {
        return delegateHorario.obtenerPorEstilista(idEstilista);
    }
}
