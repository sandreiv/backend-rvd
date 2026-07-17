/**
 * Aplicación: rvd
 * Archivo: AsignarCentroCostoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/07/2026
 * Modificaciones:
 * 17/07/2026 - Daniel Arias - Creación inicial
 */

package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.AsignarCentroCostoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.CentroCostoAsignadoListadoProjection;

public interface AsignarCentroCostoRepository extends JpaRepository<AsignarCentroCostoEntity, Long> {

    Optional<AsignarCentroCostoEntity> findFirstByIdCoordinacion(Long idCoordinacion);

    void deleteByIdCoordinacion(Long idCoordinacion);

    @Query(value = """
            SELECT
                ASCC.ASCC_ID AS id,
                COOR.COOR_ID AS idCoordinacion,
                COALESCE(COOR.COOR_NOMBRE, COOR.COOR_DESCRIPCION) AS coordinacion,
                CECO.CECO_ID AS idCentroCosto,
                CECO.CECO_DESCRIPCION AS centroCosto,
                TO_CHAR(ASCC.ASCC_ESTADO) AS estado
            FROM RVD.ASIGNARCENTROCOSTO ASCC
            LEFT JOIN RVD.COORDINACIONES COOR
                ON COOR.COOR_ID = ASCC.COOR_ID
            LEFT JOIN CONTABLEV3.CENTROCOSTO CECO
                ON CECO.CECO_ID = ASCC.CECO_ID
            ORDER BY COALESCE(COOR.COOR_NOMBRE, COOR.COOR_DESCRIPCION), CECO.CECO_DESCRIPCION
            """, nativeQuery = true)
    List<CentroCostoAsignadoListadoProjection> findAdministrationList();


    @Procedure(name = "AsignarCentroCostoEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_ASCC_ID") Long id,
            @Param("P_COOR_REGISTRADOPOR") String registradoPor
    );

    @Query(value = """
            SELECT ASCC.ASCC_ID
            FROM RVD.ASIGNARCENTROCOSTO ASCC
            WHERE ASCC.COOR_ID = :idCoordinacion
            """, nativeQuery = true)
    List<Long> findIdsByIdCoordinacion(@Param("idCoordinacion") Long idCoordinacion);

}

/* 17/07/2026 @:Daniel Arias */
