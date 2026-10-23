package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.time.LocalDate;

public record PublicBasicInformationDTO(

        Long idPersonaGeneral,

        Long idTipoDocumentoIdentidad,
        String tipoDocumento,
        String abreviaturaTipoDocumento,
        String documentoIdentidad,
        String lugarExpedicion,
        LocalDate fechaExpedicion,

        String nombreCompleto,
        String sexo,
        LocalDate fechaNacimiento,

        Long idEstadoCivil,
        String estadoCivil,

        Long idLugarNacimiento,
        String lugarNacimiento,

        String idNacionalidad,
        String nacionalidad,

        String idPaisNacimiento,
        String paisNacimiento,

        String rh,

        String direccion,
        String telefonoCelular,
        String telefono,
        String correoPersonal,
        String correoInstitucional,
        String fax,

        String libretaMilitar,
        String claseLibretaMilitar,
        String distritoMilitar,

        String numeroPasaporte,
        LocalDate fechaVigenciaPasaporte,
        String otraNacionalidad,

        String vive,
        String ubicacionFisicaHojavida

) {
}