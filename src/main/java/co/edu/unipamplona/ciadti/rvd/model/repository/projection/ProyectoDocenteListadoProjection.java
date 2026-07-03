package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface ProyectoDocenteListadoProjection {

    Long getId();
    Long getIdProyecto();
    String getNombre();
    String getDescripcion();
    Long getIdTipoProyecto();
    String getNombreTipoProyecto();
    String getDescripcionTipoProyecto();
    String getTipoTipoProyecto();
    Long getIdPersonaProyecto();
    Long getIdPersonaGeneral();
    String getHoras();
    String getTipo();
    String getObservacion();
    Long getIdTipoActividad();
    Long getIdPadreTipoActividad();
    String getNombreTipoActividad();
    String getDescripcionTipoActividad();
    String getOrdenTipoActividad();
    String getCodigoTipoActividad();
}
