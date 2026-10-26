package mx.desarrollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventario_estilista")
public class InventarioEstilista {
    @EmbeddedId
    private InventarioEstilistaId id;

    @Column(name = "cantidad")
    private Integer cantidad;

    public InventarioEstilistaId getId() {
        return id;
    }

    public void setId(InventarioEstilistaId id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

}