/**
 * Aplicación: rvd
 * Archivo: PlanTrabajoDigitalRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.PlanTrabajoDigitalEntity;

public interface PlanTrabajoDigitalRepository extends JpaRepository<PlanTrabajoDigitalEntity, Long> {
    
    @Procedure(name = "PlanTrabajoDigitalEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_PTDI_ID") Long id,
            @Param("P_CADO_REGISTRADOPOR") String registradoPor);
}
