package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;
import java.util.Date;

public interface NovedadDocenteCargaCoordinacionProjection {

    Long getIdCargaDocente();
    Long getIdPersonaGeneral();
    String getNombreCompleto();
    String getEstado();
    Long getIdCarga();
    Long getIdModalidadContratacion();
    Long getIdCategoriaCatedratico();
    Long getIdNovedadCatalogo();
    String getEstadoNovedad();
    String getTipoNovedad();
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
    String getOnceMeses();
    String getHorasDeExcepcion();
    Long getIdFechasConvocatoria();
    String getFechaConvocatoriaCodigo();
    Date getFechaConvocatoriaInicio();
    Date getFechaConvocatoriaFin();
    Integer getTieneActividades();
}
