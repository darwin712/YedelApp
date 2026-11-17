package ui;

import helper.AltasCitasHelper;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.desarrollo.entity.Cliente;
import mx.desarrollo.entity.Estilista;
import mx.desarrollo.entity.Horario;
import mx.desarrollo.entity.Servicio;
import mx.desarrollo.integration.ServiceFacadeLocator;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Named("edicionCita")
@ViewScoped
public class ModificarCitaBean implements Serializable {

    @Inject
    private CalendarioCitasBean calendario;

    @Inject
    private LoginBeanUI loginUI;

    private AltasCitasHelper helper = new AltasCitasHelper();

    private boolean editando;
    private Integer idCliente;
    private String nombreClienteExterno;
    private String telefonoClienteExterno;
    private String correoClienteExterno;
    private boolean clienteSinCuenta;
    private Integer idEstilista;
    private List<Integer> idsServicios = new ArrayList<>();
    private LocalDate fecha;
    private LocalTime hora;
    private float precio = 0;
    private Integer duracion = 0;

    private List<Cliente> listaClientes = new ArrayList<>();
    private List<Estilista> estilistasActivos = new ArrayList<>();
    private List<Servicio> listaServicios = new ArrayList<>();

    public void iniciarEdicion() {
        CalendarioCitasBean.CitaVista cita = calendario.getCitaSeleccionada();
        if (cita == null) {
            return;
        }

        listaClientes = helper.obtenerListaClientes();

        estilistasActivos = new ArrayList<>();
        for (Estilista e : helper.obtenerListaEstilistas()) {
            if (Boolean.TRUE.equals(e.getActivo())) {
                estilistasActivos.add(e);
            }
        }

        idCliente = cita.getIdCliente() == 0 ? null : cita.getIdCliente();
        nombreClienteExterno = cita.getNombreClienteExterno();
        clienteSinCuenta = idCliente == null && nombreClienteExterno != null && !nombreClienteExterno.isBlank();
        if (clienteSinCuenta) {
            telefonoClienteExterno = cita.getTelefono();
            correoClienteExterno = cita.getCorreo();
        } else {
            telefonoClienteExterno = null;
            correoClienteExterno = null;
        }

        idEstilista = cita.getIdEstilista() == 0 ? null : cita.getIdEstilista();
        if (loginUI != null && !loginUI.isPrincipal()) {
            idEstilista = loginUI.getIdEstilista();
        }
        fecha = cita.getInicio().toLocalDate();
        hora = cita.getInicio().toLocalTime();

        cargarServiciosDelEstilista();
        idsServicios = new ArrayList<>(cita.getIdsServicios());
        calcularTotales();

        editando = true;
    }

    public void cancelar() {
        editando = false;
        clienteSinCuenta = false;
        nombreClienteExterno = null;
        telefonoClienteExterno = null;
        correoClienteExterno = null;
    }

    public void onEstilistaChange() {
        if (loginUI != null && !loginUI.isPrincipal()) {
            idEstilista = loginUI.getIdEstilista();
        }
        cargarServiciosDelEstilista();
        idsServicios = new ArrayList<>();
        calcularTotales();
    }

    private void cargarServiciosDelEstilista() {
        if (idEstilista == null) {
            listaServicios = new ArrayList<>();
            return;
        }
        List<Servicio> filtrados = helper.ObtenerServicioPorEstilista(idEstilista);
        listaServicios = filtrados != null ? filtrados : new ArrayList<>();
    }

    public void calcularTotales() {
        precio = 0;
        duracion = 0;

        for (Integer idSel : idsServicios) {
            for (Servicio s : listaServicios) {
                if (s.getId().equals(idSel)) {
                    if (s.getPrecio() != null) {
                        precio += s.getPrecio().floatValue();
                    }
                    if (s.getDuracionMin() != null) {
                        duracion += s.getDuracionMin();
                    }
                    break;
                }
            }
        }
    }

