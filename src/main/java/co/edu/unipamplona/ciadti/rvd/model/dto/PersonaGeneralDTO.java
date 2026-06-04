/**
 * Aplicación: rvd
 * Archivo: PersonaGeneralDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/06/2026
 * Modificaciones:
 * 04/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.Date;

public record PersonaGeneralDTO (
    Long id,
    String tipoPersona,
    String direccion,
    String mail,
    String telefonoCelular,
    String telefono,
    Long idTipoDocumentoIdentidad,
    String documentoIdentidad,
    String lugarExpedicion,
    Date fechaExpedicion,
    String tipoDocumentoIdentidadNombre,
    String tipoDocumentoIdentidadAbreviatura,
    PersonaNaturalGeneralDTO personaNaturalGeneral
){}

/* 04/06/2026 @:Sebastian Jaimes */
