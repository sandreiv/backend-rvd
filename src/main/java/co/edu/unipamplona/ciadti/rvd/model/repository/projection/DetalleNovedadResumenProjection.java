package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface DetalleNovedadResumenProjection {

    Long getIdDetalleNovedadCargaDocente();

    String getHoras();

    String getNombreTipoActividad();

    String getCodigoTipoActividad();

    String getNombreTipoActividadPadre();

    String getCodigoTipoActividadPadre();

    String getNombreUnidadRegional();

    String getNombrePrograma();

    String getNombreGrupo();

    String getNombreMateria();

    Long getIdCentroCosto();

    String getDescripcionCentroCosto();
}
