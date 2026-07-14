package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface DocentePreasignacionProjection {

    Long getIdPersonaGeneral();
    String getDocumentoIdentidad();
    String getPrimerNombre();
    String getSegundoNombre();
    String getPrimerApellido();
    String getSegundoApellido();
    Long getIdCategoriaCatedratico();
    String getDescripcionCategoriaCatedratico();
    Long getIdEscalafon();
    Long getIdModalidadContratacion();
    String getPuntos();
    Long getIdCargaDocente();
    Long getIdCarga();
    Long getIdConvocatoria();
    Long getIdCoordinacion();
    Long getIdModalidadContratacionCarga();
    Long getIdFechasConvocatoria();
}
