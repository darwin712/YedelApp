package ui;

import helper.AltasCitasHelper;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.desarrollo.entity.Cita;
import mx.desarrollo.entity.Cliente;
import mx.desarrollo.entity.Estilista;
import mx.desarrollo.entity.Servicio;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named("RegistroUI")
@ViewScoped
public class CitasAltasBeanUI implements Serializable {

    private AltasCitasHelper helper;

    private List<Servicio> listaServicios= new ArrayList<>();
    private List<Estilista> listaEstilistas = new ArrayList<>();
    private List<Estilista> EstilistasActivos = new ArrayList<>();
    private List<Cliente> listaCliente;

    private Integer idServicioSeleccionado;
    private Cita cita;
    private Integer idEstilistaSeleccionado;
    private Integer idClienteSeleccionado;
    private Date fechaCita;
    private Servicio servicio;
    private LocalTime horaInicio;
    private String nombreCliente;
    private BigDecimal precioServicio;
    private Integer duracionTotal = 0;
    private List<Integer> idsServiciosSeleccionados = new ArrayList<>();

    @Inject
    private LoginBeanUI loginUI;
    @Inject
    private CalendarioCitasBean calendario;

    @PostConstruct
    public void init() {
        try {
            this.helper = new AltasCitasHelper();

            this.listaServicios = helper.ObtenerlistaServicios();


            this.listaCliente = helper.obtenerListaClientes();

            cita =new Cita();

            cargarEstilistasActivos();

        } catch (Throwable ex) {
            System.err.println("ERROR AL CARGAR LISTAS: " + ex);
            ex.printStackTrace();
        }
    }

    public void onEstilistaChange() {
        if (this.idEstilistaSeleccionado != null) {
            List<Servicio> filtrados = helper.ObtenerServicioPorEstilista(this.idEstilistaSeleccionado);
            this.listaServicios = (filtrados != null) ? filtrados : new ArrayList<>();
        } else {
            this.listaServicios = new ArrayList<>();
        }
    
        

        this.idsServiciosSeleccionados = new ArrayList<>();
        this.precioServicio = BigDecimal.ZERO;
        this.duracionTotal = 0;
    }

    public void guardarCita() {
        try {

            if (this.idClienteSeleccionado == null) {
                mensajeError("Debe seleccionar un cliente.");
                return;
            }

            if (this.idEstilistaSeleccionado == null) {
                mensajeError("Debe seleccionar un estilista.");
                return;
            }

            if (this.idsServiciosSeleccionados == null || this.idsServiciosSeleccionados.isEmpty()) {
                mensajeError("Debe seleccionar al menos un servicio.");
                return;
            }

            if (this.fechaCita == null) {
                mensajeError("Debe seleccionar la fecha de la cita.");
                return;
            }

            LocalDate fechaLocal;
            if (this.fechaCita instanceof java.util.Date) {
                fechaLocal = this.fechaCita.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            } else {
                fechaLocal = (LocalDate) (Object) this.fechaCita;
            }

            if (fechaLocal.isBefore(LocalDate.now())) {
                mensajeError("La fecha de la cita no puede ser anterior al día de hoy.");
                return;
            }

            if (this.horaInicio == null) {
                mensajeError("Debe seleccionar la hora de inicio.");
                return;
            }

            LocalTime apertura = LocalTime.of(8, 0);
            LocalTime cierre = LocalTime.of(18, 0);

            if (this.horaInicio.isBefore(apertura)) {
                mensajeError("El salón abre a las 9:00 AM.");
                return;
            }

            LocalTime horaFinEstimada = this.horaInicio.plusMinutes(this.duracionTotal != null ? this.duracionTotal : 0);
            if (horaFinEstimada.isAfter(cierre)) {
                mensajeError("La cita terminaría a las " + horaFinEstimada
                        + " hrs, no puede ser despues de la hora de cierre.");
                return;
            }

            if(this.helper.traslapeEstilista(this.idEstilistaSeleccionado, fechaLocal,this.horaInicio,horaFinEstimada)){
                mensajeError("El estilista seleccionado ya tiene una cita ocupada en ese rango de horario.");
                return;
            }


            Cita nuevaCita = new Cita();
            nuevaCita.setIdCliente(this.idClienteSeleccionado);
            nuevaCita.setIdEstilista(this.idEstilistaSeleccionado);
            nuevaCita.setFecha(fechaLocal);
            nuevaCita.setHoraInicio(this.horaInicio);
            nuevaCita.setEstado("Activa");


            if (this.loginUI != null && this.loginUI.getUsuario() != null) {
                nuevaCita.setIdAdministrador(this.loginUI.getUsuario().getId());
            }


            this.helper.registrarCita(nuevaCita, this.idsServiciosSeleccionados);

            calendario.recargar();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "La cita se registró correctamente."));

