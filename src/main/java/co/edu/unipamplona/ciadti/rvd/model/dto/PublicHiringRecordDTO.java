/**
 * Aplicación: rvd
 * Archivo: PublicHiringRecordDto.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 09/11/2026 - Daniel Arias  - Creación inicial
 */


package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record PublicHiringRecordDTO(

    Long idCarga,

    Long idCargaDocente,

    Long idConvocatoriaPreasignacion,

    Long idConvocatoriaContratacion,

    Long idCoordinacion,

    Long idUnidadRegional,

    Long idNivelEducativo,

    Long idModalidadContratacion,

    String convocatoria,

    String nivelEducativo,

    String unidadRegional,

    String coordinacion,

    String modalidadContratacion,

    Date fechaInicio,

    Date fechaFin,

    String estadoCategorizacion,

    String estadoPlanTrabajo,

    String periodoUniversidad,

    String onceMeses,

    String esDocentePlanta


) {}