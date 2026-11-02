package ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mx.desarrollo.entity.Cita;
import mx.desarrollo.entity.Cliente;
import mx.desarrollo.entity.Servicio;
import mx.desarrollo.entity.Estilista;
import mx.desarrollo.integration.ServiceFacadeLocator;
import mx.desarrollo.persistence.persistence.HibernateUtil;

@Named("gestionCitasBean")
@ViewScoped
public class CalendarioCitasBean implements Serializable {

    private static final ZoneId zona = ZoneId.of("America/Tijuana");
    private static final Locale esp = new Locale("es", "MX");

    private static final int horaInicio = 7;
    private static final int horaFin = 20;
    private static final int minutosCalendario = (horaFin - horaInicio) * 60;

    private static final int duracionDefault = 60;

    public enum Vista {
        MES(0), SEMANA(7), TRES_DIAS(3), DIA(1);

        private int dias;

        Vista(int dias) {
            this.dias = dias;
        }

        public int getDias() {
            return dias;
        }
    }

    private Vista vista = Vista.MES;
    private LocalDate fechaReferencia;

    private List<CitaVista> citas = new ArrayList<>();
    private List<Dia> dias = new ArrayList<>();
    private List<SelectItem> opcionesEstilistas = new ArrayList<>();
    private CitaVista citaSeleccionada;
    private Map<Integer, String> nombresEstilistas = new HashMap<>();
    private int estilistaSeleccionado = 0;

    @PostConstruct
    public void init() {
        fechaReferencia = LocalDate.now(zona);
        cargarEstilistas();
        cargarCitas();
        actualizarCalendario();
    }

    private void cargarCitas() {
        Map<Integer, Cliente> clientes = new HashMap<>();
        List<Cliente> listaClientes = ServiceFacadeLocator.getInstanceFacadeCliente().findAll();
        for (Cliente c : listaClientes) {
            clientes.put(c.getId(), c);
        }

        Map<Integer, Servicio> servicios = new HashMap<>();
        List<Servicio> listaServicios = ServiceFacadeLocator.getInstanceFacadeServicio().findAll();
        for (Servicio s : listaServicios) {
            servicios.put(s.getId(), s);
        }

        Map<Integer, List<Integer>> serviciosPorCita = obtenerServiciosPorCita();

        citas.clear();
        List<Cita> listaCitas = ServiceFacadeLocator.getInstanceFacadeCita().findAll();

        for (Cita c : listaCitas) {
            if (c.getFecha() != null && c.getHoraInicio() != null) {
                Cliente cliente = clientes.get(c.getIdCliente());

                List<Servicio> serviciosDeLaCita = new ArrayList<>();
                for (Integer idServicio : serviciosPorCita.getOrDefault(c.getId(), new ArrayList<>())) {
                    Servicio s = servicios.get(idServicio);
                    if (s != null) {
                        serviciosDeLaCita.add(s);
                    }
                }

                int duracion = 0;
                for (Servicio s : serviciosDeLaCita) {
                    if (s.getDuracionMin() != null) {
                        duracion += s.getDuracionMin();
                    }
                }
                if (duracion <= 0) {
                    duracion = duracionDefault;
                }

                LocalDateTime inicio = c.getFecha().atTime(c.getHoraInicio());
                LocalDateTime fin = inicio.plusMinutes(duracion);
                String nombreCliente = null;
                String telefono = "";
                String correo = "";
                String nombreServicio = null;
                float precio = 0;
                int idEstilista = c.getIdEstilista() == null ? 0 : c.getIdEstilista();

                if (cliente != null) {
                    nombreCliente = cliente.getNombre();

                    if (cliente.getTelefono() != null) {
                        telefono = cliente.getTelefono();
                    }
                    if (cliente.getCorreo() != null) {
                        correo = cliente.getCorreo();
                    }
                }

                List<String> nombres = new ArrayList<>();
                for (Servicio s : serviciosDeLaCita) {
                    nombres.add(s.getNombre());

                    if (s.getPrecio() != null) {
                        precio += s.getPrecio().floatValue();
                    }
                }
                if (!nombres.isEmpty()) {
                    nombreServicio = String.join(", ", nombres);
                }

                if (nombreCliente == null) {
                    nombreCliente = "Sin cliente";
                }
                if (nombreServicio == null) {
                    nombreServicio = "Sin servicio";
                }

                String nombreEstilista = nombresEstilistas.get(idEstilista);

                if (nombreEstilista == null) {
                    nombreEstilista = "Sin estilista";
                }

                CitaVista citaVista = new CitaVista(nombreCliente, nombreServicio, idEstilista, telefono, correo, nombreEstilista, duracion, precio, inicio, fin);
                citaVista.setDatosEdicion(c.getId(), c.getIdCliente() == null ? 0 : c.getIdCliente(), serviciosPorCita.getOrDefault(c.getId(), new ArrayList<>()));
                citas.add(citaVista);
            }
        }
    }

