package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Optional;

public interface DetalleDirectoEfectivoSincronizacionProjection {
    
    Long getIdCargaDocente();

    Long getIdDetalleCargaDocente();

    Optional<Integer> getEsDeNovedad();

    Long getIdTipoActividadResuelto();

    String getNombreTipoActividad();

    String getCodigoTipoActividad();

    Long getIdPrograma();

    String getNombrePrograma();

    Long getIdGrupo();

    String getNombreGrupo();

    String getCodigoMateria();

    String getNombreMateria();
}
