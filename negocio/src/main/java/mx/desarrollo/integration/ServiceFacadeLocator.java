package mx.desarrollo.integration;

import mx.desarrollo.facade.FacadeAdministrador;
import mx.desarrollo.facade.FacadeUsuario;
import mx.desarrollo.facade.FacadeCliente;

public class ServiceFacadeLocator {

    private static FacadeAdministrador facadeAdministrador;
    private static FacadeUsuario facadeUsuario;
    private static FacadeCliente facadeCliente;

    public static FacadeAdministrador getInstanceFacadeAdministrador() {
        if (facadeAdministrador == null) {
            facadeAdministrador = new FacadeAdministrador();
            return facadeAdministrador;
        } else {
            return facadeAdministrador;
        }
    }

    public static FacadeUsuario getInstanceFacadeUsuario() {
        if (facadeUsuario == null) {
            facadeUsuario = new FacadeUsuario();
            return facadeUsuario;
        } else {
            return facadeUsuario;
        }
    }

    public static FacadeCliente getInstanceFacadeCliente() {
        if (facadeCliente == null) {
            facadeCliente = new FacadeCliente();
            return facadeCliente;
        } else {
            return facadeCliente;
        }
    }
}