    public void aceptar() {
        try {
            CalendarioCitasBean.CitaVista cita = calendario.getCitaSeleccionada();
            if (cita == null) {
                error("No se encontró la cita seleccionada.");
                return;
            }
            Integer idCita = cita.getId();

            if (loginUI != null && !loginUI.isPrincipal()) {
                idEstilista = loginUI.getIdEstilista();
            }

            String externo = "";
            if (nombreClienteExterno != null) {
                externo = nombreClienteExterno.trim();
            }
            String telefono = "";
            if (telefonoClienteExterno != null) {
                telefono = telefonoClienteExterno.trim();
            }
            String correo = "";
            if (correoClienteExterno != null) {
                correo = correoClienteExterno.trim();
            }

            if (clienteSinCuenta) {
                if (externo.isEmpty()) {
                    error("Debe escribir el nombre del cliente");
                    return;
                }
                if (telefono.isEmpty() && correo.isEmpty()) {
                    error("Debe escribir al menos un método de contacto para el cliente");
                    return;
                }
                idCliente = null;
            }else{
                if (idCliente == null) {
                    error("Debe seleccionar un cliente.");
                    return;
                }
                externo = ""; telefono = ""; correo = "";
            }

            if (idEstilista == null) {
                error("Debe seleccionar un empleado.");
                return;
            }
            if (idsServicios == null || idsServicios.isEmpty()) {
                error("Debe seleccionar al menos un servicio.");
                return;
            }
            if (fecha == null) {
                error("Debe seleccionar la fecha de la cita.");
                return;
            }

            if (fecha.isBefore(LocalDate.now()) && !fecha.equals(cita.getInicio().toLocalDate())) {
                error("La fecha de la cita no puede ser anterior al día de hoy.");
                return;
            }

            if (hora == null) {
                error("Debe seleccionar la hora de inicio.");
                return;
            }

            LocalTime fin = hora.plusMinutes(duracion != null ? duracion : 0);

            if (hora.isBefore(LocalTime.of(8, 0))) {
                error("El salón abre a las 8:00 AM.");
                return;
            }
            if (fin.isAfter(LocalTime.of(20, 0))) {
                error("La cita terminaría a las " + fin + " hrs, no puede ser después de la hora de cierre.");
                return;
            }

            if (!horarioDisponible(fecha, hora, fin)) {
                error("La cita está fuera del horario del empleado seleccionado.");
                return;
            }

            if (ServiceFacadeLocator.getInstanceFacadeCita().traslapeEstilista(idEstilista, fecha, hora, fin, idCita)) {
                error("El empleado seleccionado ya tiene una cita ocupada en ese rango de horario.");
                return;
            }

            if (ServiceFacadeLocator.getInstanceFacadeCita().traslapeCliente(idCliente, fecha, hora, fin, idCita)) {
                error("El cliente seleccionado ya tiene una cita ocupada en ese rango de horario.");
                return;
            }

            String nombreCliente = null;
            if (!externo.isEmpty()) {
                nombreCliente = externo;
            }
            String telefonoCliente = null;
            if (!telefono.isEmpty()) {
                telefonoCliente = telefono;
            }
            String correoCliente = null;
            if (!correo.isEmpty()) {
                correoCliente = correo;
            }
            ServiceFacadeLocator.getInstanceFacadeCita()
                    .actualizarCitaConServicios(idCita, idCliente, nombreCliente, telefonoCliente, correoCliente, idEstilista, fecha, hora, idsServicios);

            calendario.recargar();
            editando = false;
            PrimeFaces.current().ajax().addCallbackParam("errorOcurred", false);

        } catch (Exception e) {
            e.printStackTrace();
            error("No se pudo modificar la cita.");
        }
    }

    private boolean horarioDisponible(LocalDate fechaCita, LocalTime inicio, LocalTime fin) {
        List<Horario> horarios = helper.obtenerHorariosPorEstilista(idEstilista);
        if (horarios == null || horarios.isEmpty()) {
            return true;
        }
        String dia = nombreDia(fechaCita);
        for (Horario horario : horarios) {
            if (dia.equals(horario.getDiaSemana())) {
                return horario.getHoraInicio() != null && horario.getHoraFin() != null
                        && !inicio.isBefore(horario.getHoraInicio())
                        && !fin.isAfter(horario.getHoraFin());
            }
        }
        return false;
    }

    private String nombreDia(LocalDate fechaCita) {
        return switch (fechaCita.getDayOfWeek()) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }

    public void eliminar() {
        try {
            if (calendario.getCitaSeleccionada() == null) {
                error("No se encontró la cita seleccionada.");
                return;
            }
            ServiceFacadeLocator.getInstanceFacadeCita().darDeBajaCita(calendario.getCitaSeleccionada().getId());
            calendario.recargar();
            editando = false;
            PrimeFaces.current().ajax().addCallbackParam("errorOcurred", false);

        } catch (Exception e) {
            e.printStackTrace();
            error("No se pudo eliminar la cita.");
        }
    }

    private void error(String detalle) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", detalle));
        PrimeFaces.current().ajax().addCallbackParam("errorOcurred", true);
    }

    public boolean isEditando() {
        return editando;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreClienteExterno() {
        return nombreClienteExterno;
    }

    public void setNombreClienteExterno(String nombreClienteExterno) {
        this.nombreClienteExterno = nombreClienteExterno;
    }

    public String getTelefonoClienteExterno() {
        return telefonoClienteExterno;
    }
    public void setTelefonoClienteExterno(String telefonoClienteExterno) {
        this.telefonoClienteExterno = telefonoClienteExterno;
    }

    public String getCorreoClienteExterno() {
        return correoClienteExterno;
    }
    public void setCorreoClienteExterno(String correoClienteExterno) {
        this.correoClienteExterno = correoClienteExterno;
    }

    public boolean isClienteSinCuenta() {
        return clienteSinCuenta;
    }

    public Integer getIdEstilista() {
        return idEstilista;
    }

    public void setIdEstilista(Integer idEstilista) {
        this.idEstilista = idEstilista;
    }

    public List<Integer> getIdsServicios() {
        return idsServicios;
    }

    public void setIdsServicios(List<Integer> idsServicios) {
        this.idsServicios = idsServicios;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalDate getFechaMinima() {
        return LocalDate.now(ZoneId.of("America/Tijuana"));
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public float getPrecio() {
        return precio;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public List<Estilista> getEstilistasActivos() {
        return estilistasActivos;
    }

    public List<Servicio> getListaServicios() {
        return listaServicios;
    }

    public boolean isPrincipal() {
        return loginUI != null && loginUI.isPrincipal();
    }

    public String getNombreEstilista() {
        if (idEstilista == null) {
            return "Sin empleado asignado";
        }
        for (Estilista estilista : estilistasActivos) {
            if (estilista.getId().equals(idEstilista)) {
                return estilista.getNombre() + " "
                        + (estilista.getApellidoP() == null ? "" : estilista.getApellidoP()) + " "
                        + (estilista.getApellidoM() == null ? "" : estilista.getApellidoM());
            }
        }
        return "Sin empleado asignado";
    }
}
