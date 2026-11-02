package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateCita;
import mx.desarrollo.entity.Cita;

import java.util.List;

public class FacadeCita {

    private final DelegateCita delegateCita;

    public FacadeCita() {
        this.delegateCita = new DelegateCita();
    }

    public void guardarCita(Cita cita){
        delegateCita.saveCita(cita);
    }

    public void actualizarCita(Cita Cita){
        delegateCita.updateCita(Cita);
    }

    public void eliminarCita(Cita Cita){
        delegateCita.deleteCita(Cita);
    }

    public List<Cita> findAll(){
        return delegateCita.findAll();
    }

}