package mx.desarrollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InventarioEstilistaId implements Serializable {
    private static final long serialVersionUID = -2929667575727471709L;
    @NotNull
    @Column(name = "id_estilista", nullable = false)
    private Integer idEstilista;

    @NotNull
    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    public Integer getIdEstilista() {
        return idEstilista;
    }

    public void setIdEstilista(Integer idEstilista) {
        this.idEstilista = idEstilista;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InventarioEstilistaId entity = (InventarioEstilistaId) o;
        return Objects.equals(this.idProducto, entity.idProducto) &&
                Objects.equals(this.idEstilista, entity.idEstilista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idEstilista);
    }

}