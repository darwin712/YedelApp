package ui;

import helper.RegistroAdminHelper;
import helper.RegistroClienteHelper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.desarrollo.entity.Administrador;
import mx.desarrollo.entity.Cliente;
import java.io.Serializable;
import java.util.regex.Pattern;



@Named("registroClienteUI")
@RequestScoped
public class RegistroClienteBeanUI implements Serializable {
    private RegistroClienteHelper  registroClienteHelper ;
    private Cliente cliente;
    private String confirmarContrasena;


    private static final Pattern CORREO_VALIDO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern TELEFONO_VALIDO = Pattern.compile("^[0-9]{10}$");
    @Inject
    private LoginBeanUI loginUI;

    public RegistroClienteBeanUI() {
        registroClienteHelper = new RegistroClienteHelper();
        cliente = new Cliente();
    }

    /*public String verificarAcceso(){
        if(loginUI.getRol() == null){
            return "login?faces-redirect=true";
        }
        if("CLIENTE".equals(loginUI.getRol())){
            return "login?faces-redirect=true";
        }
        if(!"ADMINISTRADOR".equals(loginUI.getRol()) || loginUI.getUsuario() == null || loginUI.getUsuario().getId() == null){
            return "index?faces-redirect=true";
        }
        if(!registroAdminHelper.esAdministradorPrincipal(loginUI.getUsuario().getId())){
            return "index?faces-redirect=true";
        }
        return null;
    }*/

    public void registrar(){
        try{

            String nombre = cliente.getNombre() == null ? "" : cliente.getNombre().trim();
            String apellidoP = cliente.getApellidoP() == null ? "" : cliente.getApellidoP().trim();
            String apellidoM = cliente.getApellidoM() == null ? "" : cliente.getApellidoM().trim();
            String telefono = cliente.getTelefono() == null ? "" : cliente.getTelefono().trim();
            String correo = cliente.getCorreo() == null ? "" : cliente.getCorreo().trim().toLowerCase();
            String contrasena = cliente.getContrasena() == null ? "" : cliente.getContrasena().trim();
            String confirmar = confirmarContrasena == null ? "" : confirmarContrasena.trim();

            if(nombre.isEmpty() || apellidoP.isEmpty() || telefono.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmar.isEmpty()){
                mensajeError("Completa todos los campos obligatorios.");
                return;
            }
            if(!CORREO_VALIDO.matcher(correo).matches()){
                mensajeError("Ingresa un correo electronico valido.");
                return;
            }
            if(!TELEFONO_VALIDO.matcher(telefono).matches()){
                mensajeError("El telefono debe tener 10 digitos.");
                return;
            }
            if(contrasena.length() < 8){
                mensajeError("La contrasena debe tener al menos 8 caracteres.");
                return;
            }
            if(!contrasena.equals(confirmar)){
                mensajeError("Las contrasenas no coinciden.");
                return;
            }
            if(registroClienteHelper.existeCorreo(correo)){
                mensajeError("Ya existe una cuenta con ese correo.");
                return;
            }

            cliente.setNombre(nombre);
            cliente.setApellidoP(apellidoP);
            cliente.setApellidoM(apellidoM.isEmpty() ? null : apellidoM);
            cliente.setTelefono(telefono);
            cliente.setCorreo(correo);
            cliente.setContrasena(contrasena);
            registroClienteHelper.guardarCliente(cliente);
            cliente = new Cliente();
            confirmarContrasena = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Su cuenta se registro correctamente."));
        } catch(Exception ex){
            mensajeError("No se pudo registrar la cuenta.");
        }
    }

    private void mensajeError(String detalle){
        FacesContext.getCurrentInstance().validationFailed();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", detalle));
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getConfirmarContrasena() {
        return confirmarContrasena;
    }

    public void setConfirmarContrasena(String confirmarContrasena) {
        this.confirmarContrasena = confirmarContrasena;
    }



}