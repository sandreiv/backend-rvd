package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;

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
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin,
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
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin,
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
}
