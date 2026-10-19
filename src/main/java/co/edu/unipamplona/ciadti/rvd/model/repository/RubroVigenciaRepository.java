/**
 * Aplicación: rvd
 * Archivo: RubroVigenciaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/10/2026
 * Modificaciones:
 * 19/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.RubroVigenciaEntity;

public interface RubroVigenciaRepository extends JpaRepository<RubroVigenciaEntity, Long> {
    
}
