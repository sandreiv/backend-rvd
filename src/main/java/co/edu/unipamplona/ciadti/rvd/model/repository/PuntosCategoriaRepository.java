/**
 * Aplicación: rvd
 * Archivo: PuntosCategoriaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 23/06/2026
 * Modificaciones:
 * 23/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.PuntosCategoriaEntity;

public interface PuntosCategoriaRepository
        extends JpaRepository<PuntosCategoriaEntity, Long> {

    PuntosCategoriaEntity findByIdCategoriaCatedratico(Long idCategoriaCatedratico);
}
