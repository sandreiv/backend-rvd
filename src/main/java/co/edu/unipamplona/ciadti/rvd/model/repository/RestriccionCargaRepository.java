/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 22/07/2026
 * Modificaciones:
 * 22/07/2026 - Joel Daniel Arias Duarte - Creación inicial para persistencia de restricciones de carga.
 * 29/07/2026 - Consulta de tipos de actividad por modalidad desde restricción de carga.
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.ActividadModalidadProjection;

public interface RestriccionCargaRepository extends JpaRepository<RestriccionCargaEntity, Long> {

    @Query("""
            SELECT
                reca.idModalidadContratacion AS idModalidadContratacion,
                tiac.id AS idTipoActividad,
                tiac.nombre AS nombreTipoActividad,
                tiac.codigo AS codigoTipoActividad,
                tiac.estado AS estadoTipoActividad,
                tiac.componente AS componenteTipoActividad
            FROM RestriccionCargaEntity reca
            JOIN TipoActividadModalidadEntity tiam
                ON tiam.idModalidadContratacion = reca.idModalidadContratacion
            JOIN TipoActividadesEntity tiac
                ON tiac.id = tiam.idTipoActividades
            WHERE reca.idModalidadContratacion = :idModalidadContratacion
            ORDER BY tiam.orden, tiac.nombre
            """)
    List<ActividadModalidadProjection> findActivitiesByModality(
            @Param("idModalidadContratacion") Long idModalidadContratacion);
}
