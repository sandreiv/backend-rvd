/**
 * Aplicación: rvd
 * Archivo: TipoObjetoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoObjetoEntity;

public interface TipoObjetoRepository extends JpaRepository<TipoObjetoEntity, Long> {

    /**
     * Tipos de objeto contractual ordenados por descripción, que es lo que se
     * pinta en el select del anexo de contratación.
     */
    List<TipoObjetoEntity> findAllByOrderByDescripcionAsc();
}
