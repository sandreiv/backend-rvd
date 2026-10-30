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
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaProyectosEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ConvocatoriaProyectosListaProjection;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PeriodoCargaProyectoProjection;

public interface ConvocatoriaProyectosRepository
        extends JpaRepository<ConvocatoriaProyectosEntity, Long> {

    @Query(value = """
            SELECT
                COPR.COPR_ID AS id,
                COPR.COPR_NOMBRE AS nombre,
                COPR.COPR_DESCRIPCION AS descripcion,
                COPR.COPR_CODIGO AS codigo,
                RECO.CONV_ID AS idConvocatoria,
                CONV.CONV_NOMBRE AS nombreConvocatoria,
                FECO.FECO_FECHAINICIO AS fechaInicio,
				FECO.FECO_FECHAFIN AS fechaFin
            FROM RVD.CONVOCATORIAPROYECTOS COPR
            LEFT JOIN RVD.RELACIONCONVOCATORIAS RECO
                ON COPR.COPR_ID = RECO.COPR_ID
            	AND TRIM(RECO.RECO_ESTADO) = '1'
            LEFT JOIN RVD.CONVOCATORIA CONV
                ON RECO.CONV_ID = CONV.CONV_ID
            LEFT JOIN RVD.FECHASCONVOCATORIA FECO
			    ON FECO.CONV_ID = CONV.CONV_ID
			    AND FECO.FECO_CODIGO = COPR.COPR_CODIGO
            ORDER BY COPR.COPR_NOMBRE
            """, nativeQuery = true)
    List<ConvocatoriaProyectosListaProjection> findAllProjectCalls();

    @Query(value = """
            SELECT
                FECO.FECO_FECHAINICIO AS fechaInicio,
                FECO.FECO_FECHAFIN AS fechaFin
            FROM RVD.CONVOCATORIAPROYECTOS COPR
            LEFT JOIN RVD.RELACIONCONVOCATORIAS RECO
                ON COPR.COPR_ID = RECO.COPR_ID
                AND TRIM(RECO.RECO_ESTADO) = '1'
            LEFT JOIN RVD.CONVOCATORIA CONV
                ON RECO.CONV_ID = CONV.CONV_ID
            LEFT JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.CONV_ID = CONV.CONV_ID
                AND FECO.FECO_CODIGO = COPR.COPR_CODIGO
            WHERE COPR.COPR_ID = :idConvocatoriaProyectos
            """, nativeQuery = true)
    Optional<PeriodoCargaProyectoProjection> findProjectCallPeriodById(
            @Param("idConvocatoriaProyectos") Long idConvocatoriaProyectos
    );

    @Procedure(name = "ConvocatoriaProyectosEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_COPR_ID") Long id,
            @Param("P_COPR_REGISTRADOPOR") String registradoPor
    );
}
