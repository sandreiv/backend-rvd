/**
 * Aplicación: rvd
 * Archivo: ResumenSolicitudCdpProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository.projection
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/09/2026
 * Modificaciones:
 * 03/09/2026 - Andrés Hernández - Creación inicial
 * 23/10/2026 - Andrés Hernández - Inclusión de campos del CDP desde TESORERIA
 */
package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.sql.Clob;
import java.time.LocalDate;
import java.util.Date;

public interface ResumenSolicitudCdpProjection {
    Long getIdCoordinacion();
    String getNombreCoordinacion();
    String getDescripcionCoordinacion();
    String getCodigo();
    String getEsAcademica();
    Long getIdUnidadRegional();
    String getNombreUnidadRegional();
    Long getIdUnidadArea();
    String getNombreUnidadArea();
    Long getIdMetodologia();
    String getDescripcionMetodologia();
    Long getIdModalidad();
    String getDescripcionModalidad();
    Long getIdPeriodoUniversidad();
    Long getAnioPeriodo();
    String getDescripcionPeriodo();
    Long getIdSolicitud();
    String getEstadoSolicitud();
    String getObservacionSolicitud();
    Clob getAdjuntoSolicitud();
    Date getFechaCambioSolicitud();
    String getNumeroCdpSolicitud();
    Long getIdRubroTesoreria();
    Long getIdCdpTesoreria();
    String getNumeroCdpTesoreria();
    LocalDate getFechaCdpTesoreria();
    String getVigenciaCdpTesoreria();
    String getArticuloCdpTesoreria();
    String getDescripcionCdpTesoreria();
}
