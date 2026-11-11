/**
 * Aplicación: rvd
 * Archivo: ContratoRegistroServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 * 10/11/2026 - Andrés Hernández - Ajuste inicial de los valores que se almacenan del contrato
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.config.security.SecurityUtils;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.DatosAnexoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.ContratoEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.ContratoRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ContratoRegistroService;
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

    /** Longitud real de CONTRATO.CONT_REGISTRADOPOR. */
    private static final int LONGITUD_REGISTRADOPOR = 30;

    private final ContratoRepository contratoRepository;

    @Override
    @Transactional
    public Long registrarContrato(
            InformacionDocenteContratacionDTO informacion,
            ValorContratacionDTO valorContratacion,
            DatosAnexoDTO datos) {

        validar(informacion, datos);

        String numeroAnexo = datos.numeroAnexo() == null ? null : datos.numeroAnexo().trim();
        ContratoEntity contrato = resolverContrato(numeroAnexo);
        boolean nuevo = contrato.getId() == null;

        aplicarDatos(contrato, informacion, valorContratacion, datos, numeroAnexo);
        ContratoEntity guardado = contratoRepository.save(contrato);

        log.info(
                "registrarContrato ===> {} CONT_ID={}, contratista={}, numero={}, cuantia={}, convocatoria={}, cdp={}",
                nuevo ? "nuevo" : "actualizado",
                guardado.getId(),
                guardado.getIdPersonaContratista(),
                guardado.getNumero(),
                guardado.getCuantia(),
                guardado.getIdConvocatoria(),
                guardado.getIdCdp());

        return guardado.getId();
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
        if (datos == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Faltan los datos del anexo para registrar el contrato");
        }
    }

    private ContratoEntity resolverContrato(String numeroAnexo) {
        if (numeroAnexo == null || numeroAnexo.isBlank()) {
            return new ContratoEntity();
        }
        return contratoRepository.findFirstByNumeroOrderByIdDesc(numeroAnexo)
                .orElseGet(ContratoEntity::new);
    }

    private void aplicarDatos(
            ContratoEntity contrato,
            InformacionDocenteContratacionDTO informacion,
            ValorContratacionDTO valorContratacion,
            DatosAnexoDTO datos,
            String numeroAnexo) {

        AuthUserDetails usuario = requireUsuario();

        contrato.setIdModalidadContratacion(informacion.idModalidadContratacion());
        // Que unidad se pone
        contrato.setIdUnidad(informacion.idUnidadRegional());
        // Que ciudad se pone
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

        contrato.setRegistradoPor(truncar(usuario.getUsername(), LONGITUD_REGISTRADOPOR));
        contrato.setFechaCambio(new Date());

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

    private String truncar(String valor, int longitud) {
        if (valor == null) {
            return null;
        }
        return valor.length() <= longitud ? valor : valor.substring(0, longitud);
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
