package mx.desarrollo.integration;

import mx.desarrollo.facade.*;

public class ServiceFacadeLocator {

    private static FacadeAdministrador facadeAdministrador;
    private static FacadeCita facadeCita;
    private static FacadeCliente facadeCliente;
    private static FacadeServicio facadeServicio;
    private static FacadeEstilista facadeEstilista;

    public static FacadeAdministrador getInstanceFacadeAdministrador() {
        if (facadeAdministrador == null) {
            facadeAdministrador = new FacadeAdministrador();
            return facadeAdministrador;
        } else {
            return facadeAdministrador;
        }
    }

    public static FacadeCita getInstanceFacadeCita() {
        if (facadeCita == null) {
            facadeCita = new FacadeCita();
            return facadeCita;
        } else {
            return facadeCita;
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

    public static FacadeServicio getInstanceFacadeServicio() {
        if (facadeServicio == null) {
            facadeServicio = new FacadeServicio();
            return facadeServicio;
        } else {
            return facadeServicio;
        }
    }

    public static FacadeEstilista getInstanceFacadeEstilista() {
        if (facadeEstilista == null) {
            facadeEstilista = new FacadeEstilista();
            return facadeEstilista;
        } else {
            return facadeEstilista;
        }
    }
}
