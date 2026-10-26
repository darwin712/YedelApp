package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "unidad_aprendizaje")
public class UnidadAprendizaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idunidad", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @NotNull
    @Column(name = "horasclase", nullable = false)
    private Integer horasclase;

    @NotNull
    @Column(name = "horastaller", nullable = false)
    private Integer horastaller;

    @NotNull
    @Column(name = "horaslaboratorio", nullable = false)
    private Integer horaslaboratorio;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idadministrador", nullable = false)
    private Administrador idadministrador;

    @OneToMany(mappedBy = "idunidad")
    private Set<Asignacion> asignacions = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getHorasclase() {
        return horasclase;
    }

    public void setHorasclase(Integer horasclase) {
        this.horasclase = horasclase;
    }

    public Integer getHorastaller() {
        return horastaller;
    }

    public void setHorastaller(Integer horastaller) {
        this.horastaller = horastaller;
    }

    public Integer getHoraslaboratorio() {
        return horaslaboratorio;
    }

    public void setHoraslaboratorio(Integer horaslaboratorio) {
        this.horaslaboratorio = horaslaboratorio;
    }

    public Administrador getIdadministrador() {
        return idadministrador;
    }

    public void setIdadministrador(Administrador idadministrador) {
        this.idadministrador = idadministrador;
    }

    public Set<Asignacion> getAsignacions() {
        return asignacions;
    }

    public void setAsignacions(Set<Asignacion> asignacions) {
        this.asignacions = asignacions;
    }

}