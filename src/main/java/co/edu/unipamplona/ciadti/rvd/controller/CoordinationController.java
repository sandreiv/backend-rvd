/**
 * Aplicación: rvd
 * Archivo: ProfessorPreloadController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 25/08/2026 - Sebastian Jaimes - Listado coordinaciones por JWT (Coordinador/Decano)
 * 27/08/2026 - Horas de actividades por carga
 * 31/08/2026 - Sebastian Jaimes - Columna V. Hora en reporte Excel
 * 31/08/2026 - Sebastian Jaimes - Reporte PDF de preasignación
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadModalidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaDocentePlantaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CategoriaCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CentroCostoResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocenteCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePlantaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechaModalidadFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.GrupoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HorasActividadesCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.MateriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ObservacionDecanoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoUniversidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProgramaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ProyectoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RelacionConvocatoriaCoordinacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ResumenCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.RestriccionProgramaHorasDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadCriterioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TipoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TotalPreasignacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.UnidadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorPuntosPrecargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.AprobacionDetalleCargaDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.PreasignacionReporteService;
import co.edu.unipamplona.ciadti.rvd.model.service.PeriodoUniversidadService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/coordination")
public class CoordinationController {

    private final ConvocatoriaPrecargaService convocatoriaPrecargaService;
    private final CoordinacionService coordinacionService;
    private final PreasignacionReporteService preasignacionReporteService;
    private final PeriodoUniversidadService periodoUniversidadService;
    
    @Operation(
        summary = "Obtiene las convocatorias de precarga activas",
        description = "Obtiene las convocatorias activas del periodo universitario para filtrar coordinaciones"
    )
    @GetMapping("/list-active-preload-calls") 
    public ResponseEntity<?> listActivePreloadCalls(@RequestParam Long idPeriodoUniversidad) throws Exception {
        List<ConvocatoriaDTO> activePreloadCalls = convocatoriaPrecargaService.findActivePreloadCalls(idPeriodoUniversidad);
        return new ResponseEntity<>(activePreloadCalls, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene las convocatorias activas asignables",
        description = "Obtiene las convocatorias activas del periodo sin restricciones vigentes para asignación libre"
    )
    @GetMapping("/list-assignable-preload-calls")
    public ResponseEntity<?> listAssignablePreloadCalls(@RequestParam Long idPeriodoUniversidad) throws Exception {
        List<ConvocatoriaDTO> activePreloadCalls = convocatoriaPrecargaService.findAssignableActivePreloadCalls(idPeriodoUniversidad);

        return new ResponseEntity<>(activePreloadCalls, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene la lista de periodos de universidad",
        description = "Obtiene la lista de periodos de universidad"
    )
    @GetMapping("/list-university-period")
    public ResponseEntity<List<PeriodoUniversidadDTO>> listUniversityPeriod() {
        List<PeriodoUniversidadDTO> periods = periodoUniversidadService.findUniversityPeriodList();
        return new ResponseEntity<>(periods, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene la lista de coordinaciones",
        description = """
            Identidad y roles salen del JWT (idPersona + roles del bootstrap).
            Coordinador: sus coordinaciones en PERSONACOORDINACION.
            Decano: coordinaciones hijas de sus facultades, carga INSCRITO.
            Sin idConvocatoria: requiere idPeriodoUniversidad.
            """
    )
    @GetMapping("/list")
    public ResponseEntity<?> listCoordinations(
            @RequestParam(required = false) Long idConvocatoria,
            @RequestParam(required = false) Long idPeriodoUniversidad) throws Exception {
        List<CoordinacionDTO> coordinations = coordinacionService.findCoordinationsByIdConvocatoria(idConvocatoria, idPeriodoUniversidad);
        
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
        @RequestParam(required = true) Long idModalidadContratacion) {
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
        summary = "Obtiene las fechas de convocatoria por carga y modalidad de contratación",
        description = "Retorna las fechas de la convocatoria asociada a la carga (idCarga) y la modalidad (idModalidadContratacion)"
    )
    @GetMapping("/work-date")
    public ResponseEntity<List<FechaModalidadFormularioDTO>> getWorkDate(@RequestParam Long idCarga, @RequestParam Long idModalidadContratacion) {
        List<FechaModalidadFormularioDTO> fechas =
                coordinacionService.getWorkDate(idCarga, idModalidadContratacion);
        return new ResponseEntity<>(fechas, HttpStatus.OK);
    }

    @GetMapping("/value-points-preload")
    public ResponseEntity<ValorPuntosPrecargaDTO> getValuePointsPreload(
            @RequestParam Long anio,
            @RequestParam Long idCategoriaCatedratico,
            @RequestParam(required = false) String idPersonaGeneral,
            @RequestParam Long idModalidadContratacion) {
        Long idPersona = parseNullableLong(idPersonaGeneral);
        ValorPuntosPrecargaDTO valores = coordinacionService.getValuePointsPreload(anio, idCategoriaCatedratico, idPersona, idModalidadContratacion);
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
        summary = "Lista los docentes de una carga según la modalidad de contratacion",
        description = """
            Si la modalidad es planta: lista todos los de DOCENTESPLANTACOORDINACION
            de la coordinación de la carga, con datos de CARGADOCENTE solo de esa carga.
            Para otras modalidades: solo docentes con registro en CARGADOCENTE de esa carga.
            """
    )
    @GetMapping("/list-professors-modality")
    public ResponseEntity<List<DocenteCoordinacionDTO>> listProfessors(@RequestParam Long idCarga, @RequestParam Long idModalidadContratacion) {
        List<DocenteCoordinacionDTO> docentes = coordinacionService.listProfessors(idCarga, idModalidadContratacion);
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
        description = "Lista los programas asociados a la coordinación en ASOCIACIONCOORDINACION, filtrados por unidad regional y nivel educativo"
    )
    @GetMapping("/list-program")
    public ResponseEntity<List<ProgramaDTO>> listPrograms(@RequestParam Long idCoordinacion, @RequestParam Long idUnidadRegional, @RequestParam Long idNivelEducativo) {
        List<ProgramaDTO> programas = coordinacionService.listProgramsByRegionalUnit(idCoordinacion, idUnidadRegional, idNivelEducativo);
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
        description = "Lista los grupos de una materia filtrados por el periodo universitario de la convocatoria"
    )
    @GetMapping("/list-subject-group")
    public ResponseEntity<List<GrupoDTO>> listSubjectGroup(@RequestParam String codigoMateria, @RequestParam Long idPeriodoUniversidad) {
        List<GrupoDTO> grupos = coordinacionService.listSubjectGroup(codigoMateria, idPeriodoUniversidad);
        return new ResponseEntity<>(grupos, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los proyectos asociados a un docente",
        description = "Lista los proyectos asociados a un docente filtrados por la convocatoria de precarga"
    )
    @GetMapping("/list-projects-professor")
    public ResponseEntity<List<ProyectoDTO>> listProjectsProfessor(
            @RequestParam Long idPersonaGeneral,
            @RequestParam Long idConvocatoria) {

        List<ProyectoDTO> proyectos =
                coordinacionService.listProjectsProfessor(
                        idPersonaGeneral,
                        idConvocatoria
                );

        return new ResponseEntity<>(
                proyectos,
                HttpStatus.OK
        );
    }

    @Operation(
        summary = "Guarda un detalle de carga docente",
        description = "Guarda un detalle de carga docente"
    )
    @PostMapping("/save-detail-professor-preload")
    public ResponseEntity<Void> saveDetailProfessorPreload(@RequestBody DetalleCargaDocenteFormularioDTO dto) {
        coordinacionService.saveDetailProfessorPreload(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Lista el detalle de carga de un docente",
        description = "Lista el detalle de carga por idCargaDocente"
    )
    @GetMapping("/list-detail-professor-preload")
    public ResponseEntity<List<DetalleCargaDocenteDTO>> listDetailProfessorPreload(@RequestParam Long idCargaDocente) {
        List<DetalleCargaDocenteDTO> detalle = coordinacionService.listDetailProfessorPreload(idCargaDocente);
        return new ResponseEntity<>(detalle, HttpStatus.OK);
    }

    @Operation(
        summary = "Consulta restricciones de horas por programa",
        description = """
            Lista los programas con máximo de horas configurado en la restricción
            de carga de la modalidad. Si se envía idCargaDocente, incluye horas
            ya asignadas y disponibles para validar actividades de horas directas.
            """
    )
    @GetMapping("/program-hour-restriction")
    public ResponseEntity<List<RestriccionProgramaHorasDTO>> listProgramHourRestrictions(@RequestParam Long idModalidadContratacion, @RequestParam(required = false) Long idCargaDocente) {
        return ResponseEntity.ok(coordinacionService.listProgramHourRestrictions(idModalidadContratacion, idCargaDocente));
    }

    @Operation(
        summary = "Edita un detalle de carga docente",
        description = "Actualiza un detalle de carga docente usando DetalleCargaDocenteDTO"
    )
    @PutMapping("/update-detail-professor-preload")
    public ResponseEntity<Void> updateDetailProfessorPreload(@RequestBody DetalleCargaDocenteDTO dto) {
        coordinacionService.updateDetailProfessorPreload(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Guarda la carga de un docente de planta de una coordinacion",
        description = "Guarda la carga de un docente de planta de una coordinacion"
    )
    @PostMapping("/save-career-professor-preload")
    public ResponseEntity<Void> saveCareerProfessorPreload(@RequestBody CargaDocentePlantaDTO dto) {
        coordinacionService.saveCareerProfessorPreload(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina un detalle carga docente (actividad) de un docente",
        description = "Elimina un detalle carga docente (actividad) de un docente"
    )
    @DeleteMapping("/delete-professor-activity/{idDetalleCargaDocente}")
    public ResponseEntity<Void> deleteProfessorActivity(@PathVariable Long idDetalleCargaDocente) {
        coordinacionService.deleteProfessorActivity(idDetalleCargaDocente);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Aprueba la preasignación de un docente",
        description = "Actualiza el estado de la carga docente a aprobada"
    )
    @PutMapping("/approve-professor-preassignment/{idCargaDocente}")
    public ResponseEntity<Void> approveProfessorPreassignment(
            @PathVariable Long idCargaDocente) {
        coordinacionService.approveProfessorPreassignment(idCargaDocente);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Guarda y aprueba la distribución de actividades del docente",
        description = "Valida, guarda actividades y aprueba la preasignación del docente en una única transacción"
    )
    @PostMapping("/approve-professor-activity-distribution")
    public ResponseEntity<Void> approveProfessorActivityDistribution(
            @RequestBody AprobacionDetalleCargaDocenteDTO dto) {
        coordinacionService.approveProfessorActivityDistribution(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Obtiene el total de una preasignación por carga",
        description = "Retorna totales de docentes, prestaciones, contratos y preasignación,además de las horas agrupadas por tipo de actividad y el total de horas"
    )
    @GetMapping("/total-preload")
    public ResponseEntity<TotalPreasignacionDTO> getTotalPreload(@RequestParam Long idCarga) {
        TotalPreasignacionDTO total = coordinacionService.getTotalPreload(idCarga);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @Operation(
        summary = "Lista los tipos de actividad según la modalidad de contratación",
        description = "Consulta RESTRICCIONCARGA y, según la modalidad (MOCO), obtiene los tipos de actividad desde TIPOACTIVIDADMODALIDAD"
    )
    @GetMapping("/list-activities-modality")
    public ResponseEntity<ActividadModalidadDTO> listActivitiesModality(@RequestParam Long idModalidadContratacion) {
        ActividadModalidadDTO actividad = coordinacionService.listActivitiesModality(idModalidadContratacion);
        return new ResponseEntity<>(actividad, HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene el resumen completo de una carga docente",
        description = "Agrupa valor de contratación, horas de actividades y distribución por centros de costo."
    )
    @GetMapping("/professor-load-summary/{idCargaDocente}")
    public ResponseEntity<ResumenCargaDocenteDTO> getProfessorLoadSummary(@PathVariable Long idCargaDocente) {
        return new ResponseEntity<>(coordinacionService.getProfessorLoadSummary(idCargaDocente), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene el valor de contratación de una carga docente",
        description = "Calcula vacaciones, cesantías, intereses, prima legal, prestaciones, valor y total de contrato."
    )
    @GetMapping("/contract-value/{idCargaDocente}")
    public ResponseEntity<ValorContratacionDTO> getContractValue(@PathVariable Long idCargaDocente) {
        return new ResponseEntity<>(coordinacionService.getContractValue(idCargaDocente), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene el resumen de horas por actividad",
        description = "Retorna tipo, código, nombre y total de horas de las actividades de la carga docente."
    )
    @GetMapping("/activity-hours/{idCargaDocente}")
    public ResponseEntity<List<ActividadHorasResumenDTO>> getActivityHours(@PathVariable Long idCargaDocente) {
        return new ResponseEntity<>(coordinacionService.listActivityHours(idCargaDocente), HttpStatus.OK);
    }

    @Operation(
        summary = "Obtiene la distribución por centros de costo",
        description = "Retorna nombre del centro, número de actividades, porcentaje de horas y valor del contrato asignado."
    )
    @GetMapping("/cost-centers/{idCargaDocente}")
    public ResponseEntity<List<CentroCostoResumenDTO>> getCostCenters(@PathVariable Long idCargaDocente) {
        return new ResponseEntity<>(coordinacionService.listCostCenters(idCargaDocente), HttpStatus.OK);
    }

    /* CAMBIAR A VISTA SEGÚN LANZADOR */
    @Operation(
        summary = "Genera el reporte Excel de preasignación de una carga",
        description = """
            Exporta el resumen de todos los docentes de la carga: encabezado
            (unidad, facultad, coordinación, periodo, convocatoria), valor hora
            (puntos vigencia), valores de contratación y horas de actividades.
            """
    )
    @GetMapping("/preload-report/{idCarga}")
    public ResponseEntity<byte[]> generatePreloadReport(@PathVariable Long idCarga) {
        FileDTO file = preasignacionReporteService.generatePreloadReport(idCarga);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.fileName() + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file.content());
    }

    @Operation(
        summary = "Obtiene las observaciones por carga",
        description = "Retorna lista con la observación, fecha y persona quien la realizo"
    )
    @GetMapping("/preload-observations/{idCarga}")
    public ResponseEntity<List<ObservacionCargaDTO>> getPreloadObservations(@PathVariable Long idCarga) {
        return new ResponseEntity<>(coordinacionService.listPreloadObservations(idCarga), HttpStatus.OK);
    }

    @Operation(
        summary = "Cambia la observacion a Vista",
        description = "Actualiza la bandera booleana de la observación a Vista"
    )
    @PutMapping("/mark-seen-observations/{idCarga}")
    public ResponseEntity<Void> markSeenObservationsByPreload(@PathVariable Long idCarga) {
        coordinacionService.markSeenObservationsByPreload(idCarga);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Cambia el estado de la carga a inscrito",
        description = "Actualiza el estado de la carga a inscrito"
    )
    @PutMapping("/send-preload-dean/{idCarga}")
    public ResponseEntity<Void> endorsePreloadDean(@PathVariable Long idCarga) {
        coordinacionService.endorsePreloadDean(idCarga);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Cambia el estado de la carga a registrado por devolución del decano",
        description = "Actualiza el estado de la carga a registrado por devolución del decano"
    )
    @PutMapping("/decline-preload-dean/{idCarga}")
    public ResponseEntity<Void> declinePreloadDean(@PathVariable Long idCarga, @RequestBody ObservacionDecanoDTO dto) {
        coordinacionService.declinePreload(idCarga, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Aprueba la carga por parte del decano",
        description = "Actualiza el estado de la carga a APROBADO DECANO"
    )
    @PutMapping("/approve-preload-dean/{idCarga}")
    public ResponseEntity<Void> approvePreloadDean(
            @PathVariable Long idCarga
    ) {
        coordinacionService.approvePreloadDean(idCarga);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Devuelve la carga por parte de desarrollo académico",
        description = "Actualiza el estado de la carga a REGISTRADO y guarda la observación"
    )
    @PutMapping("/decline-preload-development/{idCarga}")
    public ResponseEntity<Void> declinePreloadDevelopment(
            @PathVariable Long idCarga,
            @RequestBody ObservacionDecanoDTO dto
    ) {
        coordinacionService.declinePreload(idCarga, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Aval de la carga por parte de desarrollo académico",
        description = "Actualiza el estado de la carga a AVAL DESARROLLO"
    )
    @PutMapping("/approve-preload-development/{idCarga}")
    public ResponseEntity<Void> approvePreloadDevelopment(
            @PathVariable Long idCarga
    ) {
        coordinacionService.approvePreloadDevelopment(idCarga);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Obtiene las horas de actividades por carga",
        description = "Retorna las horas agrupadas por tipo de actividad padre (FAD, FAI, AC, ISU, CTEI) y el total de horas"
    )
    @GetMapping("/activities-hours")
    public ResponseEntity<HorasActividadesCargaDTO> getActivitiesHours(@RequestParam Long idCarga) {
        HorasActividadesCargaDTO horas = coordinacionService.getActivitiesHours(idCarga);
        return new ResponseEntity<>(horas, HttpStatus.OK);
    }

    /*@Operation(
        summary = "Genera el reporte PDF de preasignación de una carga",
        description = """
            Exporta el PDF institucional de preasignación: encabezado, barras de
            contexto, tablas por modalidad, resumen de totales y espacio de firmas.
            """
    )
    @GetMapping("/preload-pdf-report/{idCarga}")
    public ResponseEntity<byte[]> generatePreloadPdfReport(@PathVariable Long idCarga) {
        FileDTO file = preasignacionReporteService.generatePreloadPdfReport(idCarga);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.fileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file.content());
    }*/

}
