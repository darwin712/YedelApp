package mx.desarrollo.delegate;

import mx.desarrollo.entity.Horario;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateHorario {
    public void guardarHorario(Horario horario) {
        ServiceLocator.getInstanceHorarioDAO().save(horario);
    }

    public void actualizarHorario(Horario horario) {
        ServiceLocator.getInstanceHorarioDAO().update(horario);
    }

    public List<Horario> obtenerPorEstilista(Integer idEstilista) {
        return ServiceLocator.getInstanceHorarioDAO().obtenerPorEstilista(idEstilista);
    }
}
