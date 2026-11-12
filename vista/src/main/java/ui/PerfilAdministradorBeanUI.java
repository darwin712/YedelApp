package ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.desarrollo.entity.Administrador;
import mx.desarrollo.entity.Estilista;
import mx.desarrollo.entity.Horario;
import mx.desarrollo.entity.Servicio;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("perfilAdminUI")
@ViewScoped
public class PerfilAdministradorBeanUI implements Serializable {
    @Inject
    private LoginBeanUI loginUI;

    private Administrador administrador;
    private Estilista estilista;
    private List<Administrador> listaAdministradores = new ArrayList<>();
    private List<Horario> horarios = new ArrayList<>();
    private List<Servicio> servicios = new ArrayList<>();
    private Servicio servicioNuevo;
    private Integer idAdministradorSeleccionado;
    private boolean principal;

    @PostConstruct
    public void init() {
        if (loginUI == null || loginUI.getUsuario() == null || loginUI.getUsuario().getId() == null) {
            return;
        }
        principal = ServiceFacadeLocator.getInstanceFacadeAdministrador().esPrincipal(loginUI.getUsuario().getId());
        listaAdministradores = ServiceFacadeLocator.getInstanceFacadeAdministrador().findAll();
        idAdministradorSeleccionado = loginUI.getUsuario().getId();
        cargarAdministrador();
    }

    public String verificarAcceso() {
        if (loginUI == null || loginUI.getRol() == null) {
            return "login?faces-redirect=true";
        }
        if (!"ADMINISTRADOR".equals(loginUI.getRol())) {
            return "login?faces-redirect=true";
        }
        return null;
    }

    public void cambiarAdministrador() {
        cargarAdministrador();
    }

    private void cargarAdministrador() {
        administrador = null;
        for (Administrador admin : listaAdministradores) {
            if (admin.getId().equals(idAdministradorSeleccionado)) {
                administrador = admin;
                break;
            }
        }
        if (administrador == null) {
            return;
        }
        cargarEstilista();
        cargarHorarios();
        cargarServicios();
        servicioNuevo = new Servicio();
        servicioNuevo.setDuracionMin(60);
        servicioNuevo.setPrecio(BigDecimal.ZERO);
    }

    private void cargarEstilista() {
        estilista = null;
        if (administrador.getIdEstilista() != null) {
            for (Estilista item : ServiceFacadeLocator.getInstanceFacadeEstilista().findAll()) {
                if (item.getId().equals(administrador.getIdEstilista())) {
                    estilista = item;
                    break;
                }
            }
        }
        if (estilista == null) {
            estilista = new Estilista();
            estilista.setNombre(administrador.getNombre());
            estilista.setApellidoP(administrador.getApellidoP());
            estilista.setApellidoM(administrador.getApellidoM());
            estilista.setEspecialidad("Belleza");
            estilista.setActivo(true);
            ServiceFacadeLocator.getInstanceFacadeEstilista().guardarEstilista(estilista);
            administrador.setIdEstilista(estilista.getId());
            ServiceFacadeLocator.getInstanceFacadeAdministrador().actualizarAdministrador(administrador);
            if (loginUI.getUsuario().getId().equals(administrador.getId())) {
                loginUI.setUsuario(administrador);
            }
        }
    }

    private void cargarHorarios() {
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        Map<String, Horario> horariosGuardados = new HashMap<>();
        for (Horario horario : ServiceFacadeLocator.getInstanceFacadeHorario().obtenerPorEstilista(estilista.getId())) {
            horariosGuardados.put(horario.getDiaSemana(), horario);
        }
        horarios = new ArrayList<>();
        for (String dia : dias) {
            Horario horario = horariosGuardados.get(dia);
            if (horario == null) {
                horario = new Horario();
                horario.setIdEstilista(estilista.getId());
                horario.setDiaSemana(dia);
                horario.setHoraInicio(LocalTime.of(8, 0));
                horario.setHoraFin(LocalTime.of(20, 0));
            }
            horarios.add(horario);
        }
    }

    private void cargarServicios() {
        servicios = ServiceFacadeLocator.getInstanceFacadeServicio().obtenerServiciosPorEstilista(estilista.getId());
    }

