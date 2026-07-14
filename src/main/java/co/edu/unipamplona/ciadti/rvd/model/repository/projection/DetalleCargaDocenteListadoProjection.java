package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface DetalleCargaDocenteListadoProjection {

    Long getIdCargaDocente();
    Long getIdDetalleCargaDocente();
    String getHoras();

    Long getIdTipoActividadResuelto();
    Long getIdTipoActividadPadre();
    String getNombreTipoActividad();
    String getDescripcionTipoActividad();
    String getOrdenTipoActividad();
    String getCodigoTipoActividad();
    String getNombreTipoActividadPadre();
    String getDescripcionTipoActividadPadre();
    String getOrdenTipoActividadPadre();
    String getCodigoTipoActividadPadre();

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
