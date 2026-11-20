/**
 * Aplicación: rvd
 * Archivo: DocenteGrupoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.DocenteGrupoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocenteGrupoEntityId;

public interface DocenteGrupoRepository extends JpaRepository<DocenteGrupoEntity, DocenteGrupoEntityId> {
    
    List<DocenteGrupoEntity> findAllByIdGrupoIn(List<Long> idsGrupo);
}
