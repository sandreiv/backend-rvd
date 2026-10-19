/**
 * Aplicación: rvd
 * Archivo: NominaProcesoAsientoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/10/2026
 * Modificaciones:
 * 16/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.NominaProcesoAsientoEntity;

public interface NominaProcesoAsientoRepository extends JpaRepository<NominaProcesoAsientoEntity, Long> {
    
}
