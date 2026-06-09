package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaTipoContratacionEntity;

public interface ConvocatoriaTipoContratacionRepository
        extends JpaRepository<ConvocatoriaTipoContratacionEntity, Long> {

    @Query(value = """
            SELECT
                COTC.COTC_ID,
                COTC.CONV_ID,
                COTC.MOCO_ID,
                COTC.COTC_REGISTRADOPOR,
                COTC.COTC_FECHACAMBIO
            FROM RVD.CONVOCATORIATIPOCONTRATACION COTC
            WHERE COTC.CONV_ID = :convId
            """, nativeQuery = true)
    List<ConvocatoriaTipoContratacionEntity> findByConvocatoriaId(
            @Param("convId") Long convId);

    @Modifying
    @Query("""
            update ConvocatoriaTipoContratacionEntity c set
            c.idModalidadContratacion = :idModalidadContratacion,
            c.fechaCambio = :fechaCambio
            where c.id = :id
            """)
    int update(
            @Param("idModalidadContratacion") Long idModalidadContratacion,
            @Param("fechaCambio") Date fechaCambio,
            @Param("id") Long id);


    @Modifying
    @Query(value = """
            DELETE FROM RVD.CONVOCATORIATIPOCONTRATACION WHERE CONV_ID = :id
            """, nativeQuery = true)
    void deleteByConvocatoriaId(@Param("id") Long id);

    @Procedure(name = "ConvocatoriaTipoContratacionEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(@Param("P_COTC_ID") Long id, @Param("P_COTC_REGISTRADOPOR") String registradoPor);
}
