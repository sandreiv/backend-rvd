/**
 * Aplicación: rvd
 * Archivo: MateriaDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.dto
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 01/07/2026
 * Modificaciones:
 * 01/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.dto;

public record MateriaDTO(
    String codigoMateria,
    String nombre,
    Long horasPracticas,
    Long horasTeoricas,
    Long horasTeoricoPracticas,
    Long periodo,
    Long ponderacionAcademica,
    Long horasDirectas,
    Long capacidad,
    Boolean tieneGrupo
) {}
