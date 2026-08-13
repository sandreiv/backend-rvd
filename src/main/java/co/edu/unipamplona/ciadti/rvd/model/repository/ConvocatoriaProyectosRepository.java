/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaProyectosRepository.java
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

import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaProyectosEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ConvocatoriaProyectosListaProjection;

public interface ConvocatoriaProyectosRepository
        extends JpaRepository<ConvocatoriaProyectosEntity, Long> {

    @Query(value = """
            SELECT
                COPR.COPR_ID AS id,
                COPR.COPR_NOMBRE AS nombre,
                COPR.COPR_DESCRIPCION AS descripcion,
                COPR.COPR_CODIGO AS codigo,
                RECO.CONV_ID AS idConvocatoria,
                CONV.CONV_NOMBRE AS nombreConvocatoria
            FROM RVD.CONVOCATORIAPROYECTOS COPR
            LEFT JOIN RVD.RELACIONCONVOCATORIAS RECO
                ON COPR.COPR_ID = RECO.COPR_ID
            AND TRIM(RECO.RECO_ESTADO) = '1'
            LEFT JOIN RVD.CONVOCATORIA CONV
                ON RECO.CONV_ID = CONV.CONV_ID
            ORDER BY COPR.COPR_NOMBRE
            """, nativeQuery = true)
    List<ConvocatoriaProyectosListaProjection> findAllProjectCalls();

    @Procedure(name = "ConvocatoriaProyectosEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_COPR_ID") Long id,
            @Param("P_COPR_REGISTRADOPOR") String registradoPor
    );
}
