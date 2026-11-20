package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface DocenteEfectivoSincronizacionProjection {

    Long getIdNovedadCargaDocente();

    Long getIdCargaDocente();

    Long getIdPersonaGeneral();

    String getNombreCompleto();

    String getEstado();

    Long getIdCarga();

    Long getIdUnidadArea();

    Long getIdModalidadContratacion();

    Long getIdCategoriaCatedratico();

    Date getCargaFechaInicio();

    Date getCargaFechaFin();

    String getOnceMeses();

    String getContratacionVinculado();

    String getContratacionSincronizado();

    String getNumeroContrato();
}
