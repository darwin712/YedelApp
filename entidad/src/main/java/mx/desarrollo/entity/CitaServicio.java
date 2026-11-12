package mx.desarrollo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "cita_servicio", schema = "yedelapp")
public class CitaServicio {
    @EmbeddedId
    private CitaServicioId id;

    @MapsId("idCita")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita idCita;

    @MapsId("idServicio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio idServicio;

    public CitaServicioId getId() {
        return id;
    }

    public void setId(CitaServicioId id) {
        this.id = id;
    }

    public Cita getIdCita() {
        return idCita;
    }

    public void setIdCita(Cita idCita) {
        this.idCita = idCita;
    }

    public Servicio getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Servicio idServicio) {
        this.idServicio = idServicio;
    }

}