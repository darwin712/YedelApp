package mx.desarrollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CitaServicioId implements Serializable {
    @Serial
    private static final long serialVersionUID = -8830612443142265459L;
    @NotNull
    @Column(name = "id_cita", nullable = false)
    private Integer idCita;

    @NotNull
    @Column(name = "id_servicio", nullable = false)
    private Integer idServicio;

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CitaServicioId entity = (CitaServicioId) o;
        return Objects.equals(this.idCita, entity.idCita) &&
                Objects.equals(this.idServicio, entity.idServicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCita, idServicio);
    }
}