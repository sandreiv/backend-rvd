package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface AsociacionCoordinacionListadoProjection {
    Long getId();
    Long getIdCoordinacion();
    String getCoordinacion();
    Long getIdPrograma();
    String getPrograma();
    String getCodigoMateria();
    String getMateria();
    Long getIdCentroCosto();
    String getCentroCosto();
    String getEstado();
}