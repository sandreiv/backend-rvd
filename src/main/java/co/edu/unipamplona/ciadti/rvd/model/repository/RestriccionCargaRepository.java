/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial para persistencia de restricciones de carga.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionCargaEntity;

public interface RestriccionCargaRepository extends JpaRepository<RestriccionCargaEntity, Long> {
}