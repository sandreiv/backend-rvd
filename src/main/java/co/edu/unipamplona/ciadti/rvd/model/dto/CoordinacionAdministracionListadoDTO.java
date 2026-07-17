/**
 * Aplicación: rvd
 * Archivo: CoordinacionAdministracionListadoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionAdministracionListadoDTO(
        Long id,
        Long idCoordinacionPadre,

        String nombre,
        String descripcion,

        Long idUnidadPadre,
        String unidadPadre,

        Long idUnidadRegional,
        String unidadRegional,

        Long idUnidad,
        String unidad,

        Long idModalidad,
        String modalidad,

        Long idMetodologia,
        String metodologia,

        Long idCentroCosto,
        String centroCosto,

        String codigo,
        String esAcademica
) {}

/* 17/07/2026 @:Daniel Arias */