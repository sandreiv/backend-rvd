package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDateTime;

public interface ObservacionesCargaProjection {
    
    Long getIdObservacion();
    Long getIdPersonaGeneral();
    String getNombrePersonaGeneral();
    String getRolPersonaGeneral();
    String getObservacion();
    LocalDateTime getFecha();
    Integer getVisto();
}
