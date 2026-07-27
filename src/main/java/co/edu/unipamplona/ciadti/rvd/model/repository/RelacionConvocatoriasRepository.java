/**
 * Aplicación: rvd
 * Archivo: RelacionConvocatoriasRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionConvocatoriasEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RelacionConvocatoriasEntityId;

public interface RelacionConvocatoriasRepository
        extends JpaRepository<RelacionConvocatoriasEntity, RelacionConvocatoriasEntityId> {
}
