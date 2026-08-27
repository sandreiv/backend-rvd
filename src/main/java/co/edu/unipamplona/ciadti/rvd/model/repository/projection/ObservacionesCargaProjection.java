package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface ObservacionesCargaProjection {
    
    Long getIdPersonaGeneral();
    String getNombrePersonaGeneral();
    String getRolPersonaGeneral();
    String getObservacion();
    Date getFecha();

}
