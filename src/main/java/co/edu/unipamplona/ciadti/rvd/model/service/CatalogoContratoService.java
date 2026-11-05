/**
 * Aplicación: rvd
 * Archivo: CatalogoContratoService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial (catálogos del anexo de contratación)
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCatalogosDTO;

public interface CatalogoContratoService {

    /**
     * Catálogos que se diligencian en el anexo de contratación: forma de pago y
     * tipo de objeto contractual.
     */
    ContratoCatalogosDTO findContractCatalogs();
}
