package src.main.java.mx.desarrollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import mx.desarrollo.entity.InventarioEstilistaId;

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

    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

}
