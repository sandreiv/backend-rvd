/**
 * Aplicación: rvd
 * Archivo: CoordinacionAdministracionFormularioDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

public record CoordinacionAdministracionFormularioDTO(
        String nombre,
        String descripcion,
        Long idUnidadPadre,
        Long idUnidadRegional,
        Long idUnidad,
        Long idModalidad,
        Long idMetodologia,
        Long idCentroCosto,
        String codigo,
        String esAcademica
) {}

/* 17/07/2026 @:Daniel Arias */