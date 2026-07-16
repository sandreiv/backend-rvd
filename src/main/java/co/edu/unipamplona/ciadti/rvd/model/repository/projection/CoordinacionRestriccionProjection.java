package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface CoordinacionRestriccionProjection {
    Long getId();
    Long getIdCoordinacion();
    String getNombreCoordinacion();
    String getDescripcionCoordinacion();
    String getCodigoCoordinacion();
    Date getFechaInicio();
    Date getFechaFin();
    String getEstado();
}
