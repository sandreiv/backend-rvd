/**
 * Aplicación: rvd
 * Archivo: ContratoRegistroServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 * 10/11/2026 - Andrés Hernández - Ajuste inicial de los valores que se almacenan del contrato
 * 11/11/2026 - Andrés Hernández - Avance en el llenado de tablas relacionadas con la contratacion (DOVI, CDCO)
 * 12/11/2026 - Andrés Hernández - Avance en el llenado de tablas relacionadas con la contratacion (TRLA, TRLU, DOUN)
 * 13/11/2026 - Andrés Hernández - Actualizar la carga docente cuando acaba el proceso de vinculación
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.DatosAnexoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CdpContratoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ContratoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocenteUnidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocumentoVinculacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EstadoTrabajadorEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TrabajadorLaborEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.TrabajadorLaborUnidadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CdpContratoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ContratoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DocenteUnidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DocumentoVinculacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.EstadoTrabajadorRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionCargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TrabajadorLaborRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TrabajadorLaborUnidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ContratoRegistroService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Registro del contrato en CONTRATOS.CONTRATO.
 *
 * <p>Columnas con valor fijo (no hay catálogo que las defina hoy):
 * {@code CONT_ORIGEN = 'RVD'}, {@code CONT_TIPODIAS = 'CALENDARIO'},
 * {@code CONT_ANULADO = '0'}, {@code CONT_INICIADO = '0'},
 * {@code CONT_ESABIERTO = '0'}, {@code CONT_REGISTROAUTOMATICO = '1'} y
 * {@code FOIC_ID = 1} (FORMAINICIOCONTRATO "FECHA INICIO").</p>
 *
 * <p>Columnas con dato del sistema:
 * {@code CONV_ID} = convocatoria de contratación relacionada con la carga
 * ({@code RVD.CONVOCATORIA.CONV_CONTRATACION = '1'} por {@code CONV_IDRELACION});
 * {@code CDP_ID} = {@code TESORERIAV3.CDP.CDP_ID} del CDP de la coordinación y
 * el periodo.</p>
 *
 * <p>Columnas que quedan nulas porque todavía no se sabe qué dato va ahí:
 * {@code COVE_ID}, {@code CALI_ID},
 * {@code CONT_JUSTIFICACIONANTICIPO}, {@code DOSO_ID}, {@code NORG_ID},
 * {@code PEGE_IDUNIVERSIDADCOMISION}, {@code CONT_ESTUDIOSUPERIOR},
 * {@code REQU_ID}, {@code CONT_NUMEROHORAS},
 * {@code CONT_VALORSALARIO}, {@code TPRE_ID}, {@code CAAS_ID},
 * {@code CONT_SIGLA}, {@code UPRO_ID}, {@code CONT_DISTRIBUCIONCARGA},
 * {@code CONT_OBJETO}, {@code CONT_DESCRIPCION} y
 * {@code CONT_DIRECCIONSECOP}. En una actualización no se tocan, para no
 * borrar lo que haya escrito otro módulo.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContratoRegistroServiceImpl implements ContratoRegistroService {

    private static final ZoneId ZONA = ZoneId.of("America/Bogota");
    private static final String ORIGEN_RVD = "RVD";
    private static final String TIPO_DIAS_CALENDARIO = "CALENDARIO";
    private static final String NO = "0";
    private static final String SI = "1";
    private static final BigDecimal CERO = BigDecimal.ZERO;
    private static final String TRABAJADOR_ASIGNACION_PROPIEDAD = "P";
    private static final String TRABAJADOR_LABOR_UNIDAD_ESTADO = "ACTIVO";

    /** Longitud real de CONTRATO.CONT_REGISTRADOPOR. */
    private static final int LONGITUD_REGISTRADOPOR = 30;

    private final ContratoRepository contratoRepository;
    private final DocumentoVinculacionRepository documentoVinculacionRepository;
    private final CdpContratoRepository cdpContratoRepository;
    private final RestriccionCargaRepository restriccionCargaRepository;
    private final EstadoTrabajadorRepository estadoTrabajadorRepository;
    private final TrabajadorLaborRepository trabajadorLaborRepository;
    private final TrabajadorLaborUnidadRepository trabajadorLaborUnidadRepository;
    private final DocenteUnidadRepository docenteUnidadRepository;
    private final CargaDocenteRepository cargaDocenteRepository;

    @Override
    @Transactional
    public Long registrarContrato(
            InformacionDocenteContratacionDTO informacion,
            Long idCargaDocente,
            ValorContratacionDTO valorContratacion,
            Long idCdp,
            DatosAnexoDTO datos) {

        validar(informacion, datos);

        String numeroAnexo = datos.numeroAnexo() == null ? null : datos.numeroAnexo().trim();
        Date fechaActual = new Date();

        ContratoEntity contrato = new ContratoEntity();

        // Guardar en CONTRATOS.CONTRATO
        aplicarDatosContrato(contrato, informacion, valorContratacion, datos, numeroAnexo, fechaActual);
        ContratoEntity newContrato = contratoRepository.save(contrato);

        // Guardar en CONTRATOS.CDPCONTRATO
        guardarCdpContrato(newContrato.getId(), idCdp, fechaActual);

        // Guardar en TALENTOV3.DOCUMENTOVINCULACION
        Long idDovi = guardarDocumentoVinculacion(newContrato.getId(), fechaActual);

        // Guardar en TALENTOV3.TRABAJADORLABOR
        guardarTrabajadorLabor(informacion, datos.idLabor(), idDovi, fechaActual);

        // Guardar en TALENTOV3.TRABAJADORLABORUNIDAD
        Long idTrlu = guardarTrabajadorLaborUnidad(informacion, datos.idLabor(), idDovi, fechaActual);

        // Guardar en ACADEMICO.DOCENTEUNIDAD
        guardarDocenteUnidad(informacion, datos.idLabor(), idDovi, idTrlu, fechaActual);

        // Actualiza el estado de la carga docente vinculación a 1
        actualizarVinculacionCargaDocente(idCargaDocente, fechaActual);


        log.info(
                "registrarContrato ===> CONT_ID={}, contratista={}, numero={}, cuantia={}",
                newContrato.getId(),
                newContrato.getIdPersonaContratista(),
                newContrato.getNumero(),
                newContrato.getCuantia());

        return newContrato.getId();
    }

    private void aplicarDatosContrato(
            ContratoEntity contrato,
            InformacionDocenteContratacionDTO informacion,
            ValorContratacionDTO valorContratacion,
            DatosAnexoDTO datos,
            String numeroAnexo,
            Date fechaActual) {

        AuthUserDetails usuario = requireUsuario();

        contrato.setIdModalidadContratacion(informacion.idModalidadContratacion());
        contrato.setIdUnidad(informacion.idUnidadRegional());
        contrato.setIdCiudad(informacion.idCiudadUnidadRegional());

        contrato.setIdPersonaContratista(informacion.idPersonaGeneral());
        contrato.setIdPersonaContratante(usuario.getIdPersonaGeneral());

        contrato.setIdFormaPago(datos.idFormaPago());
        contrato.setIdTipoObjeto(datos.idTipoObjeto());
        contrato.setIdFormaInicioContrato(datos.idFormaInicioContrato());

        // Que numero se pone
        contrato.setNumero(numeroAnexo);

        contrato.setFechaFirma(toDate(datos.fechaFirma()));
        contrato.setFechaInicio(informacion.fechaInicio());
        contrato.setFechaFin(informacion.fechaFin());

        contrato.setCuantia(resolverCuantia(valorContratacion.totalContrato(), informacion.valorContratoTotal()));
        contrato.setValorSalario(resolverSalario(valorContratacion.salario(), informacion.valorContratoTotal()));
        contrato.setDuracionContrato(calcularDuracion(informacion.fechaInicio(), informacion.fechaFin()));

        contrato.setRegistradoPor(formatRegistradoPor(usuario.getIdPersonaGeneral(), LONGITUD_REGISTRADOPOR));
        contrato.setFechaCambio(fechaActual);

        contrato.setTipoDias(TIPO_DIAS_CALENDARIO);
        contrato.setOrigen(ORIGEN_RVD);
        contrato.setAnulado(NO);
        contrato.setIniciado(NO);
        contrato.setEsAbierto(NO);
        contrato.setRegistroAutomatico(NO);

        contrato.setAnticipo(CERO);
        contrato.setObjeto(datos.objeto());
        contrato.setDescripcion(datos.descripcion());
    }

    private Long guardarDocumentoVinculacion(Long idContrato, Date fechaActual) {
        AuthUserDetails usuario = requireUsuario();

        if (documentoVinculacionRepository.existsByIdContrato(idContrato)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El contrato ya ha sido relacionado con un documento vinculacion"
            );
        }

        DocumentoVinculacionEntity documentoVinculacion = new DocumentoVinculacionEntity();
        documentoVinculacion.setIdContrato(idContrato);
        documentoVinculacion.setRegistradoPor(formatRegistradoPor(usuario.getIdPersonaGeneral(), LONGITUD_REGISTRADOPOR));
        documentoVinculacion.setFechaCambio(fechaActual);

        DocumentoVinculacionEntity newDovi = documentoVinculacionRepository.save(documentoVinculacion);
        return newDovi.getId();
    }

    private void guardarCdpContrato(Long idContrato, Long idCdp, Date fechaActual) {
        AuthUserDetails usuario = requireUsuario();

        if (cdpContratoRepository.existsByIdContratoAndIdCdp(idContrato, idCdp)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El contrato ya ha sido relacionado con el cdp"
            );
        }

        CdpContratoEntity cdpContrato = new CdpContratoEntity();
        cdpContrato.setIdContrato(idContrato);
        cdpContrato.setIdCdp(idCdp);
        cdpContrato.setRegistradoPor(formatRegistradoPor(usuario.getIdPersonaGeneral(), LONGITUD_REGISTRADOPOR));
        cdpContrato.setFechaCambio(fechaActual);

        cdpContratoRepository.save(cdpContrato);
    }

    private void guardarTrabajadorLabor(InformacionDocenteContratacionDTO informacion, Long idLabor, Long idDocumentoVinculacion, Date fechaActual) {
        AuthUserDetails usuario = requireUsuario();

        if (trabajadorLaborRepository.existsByIdPersonaGeneralAndIdDocumentoVinculacionAndIdLabor(informacion.idPersonaGeneral(), idDocumentoVinculacion, idLabor)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El contrato ya ha sido relacionado con un trabajador labor"
            );
        }

        RestriccionCargaEntity restriccionCarga = restriccionCargaRepository.findById(informacion.idModalidadContratacion())
            .orElseThrow(() -> new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La modalidad de contratación no tiene una restricción de carga asociada"
            ));
        if ((restriccionCarga.getIdDedicacionDocente() == null) || (restriccionCarga.getIdClasificacionDocente() == null)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La modalidad de contratación no tiene clasificación o dedicación del docente"
            );
        }

        Long idEstadoTrabajador = estadoTrabajadorRepository.findEstadoInicialTrabajador()
            .map(EstadoTrabajadorEntity::getId)
            .orElseThrow(() -> new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No existe un estado inicial que asignar al trabajador"
            ));
        
        TrabajadorLaborEntity trabajadorLabor = new TrabajadorLaborEntity();
        trabajadorLabor.setIdPersonaGeneral(informacion.idPersonaGeneral());
        trabajadorLabor.setIdLabor(idLabor);
        trabajadorLabor.setIdDocumentoVinculacion(idDocumentoVinculacion);
        trabajadorLabor.setIdClasificacionDocente(restriccionCarga.getIdClasificacionDocente());
        trabajadorLabor.setIdDedicacionDocente(restriccionCarga.getIdDedicacionDocente());
        trabajadorLabor.setIdEstadoTrabajador(idEstadoTrabajador);
        trabajadorLabor.setFechaInicial(informacion.fechaInicio());
        trabajadorLabor.setFechaFinal(informacion.fechaFin());
        trabajadorLabor.setFechaPosesion(informacion.fechaInicio());
        trabajadorLabor.setFechaInicioEstado(informacion.fechaInicio());
        trabajadorLabor.setFechaInicialAjustada(informacion.fechaInicio());
        trabajadorLabor.setFechaPeriodoVacaciones(informacion.fechaInicio());
        trabajadorLabor.setRegistradoPor(formatRegistradoPor(usuario.getIdPersonaGeneral(), LONGITUD_REGISTRADOPOR));
        trabajadorLabor.setFechaCambio(fechaActual);

        trabajadorLaborRepository.save(trabajadorLabor);
    }

    private Long guardarTrabajadorLaborUnidad(InformacionDocenteContratacionDTO informacion, Long idLabor, Long idDocumentoVinculacion, Date fechaActual) {
        AuthUserDetails usuario = requireUsuario();

        if (trabajadorLaborUnidadRepository.existsByIdPersonaGeneralAndIdDocumentoVinculacionAndIdLaborAndIdUnidad(informacion.idPersonaGeneral(), idDocumentoVinculacion, idLabor, informacion.idUnidadArea())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El contrato ya ha sido relacionado con un trabajador labor unidad"
            );
        }

        TrabajadorLaborUnidadEntity trabajadorLaborUnidad = new TrabajadorLaborUnidadEntity();
        trabajadorLaborUnidad.setIdPersonaGeneral(informacion.idPersonaGeneral());
        trabajadorLaborUnidad.setIdLabor(idLabor);
        trabajadorLaborUnidad.setIdUnidad(informacion.idUnidadArea());      // La facultad de la coordinación
        trabajadorLaborUnidad.setIdDocumentoVinculacion(idDocumentoVinculacion);
        trabajadorLaborUnidad.setFechaInicioEstado(informacion.fechaInicio());
        trabajadorLaborUnidad.setTipoAsignacion(TRABAJADOR_ASIGNACION_PROPIEDAD);
        trabajadorLaborUnidad.setEstado(TRABAJADOR_LABOR_UNIDAD_ESTADO);
        trabajadorLaborUnidad.setFechaInicial(informacion.fechaInicio());
        trabajadorLaborUnidad.setFechaFinal(informacion.fechaFin());
        trabajadorLaborUnidad.setRegistradoPor(formatRegistradoPor(usuario.getIdPersonaGeneral(), LONGITUD_REGISTRADOPOR));
        trabajadorLaborUnidad.setFechaCambio(fechaActual);

        TrabajadorLaborUnidadEntity newTrlu = trabajadorLaborUnidadRepository.save(trabajadorLaborUnidad);
        return newTrlu.getId();
    }

    private void guardarDocenteUnidad(InformacionDocenteContratacionDTO informacion, Long idLabor, Long idDocumentoVinculacion, Long idTrabajadorLaborUnidad, Date fechaActual) {
        AuthUserDetails usuario = requireUsuario();

        DocenteUnidadEntity docenteUnidadArea = new DocenteUnidadEntity();
        docenteUnidadArea.setIdPersonaGeneral(informacion.idPersonaGeneral());
        docenteUnidadArea.setIdUnidad(informacion.idUnidadArea());
        docenteUnidadArea.setIdLabor(idLabor);
        docenteUnidadArea.setIdDocumentoVinculacion(idDocumentoVinculacion);
        docenteUnidadArea.setIdTrabajadorLaborUnidad(idTrabajadorLaborUnidad);
        docenteUnidadArea.setRegistradoPor(formatRegistradoPor(usuario.getIdPersonaGeneral(), LONGITUD_REGISTRADOPOR));
        docenteUnidadArea.setFechaCambio(fechaActual);

        DocenteUnidadEntity docenteUnidadRegional = new DocenteUnidadEntity();
        docenteUnidadRegional.setIdPersonaGeneral(informacion.idPersonaGeneral());
        docenteUnidadRegional.setIdUnidad(informacion.idUnidadRegional());
        docenteUnidadRegional.setIdLabor(idLabor);
        docenteUnidadRegional.setIdDocumentoVinculacion(idDocumentoVinculacion);
        docenteUnidadRegional.setRegistradoPor(formatRegistradoPor(usuario.getIdPersonaGeneral(), LONGITUD_REGISTRADOPOR));
        docenteUnidadRegional.setFechaCambio(fechaActual);

        docenteUnidadRepository.saveAll(List.of(docenteUnidadArea, docenteUnidadRegional));
    }

    private void actualizarVinculacionCargaDocente(Long idCargaDocente, Date fechaActual) {
        CargaDocenteEntity cargaDocente = cargaDocenteRepository.findById(idCargaDocente)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga docente con id " + idCargaDocente));
        
        cargaDocente.setContratacionVinculado(SI);
        cargaDocente.setRegistradoPor(RegistradoPorUtils.value(Accion.INSERT));
        cargaDocente.setFechaCambio(fechaActual);
        cargaDocenteRepository.save(cargaDocente);
    }

    
    private void validar(InformacionDocenteContratacionDTO informacion, DatosAnexoDTO datos) {
        if (informacion == null || informacion.idPersonaGeneral() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No hay información del docente para registrar el contrato");
        }
        if (informacion.fechaInicio() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene fecha de inicio, no se puede registrar el contrato");
        }
        if (informacion.idModalidadContratacion() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene modalidad de contratación");
        }
        if (informacion.idUnidadRegional() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene unidad regional");
        }
        if (informacion.idUnidadArea() == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene unidad de area");
        }
        if (datos == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Faltan los datos del anexo para registrar el contrato");
        }
    }

    private BigDecimal resolverCuantia(BigDecimal valorContrato, BigDecimal valorCarga) {
        BigDecimal cuantia = valorContrato != null ? valorContrato : valorCarga;
        if (cuantia == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No hay valor de contrato para registrar (CONT_CUANTIA es obligatorio)");
        }
        return cuantia;
    }

    private String resolverSalario(BigDecimal valorSalario, BigDecimal valorCarga) {
        BigDecimal salario = valorSalario != null ? valorSalario : valorCarga;
        if (salario == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No hay valor de salario para registrar (CONT_VALORSALARIO es obligatorio)");
        }
        return salario.toPlainString();
    }

    /**
     * Duración en meses entre la fecha de inicio y la de fin, con el formato
     * 'MES / DIA'; 05/01/2026 → 09/12/2026 = 11 / 09.
     */
    private String calcularDuracion(Date fechaInicio, Date fechaFin) {
        LocalDate inicio = toLocalDate(fechaInicio);
        LocalDate fin = toLocalDate(fechaFin);

        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;

        long meses = dias / 30;
        long diasRestantes = dias % 30;

        return meses + " / " + diasRestantes;
    }

    private AuthUserDetails requireUsuario() {
        try {
            AuthUserDetails usuario = SecurityUtils.requireUser();
            if (usuario.getIdPersonaGeneral() == null) {
                throw new ApiException(
                        HttpStatus.FORBIDDEN,
                        "El token no trae idPersona para registrar el contratante");
            }
            return usuario;
        } catch (IllegalStateException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    private String formatRegistradoPor(Long valor, int longitud) {
        return valor != null ? String.valueOf(valor) : null;
    }

    private Date toDate(LocalDate fecha) {
        if (fecha == null) {
            return null;
        }
        return Date.from(fecha.atStartOfDay(ZONA).toInstant());
    }

    private LocalDate toLocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZONA).toLocalDate();
    }
}
