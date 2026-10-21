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

    String estadoPlanTrabajo

) {}