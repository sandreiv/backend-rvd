/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/07/2026
 * Modificaciones:
 * 06/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

import java.util.List;
import java.util.Optional;

public record DetalleCargaDocenteDTO(
    Long idDetalleCargaDocente,
    Long idCargaDocente,
    Optional<Integer> esDeNovedad,
    List<DetalleCargaDocenteActividadDTO> detalles
){}
