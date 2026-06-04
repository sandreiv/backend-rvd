/**
 * Aplicación: rvd
 * Archivo: ModalidadContratacionService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/06/2026
 * Modificaciones:
 * 04/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadContratacionDTO;

public interface ModalidadContratacionService {

    List<ModalidadContratacionDTO> findModalityList();
}
/* 04/06/2026 @:Sebastian Jaimes */
