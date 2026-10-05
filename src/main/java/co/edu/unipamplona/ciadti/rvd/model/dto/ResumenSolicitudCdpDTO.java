/**
 * Aplicación: rvd
 * Archivo: ResumenSolicitudCdpDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 03/09/2026
 * Modificaciones:
 * 03/09/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record ResumenSolicitudCdpDTO(
    Long id,
    String nombre,
    String descripcion,
    String codigo,
    String esAcademica,
    UnidadDTO unidadRegional,
    UnidadDTO unidadArea,
    MetodologiaDTO metodologia,
    ModalidadDTO modalidad,
    PeriodoUniversidadDTO periodoUniversidad,
    SolicitudCdpListadoDTO solicitud
) {}