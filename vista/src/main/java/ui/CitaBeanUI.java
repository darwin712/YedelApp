package ui;


import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped; // ¡Importante: con jakarta!
import mx.desarrollo.delegate.DelegateCita;

import java.io.Serializable;

@Named("citaBean")
@ViewScoped
public class CitaBeanUI implements Serializable {
    private Integer idCitaSeleccionada;
    private DelegateCita delegateCita;

    public CitaBeanUI() {
        this.delegateCita = new DelegateCita();
    }

    public String bajaCita() {
        try {
            if (idCitaSeleccionada != null) {
                delegateCita.darDeBajaCita(idCitaSeleccionada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getIdCitaSeleccionada() {
        return idCitaSeleccionada;
    }

    public void setIdCitaSeleccionada(Integer idCitaSeleccionada) {
        this.idCitaSeleccionada = idCitaSeleccionada;
    }
}