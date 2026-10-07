/**
 * Aplicación: rvd
 * Archivo: DocenteEfectivoCargaCoordinacionProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/09/2026
 * Modificaciones:
 * 16/09/2026 - Andrés Hernández - Creación inicial
 * 24/09/2026 - Daniel Arias - Agregado el tipo de novedad
 * 05/10/2026 - Andrés Hernández - Agregado el ID novedad de la carga docente
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.math.BigDecimal;
import java.util.Date;

public interface DocenteEfectivoCoordinacionProjection {

    Long getIdNovedadCargaDocente();
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
    String getMotivoRechazo();
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
