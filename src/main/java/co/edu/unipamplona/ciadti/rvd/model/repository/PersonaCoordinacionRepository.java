package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;


import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaCoordinacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.PersonaCoordinacionEntityId;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PersonaCoordinacionListadoProjection;

public interface PersonaCoordinacionRepository
        extends JpaRepository<PersonaCoordinacionEntity, PersonaCoordinacionEntityId> {

    @Query(value = """
            SELECT
                PECO.PEGE_ID AS idPersonaGeneral,
                COALESCE(
                    NULLIF(TRIM(
                        NVL(PENG.PENG_PRIMERAPELLIDO, '') || ' ' ||
                        NVL(PENG.PENG_SEGUNDOAPELLIDO, '') || ' ' ||
                        NVL(PENG.PENG_PRIMERNOMBRE, '') || ' ' ||
                        NVL(PENG.PENG_SEGUNDONOMBRE, '')
                    ), ''),
                    'Persona ' || PECO.PEGE_ID
                ) AS persona,
                PEGE.PEGE_DOCUMENTOIDENTIDAD AS documentoIdentidad,
                COOR.COOR_ID AS idCoordinacion,
                COALESCE(COOR.COOR_NOMBRE, COOR.COOR_DESCRIPCION) AS coordinacion,
                TO_CHAR(PECO.PECO_ESTADO) AS estado
            FROM RVD.PERSONACOORDINACION PECO
            LEFT JOIN GENERAL.PERSONAGENERAL PEGE
                ON PEGE.PEGE_ID = PECO.PEGE_ID
            LEFT JOIN GENERAL.PERSONANATURALGENERAL PENG
                ON PENG.PEGE_ID = PECO.PEGE_ID
            LEFT JOIN RVD.COORDINACIONES COOR
                ON COOR.COOR_ID = PECO.COOR_ID
            ORDER BY PECO.PEGE_ID, COOR.COOR_ID
            """, nativeQuery = true)
    List<PersonaCoordinacionListadoProjection> findAdministrationList();



    @Procedure(name = "PersonaCoordinacionEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(
            @Param("P_PEGE_ID") Long idPersonaGeneral,
            @Param("P_COOR_ID") Long idCoordinacion,
            @Param("P_COOR_REGISTRADOPOR") String registradoPor
    );
}