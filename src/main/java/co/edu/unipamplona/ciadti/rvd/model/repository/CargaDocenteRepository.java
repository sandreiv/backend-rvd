/**
 * Aplicación: rvd
 * Archivo: CargaDocenteRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.DocenteCargaCoordinacionProjection;

public interface CargaDocenteRepository
        extends JpaRepository<CargaDocenteEntity, Long> {

    @Query(value = """
                SELECT
                PEGE.PEGE_ID AS idPersonaGeneral,
                TRIM(
                TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                || ' ' ||
                TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                ) AS nombreCompleto,
                CADO.CADO_ID AS idCargaDocente,
                CADO.CADO_ESTADO AS estado,
                CADO.CARG_ID AS idCarga,
                CADO.MOCO_ID AS idModalidadContratacion,
                CADO.CACA_ID AS idCategoriaCatedratico,
                CADO.CADO_FECHAINICIO AS cargaFechaInicio,
                CADO.CADO_FECHAFIN AS cargaFechaFin,
                CADO.CADO_VALORCONTRATO AS valorContrato,
                CADO.CADO_VALORPRESTACIONES AS valorPrestaciones,
                CADO.CADO_SALARIO AS asignacionSalarial,
                CADO.CADO_TOTALCONTRATO AS totalContrato,
                CADO.CADO_VALORHORA AS valorHora,
                CADO.CADO_PUNTOS AS puntos,
                CADO.CADO_VALORPUNTO AS valorPunto,
                CADO.CADO_SEMANAS AS semanas,
                FECO.FECO_ID AS idFechasConvocatoria,
                FECO.FECO_CODIGO AS fechaConvocatoriaCodigo,
                FECO.FECO_FECHAINICIO AS fechaConvocatoriaInicio,
                FECO.FECO_FECHAFIN AS fechaConvocatoriaFin
                FROM RVD.CARGADOCENTE CADO
                INNER JOIN RVD.CARGA CARG
                ON CARG.CARG_ID = CADO.CARG_ID
                LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = CADO.PEGE_ID
                LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
                LEFT JOIN RVD.FECHASCONVOCATORIA FECO
                ON FECO.FECO_ID = CADO.FECO_ID
                WHERE CARG.COOR_ID = :idCoordinacion
                AND CADO.MOCO_ID = :idModalidadContratacion
                ORDER BY
                CASE
                    WHEN PEGE.PEGE_ID IS NULL THEN 1
                    WHEN TRIM(
                        TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                        || ' ' ||
                        TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                    ) IS NULL THEN 1
                    ELSE 0
                END,
                UPPER(TRIM(
                    TRIM(PENG.PENG_PRIMERNOMBRE || ' ' || PENG.PENG_SEGUNDONOMBRE)
                    || ' ' ||
                    TRIM(PENG.PENG_PRIMERAPELLIDO || ' ' || PENG.PENG_SEGUNDOAPELLIDO)
                )) NULLS LAST
            """, nativeQuery = true)
    List<DocenteCargaCoordinacionProjection> findProfessorsByCoordinationAndModality(
            @Param("idCoordinacion") Long idCoordinacion,
            @Param("idModalidadContratacion") Long idModalidadContratacion);

    @Procedure(name = "CargaDocenteEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_CADO_ID") Long id,
            @Param("P_CADO_REGISTRADOPOR") String registradoPor);

    boolean existsByIdPersonaGeneralAndIdCargaAndIdModalidadContratacionAndIdFechasConvocatoria(
            Long idPersonaGeneral, Long idCarga, Long idModalidadContratacion, Long idFechasConvocatoria);
}
