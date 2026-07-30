/**
 * Aplicación: rvd
 * Archivo: TipoActividadModalidadRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial para persistencia de tipos de actividad por modalidad.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Optional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoActividadModalidadEntity;

public interface TipoActividadModalidadRepository
        extends JpaRepository<TipoActividadModalidadEntity, Long> {

    Optional<TipoActividadModalidadEntity> findByIdModalidadContratacion(
            Long idModalidadContratacion);

    List<TipoActividadModalidadEntity> findAllByIdModalidadContratacion(
        Long idModalidadContratacion);        

    void deleteByIdModalidadContratacion(Long idModalidadContratacion);
}