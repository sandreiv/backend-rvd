/**
 * Aplicación: rvd
 * Archivo: ProyectosService.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/07/2026
 * Modificaciones:
 * 27/07/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaProyectoListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectosListaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoProyectoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoProyectoListaDTO;

public interface ProyectosService {

    List<ProyectosListaDTO> listProjects();

    List<ProyectosListaDTO> listProducts(Long idProyecto);

    void saveProject(ProyectosFormularioDTO dto);

    void updateProject(Long id, ProyectosFormularioDTO dto);

    void deleteProject(Long id);

    void deleteBulkProjects(List<Long> ids);

    List<PersonaProyectoListaDTO> listProjectPersons(Long idProyecto);

    void saveProjectPerson(PersonaProyectoFormularioDTO dto);

    void updateProjectPerson(Long id, PersonaProyectoFormularioDTO dto);

    void deleteProjectPerson(Long id);

    void deleteBulkProjectPersons(List<Long> ids);

    List<TipoProyectoListaDTO> listProjectTypes();

    void saveProjectType(TipoProyectoFormularioDTO dto);

    void updateProjectType(Long id, TipoProyectoFormularioDTO dto);

    void deleteProjectType(Long id);

    void deleteBulkProjectTypes(List<Long> ids);

    List<ConvocatoriaProyectosListaDTO> listProjectCalls();

    void saveProjectCall(ConvocatoriaProyectosFormularioDTO dto);

    void updateProjectCall(Long id, ConvocatoriaProyectosFormularioDTO dto);

    void deleteProjectCall(Long id);

    void deleteBulkProjectCalls(List<Long> ids);
}