    private Map<Integer, List<Integer>> obtenerServiciosPorCita() {
        Map<Integer, List<Integer>> mapa = new HashMap<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            List<Object[]> filas = em
                    .createNativeQuery("SELECT id_cita, id_servicio FROM cita_servicio")
                    .getResultList();

            for (Object[] f : filas) {
                int idCita = ((Number) f[0]).intValue();
                int idServicio = ((Number) f[1]).intValue();

                if (!mapa.containsKey(idCita)) {
                    mapa.put(idCita, new ArrayList<>());
                }
                mapa.get(idCita).add(idServicio);
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return mapa;
    }

    public void recargar() {
        cargarCitas();
        actualizarCalendario();
    }

    private void cargarEstilistas() {
        opcionesEstilistas.clear();
        nombresEstilistas.clear();
        List<Estilista> listaEstilistas = ServiceFacadeLocator.getInstanceFacadeEstilista().findAll();

        for (Estilista e : listaEstilistas) {
            opcionesEstilistas.add(new SelectItem(e.getId(), "Citas de "+e.getNombre()));
            nombresEstilistas.put(e.getId(), e.getNombre());
        }
    }

    public void cambiarVista(Vista nuevaVista) {
        vista = nuevaVista;
        actualizarCalendario();
    }

    public void cambiarVista(String nuevaVista) {
        vista = Vista.valueOf(nuevaVista);
        actualizarCalendario();
    }

    public void hoy() {
        fechaReferencia = LocalDate.now(zona);
        actualizarCalendario();
    }

    public void anterior() {
        mover(-1);
    }

    public void siguiente() {
        mover(1);
    }

    private void mover(int direccion) {
        if (vista == Vista.MES) {
            fechaReferencia = fechaReferencia.plusMonths(direccion);
        } else {
            fechaReferencia = fechaReferencia.plusDays(vista.getDias() * direccion);
        }
        actualizarCalendario();
    }

    private LocalDate primerDia() {
        switch (vista) {
            case MES:
                return YearMonth.from(fechaReferencia).atDay(1);
            case SEMANA:
                int diferencia = fechaReferencia.getDayOfWeek().getValue() % 7;
                return fechaReferencia.minusDays(diferencia);
            default:
                return fechaReferencia;
        }
    }

    private void actualizarCalendario() {
        dias.clear();
        LocalDate hoy = LocalDate.now(zona);
        LocalDate primerDia = primerDia();
        int cantidad;

        if (vista == Vista.MES) {
            cantidad = primerDia.lengthOfMonth();
        } else {
            cantidad = vista.getDias();
        }

        for (int i = 0; i < cantidad; i++) {
            LocalDate fecha = primerDia.plusDays(i);
            List<CitaVista> citasDelDia = obtenerCitasDelDia(fecha);

            if (vista != Vista.MES) {
                asignarColumnas(citasDelDia);
            }

            Dia dia = new Dia(fecha, fecha.equals(hoy), citasDelDia);
            dias.add(dia);
        }
    }

    private List<CitaVista> obtenerCitasDelDia(LocalDate fecha) {
        List<CitaVista> resultado = new ArrayList<>();

        for (CitaVista cita : citas) {
            if (cita.getInicio().toLocalDate().equals(fecha)) {
                boolean pasaFiltro = estilistaSeleccionado == 0 || estilistaSeleccionado == cita.getIdEstilista();
                if (pasaFiltro && (vista == Vista.MES || cita.esVisibleEnHoras())) {
                    resultado.add(cita);
                }
            }
        }

        resultado.sort(Comparator.comparing(CitaVista::getInicio));
        return resultado;
    }

    private void asignarColumnas(List<CitaVista> lista) {
        List<CitaVista> grupo = new ArrayList<>();
        List<LocalDateTime> finColumnas = new ArrayList<>();
        LocalDateTime finGrupo = null;

        for (CitaVista cita : lista) {
            if (finGrupo != null && !cita.getInicio().isBefore(finGrupo)) {
                cerrarGrupo(grupo, finColumnas.size());
                grupo.clear();
                finColumnas.clear();
                finGrupo = null;
            }

            int columna = 0;
            while (columna < finColumnas.size() && cita.getInicio().isBefore(finColumnas.get(columna))) {
                columna++;
            }

            if (columna == finColumnas.size()) {
                finColumnas.add(cita.getFin());
            } else {
                finColumnas.set(columna, cita.getFin());
            }

            cita.setColumna(columna);
            grupo.add(cita);

            if (finGrupo == null || cita.getFin().isAfter(finGrupo)) {
                finGrupo = cita.getFin();
            }
        }
        cerrarGrupo(grupo, finColumnas.size());
    }

    private void cerrarGrupo(List<CitaVista> grupo, int totalColumnas) {
        for (CitaVista cita : grupo) {
            cita.setColumnas(totalColumnas);
        }
    }

    public Vista getVista() {
        return vista;
    }

    public boolean isVistaMes() {
        return vista == Vista.MES;
    }

    public List<Dia> getDias() {
        return dias;
    }

    public int getAlturaPixeles() {
        return minutosCalendario;
    }

    public List<LocalTime> getHoras() {
        List<LocalTime> horas = new ArrayList<>();

        for (int i = horaInicio; i < horaFin; i++) {
            horas.add(LocalTime.of(i, 0));
        }
        return horas;
    }

    public String getTitulo() {
        LocalDate primerDia = primerDia();
        switch (vista) {
            case DIA:
                String dia = primerDia.format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM yyyy", esp));
                return capitalizar(dia);
            default:
                String mes = primerDia.format(DateTimeFormatter.ofPattern("MMMM yyyy", esp));
                return capitalizar(mes);
        }
    }
    private String capitalizar(String texto) {
        return texto.substring(0, 1).toUpperCase(esp) + texto.substring(1);
    }

    public void filtrarPorEstilista() {
        actualizarCalendario();
    }

    public int getEstilistaSeleccionado() {
        return estilistaSeleccionado;
    }

    public void setEstilistaSeleccionado(int estilistaSeleccionado) {
        this.estilistaSeleccionado = estilistaSeleccionado;
    }

    public void verCita(CitaVista cita) {
        this.citaSeleccionada = cita;
    }

    public CitaVista getCitaSeleccionada() {
        return citaSeleccionada;
    }

    public List<SelectItem> getOpcionesEstilistas() {
        return opcionesEstilistas;
    }

    public static class CitaVista implements Serializable {
        private final String cliente;
        private final String servicio;
        private final int idEstilista;
        private final String telefono;
        private final String correo;
        private final String estilista;
        private final int duracion;
        private final float precio;
        private int id;
        private int idCliente;
        private List<Integer> idsServicios = new ArrayList<>();

        private final LocalDateTime inicio;
        private final LocalDateTime fin;

        private int columna = 0;
        private int columnas = 1;

        public void setDatosEdicion(int id, int idCliente, List<Integer> idsServicios) {
            this.id = id;
            this.idCliente = idCliente;
            this.idsServicios = idsServicios;
        }

        public int getId() {
            return id;
        }
        public int getIdCliente() {
            return idCliente;
        }
        public List<Integer> getIdsServicios() {
            return idsServicios;
        }

        public CitaVista(String cliente, String servicio, int idEstilista, String telefono, String correo, String estilista, int duracion, float precio, LocalDateTime inicio, LocalDateTime fin) {
            this.cliente = cliente;
            this.servicio = servicio;
            this.idEstilista = idEstilista;
            this.telefono = telefono;
            this.correo = correo;
            this.estilista = estilista;
            this.duracion = duracion;
            this.precio = precio;
            this.inicio = inicio;
            this.fin = fin;
        }

        public String getCliente() {
            return cliente;
        }

        public String getServicio() {
            return servicio;
        }

        public LocalDateTime getInicio() {
            return inicio;
        }

        public LocalDateTime getFin() {
            return fin;
        }

        public int getIdEstilista() {
            return idEstilista;
        }

        public String getTelefono() {
            return telefono;
        }

        public String getCorreo() {
            return correo;
        }

        public String getEstilista() {
            return estilista;
        }

        public int getDuracion() {
            return duracion;
        }

        public String getPrecio() {
            return String.format("$%.2f",precio);
        }

        public void setColumna(int columna) {
            this.columna = columna;
        }

        public void setColumnas(int columnas) {
            this.columnas = columnas;
        }

        private int minutosDesdeInicioDelDia(LocalDateTime momento) {
            return momento.getHour() * 60 + momento.getMinute() - horaInicio * 60;
        }

        public boolean esVisibleEnHoras() {
            int inicio = minutosDesdeInicioDelDia(this.inicio);
            int fin = minutosDesdeInicioDelDia(this.fin);

            return fin > 0 && inicio < minutosCalendario;
        }

        public String getEstiloColor() {
            if (idEstilista == 0) {
                return "--fondo:#F0ECF3;--borde:#b996ab;--texto:#6d1753;";
            }
            int tono = (int)((idEstilista * 137.508) % 360);
            return "--h:"+tono+";";
        }

        public String getEstiloPosicion() {
            int desde = minutosDesdeInicioDelDia(inicio);
            int hasta = minutosDesdeInicioDelDia(fin);

            if (desde < 0) {
                desde = 0;
            }
            if (hasta > minutosCalendario) {
                hasta = minutosCalendario;
            }

            int alto = hasta - desde;
            if (alto < 22) {
                alto = 22;
            }

            double ancho = 100.0 / columnas;
            return String.format(Locale.ROOT, "top:%dpx;height:%dpx;left:%.2f%%;width:%.2f%%;", desde, alto, columna * ancho, ancho
            );
        }
    }

    public static class Dia implements Serializable {
        private final LocalDate fecha;
        private final boolean hoy;
        private final List<CitaVista> citas;

        public Dia(LocalDate fecha, boolean hoy, List<CitaVista> citas) {
            this.fecha = fecha;
            this.hoy = hoy;
            this.citas = citas;
        }

        public LocalDate getFecha() {
            return fecha;
        }
        public boolean isHoy() {
            return hoy;
        }
        public List<CitaVista> getCitas() {
            return citas;
        }
        public boolean isFinDeSemana() {
            DayOfWeek dia = fecha.getDayOfWeek();
            return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
        }

        public boolean isSabado() {
            return fecha.getDayOfWeek() == DayOfWeek.SATURDAY;
        }

        public String getNombreDia() {
            return fecha.getDayOfWeek().getDisplayName(TextStyle.SHORT, esp).replace(".", "");
        }

        public String getEstiloMes() {
            if (fecha.getDayOfMonth() != 1) {
                return "";
            }
            int columna = fecha.getDayOfWeek().getValue() % 7 + 1;
            return "grid-column-start:" + columna + ";";
        }
    }
}
