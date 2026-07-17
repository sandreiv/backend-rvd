/**
 * Aplicación: rvd
 * Archivo: RelacionCargaProyectoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionCargaProyectoEntity;

public interface RelacionCargaProyectoRepository extends JpaRepository<RelacionCargaProyectoEntity, Long> {

    void deleteByIdDetalleCargaDocente(Long idDetalleCargaDocente);
}

/* 17/07/2026 @:Daniel Arias */
