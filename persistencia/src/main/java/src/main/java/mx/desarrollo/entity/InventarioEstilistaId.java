package mx.desarrollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InventarioEstilistaId implements Serializable {
    @Serial
    private static final long serialVersionUID = 4083793940314988059L;
    @Column(name = "id_estilista", nullable = false)
    private Integer idEstilista;

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
        if (o == null || getClass() != o.getClass()) return false;
        InventarioEstilistaId entity = (InventarioEstilistaId) o;
        return Objects.equals(this.idEstilista, entity.idEstilista) &&
                Objects.equals(this.idProducto, entity.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEstilista, idProducto);
    }
}