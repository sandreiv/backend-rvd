/**
 * Aplicación: rvd
 * Archivo: NovedadCargaDocenteServiceImplTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/09/2026
 * Modificaciones:
 * 18/09/2026 - Sebastian Jaimes - Creación inicial
 * 18/09/2026 - Sebastian Jaimes - persiste montos de contratación en
 * la fotografía nueva
 * 18/09/2026 - Sebastian Jaimes - CARG_VALOR solo al aprobar novedad
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.CambioModalidadHoraCatedraticoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.DetalleCargaDocenteItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.FechasConvocatoriaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.CargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.DetalleNovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadCargaDocenteRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.NovedadRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.CargaBudgetService;

@ExtendWith(MockitoExtension.class)
class NovedadCargaDocenteServiceImplTest {

    private static final Long CADO_ID = 449L;
    private static final Long CARG_ID = 112L;
    private static final Long NOVE_ID = 6L;
    private static final Long MOCO_ID = 1L;
    private static final Long CACA_ID = 4L;
    private static final Long FECO_ID = 325L;
    private static final LocalDate INICIO = LocalDate.of(2026, 9, 8);
    private static final LocalDate FIN = LocalDate.of(2026, 11, 13);

    @Mock
    private CargaDocenteRepository cargaDocenteRepository;

    @Mock
    private NovedadCargaDocenteRepository novedadCargaDocenteRepository;

    @Mock
    private DetalleNovedadCargaDocenteRepository
            detalleNovedadCargaDocenteRepository;

    @Mock
    private NovedadRepository novedadRepository;

    @Mock
    private CargaBudgetService cargaBudgetService;

    @InjectMocks
    private NovedadCargaDocenteServiceImpl service;

    @Test
    void persistsInclusiveCatedraAmountsOnNewPhotograph() {
        stubCargaAndCatalog();
        when(novedadCargaDocenteRepository.countNoveltyInReview(CADO_ID))
                .thenReturn(0L);
        when(novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(CADO_ID))
                .thenReturn(Optional.empty());
        when(cargaBudgetService.computeInclusive(any()))
                .thenReturn(inclusiveCatedraValue());
        when(novedadCargaDocenteRepository
                .insertContractModalityFromCargaDocente(
                        eq(CADO_ID),
                        isNull(),
                        eq(MOCO_ID),
                        eq(CACA_ID),
                        eq(FECO_ID),
                        eq(NOVE_ID),
                        eq(INICIO),
                        eq(FIN),
                        eq(new BigDecimal("6400000.00")),
                        eq(new BigDecimal("1361066.66")),
                        eq(new BigDecimal("1230769.23")),
                        eq(new BigDecimal("7761066.66")),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        anyString()))
                .thenReturn(1);

        service.saveContractModalityProfessor(dto());

        verify(cargaBudgetService).computeInclusive(any());
        verify(cargaBudgetService, never()).compute(any());
        verify(cargaBudgetService)
                .assertNotExceedsAuthorized(eq(CARG_ID), any());
        verify(cargaBudgetService, never()).refreshCargValor(any());
        verify(novedadCargaDocenteRepository)
                .insertContractModalityFromCargaDocente(
                        eq(CADO_ID),
                        isNull(),
                        eq(MOCO_ID),
                        eq(CACA_ID),
                        eq(FECO_ID),
                        eq(NOVE_ID),
                        eq(INICIO),
                        eq(FIN),
                        eq(new BigDecimal("6400000.00")),
                        eq(new BigDecimal("1361066.66")),
                        eq(new BigDecimal("1230769.23")),
                        eq(new BigDecimal("7761066.66")),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        anyString());
    }

    @Test
    void insertsFromCargaDocenteWhenNoPreviousPhotograph() {
        stubHappyPath();
        when(novedadCargaDocenteRepository.countNoveltyInReview(CADO_ID))
                .thenReturn(0L);
        when(novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(CADO_ID))
                .thenReturn(Optional.empty());
        when(novedadCargaDocenteRepository
                .insertContractModalityFromCargaDocente(
                        eq(CADO_ID),
                        isNull(),
                        eq(MOCO_ID),
                        eq(CACA_ID),
                        eq(FECO_ID),
                        eq(NOVE_ID),
                        eq(INICIO),
                        eq(FIN),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        anyString()))
                .thenReturn(1);

        service.saveContractModalityProfessor(dto());

        verify(novedadCargaDocenteRepository)
                .insertContractModalityFromCargaDocente(
                        eq(CADO_ID),
                        isNull(),
                        eq(MOCO_ID),
                        eq(CACA_ID),
                        eq(FECO_ID),
                        eq(NOVE_ID),
                        eq(INICIO),
                        eq(FIN),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        anyString());
        verify(novedadCargaDocenteRepository, never())
                .insertContractModalityFromNovelty(
                        any(), any(), any(), any(), any(), any(),
                        any(), any(), any(), any(), any(), any(),
                        any(), any(), any(), any(), any(), any(),
                        any(), any());
        verify(novedadCargaDocenteRepository, never()).save(any());
        verify(cargaDocenteRepository, never()).save(any());
        verify(detalleNovedadCargaDocenteRepository, never())
                .deleteByProcedure(any(), any());
        verify(detalleNovedadCargaDocenteRepository).save(any());
        verify(cargaBudgetService)
                .assertNotExceedsAuthorized(eq(CARG_ID), any());
        verify(cargaBudgetService, never()).refreshCargValor(any());
        verify(cargaBudgetService, never())
                .refreshPreassignmentTotals(any());
    }

    @Test
    void insertsFromNoveltyWhenPreviousPhotographExists() {
        stubHappyPath();
        NovedadCargaDocenteEntity previous = new NovedadCargaDocenteEntity();
        previous.setIdCargaDocente(CADO_ID);
        previous.setEstadoNovedad("1");
        when(novedadCargaDocenteRepository.countNoveltyInReview(CADO_ID))
                .thenReturn(0L);
        when(novedadCargaDocenteRepository
                .findProfessorRecordToDuplicateNovelty(CADO_ID))
                .thenReturn(Optional.of(previous));
        when(novedadCargaDocenteRepository
                .insertContractModalityFromNovelty(
                        eq(CADO_ID),
                        isNull(),
                        eq(MOCO_ID),
                        eq(CACA_ID),
                        eq(FECO_ID),
                        eq(NOVE_ID),
                        eq(INICIO),
                        eq(FIN),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        anyString()))
                .thenReturn(1);

        service.saveContractModalityProfessor(dto());

        verify(novedadCargaDocenteRepository)
                .insertContractModalityFromNovelty(
                        eq(CADO_ID),
                        isNull(),
                        eq(MOCO_ID),
                        eq(CACA_ID),
                        eq(FECO_ID),
                        eq(NOVE_ID),
                        eq(INICIO),
                        eq(FIN),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        anyString());
        verify(novedadCargaDocenteRepository, never())
                .insertContractModalityFromCargaDocente(
                        any(), any(), any(), any(), any(), any(),
                        any(), any(), any(), any(), any(), any(),
                        any(), any(), any(), any(), any(), any(),
                        any(), any());
        verify(novedadCargaDocenteRepository, never()).save(any());
        verify(detalleNovedadCargaDocenteRepository, never())
                .deleteByProcedure(any(), any());
    }

    @Test
    void rejectsWhenNoveltyAlreadyInReview() {
        stubCargaAndCatalog();
        when(novedadCargaDocenteRepository.countNoveltyInReview(CADO_ID))
                .thenReturn(1L);

        ApiException ex = assertThrows(
                ApiException.class,
                () -> service.saveContractModalityProfessor(dto()));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(novedadCargaDocenteRepository, never())
                .insertContractModalityFromCargaDocente(
                        any(), any(), any(), any(), any(), any(),
                        any(), any(), any(), any(), any(), any(),
                        any(), any(), any(), any(), any(), any(),
                        any(), any());
        verify(novedadCargaDocenteRepository, never()).save(any());
    }

    @Test
    void approveInReviewNoveltyRefreshesCargValorOnly() {
        stubCargaDocente();
        when(novedadCargaDocenteRepository.countNoveltyInReview(CADO_ID))
                .thenReturn(1L);
        when(novedadCargaDocenteRepository.updateEstadoNovedadInReview(
                eq(CADO_ID),
                anyString()))
                .thenReturn(1);

        service.approveProfessorNovelty(CADO_ID);

        verify(novedadCargaDocenteRepository)
                .updateEstadoNovedadInReview(eq(CADO_ID), anyString());
        verify(cargaBudgetService).refreshCargValor(CARG_ID);
        verify(cargaBudgetService, never())
                .refreshPreassignmentTotals(any());
    }

    @Test
    void approveWithoutNoveltyInReviewThrowsConflict() {
        stubCargaDocente();
        when(novedadCargaDocenteRepository.countNoveltyInReview(CADO_ID))
                .thenReturn(0L);

        ApiException ex = assertThrows(
                ApiException.class,
                () -> service.approveProfessorNovelty(CADO_ID));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(cargaBudgetService, never()).refreshCargValor(any());
    }

    private void stubHappyPath() {
        stubCargaAndCatalog();
        when(cargaBudgetService.computeInclusive(any()))
                .thenReturn(inclusiveCatedraValue());
    }

    private void stubCargaAndCatalog() {
        stubCargaDocente();
        NovedadEntity novedad = new NovedadEntity();
        novedad.setId(NOVE_ID);
        novedad.setComponente("change-contract-modality");
        when(novedadRepository.findById(NOVE_ID))
                .thenReturn(Optional.of(novedad));
    }

    private void stubCargaDocente() {
        CargaDocenteEntity cargaDocente = new CargaDocenteEntity();
        cargaDocente.setId(CADO_ID);
        cargaDocente.setIdCarga(CARG_ID);
        when(cargaDocenteRepository.findById(CADO_ID))
                .thenReturn(Optional.of(cargaDocente));
    }

    private CambioModalidadHoraCatedraticoDTO dto() {
        return new CambioModalidadHoraCatedraticoDTO(
                CADO_ID,
                CARG_ID,
                null,
                MOCO_ID,
                CACA_ID,
                NOVE_ID,
                new FechasConvocatoriaFormularioDTO(
                        FECO_ID,
                        "P1",
                        INICIO,
                        FIN),
                "10",
                "8",
                null,
                new BigDecimal("83734"),
                null,
                new BigDecimal("23924"),
                null,
                null,
                null,
                null,
                null,
                List.of(detalle()));
    }

    private DetalleCargaDocenteItemDTO detalle() {
        return new DetalleCargaDocenteItemDTO(
                2L,
                null,
                "FAD",
                8L,
                null,
                5L,
                null,
                10L,
                1L,
                null);
    }

    private ValorContratacionDTO inclusiveCatedraValue() {
        return new ValorContratacionDTO(
                new BigDecimal("266666.67"),
                new BigDecimal("533333.33"),
                new BigDecimal("27733.33"),
                new BigDecimal("533333.33"),
                new BigDecimal("1361066.66"),
                new BigDecimal("6400000.00"),
                new BigDecimal("7761066.66"),
                new BigDecimal("1230769.23"));
    }
}
