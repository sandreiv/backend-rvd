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
}
