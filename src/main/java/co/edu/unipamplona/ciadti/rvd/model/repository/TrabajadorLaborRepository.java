/**
 * Aplicación: rvd
 * Archivo: TrabajadorLaborRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/10/2026
 * Modificaciones:
 * 16/10/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unipamplona.ciadti.rvd.model.entity.TrabajadorLaborEntity;

public interface TrabajadorLaborRepository extends JpaRepository<TrabajadorLaborEntity, Long> {

    @Query("""
            SELECT COUNT(trla) > 0
            FROM TrabajadorLaborEntity trla
            WHERE trla.idPersonaGeneral = :idPersonaGeneral
                AND (trla.fechaFinal IS NULL OR trla.fechaFinal >= CURRENT_DATE)
            """)
    boolean existsVigenteByIdPersonaGeneral(@Param("idPersonaGeneral") Long idPersonaGeneral);
}
/* 16/10/2026 @:Sebastian Jaimes */
