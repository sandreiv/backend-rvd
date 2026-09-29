package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDateTime;

public interface ObservacionResumenProjection {

    Long getIdObservacion();

    Long getIdPersonaGeneral();

    String getNombrePersonaGeneral();

    String getObservacion();

    LocalDateTime getFecha();
}
