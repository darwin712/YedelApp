package mx.desarrollo.facade;


import mx.desarrollo.delegate.DelegateEstilista;
import mx.desarrollo.entity.Estilista;

import java.util.List;

public class FacadeEstilista {

    private final DelegateEstilista delegateEstilista;

    public FacadeEstilista() {
        this.delegateEstilista = new DelegateEstilista();
    }



    public void saveEstilista(Estilista Estilista){
        delegateEstilista.saveEstilista(Estilista);
    }

    public List<Estilista> findAll(){
        return delegateEstilista.findAll();
    }

}