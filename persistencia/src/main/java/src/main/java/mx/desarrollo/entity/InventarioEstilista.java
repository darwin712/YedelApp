package mx.desarrollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "inventario_estilista", schema = "yedelapp")
public class InventarioEstilista implements Serializable {
    @Serial
    private static final long serialVersionUID = -936047286636356190L;
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