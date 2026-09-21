/**
 * Aplicación: rvd
 * Archivo: CargaBudgetServiceImplTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/09/2026
 * Modificaciones:
 * 18/09/2026 - Sebastian Jaimes - Creación inicial
 * 18/09/2026 - Sebastian Jaimes - fromNovelty prefiere NOCD_HORAS
 * 18/09/2026 - Sebastian Jaimes - computeInclusive de cátedra con
 * prestaciones y PSM
 * 18/09/2026 - Sebastian Jaimes - sin novedad el budget iguala
 * totalContrato de contratación
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetOverlay;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleNovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.ModalidadContratacionEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.RestriccionCargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleNovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.ModalidadContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionCargaRepository;

@ExtendWith(MockitoExtension.class)
class CargaBudgetServiceImplTest {

    private static final Long ID_CARGA = 10L;
    private static final Long ID_TCO = 1L;
    private static final Long ID_CATEDRA = 2L;
    private static final Long ID_PLANTA = 3L;
    private static final Long CADO_TCO = 101L;
    private static final Long CADO_CATEDRA = 102L;
    private static final Long CADO_PLANTA = 103L;
    private static final LocalDate INICIO = LocalDate.of(2026, 6, 8);
    private static final LocalDate FIN = LocalDate.of(2026, 11, 10);
    private static final BigDecimal SALARIO_TCO = new BigDecimal("2631640");
    private static final BigDecimal TOTAL_CATEDRA = new BigDecimal("6400000.00");

    @Mock
    private CargaRepository cargaRepository;

    @Mock
    private CargaDocenteRepository cargaDocenteRepository;

    @Mock
    private NovedadCargaDocenteRepository novedadCargaDocenteRepository;

    @Mock
    private DetalleCargaDocenteRepository detalleCargaDocenteRepository;

    @Mock
    private DetalleNovedadCargaDocenteRepository
            detalleNovedadCargaDocenteRepository;

    @Mock
    private ModalidadContratacionRepository modalidadContratacionRepository;

    @Mock
    private RestriccionCargaRepository restriccionCargaRepository;

    @InjectMocks
    private CargaBudgetServiceImpl service;

    @BeforeEach
    void stubModalities() {
        lenient().when(modalidadContratacionRepository.findById(ID_TCO))
                .thenReturn(Optional.of(modality(
                        ID_TCO,
                        "Tiempo Completo Ocasional",
                        "TCO")));
        lenient().when(modalidadContratacionRepository.findById(ID_CATEDRA))
                .thenReturn(Optional.of(modality(
                        ID_CATEDRA,
                        "Hora Catedra",
                        "HC")));
        lenient().when(modalidadContratacionRepository.findById(ID_PLANTA))
                .thenReturn(Optional.of(modality(
                        ID_PLANTA,
                        "Docente de Planta",
                        "PLANTA")));
        lenient().when(restriccionCargaRepository.findById(ID_CATEDRA))
                .thenReturn(Optional.of(restriccion("CATEDRA")));
        lenient().when(restriccionCargaRepository.findById(ID_TCO))
                .thenReturn(Optional.of(restriccion("SALARIO")));
        lenient().when(restriccionCargaRepository.findById(ID_PLANTA))
                .thenReturn(Optional.empty());
    }

    @Test
    void sumsMixedCargaDocenteAndEffectiveNovelty() {
        stubCarga(new BigDecimal("30000000"));
        CargaDocenteEntity tco = tcoDocente();
        CargaDocenteEntity catedraBase = catedraDocente();
        when(cargaDocenteRepository.findByIdCarga(ID_CARGA))
                .thenReturn(List.of(tco, catedraBase));
        when(novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(CADO_TCO))
                .thenReturn(Optional.empty());
        when(novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(CADO_CATEDRA))
                .thenReturn(Optional.of(catedraNovelty()));
        when(detalleNovedadCargaDocenteRepository
                .findByIdNovedadCargaDocente(CADO_CATEDRA))
                .thenReturn(List.of(novedadDetalle("8")));

        CargaBudgetDTO budget = service.getBudget(ID_CARGA);

        assertMoney("24355837.61", budget.valorCarga());
        assertEquals(2, budget.docentes().size());
        assertMoney("16594770.95", budget.docentes().get(0).totalContrato());
        assertMoney("7761066.66", budget.docentes().get(1).totalContrato());
    }

    @Test
    void withoutNoveltyCatedraMatchesInclusiveContractTotal() {
        stubCarga(new BigDecimal("30000000"));
        CargaDocenteEntity catedra = catedraDocente();
        catedra.setHoras("8");
        when(cargaDocenteRepository.findByIdCarga(ID_CARGA))
                .thenReturn(List.of(catedra));
        when(novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(CADO_CATEDRA))
                .thenReturn(Optional.empty());

        CargaBudgetDTO budget = service.getBudget(ID_CARGA);

        assertMoney("7761066.66", budget.valorCarga());
    }

    @Test
    void fromNoveltyPrefersNocdHorasOverHistoricalDetails() {
        stubCarga(new BigDecimal("30000000"));
        NovedadCargaDocenteEntity novelty = catedraNovelty();
        novelty.setHoras("8");
        when(cargaDocenteRepository.findByIdCarga(ID_CARGA))
                .thenReturn(List.of(catedraDocente()));
        when(novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(CADO_CATEDRA))
                .thenReturn(Optional.of(novelty));

        CargaBudgetDTO budget = service.getBudget(ID_CARGA);

        assertMoney("7761066.66", budget.valorCarga());
    }

    @Test
    void computeKeepsCatedraBudgetWithoutBenefits() {
        ValorContratacionDTO valor = service.compute(
                overlayCatedra(CADO_TCO, "8"));

        assertMoney("6400000.00", valor.valorContrato());
        assertMoney("0.00", valor.totalPrestaciones());
        assertMoney("6400000.00", valor.totalContrato());
        assertMoney("0.00", valor.salario());
    }

    @Test
    void computeInclusiveCatedraIncludesBenefitsAndPsm() {
        ValorContratacionDTO valor = service.computeInclusive(
                overlayCatedra(CADO_TCO, "8"));

        assertMoney("6400000.00", valor.valorContrato());
        assertMoney("1361066.66", valor.totalPrestaciones());
        assertMoney("7761066.66", valor.totalContrato());
        assertMoney("1230769.23", valor.salario());
    }

    @Test
    void computeInclusiveTcoUsesInclusiveDaysAndSalary() {
        ValorContratacionDTO valor = service.computeInclusive(
                overlayTco(CADO_TCO));

        assertMoney("13684528.00", valor.valorContrato());
        assertMoney("2910242.95", valor.totalPrestaciones());
        assertMoney("16594770.95", valor.totalContrato());
        assertMoney("2631640.00", valor.salario());
    }

    @Test
    void computeInclusivePlantaIsZero() {
        ValorContratacionDTO valor = service.computeInclusive(
                overlayPlanta(CADO_PLANTA));

        assertMoney("0.00", valor.valorContrato());
        assertMoney("0.00", valor.totalPrestaciones());
        assertMoney("0.00", valor.totalContrato());
        assertMoney("0.00", valor.salario());
    }

    @Test
    void overlayFromTcoToCatedraLowersProjectedTotal() {
        when(cargaDocenteRepository.findByIdCarga(ID_CARGA))
                .thenReturn(List.of(tcoDocente()));

        BigDecimal preview = service.preview(
                ID_CARGA,
                overlayCatedra(CADO_TCO, "8"));

        assertMoney("7761066.66", preview);
    }

    @Test
    void overlayThatExceedsAuthorizedThrowsBadRequest() {
        stubCarga(new BigDecimal("1000000"));
        when(cargaDocenteRepository.findByIdCarga(ID_CARGA))
                .thenReturn(List.of(tcoDocente()));

        ApiException ex = assertThrows(
                ApiException.class,
                () -> service.assertNotExceedsAuthorized(
                        ID_CARGA,
                        overlayCatedra(CADO_TCO, "8")));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void plantaEffectiveTotalIsZero() {
        stubCarga(new BigDecimal("30000000"));
        CargaDocenteEntity planta = new CargaDocenteEntity();
        planta.setId(CADO_PLANTA);
        planta.setIdCarga(ID_CARGA);
        planta.setIdModalidadContratacion(ID_PLANTA);
        planta.setSalario(SALARIO_TCO);
        planta.setFechaInicio(INICIO);
        planta.setFechaFin(FIN);
        when(cargaDocenteRepository.findByIdCarga(ID_CARGA))
                .thenReturn(List.of(planta));
        when(novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(CADO_PLANTA))
                .thenReturn(Optional.empty());

        CargaBudgetDTO budget = service.getBudget(ID_CARGA);

        assertMoney("0.00", budget.valorCarga());
        assertMoney("0.00", budget.docentes().get(0).totalContrato());
    }

    @Test
    void refreshWritesSumIntoCargValor() {
        CargaEntity carga = stubCarga(new BigDecimal("30000000"));
        when(cargaDocenteRepository.findByIdCarga(ID_CARGA))
                .thenReturn(List.of(tcoDocente()));
        when(novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(CADO_TCO))
                .thenReturn(Optional.empty());

        service.refreshCargValor(ID_CARGA);

        assertMoney("16594770.95", carga.getValor());
        assertMoney("30000000", carga.getValorAutorizado());
    }

    @Test
    void refreshPreassignmentWritesValorAndAutorizado() {
        CargaEntity carga = stubCarga(new BigDecimal("100"));
        when(cargaDocenteRepository.findByIdCarga(ID_CARGA))
                .thenReturn(List.of(tcoDocente()));

        service.refreshPreassignmentTotals(ID_CARGA);

        assertMoney("16594770.95", carga.getValor());
        assertMoney("16594770.95", carga.getValorAutorizado());
    }

    private CargaEntity stubCarga(BigDecimal autorizado) {
        CargaEntity carga = new CargaEntity();
        carga.setId(ID_CARGA);
        carga.setValorAutorizado(autorizado);
        when(cargaRepository.findById(ID_CARGA))
                .thenReturn(Optional.of(carga));
        return carga;
    }

    private CargaDocenteEntity tcoDocente() {
        CargaDocenteEntity entity = new CargaDocenteEntity();
        entity.setId(CADO_TCO);
        entity.setIdCarga(ID_CARGA);
        entity.setIdModalidadContratacion(ID_TCO);
        entity.setSalario(SALARIO_TCO);
        entity.setFechaInicio(INICIO);
        entity.setFechaFin(FIN);
        return entity;
    }

    private CargaDocenteEntity catedraDocente() {
        CargaDocenteEntity entity = new CargaDocenteEntity();
        entity.setId(CADO_CATEDRA);
        entity.setIdCarga(ID_CARGA);
        entity.setIdModalidadContratacion(ID_CATEDRA);
        entity.setValorHora(new BigDecimal("50000"));
        entity.setSemanas("16");
        entity.setFechaInicio(INICIO);
        entity.setFechaFin(FIN);
        return entity;
    }

    private NovedadCargaDocenteEntity catedraNovelty() {
        NovedadCargaDocenteEntity entity = new NovedadCargaDocenteEntity();
        entity.setIdCargaDocente(CADO_CATEDRA);
        entity.setIdModalidadContratacion(ID_CATEDRA);
        entity.setValorHora(new BigDecimal("50000"));
        entity.setSemanas("16");
        entity.setFechaInicio(INICIO);
        entity.setFechaFin(FIN);
        entity.setEstadoNovedad("0");
        return entity;
    }

    private DetalleNovedadCargaDocenteEntity novedadDetalle(String horas) {
        DetalleNovedadCargaDocenteEntity detalle =
                new DetalleNovedadCargaDocenteEntity();
        detalle.setHoras(horas);
        return detalle;
    }

    private CargaBudgetOverlay overlayCatedra(
            Long idCargaDocente,
            String horas) {
        return new CargaBudgetOverlay(
                idCargaDocente,
                ID_CATEDRA,
                INICIO,
                FIN,
                null,
                new BigDecimal("50000"),
                new BigDecimal("16"),
                new BigDecimal(horas),
                null,
                null);
    }

    private CargaBudgetOverlay overlayTco(Long idCargaDocente) {
        return new CargaBudgetOverlay(
                idCargaDocente,
                ID_TCO,
                INICIO,
                FIN,
                SALARIO_TCO,
                null,
                null,
                null,
                null,
                null);
    }

    private CargaBudgetOverlay overlayPlanta(Long idCargaDocente) {
        return new CargaBudgetOverlay(
                idCargaDocente,
                ID_PLANTA,
                INICIO,
                FIN,
                SALARIO_TCO,
                null,
                null,
                null,
                null,
                null);
    }

    private ModalidadContratacionEntity modality(
            Long id,
            String nombre,
            String sigla) {
        ModalidadContratacionEntity entity = new ModalidadContratacionEntity();
        entity.setId(id);
        entity.setNombre(nombre);
        entity.setSigla(sigla);
        return entity;
    }

    private RestriccionCargaEntity restriccion(String formaPago) {
        RestriccionCargaEntity entity = new RestriccionCargaEntity();
        entity.setFormaPago(formaPago);
        return entity;
    }

    private void assertMoney(String expected, BigDecimal actual) {
        assertEquals(
                0,
                new BigDecimal(expected).compareTo(actual),
                () -> "esperado " + expected + " pero fue " + actual);
    }
}
