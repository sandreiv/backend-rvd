/**
 * Aplicación: rvd
 * Archivo: CdpContratoRepository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 11/11/2026
 * Modificaciones:
 * 11/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.CdpContratoEntity;

public interface CdpContratoRepository extends JpaRepository<CdpContratoEntity, Long> {
    
    boolean existsByIdContratoAndIdCdp(Long idContrato, Long idCdp);
}
