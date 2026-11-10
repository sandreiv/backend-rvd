/**
 * Aplicación: rvd
 * Archivo: PublicHiringRecordProjection.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 09/11/2026
 * Modificaciones:
 * 10/06/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.util.Date;

public interface PublicHiringRecordProjection {

    Long getIdCarga();

    Long getIdCargaDocente();

    Long getIdPeriodoUniversitario();

    String getPeriodoUniversidad();

    String getOnceMeses();
    
    String getEsDocentePlanta();

    Long getIdConvocatoriaPreasignacion();

    Long getIdConvocatoriaContratacion();

    Long getIdCoordinacion();

    Long getIdUnidadRegional();

    Long getIdNivelEducativo();

    Long getIdModalidadContratacion();

    String getConvocatoria();

    String getNivelEducativo();

    String getUnidadRegional();

    String getCoordinacion();

    String getModalidadContratacion();

    Date getFechaInicio();

    Date getFechaFin();

    String getEstadoCategorizacion();

    String getEstadoPlanTrabajo();
}