    public void guardarDatos() {
        try {
            if (administrador.getNombre() == null || administrador.getNombre().trim().isEmpty()
                    || administrador.getCorreo() == null || administrador.getCorreo().trim().isEmpty()) {
                mensajeError("Debe ingresar el nombre y el correo.");
                return;
            }
            for (Administrador item : listaAdministradores) {
                if (!item.getId().equals(administrador.getId()) && item.getCorreo() != null
                        && item.getCorreo().equalsIgnoreCase(administrador.getCorreo().trim())) {
                    mensajeError("Ya existe un administrador con ese correo.");
                    return;
                }
            }
            administrador.setNombre(administrador.getNombre().trim());
            administrador.setCorreo(administrador.getCorreo().trim().toLowerCase());
            ServiceFacadeLocator.getInstanceFacadeAdministrador().actualizarAdministrador(administrador);
            estilista.setNombre(administrador.getNombre());
            estilista.setApellidoP(administrador.getApellidoP());
            estilista.setApellidoM(administrador.getApellidoM());
            ServiceFacadeLocator.getInstanceFacadeEstilista().actualizarEstilista(estilista);
            if (loginUI.getUsuario().getId().equals(administrador.getId())) {
                loginUI.setUsuario(administrador);
            }
            mensajeExito("Los datos se guardaron correctamente.");
        } catch (Exception e) {
            mensajeError("No se pudieron guardar los datos.");
        }
    }

    public void guardarHorarios() {
        try {
            for (Horario horario : horarios) {
                if (horario.getHoraInicio() == null || horario.getHoraFin() == null
                        || horario.getHoraInicio().isBefore(LocalTime.of(8, 0))
                        || horario.getHoraFin().isAfter(LocalTime.of(20, 0))
                        || !horario.getHoraInicio().isBefore(horario.getHoraFin())) {
                    mensajeError("El horario debe estar entre las 8:00 AM y las 8:00 PM.");
                    return;
                }
            }
            for (Horario horario : horarios) {
                if (horario.getId() == null) {
                    ServiceFacadeLocator.getInstanceFacadeHorario().guardarHorario(horario);
                } else {
                    ServiceFacadeLocator.getInstanceFacadeHorario().actualizarHorario(horario);
                }
            }
            mensajeExito("El horario se guardó correctamente.");
        } catch (Exception e) {
            mensajeError("No se pudo guardar el horario.");
        }
    }

    public void agregarServicio() {
        try {
            if (servicioNuevo.getNombre() == null || servicioNuevo.getNombre().trim().isEmpty()) {
                mensajeError("Debe ingresar el nombre del servicio.");
                return;
            }
            if (servicioNuevo.getPrecio() == null || servicioNuevo.getPrecio().compareTo(BigDecimal.ZERO) < 0
                    || servicioNuevo.getDuracionMin() == null || servicioNuevo.getDuracionMin() <= 0) {
                mensajeError("Debe ingresar un precio y una duración válidos.");
                return;
            }
            ServiceFacadeLocator.getInstanceFacadeServicio().guardarServicio(servicioNuevo);
            ServiceFacadeLocator.getInstanceFacadeServicio().asignarServicioEstilista(estilista.getId(), servicioNuevo.getId());
            cargarServicios();
            servicioNuevo = new Servicio();
            servicioNuevo.setDuracionMin(60);
            servicioNuevo.setPrecio(BigDecimal.ZERO);
            mensajeExito("El servicio se agregó correctamente.");
        } catch (Exception e) {
            mensajeError("No se pudo agregar el servicio.");
        }
    }

    public void guardarServicios() {
        try {
            for (Servicio servicio : servicios) {
                if (servicio.getPrecio() == null || servicio.getPrecio().compareTo(BigDecimal.ZERO) < 0
                        || servicio.getDuracionMin() == null || servicio.getDuracionMin() <= 0) {
                    mensajeError("Todos los servicios deben tener un precio y una duración válidos.");
                    return;
                }
            }
            for (Servicio servicio : servicios) {
                ServiceFacadeLocator.getInstanceFacadeServicio().actualizarServicio(servicio);
            }
            mensajeExito("Los servicios se guardaron correctamente.");
        } catch (Exception e) {
            mensajeError("No se pudieron guardar los servicios.");
        }
    }

    private void mensajeError(String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", detalle));
    }

    private void mensajeExito(String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", detalle));
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public Estilista getEstilista() {
        return estilista;
    }

    public List<Administrador> getListaAdministradores() {
        return listaAdministradores;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public Servicio getServicioNuevo() {
        return servicioNuevo;
    }

    public void setServicioNuevo(Servicio servicioNuevo) {
        this.servicioNuevo = servicioNuevo;
    }

    public Integer getIdAdministradorSeleccionado() {
        return idAdministradorSeleccionado;
    }

    public void setIdAdministradorSeleccionado(Integer idAdministradorSeleccionado) {
        this.idAdministradorSeleccionado = idAdministradorSeleccionado;
    }

    public boolean isPrincipal() {
        return principal;
    }
}
