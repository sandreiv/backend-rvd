package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDate;

public interface FechaModalidadProjection {

    Long getId();
    String getVacaciones();
    LocalDate getFechaInicio();
    LocalDate getFechaFin();
    String getSemanas();
    String getMinimo();
    String getMaximo();
}
