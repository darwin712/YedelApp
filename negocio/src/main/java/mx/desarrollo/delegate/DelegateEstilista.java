package mx.desarrollo.delegate;

import mx.desarrollo.entity.Estilista;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateEstilista{
public void saveEstilista(Estilista estilista){
    ServiceLocator.getInstanceEstilistaDAO().save(estilista);
}

public void updateEstilista(Estilista estilista){
    ServiceLocator.getInstanceEstilistaDAO().update(estilista);
}

public void deleteEstilista(Estilista estilista){
    ServiceLocator.getInstanceEstilistaDAO().delete(estilista);
}

public List<Estilista> findAll(){
    return ServiceLocator.getInstanceEstilistaDAO().findAll();
}
}
