package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface PersonaCoordinacionListadoProjection {
    Long getIdPersonaGeneral();
    String getPersona();
    String getDocumentoIdentidad();
    Long getIdCoordinacion();
    String getCoordinacion();
    String getEstado();
}