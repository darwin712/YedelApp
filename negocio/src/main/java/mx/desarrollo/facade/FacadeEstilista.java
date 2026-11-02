package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateEstilista;
import mx.desarrollo.entity.Estilista;

import java.util.List;

public class FacadeEstilista {

    private final DelegateEstilista delegateEstilista;

    public FacadeEstilista() {
        this.delegateEstilista = new DelegateEstilista();
    }

    public void guardarEstilista(Estilista estilista){
        delegateEstilista.saveEstilista(estilista);
    }

    public void actualizarEstilista(Estilista estilista){
        delegateEstilista.updateEstilista(estilista);
    }

    public void eliminarEstilista(Estilista estilista){
        delegateEstilista.deleteEstilista(estilista);
    }

    public List<Estilista> findAll(){
        return delegateEstilista.findAll();
    }

}