/**
 * Aplicación: rvd
 * Archivo: FormaInicioContratoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/11/2026
 * Modificaciones:
 * 10/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.FormaInicioContratoEntity;

public interface FormaInicioContratoRepository extends JpaRepository<FormaInicioContratoEntity, Long> {
    
    List<FormaInicioContratoEntity> findAllByOrderByNombreAsc();
}
