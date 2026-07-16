/**
 * Aplicación: rvd
 * Archivo: CargaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 01/07/2026
 * Modificaciones:
 * 01/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;

public interface CargaRepository extends JpaRepository<CargaEntity, Long> {

    boolean existsByIdCoordinacion(Long idCoordinacion);

    boolean existsByIdCoordinacionAndIdConvocatoria(Long idCoordinacion, Long idConvocatoria);

    Optional<CargaEntity> findFirstByIdCoordinacionOrderByIdDesc(Long idCoordinacion);

    boolean existsByIdAndIdConvocatoria(Long id, Long idConvocatoria);
}