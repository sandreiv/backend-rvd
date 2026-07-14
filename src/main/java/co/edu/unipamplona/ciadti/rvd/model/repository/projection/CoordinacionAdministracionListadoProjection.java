package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface CoordinacionAdministracionListadoProjection {
    Long getId();
    Long getIdCoordinacionPadre();

    String getNombre();
    String getDescripcion();

    Long getIdUnidadPadre();
    String getUnidadPadre();

    Long getIdUnidadRegional();
    String getUnidadRegional();

    Long getIdUnidad();
    String getUnidad();

    Long getIdModalidad();
    String getModalidad();

    Long getIdMetodologia();
    String getMetodologia();

    Long getIdCentroCosto();
    String getCentroCosto();

    String getCodigo();
    String getEsAcademica();
}