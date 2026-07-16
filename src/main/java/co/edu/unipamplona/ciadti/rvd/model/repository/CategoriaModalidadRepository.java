/**
 * Aplicación: rvd
 * Archivo: CategoriaModalidadRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 14/07/2026
 * Modificaciones:
 * 14/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.CategoriaModalidadEntity;

public interface CategoriaModalidadRepository
        extends JpaRepository<CategoriaModalidadEntity, Long> {

    Optional<CategoriaModalidadEntity> findByIdModalidadContratacion(
            Long idModalidadContratacion);
}
