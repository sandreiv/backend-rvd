/**
 * Aplicación: rvd
 * Archivo: TipoProyectoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.TipoProyectoEntity;

public interface TipoProyectoRepository extends JpaRepository<TipoProyectoEntity, Long> {

    @Query(value = """
            SELECT
                TIPR.*
            FROM RVD.TIPOPROYECTO TIPR
            ORDER BY TIPR.TIPR_NOMBRE
            """, nativeQuery = true)
    List<TipoProyectoEntity> findAllProjectTypes();

    @Procedure(name = "TipoProyectoEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_TIPR_ID") Long id,
            @Param("P_TIPR_REGISTRADOPOR") String registradoPor
    );
}
