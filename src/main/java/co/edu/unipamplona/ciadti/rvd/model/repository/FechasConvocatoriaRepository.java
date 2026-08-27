/**
 * Aplicación: rvd
 * Archivo: FechasConvocatoriaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 14/07/2026
 * Modificaciones:
 * 14/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.FechaModalidadProjection;

public interface FechasConvocatoriaRepository
        extends JpaRepository<FechasConvocatoriaEntity, Long> {

    List<FechasConvocatoriaEntity> findByIdConvocatoriaTipoContratacion(
            Long idConvocatoriaTipoContratacion);

    void deleteByIdConvocatoriaTipoContratacion(Long idConvocatoriaTipoContratacion);

    @Modifying
    @Query("""
            update FechasConvocatoriaEntity f set
            f.codigo = :codigo,
            f.fechaInicio = :fechaInicio,
            f.fechaFin = :fechaFin,
            f.onceMeses = :onceMeses,
            f.fechaCambio = :fechaCambio
            where f.id = :id
            """)
    int updateGeneral(
            @Param("codigo") String codigo,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("onceMeses") String onceMeses,
            @Param("fechaCambio") Date fechaCambio,
            @Param("id") Long id);

    @Modifying
    @Query("""
            update FechasConvocatoriaEntity f set
            f.fechaInicio = :fechaInicio,
            f.fechaFin = :fechaFin,
            f.semanas = :semanas,
            f.vacaciones = :vacaciones,
            f.onceMeses = :onceMeses,
            f.fechaCambio = :fechaCambio
            where f.id = :id
            """)
    int updateModalidad(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("semanas") String semanas,
            @Param("vacaciones") String vacaciones,
            @Param("onceMeses") String onceMeses,
            @Param("fechaCambio") Date fechaCambio,
            @Param("id") Long id);


    @Modifying
    @Query(value = """
            DELETE FROM RVD.FECHASCONVOCATORIA WHERE CONV_ID = :id
            """, nativeQuery = true)
    void deleteByConvocatoriaId(@Param("id") Long id); 
    
    
    @Procedure(name = "FechasConvocatoriaEntity.deleteByProcedure")
    BigDecimal deleteByProcedure(@Param("P_FECO_ID") Long id, @Param("P_FECO_REGISTRADOPOR") String registradoPor);


    /**
     * Busca las fechas de convocatoria por carga y modalidad de contratación.
     * Revisa que el tipo de restricción de la carga sea semanal.
     */
    @Query("""
            SELECT
                feco.id AS id,
                feco.vacaciones AS vacaciones,
                feco.fechaInicio AS fechaInicio,
                feco.fechaFin AS fechaFin,
                feco.semanas AS semanas,
                reca.minimo AS minimo,
                reca.maximo AS maximo
            FROM CargaEntity carg
            JOIN ConvocatoriaTipoContratacionEntity cotc
                ON cotc.idConvocatoria = carg.idConvocatoria
            JOIN RestriccionCargaEntity reca
                ON reca.idModalidadContratacion = cotc.idModalidadContratacion
            JOIN FechasConvocatoriaEntity feco
                ON feco.idConvocatoriaTipoContratacion = cotc.id
            WHERE carg.id = :idCarga
                AND cotc.idModalidadContratacion = :mocoId
                AND reca.tipoHoras = 'SEMANAL'
            """)
    List<FechaModalidadProjection> findByCargaAndModalityAndRestrictionSemanal(
            @Param("idCarga") Long idCarga,
            @Param("mocoId") Long mocoId);

     @Query("""
        SELECT
            feco.id AS id,
            feco.vacaciones AS vacaciones,
            feco.fechaInicio AS fechaInicio,
            feco.fechaFin AS fechaFin,
            feco.semanas AS semanas,
            reca.minimo AS minimo,
            reca.maximo AS maximo
        FROM CargaEntity carg
        JOIN RestriccionCargaEntity reca
            ON reca.idModalidadContratacion = :mocoId
        JOIN FechasConvocatoriaEntity feco
            ON feco.idConvocatoria = carg.idConvocatoria
            AND feco.codigo = 'CNV'
        WHERE carg.id = :idCarga
            AND reca.tipoHoras = 'SEMANAL'
        """)
        List<FechaModalidadProjection>
        findPlantByCargaAndModalityAndRestrictionSemanal(
                @Param("idCarga") Long idCarga,
                @Param("mocoId") Long mocoId);       

    @Query("""
            SELECT feco
            FROM FechasConvocatoriaEntity feco
            JOIN ConvocatoriaTipoContratacionEntity cotc
                ON feco.idConvocatoriaTipoContratacion = cotc.id
            WHERE cotc.idConvocatoria = :idConvocatoria
                AND cotc.idModalidadContratacion = :idModalidadContratacion
            """)
    Optional<FechasConvocatoriaEntity> findByConvocatoriaAndModalidad(
            @Param("idConvocatoria") Long idConvocatoria,
            @Param("idModalidadContratacion") Long idModalidadContratacion);
}
