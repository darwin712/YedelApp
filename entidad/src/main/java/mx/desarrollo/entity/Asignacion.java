package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalTime;

@Entity
@Table(name = "asignacion")
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idasignacion", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idprofesor", nullable = false)
    private Profesor idprofesor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idunidad", nullable = false)
    private UnidadAprendizaje idunidad;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idadministrador", nullable = false)
    private Administrador idadministrador;

    @Size(max = 10)
    @NotNull
    @Column(name = "grupo", nullable = false, length = 10)
    private String grupo;

    @Size(max = 10)
    @NotNull
    @Column(name = "semestre", nullable = false, length = 10)
    private String semestre;

    @Size(max = 15)
    @NotNull
    @Column(name = "diasemana", nullable = false, length = 15)
    private String diasemana;

    @Size(max = 15)
    @NotNull
    @ColumnDefault("'Clase'")
    @Column(name = "tipo", nullable = false, length = 15)
    private String tipo;

    @NotNull
    @Column(name = "horainicio", nullable = false)
    private LocalTime horainicio;

    @NotNull
    @Column(name = "horafin", nullable = false)
    private LocalTime horafin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Profesor getIdprofesor() {
        return idprofesor;
    }

    public void setIdprofesor(Profesor idprofesor) {
        this.idprofesor = idprofesor;
    }

    public UnidadAprendizaje getIdunidad() {
        return idunidad;
    }

    public void setIdunidad(UnidadAprendizaje idunidad) {
        this.idunidad = idunidad;
    }

    public Administrador getIdadministrador() {
        return idadministrador;
    }

    public void setIdadministrador(Administrador idadministrador) {
        this.idadministrador = idadministrador;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getDiasemana() {
        return diasemana;
    }

    public void setDiasemana(String diasemana) {
        this.diasemana = diasemana;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalTime getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(LocalTime horainicio) {
        this.horainicio = horainicio;
    }

    public LocalTime getHorafin() {
        return horafin;
    }

    public void setHorafin(LocalTime horafin) {
        this.horafin = horafin;
    }

}