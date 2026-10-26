package mx.desarrollo.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "estilista_servicio")
public class EstilistaServicio {
    @EmbeddedId
    private EstilistaServicioId id;

    public EstilistaServicioId getId() {
        return id;
    }

    public void setId(EstilistaServicioId id) {
        this.id = id;
    }

    //TODO [Reverse Engineering] generate columns from DB
}