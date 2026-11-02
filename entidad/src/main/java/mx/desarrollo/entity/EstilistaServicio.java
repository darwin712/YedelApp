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
    private mx.desarrollo.entity.Estilista idEstilista;

    @MapsId("idServicio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_servicio", nullable = false)
    private mx.desarrollo.entity.Servicio idServicio;

    public EstilistaServicioId getId() {
        return id;
    }

    public void setId(EstilistaServicioId id) {
        this.id = id;
    }

    public mx.desarrollo.entity.Estilista getIdEstilista() {
        return idEstilista;
    }

    public void setIdEstilista(mx.desarrollo.entity.Estilista idEstilista) {
        this.idEstilista = idEstilista;
    }

    public mx.desarrollo.entity.Servicio getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(mx.desarrollo.entity.Servicio idServicio) {
        this.idServicio = idServicio;
    }

}