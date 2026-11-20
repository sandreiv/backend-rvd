/**
 * Aplicación: rvd
 * Archivo: SyncProfessorService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 19/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

public interface SyncProfessorService {

    int syncProfessors(Long idCarga, List<Long> idsCargaDocente);
}
