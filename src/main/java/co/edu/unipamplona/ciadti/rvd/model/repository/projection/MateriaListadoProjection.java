package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

public interface MateriaListadoProjection {

    String getCodigoMateria();
    String getNombre();
    Long getHorasPracticas();
    Long getHorasTeoricas();
    Long getHorasTeoricoPracticas();
    Long getPeriodo();
    Long getPonderacionAcademica();
    Long getCapacidad();
    Long getIdCentroCosto();
    String getDescripcionCentroCosto();
}
