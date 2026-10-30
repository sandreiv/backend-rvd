/**
 * Aplicación: rvd
 * Archivo: ContratoReporteServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/10/2026
 * Modificaciones:
 * 30/10/2026 - Creación inicial (anexo de contratación en PDF: 4 meses y con funciones administrativas)
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadDirectaDetalleDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ActividadHorasResumenDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FileDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HiringCdpInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.HiringValuesInformationDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PtdInfoWithWeightsDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoAcademicoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoActividadDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoAnexoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoCteiDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoDiscriminadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoDocenteReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoFirmasDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoMateriaDirectaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoPlanTrabajoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoPresupuestoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoRemuneracionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoSuscripcionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.TipoContratoDocente;
import co.edu.unipamplona.ciadti.rvd.model.repository.TituloPersonaNaturalGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.projection.PublicAcademicTrainingProjection;
import co.edu.unipamplona.ciadti.rvd.model.service.ContratacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.ContratoReporteService;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;
import co.edu.unipamplona.ciadti.rvd.model.service.PlanTrabajoDigitalService;
import co.edu.unipamplona.ciadti.rvd.report.ContratoPdfExporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContratoReporteServiceImpl implements ContratoReporteService {

    private static final String CODIGO_FAD = "FAD";
    private static final String CODIGO_FAI = "FAI";
    private static final String CODIGO_CTEI = "CTEI";
    private static final String CODIGO_ISU = "ISU";
    private static final String CODIGO_AC = "AC";

    /**
     * Funciones del docente ocasional tiempo completo con funciones
     * administrativas. Van fijas en el anexo: no dependen de la base de datos.
     */
    private static final List<String> FUNCIONES_COORDINADOR = List.of(
            "Elaborar y presentar a consideración de la Decanatura la programación de prácticas "
                    + "extramurales de acuerdo con las exigencias del programa, para cada periodo académico.",
            "Controlar la cumplida ejecución del programa académico.",
            "Asistir a los consejos de facultad con el propósito de analizar la problemática "
                    + "académico administrativa del programa y proponer soluciones.",
            "Garantizar la atención del servicio de asesoría y consejería a los estudiantes del Programa.",
            "Participar en la asignación de carga académica para cada profesor.",
            "Verificar que los profesores vinculados y/o por vincular al programa cumplan con el "
                    + "perfil requerido y acrediten su formación académica y experiencia profesional.",
            "Organizar, controlar y evaluar las actividades académicas desarrolladas por los "
                    + "profesores y estudiantes vinculados al programa.",
            "Conceptuar sobre aspectos académicos que le correspondan o que sean sometidos a su consideración.",
            "Presentar informes sobre las actividades desarrolladas a la Decanatura y a la "
                    + "Vicerrectoría Académica.",
            "Distribuir la labor docente y los horarios del Programa de acuerdo con el plan de estudios vigente.",
            "Evaluar al final de cada periodo académico la actividad desarrollada por cada uno de los profesores.",
            "Coordinar los servicios administrativos y académicos para el desarrollo de los programas y "
                    + "resolver los asuntos relacionados con horarios de estudiantes, disponibilidad de "
                    + "aulas, laboratorios, campos para práctica, recursos y materiales.",
            "Presentar a la facultad las necesidades de profesores de vinculación especial y participar "
                    + "con la Vicerrectoría Académica y los decanos en la selección de los Docentes de "
                    + "vinculación especial.",
            "Ejercer la supervisión de los contratos de los profesores de su programa.",
            "Las demás que se establezcan en las normas de la Universidad y/o en la ley.");

    private final CoordinacionService coordinacionService;
    private final ContratacionService contratacionService;
    private final PlanTrabajoDigitalService planTrabajoDigitalService;
    private final TituloPersonaNaturalGeneralRepository tituloPersonaNaturalGeneralRepository;
    private final ContratoPdfExporter contratoPdfExporter;

    @Override
    @Transactional(readOnly = true)
    public FileDTO generateContractPdf(Long idCargaDocente, TipoContratoDocente tipoForzado) {
        log.debug("generateContractPdf ===> idCargaDocente={}, tipoForzado={}", idCargaDocente, tipoForzado);

        if (idCargaDocente == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id de la carga docente es obligatorio");
        }

        InformacionDocenteContratacionDTO informacion =
                coordinacionService.findProfessorInformation(idCargaDocente);
        if (informacion == null) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No existe información del docente para la carga docente " + idCargaDocente);
        }

        Long idPersonaGeneral = informacion.idPersonaGeneral();

        PublicAcademicTrainingProjection formacion = resolveFormacionAcademica(idPersonaGeneral);

        HiringCdpInformationDTO cdp = resolveCdp(informacion);
        HiringValuesInformationDTO valores = contratacionService.hiringValuesInformation(idCargaDocente);
        String observaciones = resolveObservaciones(idPersonaGeneral, idCargaDocente, informacion);

        TipoContratoDocente tipo = tipoForzado != null
                ? tipoForzado
                : resolveTipoContrato(informacion);

        ContratoDocenteReporteDTO reporte = new ContratoDocenteReporteDTO(
                buildAnexo(informacion),
                buildDocente(informacion),
                buildAcademico(informacion, formacion),
                buildRemuneracion(informacion, valores),
                buildDiscriminado(valores),
                buildPlanTrabajo(informacion, observaciones),
                new ContratoPresupuestoDTO(
                        cdp != null ? cdp.numero() : null,
                        cdp != null ? cdp.rubros() : null),
                buildSuscripcion(informacion, cdp),
                new ContratoFirmasDTO(
                        null,
                        null,
                        null,
                        informacion.nombreCompleto()),
                tipo == TipoContratoDocente.CON_FUNCIONES_ADMINISTRATIVAS
                        ? FUNCIONES_COORDINADOR
                        : List.of());

        byte[] content = contratoPdfExporter.export(reporte, tipo);
        String fileName = buildFileName(tipo, informacion.documentoIdentidad());
        log.info(
                "generateContractPdf ===> idCargaDocente={}, tipo={}, bytes={}",
                idCargaDocente,
                tipo,
                content.length);
        return new FileDTO(fileName, content);
    }

    /**
     * Regla de negocio del anexo: si el docente es el coordinador del programa
     * de la carga se firma el anexo con funciones administrativas; en caso
     * contrario se usa el anexo de 4 meses (docencia de pregrado).
     */
    private TipoContratoDocente resolveTipoContrato(InformacionDocenteContratacionDTO informacion) {
        Long total = informacion.esCoordinador();
        boolean esCoordinador = total != null && total > 0;
        log.debug(
                "resolveTipoContrato ===> idPersonaGeneral={}, idCoordinacion={}, esCoordinador={}",
                informacion.idPersonaGeneral(),
                informacion.idCoordinacion(),
                esCoordinador);
        return esCoordinador
                ? TipoContratoDocente.CON_FUNCIONES_ADMINISTRATIVAS
                : TipoContratoDocente.CUATRO_MESES;
    }

    private HiringCdpInformationDTO resolveCdp(InformacionDocenteContratacionDTO informacion) {
        if (informacion.idPeriodoUniversidad() == null || informacion.idCoordinacion() == null) {
            log.warn("resolveCdp ===> La carga no tiene periodo o coordinación para buscar el CDP");
            return null;
        }
        try {
            return contratacionService.hiringCdpInformation(
                    informacion.idPeriodoUniversidad(),
                    informacion.idCoordinacion());
        } catch (RuntimeException ex) {
            log.warn("resolveCdp ===> No fue posible obtener el CDP: {}", ex.getMessage());
            return null;
        }
    }

    private String resolveObservaciones(
            Long idPersonaGeneral,
            Long idCargaDocente,
            InformacionDocenteContratacionDTO informacion) {
        if (idPersonaGeneral == null || informacion.idPeriodoUniversidad() == null) {
            return null;
        }
        try {
            PtdInfoWithWeightsDTO ptd = planTrabajoDigitalService.getInfoPtdWithWeights(
                    idPersonaGeneral,
                    idCargaDocente,
                    informacion.idPeriodoUniversidad());
            return ptd != null && ptd.observacion() != null
                    ? ptd.observacion().descripcion()
                    : null;
        } catch (RuntimeException ex) {
            log.warn("resolveObservaciones ===> No fue posible obtener la observación del PTD: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Último título del docente (el registrado más recientemente): nivel académico,
     * título, institución, fecha de grado y país. Es el bloque INFORMACIÓN ACADÉMICA
     * del anexo; si el docente no tiene títulos con nivel homologado se cae a los
     * datos del título que trae la consulta del docente.
     */
    private PublicAcademicTrainingProjection resolveFormacionAcademica(Long idPersonaGeneral) {
        if (idPersonaGeneral == null) {
            return null;
        }
        try {
            return tituloPersonaNaturalGeneralRepository
                    .findLatestAcademicTraining(idPersonaGeneral)
                    .orElse(null);
        } catch (RuntimeException ex) {
            log.warn("resolveFormacionAcademica ===> No fue posible obtener el último título: {}",
                    ex.getMessage());
            return null;
        }
    }

    private ContratoAnexoDTO buildAnexo(InformacionDocenteContratacionDTO informacion) {
        String programa = StringUtils.hasText(informacion.programa())
                ? informacion.programa()
                : informacion.coordinacion();
        return new ContratoAnexoDTO(
                informacion.modalidadContratacion(),
                informacion.unidadRegional(),
                programa,
                null,
                toLocalDate(informacion.fechaInicio()),
                toLocalDate(informacion.fechaFin()));
    }

    private ContratoDocenteDTO buildDocente(InformacionDocenteContratacionDTO informacion) {
        return new ContratoDocenteDTO(
                informacion.nombreCompleto(),
                informacion.documentoIdentidad(),
                informacion.expedidoEn(),
                null,
                informacion.genero(),
                toLocalDate(informacion.fechaNacimiento()),
                informacion.ciudadNacimiento(),
                informacion.departamentoNacimiento(),
                informacion.direccionDomicilio(),
                informacion.telefono(),
                informacion.celular(),
                informacion.correoPersonal());
    }

    private ContratoAcademicoDTO buildAcademico(
            InformacionDocenteContratacionDTO informacion,
            PublicAcademicTrainingProjection formacion) {
        if (formacion != null) {
            return new ContratoAcademicoDTO(
                    formacion.getNivelAcademico(),
                    formacion.getTitulo(),
                    formacion.getInstitucion(),
                    toLocalDate(formacion.getFechaGrado()),
                    null,
                    formacion.getPais());
        }

        String descripcion = "F".equalsIgnoreCase(informacion.genero())
                ? informacion.tituloFemenino()
                : informacion.tituloMasculino();
        if (!StringUtils.hasText(descripcion)
                && !StringUtils.hasText(informacion.establecimiento())) {
            return new ContratoAcademicoDTO(null, null, null, null, null, null);
        }
        String institucion = StringUtils.hasText(informacion.establecimiento())
                ? informacion.establecimiento()
                : informacion.otraInstitucion();
        return new ContratoAcademicoDTO(
                null,
                descripcion,
                institucion,
                toLocalDate(informacion.fechaObtencionTitulo()),
                null,
                informacion.paisTitulo());
    }

    private ContratoRemuneracionDTO buildRemuneracion(
            InformacionDocenteContratacionDTO informacion,
            HiringValuesInformationDTO valores) {
        ValorContratacionDTO valor = valores != null ? valores.valorContratacion() : null;
        return new ContratoRemuneracionDTO(
                informacion.categoriaDocente(),
                parseDecimal(informacion.puntos()),
                parseDecimal(informacion.horas()),
                valor != null ? valor.salario() : informacion.asignacionSalarial(),
                valor != null ? valor.valorContrato() : null,
                valor != null ? valor.totalPrestaciones() : null,
                valor != null ? valor.totalContrato() : null);
    }

    private ContratoDiscriminadoDTO buildDiscriminado(HiringValuesInformationDTO valores) {
        ValorContratacionDTO valor = valores != null ? valores.valorContratacion() : null;
        if (valor == null) {
            return new ContratoDiscriminadoDTO(null, null, null, null);
        }
        return new ContratoDiscriminadoDTO(
                valor.valorContrato(),
                valor.valorCesantias(),
                valor.valorVacaciones(),
                valor.valorPrimaLegal());
    }

    private ContratoPlanTrabajoDTO buildPlanTrabajo(
            InformacionDocenteContratacionDTO informacion,
            String observaciones) {
        List<ContratoMateriaDirectaDTO> directo = new ArrayList<>();
        List<ContratoActividadDTO> indirecto = new ArrayList<>();
        List<ContratoActividadDTO> cteiDedicacion = new ArrayList<>();
        List<ContratoActividadDTO> isu = new ArrayList<>();
        List<ContratoActividadDTO> administracion = new ArrayList<>();

        BigDecimal totalDirecto = BigDecimal.ZERO;
        BigDecimal totalIndirecto = BigDecimal.ZERO;
        BigDecimal totalCtei = BigDecimal.ZERO;
        BigDecimal totalAdministracion = BigDecimal.ZERO;
        BigDecimal totalIsu = BigDecimal.ZERO;

        List<ActividadHorasResumenDTO> actividades = informacion.horasActividades() == null
                ? List.of()
                : informacion.horasActividades();

        for (ActividadHorasResumenDTO actividad : actividades) {
            String codigo = actividad.codigo() == null
                    ? ""
                    : actividad.codigo().trim().toUpperCase(Locale.ROOT);
            BigDecimal horas = actividad.totalHoras() != null ? actividad.totalHoras() : BigDecimal.ZERO;
            switch (codigo) {
                case CODIGO_FAD -> {
                    totalDirecto = totalDirecto.add(horas);
                    if (actividad.detalles() == null || actividad.detalles().isEmpty()) {
                        directo.add(new ContratoMateriaDirectaDTO(
                                null, null, actividad.nombre(), null, null, null, horas));
                    } else {
                        for (ActividadDirectaDetalleDTO detalle : actividad.detalles()) {
                            directo.add(new ContratoMateriaDirectaDTO(
                                    detalle.unidad(),
                                    detalle.programa(),
                                    detalle.materia(),
                                    detalle.grupo(),
                                    aDecimal(detalle.ponderacionAcademica()),
                                    totalHorasTeoricasPracticas(
                                            detalle.horasTeoricas(),
                                            detalle.horasPracticas()),
                                    detalle.horas()));
                        }
                    }
                }
                case CODIGO_FAI -> {
                    totalIndirecto = totalIndirecto.add(horas);
                    indirecto.add(new ContratoActividadDTO(actividad.nombre(), horas));
                }
                case CODIGO_CTEI -> {
                    totalCtei = totalCtei.add(horas);
                    cteiDedicacion.add(new ContratoActividadDTO(actividad.nombre(), horas));
                }
                case CODIGO_AC -> {
                    totalAdministracion = totalAdministracion.add(horas);
                    administracion.add(new ContratoActividadDTO(actividad.nombre(), horas));
                }
                case CODIGO_ISU -> {
                    totalIsu = totalIsu.add(horas);
                    isu.add(new ContratoActividadDTO(actividad.nombre(), horas));
                }
                default -> log.debug(
                        "buildPlanTrabajo ===> Actividad sin bloque en el anexo. codigo={}, nombre={}",
                        actividad.codigo(),
                        actividad.nombre());
            }
        }

        BigDecimal totalRelacionadas = totalDirecto
                .add(totalIndirecto)
                .add(totalCtei)
                .add(totalAdministracion)
                .add(totalIsu);

        return new ContratoPlanTrabajoDTO(
                directo,
                totalDirecto,
                indirecto,
                totalIndirecto,
                new ContratoCteiDTO(null, List.of(), cteiDedicacion, totalCtei),
                isu,
                totalIsu,
                administracion,
                totalAdministracion,
                observaciones,
                totalRelacionadas);
    }

    private ContratoSuscripcionDTO buildSuscripcion(
            InformacionDocenteContratacionDTO informacion,
            HiringCdpInformationDTO cdp) {
        LocalDate fecha = cdp != null && cdp.fecha() != null ? cdp.fecha() : LocalDate.now();
        return new ContratoSuscripcionDTO(limpiarUnidad(informacion.unidadRegional()), fecha);
    }

    private String limpiarUnidad(String unidad) {
        if (!StringUtils.hasText(unidad)) {
            return null;
        }
        return unidad.trim()
                .replaceAll("(?i)^unidad\\s+regional,?\\s*", "")
                .replaceAll("(?i)^extensi[oó]n\\s+", "")
                .replaceAll("(?i)^sede\\s+", "")
                .trim();
    }

    private String buildFileName(TipoContratoDocente tipo, String documento) {
        String base = tipo == TipoContratoDocente.CON_FUNCIONES_ADMINISTRATIVAS
                ? "anexo-coordinador"
                : "anexo-4-meses";
        String sufijo = StringUtils.hasText(documento)
                ? documento.trim().replaceAll("[^0-9A-Za-z]", "")
                : "docente";
        return base + "-" + sufijo + ".pdf";
    }

    /**
     * Créditos de la materia: la ponderación académica que trae el detalle de la
     * carga docente (DetalleCargaDocenteListadoProjection.getPonderacionAcademica).
     */
    private BigDecimal aDecimal(Long valor) {
        return valor == null ? null : BigDecimal.valueOf(valor);
    }

    /**
     * HTP de la materia: horas teóricas + horas prácticas del detalle de la carga
     * docente (getHorasTeoricas + getHorasPracticas).
     */
    private BigDecimal totalHorasTeoricasPracticas(Long horasTeoricas, Long horasPracticas) {
        if (horasTeoricas == null && horasPracticas == null) {
            return null;
        }
        long total = (horasTeoricas != null ? horasTeoricas : 0L)
                + (horasPracticas != null ? horasPracticas : 0L);
        return BigDecimal.valueOf(total);
    }

    private BigDecimal parseDecimal(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return new BigDecimal(value.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private LocalDate toLocalDate(Date fecha) {
        if (fecha == null) {
            return null;
        }
        if (fecha instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
