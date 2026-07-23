package co.edu.unipamplona.ciadti.rvd.model.service;

public interface ConvocatoriaEstadoService {

    void syncEstadoConvocatoria(Long idConvocatoria);

    void syncEstadoConvocatoriaByFecha(Long idFechasConvocatoria);

    void syncEstadosConvocatoriasConRestricciones();
}