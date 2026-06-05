/**
 * Aplicación: rvd
 * Archivo: ConvocatoriaPrecargaService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;

public interface ConvocatoriaPrecargaService {

    List<ConvocatoriaDTO> findCallListWithDates();

    List<PersonaAutorizaConvocatoriaDTO> searchGeneralPerson(
            String nombre,
            String documento);

    void save(ConvocatoriaFormularioDTO ConvocatoriaFormularioDTO);

    ConvocatoriaFormularioDTO findCallDetail(Long id);
}
/* 02/06/2026 @:Sebastian Jaimes */
