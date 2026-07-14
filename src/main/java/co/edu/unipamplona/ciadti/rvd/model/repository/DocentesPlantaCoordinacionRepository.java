package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;
import java.math.BigDecimal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.query.Procedure;

import co.edu.unipamplona.ciadti.rvd.model.entity.DocentesPlantaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocentesPlantaCoordinacionEntityId;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaCoordinacionListadoProjection;

public interface DocentesPlantaCoordinacionRepository extends JpaRepository<DocentesPlantaCoordinacionEntity, DocentesPlantaCoordinacionEntityId> {

    @Query("""
            SELECT dopc FROM DocentesPlantaCoordinacionEntity dopc
            INNER JOIN FETCH dopc.personaGeneral pege
            INNER JOIN FETCH pege.personaNaturalGeneral peng
            WHERE dopc.idCoordinacion = :idCoordinacion
            ORDER BY
                CASE WHEN peng.primerApellido IS NULL
                    AND peng.primerNombre IS NULL THEN 1 ELSE 0 END,
                UPPER(peng.primerNombre),
                UPPER(peng.segundoNombre),
                UPPER(peng.primerApellido),
                UPPER(peng.segundoApellido)
            """)
    List<DocentesPlantaCoordinacionEntity> findByIdCoordinacion(@Param("idCoordinacion") Long idCoordinacion);

   @Query(value = """
                SELECT
                DOPC.PEGE_ID AS idPersonaGeneral,
                TRIM(
                        PENG.PENG_PRIMERAPELLIDO || ' ' ||
                        NVL(PENG.PENG_SEGUNDOAPELLIDO, '') || ' ' ||
                        PENG.PENG_PRIMERNOMBRE || ' ' ||
                        NVL(PENG.PENG_SEGUNDONOMBRE, '')
                ) AS persona,
                PEGE.PEGE_DOCUMENTOIDENTIDAD AS documentoIdentidad,
                COOR.COOR_ID AS idCoordinacion,
                COALESCE(COOR.COOR_NOMBRE, COOR.COOR_DESCRIPCION) AS coordinacion,
                DOPC.DOPC_ESTADO AS estado
                FROM RVD.DOCENTESPLANTACOORDINACION DOPC
                INNER JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = DOPC.PEGE_ID
                INNER JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PEGE.PEGE_ID
                INNER JOIN RVD.COORDINACIONES COOR
                ON COOR.COOR_ID = DOPC.COOR_ID
                ORDER BY PENG.PENG_PRIMERAPELLIDO,
                        PENG.PENG_SEGUNDOAPELLIDO,
                        PENG.PENG_PRIMERNOMBRE,
                        PENG.PENG_SEGUNDONOMBRE
                """, nativeQuery = true)
        List<PersonaCoordinacionListadoProjection> findAdministrationList();    
        
        
        @Procedure(name = "DocentesPlantaCoordinacionEntity.deleteByProcedure")
        BigDecimal deleteByProcedure(
                @Param("P_PEGE_ID") Long idPersonaGeneral,
                @Param("P_COOR_ID") Long idCoordinacion,
                @Param("P_COOR_REGISTRADOPOR") String registradoPor
        );        

}
