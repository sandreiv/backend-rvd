package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDateTime;

public interface HistorialCargaDocenteObservacionProjection {

    Long getIdHistorial();

    Long getIdPersonaGeneral();

    String getNombrePersonaGeneral();

    String getRolPersonaGeneral();

    String getObservacion();

    LocalDateTime getFecha();

    String getEstado();
}