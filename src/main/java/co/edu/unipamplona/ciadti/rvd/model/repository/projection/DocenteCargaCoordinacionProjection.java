package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;
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
    BigDecimal getValorContrato();
    BigDecimal getValorPrestaciones();
    BigDecimal getAsignacionSalarial();
    BigDecimal getTotalContrato();
    BigDecimal getValorHora();
    String getPuntos();
    BigDecimal getValorPunto();
    String getSemanas();
    Long getIdFechasConvocatoria();
    String getFechaConvocatoriaCodigo();
    Date getFechaConvocatoriaInicio();
    Date getFechaConvocatoriaFin();
}
