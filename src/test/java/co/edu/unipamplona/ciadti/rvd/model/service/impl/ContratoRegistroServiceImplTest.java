/**
 * Aplicación: rvd
 * Archivo: ContratoRegistroServiceImplTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/11/2026
 * Modificaciones:
 * 18/11/2026 - Juan Diego Florez Vera - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import co.edu.unipamplona.ciadti.rvd.config.security.AuthUserDetails;
import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.DatosAnexoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.InformacionDocenteContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ContratoEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DocumentoVinculacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.EstadoTrabajadorEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.LaborEntity;
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
import co.edu.unipamplona.ciadti.rvd.model.repository.LaborRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TrabajadorLaborUnidadRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TrabajadorRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.CoordinacionService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ContratoRegistroServiceImplTest {

    private static final Long CADO_ID = 523L;
    private static final Long ID_PERSONA = 99L;
    private static final String NUMERO_ANTERIOR = "13";
    private static final Long ID_LABOR = 2L;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private DocumentoVinculacionRepository documentoVinculacionRepository;

    @Mock
    private CdpContratoRepository cdpContratoRepository;

    @Mock
    private RestriccionCargaRepository restriccionCargaRepository;

    @Mock
    private EstadoTrabajadorRepository estadoTrabajadorRepository;

    @Mock
    private TrabajadorRepository trabajadorRepository;

    @Mock
    private TrabajadorLaborRepository trabajadorLaborRepository;

    @Mock
    private TrabajadorLaborUnidadRepository trabajadorLaborUnidadRepository;

    @Mock
    private DocenteUnidadRepository docenteUnidadRepository;

    @Mock
    private CargaDocenteRepository cargaDocenteRepository;

    @Mock
    private CoordinacionService coordinacionService;

    @Mock
    private LaborRepository laborRepository;

    @Captor
    private ArgumentCaptor<ContratoEntity> contratoCaptor;

    @InjectMocks
    private ContratoRegistroServiceImpl service;

    private InformacionDocenteContratacionDTO informacion;
    private ValorContratacionDTO valorContratacion;
    private CargaDocenteEntity cargaDocente;

    @BeforeEach
    void setUp() {
        informacion = new InformacionDocenteContratacionDTO(
                ID_PERSONA,
                "Docente Prueba",
                "123",
                "Calle 1",
                "a@b.com",
                "c@d.com",
                "DOCENTE",
                toDate("2026-02-01"),
                toDate("2026-12-31"),
                "PRUEBA",
                "A",
                BigDecimal.valueOf(1000),
                "40",
                1L,
                "NIVEL ASOCIADO",
                "10",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                1L,
                "Coordinacion",
                "Unidad Regional",
                "Facultad",
                1L,
                "Programa",
                0L,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                1L,
                2L,
                3L,
                4L,
                BigDecimal.valueOf(1000),
                1L,
                null,
                null,
                NUMERO_ANTERIOR,
                List.of());

        valorContratacion = new ValorContratacionDTO(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(1000));

        cargaDocente = new CargaDocenteEntity();
        cargaDocente.setId(CADO_ID);
        cargaDocente.setIdPersonaGeneral(ID_PERSONA);
        cargaDocente.setNumeroContrato(NUMERO_ANTERIOR);

        LaborEntity labor = new LaborEntity();
        labor.setId(ID_LABOR);
        labor.setDocente("1");
        when(laborRepository.findById(ID_LABOR)).thenReturn(Optional.of(labor));

        AuthUserDetails usuario = new AuthUserDetails(77L, "prueba", List.of(), List.of());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuario, null, List.of()));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private Date toDate(String fecha) {
        return Date.from(LocalDate.parse(fecha).atStartOfDay(
                java.time.ZoneId.of("America/Bogota")).toInstant());
    }

    private DatosAnexoDTO datosAnexo(String numeroAnexo, Long idLabor) {
        return new DatosAnexoDTO(
                numeroAnexo,
                LocalDate.of(2026, 11, 18),
                "OBJETO DE PRUEBA",
                "DESCRIPCION DE PRUEBA",
                3L,
                5L,
                1L,
                idLabor);
    }

    @Test
    void debeActualizarElContratoExistenteCuandoElNumeroDeAnexoCambia() {
        ContratoEntity contratoExistente = new ContratoEntity();
        contratoExistente.setId(24L);

        when(cargaDocenteRepository.findById(CADO_ID)).thenReturn(Optional.of(cargaDocente));
        when(contratoRepository.findByDocenteYNumero(ID_PERSONA, NUMERO_ANTERIOR))
                .thenReturn(Optional.of(contratoExistente));
        when(contratoRepository.save(any(ContratoEntity.class)))
                .thenAnswer(invocacion -> {
                    ContratoEntity contrato = invocacion.getArgument(0);
                    if (contrato.getId() == null) {
                        contrato.setId(25L);
                    }
                    return contrato;
                });

        Long idContrato = service.registrarContrato(
                informacion,
                CADO_ID,
                valorContratacion,
                10L,
                datosAnexo("14", null));

        assertEquals(24L, idContrato);
        verify(contratoRepository).save(contratoCaptor.capture());
        assertEquals("14", contratoCaptor.getValue().getNumero());
    }

    @Test
    void debeReutilizarElContratoGeneradoAlVincularElDocente() {
        ContratoEntity contratoGenerado = new ContratoEntity();
        contratoGenerado.setId(24L);
        contratoGenerado.setNumero(NUMERO_ANTERIOR);
        contratoGenerado.setObjeto("OBJETO DE PRUEBA");

        RestriccionCargaEntity restriccion = new RestriccionCargaEntity();
        restriccion.setIdModalidadContratacion(1L);
        restriccion.setIdClasificacionDocente(2L);
        restriccion.setIdDedicacionDocente(3L);

        EstadoTrabajadorEntity estadoInicial = new EstadoTrabajadorEntity();
        estadoInicial.setId(1L);

        when(cargaDocenteRepository.findById(CADO_ID)).thenReturn(Optional.of(cargaDocente));
        when(contratoRepository.findByDocenteYNumero(ID_PERSONA, NUMERO_ANTERIOR))
                .thenReturn(Optional.of(contratoGenerado));
        when(restriccionCargaRepository.findById(1L)).thenReturn(Optional.of(restriccion));
        when(estadoTrabajadorRepository.findEstadoInicialTrabajador())
                .thenReturn(Optional.of(estadoInicial));
        when(documentoVinculacionRepository.existsByIdContrato(24L)).thenReturn(false);
        when(documentoVinculacionRepository.save(any(DocumentoVinculacionEntity.class)))
                .thenAnswer(invocacion -> {
                    DocumentoVinculacionEntity documento = invocacion.getArgument(0);
                    documento.setId(30L);
                    return documento;
                });
        when(trabajadorRepository.existsById(ID_PERSONA)).thenReturn(true);
        when(trabajadorLaborUnidadRepository.save(any(TrabajadorLaborUnidadEntity.class)))
                .thenAnswer(invocacion -> {
                    TrabajadorLaborUnidadEntity unidad = invocacion.getArgument(0);
                    unidad.setId(31L);
                    return unidad;
                });

        Long idContrato = service.vincularDocente(
                informacion,
                CADO_ID,
                valorContratacion,
                10L,
                datosAnexo(NUMERO_ANTERIOR, 7L));

        assertEquals(24L, idContrato);
        // El contrato generado no se vuelve a guardar: sus datos quedan intactos.
        verify(contratoRepository, never()).save(any(ContratoEntity.class));
    }

    @Test
    void debeExigirLaLaborDocenteParaVincular() {
        ApiException exception = assertThrows(
                ApiException.class,
                () -> service.vincularDocente(
                        informacion,
                        CADO_ID,
                        valorContratacion,
                        10L,
                        datosAnexo(NUMERO_ANTERIOR, null)));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void debeExigirElCdpParaRegistrarElContrato() {
        ApiException exception = assertThrows(
                ApiException.class,
                () -> service.registrarContrato(
                        informacion,
                        CADO_ID,
                        valorContratacion,
                        null,
                        datosAnexo(NUMERO_ANTERIOR, null)));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("CDP"));

        // Sin CDP no se debe registrar ningún contrato.
        verify(contratoRepository, never()).save(any(ContratoEntity.class));
    }


    @Test
    void debeVincularElDocenteSinGenerarContratoNiPdf() {
        when(cargaDocenteRepository.findById(CADO_ID)).thenReturn(Optional.of(cargaDocente));
        when(coordinacionService.findProfessorInformation(CADO_ID)).thenReturn(informacion);

        ContratoEntity contrato = new ContratoEntity();
        contrato.setId(55L);
        contrato.setNumero(NUMERO_ANTERIOR);
        when(contratoRepository.findByDocenteYNumero(ID_PERSONA, NUMERO_ANTERIOR))
                .thenReturn(Optional.of(contrato));
        when(documentoVinculacionRepository.save(any(DocumentoVinculacionEntity.class)))
                .thenAnswer(invocation -> {
                    DocumentoVinculacionEntity entity = invocation.getArgument(0);
                    entity.setId(70L);
                    return entity;
                });
        when(trabajadorRepository.existsById(ID_PERSONA)).thenReturn(true);
        when(trabajadorLaborRepository.save(any(TrabajadorLaborEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(trabajadorLaborUnidadRepository.save(any(TrabajadorLaborUnidadEntity.class)))
                .thenAnswer(invocation -> {
                    TrabajadorLaborUnidadEntity entity = invocation.getArgument(0);
                    entity.setId(80L);
                    return entity;
                });
        when(restriccionCargaRepository.findById(any()))
                .thenReturn(Optional.of(restriccionCarga()));
        when(estadoTrabajadorRepository.findEstadoInicialTrabajador())
                .thenReturn(Optional.of(estadoTrabajador()));

        Long idContrato = service.vincularDocentePorCargaDocente(CADO_ID, "2026-1", ID_LABOR);

        assertNotNull(idContrato);
        assertEquals(55L, idContrato);

        // La vinculacion reutiliza el contrato generado: no registra otro.
        verify(contratoRepository, never()).save(any(ContratoEntity.class));
        verify(documentoVinculacionRepository).save(any(DocumentoVinculacionEntity.class));
        verify(trabajadorLaborRepository).save(any(TrabajadorLaborEntity.class));
        verify(trabajadorLaborUnidadRepository).save(any(TrabajadorLaborUnidadEntity.class));
        verify(docenteUnidadRepository).saveAll(any());
    }

    @Test
    void debeExigirLaLaborDocenteParaVincularPorCargaDocente() {
        when(cargaDocenteRepository.findById(CADO_ID)).thenReturn(Optional.of(cargaDocente));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> service.vincularDocentePorCargaDocente(CADO_ID, "2026-1", null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(documentoVinculacionRepository, never()).save(any(DocumentoVinculacionEntity.class));
    }

    @Test
    void debeExigirContratoGeneradoParaVincularPorCargaDocente() {
        CargaDocenteEntity sinContrato = new CargaDocenteEntity();
        sinContrato.setId(CADO_ID);
        sinContrato.setIdPersonaGeneral(ID_PERSONA);
        when(cargaDocenteRepository.findById(CADO_ID)).thenReturn(Optional.of(sinContrato));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> service.vincularDocentePorCargaDocente(CADO_ID, "2026-1", 7L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("contrato generado"));
        verify(documentoVinculacionRepository, never()).save(any(DocumentoVinculacionEntity.class));
    }

    @Test
    void debeRechazarLaVinculacionDeUnDocenteYaVinculado() {
        cargaDocente.setContratacionVinculado("1");
        when(cargaDocenteRepository.findById(CADO_ID)).thenReturn(Optional.of(cargaDocente));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> service.vincularDocentePorCargaDocente(CADO_ID, "2026-1", 7L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("ya ha sido vinculado"));
    }

    private RestriccionCargaEntity restriccionCarga() {
        RestriccionCargaEntity restriccion = new RestriccionCargaEntity();
        restriccion.setIdDedicacionDocente(1L);
        restriccion.setIdClasificacionDocente(1L);
        return restriccion;
    }

    private EstadoTrabajadorEntity estadoTrabajador() {
        EstadoTrabajadorEntity estado = new EstadoTrabajadorEntity();
        estado.setId(1L);
        return estado;
    }

    @Test
    void debeConsultarElAnexoGeneradoDeLaCargaDocente() {
        ContratoEntity contrato = new ContratoEntity();
        contrato.setId(24L);
        contrato.setNumero(NUMERO_ANTERIOR);
        contrato.setFechaFirma(toDate("2026-11-18"));
        contrato.setIdFormaPago(3L);
        contrato.setIdTipoObjeto(5L);
        contrato.setIdFormaInicioContrato(1L);
        contrato.setObjeto("OBJETO DE PRUEBA");
        contrato.setDescripcion("DESCRIPCION DE PRUEBA");

        when(cargaDocenteRepository.findById(CADO_ID)).thenReturn(Optional.of(cargaDocente));
        when(contratoRepository.findByDocenteYNumero(ID_PERSONA, NUMERO_ANTERIOR))
                .thenReturn(Optional.of(contrato));

        var anexo = service.findAnexoGenerado(CADO_ID);

        assertNotNull(anexo);
        assertEquals("13", anexo.numeroAnexo());
        assertEquals(LocalDate.of(2026, 11, 18), anexo.fechaFirma());
        assertEquals(3L, anexo.idFormaPago());
        assertEquals(5L, anexo.idTipoObjeto());
        assertEquals(1L, anexo.idFormaInicioContrato());
        assertEquals("OBJETO DE PRUEBA", anexo.objeto());
        assertEquals("DESCRIPCION DE PRUEBA", anexo.descripcion());
    }
}
