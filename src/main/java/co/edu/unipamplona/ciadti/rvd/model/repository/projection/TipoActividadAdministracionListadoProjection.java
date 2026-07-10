package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface TipoActividadAdministracionListadoProjection {
    Long getId();
    String getNombre();
    String getDescripcion();
    String getCodigo();
    String getMinimoHoras();
    String getMaximoHoras();
    String getOrden();
    String getEstado();
}