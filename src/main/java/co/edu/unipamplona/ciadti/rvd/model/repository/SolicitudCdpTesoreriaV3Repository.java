/**
 * Aplicación: rvd
 * Archivo: SolicitudCdpTesoreriaV3Repository.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.repository
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/10/2026
 * Modificaciones:
 * 16/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unipamplona.ciadti.rvd.model.entity.SolicitudCdpTesoreriaV3Entity;

public interface SolicitudCdpTesoreriaV3Repository extends JpaRepository<SolicitudCdpTesoreriaV3Entity, Long> {
    
}
