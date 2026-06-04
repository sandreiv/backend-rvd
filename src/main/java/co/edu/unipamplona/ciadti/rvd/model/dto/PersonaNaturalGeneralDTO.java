package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record PersonaNaturalGeneralDTO(
        Long idPersonaGeneral,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        String sexo,
        String libretaMilitar,
        String rh,
        String distritoMilitar,
        String numeroPasaporte,
        String otraNacionalidad,
        String emailInstitucional,
        String ubicacionFisicaHojavida,
        String claseLibretaMilitar,
        String vive,
        String fax,
        Date fechaVigenciaPasaporte,
        Date fechaNacimiento,
        String registradoPor,
        Date fechaCambio,
        Long idEstadoCivil,
        EstadoCivilGeneralDTO estadoCivil,
        String idNacionalidad,
        PaisGeneralDTO nacionalidad,
        String idNacimiento,
        PaisGeneralDTO nacimiento,
        Long idLugarNacimiento,
        CiudadGeneralDTO lugarNacimiento
) {
}

