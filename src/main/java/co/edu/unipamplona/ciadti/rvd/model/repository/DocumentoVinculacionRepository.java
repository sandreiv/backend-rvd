/**
 * Aplicación: rvd
 * Archivo: DocumentoVinculacionRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.DocumentoVinculacionEntity;

public interface DocumentoVinculacionRepository extends JpaRepository<DocumentoVinculacionEntity, Long> {
    
    boolean existsByIdContrato(Long idContrato);
}
