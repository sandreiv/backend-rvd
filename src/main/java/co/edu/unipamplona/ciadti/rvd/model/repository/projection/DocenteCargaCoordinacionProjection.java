package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface DocenteCargaCoordinacionProjection {

    Long getIdCargaDocente();
    Long getIdPersonaGeneral();
    String getNombreCompleto();
    String getEstado();
    Long getIdCarga();
    Long getIdModalidadContratacion();
    Long getIdCategoriaCatedratico();
    Date getCargaFechaInicio();
    Date getCargaFechaFin();
    String getValorContrato();
    String getValorPrestaciones();
    String getAsignacionSalarial();
    String getTotalContrato();
    String getValorHora();
    String getPuntos();
    String getValorPunto();
    String getSemanas();
    Long getIdFechasConvocatoria();
    String getFechaConvocatoriaCodigo();
    Date getFechaConvocatoriaInicio();
    Date getFechaConvocatoriaFin();
}
