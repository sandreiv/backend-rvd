/**
 * Aplicación: rvd
 * Archivo: AsosiacionCoordinacionCatalogosDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;

public record AsociacionCoordinacionCatalogosDTO(
        List<CatalogoAdministracionDTO> coordinaciones,
        List<CatalogoAdministracionDTO> programas,
        List<MateriaCatalogoAdministracionDTO> materias,
        List<CatalogoAdministracionDTO> centrosCosto,
        List<CatalogoAdministracionDTO> personas
) {}

/* 17/07/2026 @:Daniel Arias */