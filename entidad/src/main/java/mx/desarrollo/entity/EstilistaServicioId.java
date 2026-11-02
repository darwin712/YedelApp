package mx.desarrollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EstilistaServicioId implements Serializable {
    @Serial
    private static final long serialVersionUID = 492186031190096242L;
    @NotNull
    @Column(name = "id_estilista", nullable = false)
    private Integer idEstilista;

    @NotNull
    @Column(name = "id_servicio", nullable = false)
    private Integer idServicio;

    public Integer getIdEstilista() {
        return idEstilista;
    }

    public void setIdEstilista(Integer idEstilista) {
        this.idEstilista = idEstilista;
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
        EstilistaServicioId entity = (EstilistaServicioId) o;
        return Objects.equals(this.idEstilista, entity.idEstilista) &&
                Objects.equals(this.idServicio, entity.idServicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEstilista, idServicio);
    }
}