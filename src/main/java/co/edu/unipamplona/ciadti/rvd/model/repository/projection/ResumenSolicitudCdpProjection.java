package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.sql.Clob;

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
}
