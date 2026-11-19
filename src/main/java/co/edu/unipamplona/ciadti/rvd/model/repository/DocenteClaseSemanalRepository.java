/**
 * Aplicación: rvd
 * Archivo: DocenteClaseSemanalRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.DocenteClaseSemanalEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocenteClaseSemanalEntityId;

public interface DocenteClaseSemanalRepository extends JpaRepository<DocenteClaseSemanalEntity, DocenteClaseSemanalEntityId> {
    
}
