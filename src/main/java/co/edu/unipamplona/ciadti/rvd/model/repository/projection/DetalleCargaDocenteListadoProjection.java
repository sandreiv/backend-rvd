package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Optional;

public interface DetalleCargaDocenteListadoProjection {

    Long getIdCargaDocente();
    Long getIdDetalleCargaDocente();
    Optional<Integer> getEsDeNovedad();
    String getHoras();

    Long getIdTipoActividadResuelto();
    Long getIdTipoActividadPadre();
    String getNombreTipoActividad();
    String getDescripcionTipoActividad();
    String getOrdenTipoActividad();
    String getCodigoTipoActividad();
    String getComponenteTipoActividad();
    String getNombreTipoActividadPadre();
    String getDescripcionTipoActividadPadre();
    String getOrdenTipoActividadPadre();
    String getCodigoTipoActividadPadre();
    String getComponenteTipoActividadPadre();

    Long getIdUnidadRegional();
    String getNombreUnidadRegional();
    Long getIdPrograma();
    String getNombrePrograma();
    Long getIdGrupo();
    String getNombreGrupo();
    Long getCapacidadGrupo();
    String getCodigoMateria();
    String getNombreMateria();
    Long getIdCentroCosto();
    String getDescripcionCentroCosto();

    Long getIdPersonaProyecto();
    Long getIdProyecto();
    Long getIdProyectoPadre();
    String getNombreProyecto();
    String getDescripcionProyecto();
    Long getIdTipoProyecto();
    String getNombreTipoProyecto();
    String getDescripcionTipoProyecto();
    String getTipoTipoProyecto();
    Long getIdProyectoPadreEntidad();
    String getNombreProyectoPadre();
    String getDescripcionProyectoPadre();
    Long getIdTipoProyectoPadre();
    String getNombreTipoProyectoPadre();
    String getDescripcionTipoProyectoPadre();
    String getTipoTipoProyectoPadre();
}
