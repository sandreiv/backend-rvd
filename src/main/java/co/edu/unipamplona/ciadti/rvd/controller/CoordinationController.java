/**
 * Aplicación: rvd
 * Archivo: ProfessorPreloadController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadCriterioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CategoriaCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePlantaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechaModalidadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/coordination")
public class CoordinationController {

    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final CoordinacionService coordinacionService;
    
    @Operation(
        summary = "Obtiene las convocatorias de precarga activas",
        description = "Obtiene las convocatorias con el fin de filtrar las coordinaciones"
    )
    @GetMapping("/list-active-preload-calls") 
    public ResponseEntity<?> listActivePreloadCalls() throws Exception {
        List<ConvocatoriaDTO> activePreloadCalls = convocatoriaPrecargaService.findActivePreloadCalls();
        return new ResponseEntity<>(activePreloadCalls, HttpStatus.OK);
    }

    // Controlador para buscar las coordinaciones del usuario
    /**
     * TO-DO: implementar obtencion de ID del usuario logueado.
     * Dos opciones: obtenerlo desde el contexto de la aplicacion aca en el backend.
     * Obtener el ID desde el frontend /{idPersonaGeneral}
     * 
     */
    @Operation(
        summary = "Obtiene la lista de coordinaciones",
        description = """
            Sin idConvocatoria: coordinaciones sin carga (convocatoria y carga null).
            Con idConvocatoria: coordinaciones con carga en esa convocatoria activa.
            """
    )
    @GetMapping("/list")
    public ResponseEntity<?> listCoordinations(@RequestParam(required = false) Long idConvocatoria) throws Exception {
        //String idUsuario = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        // Este Id es de prueba y solo sera temporal. ------------------ CAMBIAR --------------------
        Long idUsuario = 239419L;
        List<CoordinacionDTO> coordinations = coordinacionService.findCoordinationsByIdConvocatoria(idConvocatoria, idUsuario);
        return new ResponseEntity<>(coordinations, HttpStatus.OK);
    }

    @Operation(
        summary = "Guarda la relacion coordinacion-convocatoria en carga",
        description = "Crea un registro en RVD.CARGA con COOR_ID y CONV_ID"
    )
    @PostMapping("/save-preload")
    public ResponseEntity<Void> savePreload(@RequestBody RelacionConvocatoriaCoordinacionDTO dto) {
        coordinacionService.savePreload(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Busca docentes para preasignacion",
        description = "Busca por documento y/o fragmento de nombre o apellido y la modalidad de contratacion"
    )
    @GetMapping("/search-professor")
    public ResponseEntity<List<DocentePreasignacionDTO>> searchProfessor(
        @RequestParam(required = false) String nombre, 
        @RequestParam(required = false) String documento,
        @RequestParam(required = true) Long idModalidadContratacion
    ) {
        List<DocentePreasignacionDTO> docentes = coordinacionService.searchProfessor(nombre, documento, idModalidadContratacion);
        return new ResponseEntity<>(docentes, HttpStatus.OK);
    }

    @Operation(
        summary = "Busca los docentes de carrera de una coordinacion",
        description = "Busca los docentes de carrera de una coordinacion por el id de la coordinacion"
    )
    @GetMapping("/list-career-professors/{idCoordinacion}")
    public ResponseEntity<List<DocentePlantaCoordinacionDTO>> listCareerProfessors(@PathVariable Long idCoordinacion) {
        List<DocentePlantaCoordinacionDTO> docentesCarrera = coordinacionService.listCareerProfessors(idCoordinacion);
        return new ResponseEntity<>(docentesCarrera, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene las fechas de convocatoria por coordinación y modalidad de contratación",
        description = "Retorna las fechas de la convocatoria en carga para la coordinación (coorId) y la modalidad de contratación (mocoId)"
    )
    @GetMapping("/work-date")
    public ResponseEntity<List<FechaModalidadFormularioDTO>> getWorkDate(@RequestParam Long idCoordinacion, @RequestParam Long idModalidadContratacion) {
        List<FechaModalidadFormularioDTO> fechas = coordinacionService.getWorkDate(idCoordinacion, idModalidadContratacion);
        return new ResponseEntity<>(fechas, HttpStatus.OK);
    }

    @GetMapping("/value-points-preload")
    public ResponseEntity<ValorPuntosPrecargaDTO> getValuePointsPreload(@RequestParam Long anio, @RequestParam Long idCategoriaCatedratico, @RequestParam(required = false) String idPersonaGeneral) {
        Long idPersona = parseNullableLong(idPersonaGeneral);
        ValorPuntosPrecargaDTO valores = coordinacionService.getValuePointsPreload(anio, idCategoriaCatedratico, idPersona);
        return new ResponseEntity<>(valores, HttpStatus.OK);
    }
    private Long parseNullableLong(String value) {
        if (value == null || value.isBlank() || "null".equalsIgnoreCase(value.trim())) {
            return null;
        }
        return Long.valueOf(value.trim());
    }

    @Operation(
        summary = "Obtiene una lista de las categorias de catedratico",
        description = "Obtiene una lista de las categorias de catedratico"
    )
    @GetMapping("/professor-category")
    public ResponseEntity<List<CategoriaCatedraticoDTO>> listProfessorCategory(@RequestParam Long idModalidadContratacion) {
        List<CategoriaCatedraticoDTO> categorias = coordinacionService.listProfessorCategory(idModalidadContratacion);
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @Operation(
        summary = "Agrega un docente a la modalidad de contratacion de una coordinacion",
        description = "Agrega un docente a la modalidad de contratacion de una coordinacion"
    )
    @PostMapping("/add-professor")
    public ResponseEntity<Void> addProfessor(@RequestBody CargaDocenteFormularioDTO dto) {
        coordinacionService.addProfessor(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista los docentes de una coordinacion según la modalidad de contratacion",
        description = "Lista los docentes de una coordinacion según la modalidad de contratacion"
    )
    @GetMapping("/list-professors-modality")
    public ResponseEntity<List<DocenteCoordinacionDTO>> listProfessors(@RequestParam Long idCoordinacion, @RequestParam Long idModalidadContratacion) {
        List<DocenteCoordinacionDTO> docentes = coordinacionService.listProfessors(idCoordinacion, idModalidadContratacion);
        return new ResponseEntity<>(docentes, HttpStatus.OK);
    }

    @Operation(
        summary = "Edita un docente de una coordinacion por el id de la carga docente",
        description = "Edita el registro de CARGADOCENTE"
    )
    @PutMapping("/update-professor/{idCargaDocente}")
    public ResponseEntity<Void> updateProfessor(@PathVariable Long idCargaDocente, @RequestBody CargaDocenteFormularioDTO dto) {
        coordinacionService.updateProfessor(idCargaDocente, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina un docente de una coordinacion por el id de la carga docente",
        description = "Elimina el registro de CARGADOCENTE"
    )
    @DeleteMapping("/delete-professor/{idCargaDocente}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long idCargaDocente) {
        coordinacionService.deleteProfessor(idCargaDocente);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista los criterios",
        description = "Lista los criterios, los cuales son el nivel mas bajo de un tipo actividad"
    )
    @GetMapping("/list-criteria")
    public ResponseEntity<List<TipoActividadCriterioDTO>> listCriteria(@RequestParam Long idTipoActividad) {
        List<TipoActividadCriterioDTO> criterios = coordinacionService.listCriteria(idTipoActividad);
        return new ResponseEntity<>(criterios, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los tipos de actividad padre",
        description = "Lista los tipos de actividad que no tienen TIAC_IDPADRE"
    )
    @GetMapping("/list-activity-types")
    public ResponseEntity<List<TipoActividadDTO>> listActivityTypes() {
        List<TipoActividadDTO> tiposActividad = coordinacionService.listActivityTypes();
        return new ResponseEntity<>(tiposActividad, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista las unidades regionales de una coordinación",
        description = "Lista todas las unidades academicas marcadas como regionales de una coordinación"
    )
    @GetMapping("/list-regional-unit")
    public ResponseEntity<List<UnidadDTO>> listRegionalUnits(@RequestParam Long idCoordinacion) {
        List<UnidadDTO> unidades = coordinacionService.listRegionalUnits(idCoordinacion);
        return new ResponseEntity<>(unidades, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los programas de una unidad regional según el nivel educativo",
        description = "Lista los programas academicos asociados a una unidad regional según el nivel educativo"
    )
    @GetMapping("/list-program")
    public ResponseEntity<List<ProgramaDTO>> listPrograms(@RequestParam Long idUnidadRegional, @RequestParam Long idNivelEducativo) {
        List<ProgramaDTO> programas = coordinacionService.listProgramsByRegionalUnit(idUnidadRegional, idNivelEducativo);
        return new ResponseEntity<>(programas, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista las materias de un programa según la coordinación",
        description = "Lista las materias de un programa según la coordinación. (Se revisa si es transversal o no)"
    )
    @GetMapping("/list-subject")
    public ResponseEntity<List<MateriaDTO>> listSubjects(@RequestParam Long idPrograma, @RequestParam Long idCoordinacion) {
        List<MateriaDTO> materias = coordinacionService.listSubjects(idPrograma, idCoordinacion);
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los grupos de una materia",
        description = "Lista los grupos de una materia"
    )
    @GetMapping("/list-subject-group")
    public ResponseEntity<List<GrupoDTO>> listSubjectGroup(@RequestParam String codigoMateria) {
        List<GrupoDTO> grupos = coordinacionService.listSubjectGroup(codigoMateria);
        return new ResponseEntity<>(grupos, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los proyectos asociados a un docente",
        description = "Lista los proyectos asociados a un docente"
    )
    @GetMapping("/list-projects-professor")
    public ResponseEntity<List<ProyectoDTO>> listProjectsProfessor(@RequestParam Long idPersonaGeneral) {
        List<ProyectoDTO> proyectos = coordinacionService.listProjectsProfessor(idPersonaGeneral);
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }
}
