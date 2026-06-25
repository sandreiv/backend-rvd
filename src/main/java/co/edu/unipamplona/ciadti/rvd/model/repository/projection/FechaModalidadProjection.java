package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface FechaModalidadProjection {

    Long getId();
    String getVacaciones();
    Date getFechaInicio();
    Date getFechaFin();
    String getSemanas();
    String getMinimo();
    String getMaximo();
}
