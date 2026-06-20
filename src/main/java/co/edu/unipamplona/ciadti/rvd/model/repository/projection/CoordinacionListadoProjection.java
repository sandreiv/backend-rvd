package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface CoordinacionListadoProjection {

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
    Long getIdConvocatoria();
    String getNombreConvocatoria();
    String getDescripcionConvocatoria();
    String getEstadoConvocatoria();
    Long getIdNivelEducativo();
    String getDescripcionNivelEducativo();
    Long getIdPeriodoUniversidad();
    Long getAnioPeriodo();
    String getDescripcionPeriodo();
    Long getIdCarga();
    Long getIdEstadoCarga();
    String getNombreEstadoCarga();
    String getDescripcionEstadoCarga();
    Long getIdModalidadContratacion();
    String getNombreModalidadContratacion();
}
