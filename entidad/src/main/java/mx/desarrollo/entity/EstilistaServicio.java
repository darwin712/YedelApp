package mx.desarrollo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "estilista_servicio", schema = "yedelapp")
public class EstilistaServicio {
    @EmbeddedId
    private EstilistaServicioId id;

    @MapsId("idEstilista")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_estilista", nullable = false)
    private Estilista idEstilista;

    @MapsId("idServicio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio idServicio;

    public EstilistaServicioId getId() {
        return id;
    }

    public void setId(EstilistaServicioId id) {
        this.id = id;
    }

    public Estilista getIdEstilista() {
        return idEstilista;
    }

    public void setIdEstilista(Estilista idEstilista) {
        this.idEstilista = idEstilista;
    }

    public Servicio getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Servicio idServicio) {
        this.idServicio = idServicio;
    }

}