            PrimeFaces.current().ajax().addCallbackParam("errorOcurred", false);
            limpiarFormulario();

        } catch (Exception e) {
            e.printStackTrace();
            mensajeError("No se pudo registrar la cita en la base de datos.");
        }
    }

    public void limpiarFormulario() {
        this.idClienteSeleccionado = null;
        this.idEstilistaSeleccionado = null;
        this.idsServiciosSeleccionados = new ArrayList<>();
        this.listaServicios = new ArrayList<>();
        this.fechaCita = null;
        this.horaInicio = null;
        this.precioServicio = BigDecimal.ZERO;
        this.duracionTotal = 0;
    }

    private void mensajeError(String detalle){
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", detalle));
        PrimeFaces.current().ajax().addCallbackParam("errorOcurred", true);
    }

    public void cargarEstilistasActivos(){

        listaEstilistas = helper.obtenerListaEstilistas();


        for (Estilista e : listaEstilistas){

            if(e.getActivo() == true){

                EstilistasActivos.add(e);

            }
        }
    }


    public void actualizarPrecio() {
        precioServicio = null;
        for (Servicio s : listaServicios) {
            if (s.getId().equals(idServicioSeleccionado)) {
                precioServicio = s.getPrecio();
                break;
            }
        }
    }

    public void calcularTotales() {
        this.precioServicio = BigDecimal.ZERO;
        this.duracionTotal = 0;

        if (idsServiciosSeleccionados != null && !idsServiciosSeleccionados.isEmpty()) {

            for (Integer idSel : idsServiciosSeleccionados) {
                for (Servicio s : listaServicios) {
                    if (s.getId().equals(idSel)) {

                        if (s.getPrecio() != null) {
                            this.precioServicio = this.precioServicio.add(s.getPrecio());
                        }

                        if (s.getDuracionMin() != null) {
                            this.duracionTotal += s.getDuracionMin();
                        }

                        break;
                    }
                }
            }
        }
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Integer getIdServicioSeleccionado() {
        return idServicioSeleccionado;
    }

    public void setIdServicioSeleccionado(Integer idServicioSeleccionado) {
        this.idServicioSeleccionado = idServicioSeleccionado;
    }

    public List<Servicio> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<Servicio> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public List<Estilista> getListaEstilistas() {
        return listaEstilistas;
    }

    public List<Estilista> getEstilistasActivos() {
        return EstilistasActivos;
    }

    public void setEstilistasActivos(List<Estilista> estilistasActivos) {
        this.EstilistasActivos = estilistasActivos;
    }

    public void setListaEstilistas(List<Estilista> listaEstilistas) {
        this.listaEstilistas = listaEstilistas;
    }

    public Cita getCita() {
        return cita;
    }

    public BigDecimal getPrecioServicio() {
        return precioServicio;
    }


    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public List<Cliente> getListaCliente() {
        return listaCliente;
    }

    public void setListaCliente(List<Cliente> listaCliente) {
        this.listaCliente = listaCliente;
    }

    public Integer getIdClienteSeleccionado() {
        return idClienteSeleccionado;
    }

    public void setIdClienteSeleccionado(Integer idClienteSeleccionado) {
        this.idClienteSeleccionado = idClienteSeleccionado;
    }

    public Integer getIdEstilistaSeleccionado() {
        return idEstilistaSeleccionado;
    }

    public void setIdEstilistaSeleccionado(Integer idEstilistaSeleccionado) {
        this.idEstilistaSeleccionado = idEstilistaSeleccionado;
    }

    public List<Integer> getIdsServiciosSeleccionados() {
        return idsServiciosSeleccionados;
    }

    public void setIdsServiciosSeleccionados(List<Integer> idsServiciosSeleccionados) {
        this.idsServiciosSeleccionados = idsServiciosSeleccionados;
    }

    public void setPrecioServicio(BigDecimal precioServicio) {
        this.precioServicio = precioServicio;
    }

    public Integer getDuracionTotal() {
        return duracionTotal;
    }

    public void setDuracionTotal(Integer duracionTotal) {
        this.duracionTotal = duracionTotal;
    }
}