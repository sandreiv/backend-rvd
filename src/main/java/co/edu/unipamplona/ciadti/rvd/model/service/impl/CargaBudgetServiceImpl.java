/**
 * Aplicación: rvd
 * Archivo: CargaBudgetServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/09/2026
 * Modificaciones:
 * 18/09/2026 - Sebastian Jaimes - Creación inicial
 * 18/09/2026 - Sebastian Jaimes - fromNovelty usa NOCD_HORAS para no
 * duplicar horas de detalles históricos
 * 18/09/2026 - Sebastian Jaimes - computeInclusive usa fórmula de
 * contratación; compute queda para el tope
 * 18/09/2026 - Sebastian Jaimes - GET y CARG_VALOR usan contratación;
 * autorizado solo en preasignación
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetDocenteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.CargaBudgetOverlay;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.CargaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.DetalleCargaDocenteEntity;
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
import co.edu.unipamplona.ciadti.rvd.model.service.CargaBudgetService;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils;
import co.edu.unipamplona.ciadti.rvd.util.RegistradoPorUtils.Accion;
import co.edu.unipamplona.ciadti.rvd.util.ValorContratacionCalculator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CargaBudgetServiceImpl implements CargaBudgetService {

    private static final int ESCALA_MONETARIA = 2;

    private enum BudgetKind {
        PLANTA,
        CATEDRA,
        TCO
    }

    private record BudgetSnapshot(
            Long idModalidadContratacion,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            BigDecimal asignacionSalarial,
            BigDecimal valorHora,
            BigDecimal semanas,
            BigDecimal horasActividades,
            String puntos,
            BigDecimal valorPunto) {
    }

    private final CargaRepository cargaRepository;
    private final CargaDocenteRepository cargaDocenteRepository;
    private final NovedadCargaDocenteRepository novedadCargaDocenteRepository;
    private final DetalleCargaDocenteRepository detalleCargaDocenteRepository;
    private final DetalleNovedadCargaDocenteRepository
            detalleNovedadCargaDocenteRepository;
    private final ModalidadContratacionRepository
            modalidadContratacionRepository;
    private final RestriccionCargaRepository restriccionCargaRepository;

    @Override
    @Transactional(readOnly = true)
    public CargaBudgetDTO getBudget(Long idCarga) {
        CargaEntity carga = findCarga(idCarga);
        List<CargaBudgetDocenteDTO> docentes = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        List<CargaDocenteEntity> rows =
                cargaDocenteRepository.findByIdCarga(idCarga);
        int index = 0;
        while (index < rows.size()) {
            CargaDocenteEntity docente = rows.get(index);
            BigDecimal monto = totalContratoOf(docente, null);
            docentes.add(new CargaBudgetDocenteDTO(docente.getId(), monto));
            total = total.add(monto);
            index++;
        }
        return new CargaBudgetDTO(
                scale(total),
                carga.getValorAutorizado(),
                docentes);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal preview(Long idCarga, CargaBudgetOverlay overlay) {
        return scale(sumEffective(idCarga, overlay));
    }

    @Override
    @Transactional(readOnly = true)
    public ValorContratacionDTO compute(CargaBudgetOverlay overlay) {
        if (overlay == null) {
            return ValorContratacionCalculator.zero();
        }
        return calculate(toSnapshot(overlay));
    }

    @Override
    @Transactional(readOnly = true)
    public ValorContratacionDTO computeInclusive(CargaBudgetOverlay overlay) {
        if (overlay == null) {
            return ValorContratacionCalculator.zero();
        }
        return calculateInclusive(toSnapshot(overlay));
    }

    @Override
    @Transactional(readOnly = true)
    public void assertNotExceedsAuthorized(
            Long idCarga,
            CargaBudgetOverlay overlay) {
        CargaEntity carga = findCarga(idCarga);
        BigDecimal autorizado = carga.getValorAutorizado();
        if (autorizado == null) {
            return;
        }
        BigDecimal proyectado = preview(idCarga, overlay);
        if (proyectado.compareTo(autorizado) > 0) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "El valor proyectado de la carga supera el valor autorizado");
        }
    }

    @Override
    @Transactional
    public void refreshCargValor(Long idCarga) {
        CargaEntity carga = findCarga(idCarga);
        carga.setValor(scale(sumEffective(idCarga, null)));
        carga.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        carga.setFechaCambio(new Date());
        cargaRepository.save(carga);
        log.info(
                "refreshCargValor ===> CARG_VALOR actualizado. idCarga={}, valor={}",
                idCarga,
                carga.getValor());
    }

    @Override
    @Transactional
    public void refreshPreassignmentTotals(Long idCarga) {
        CargaEntity carga = findCarga(idCarga);
        BigDecimal total = scale(sumPreassignment(idCarga));
        carga.setValor(total);
        carga.setValorAutorizado(total);
        carga.setRegistradoPor(RegistradoPorUtils.value(Accion.UPDATE));
        carga.setFechaCambio(new Date());
        cargaRepository.save(carga);
        log.info(
                "refreshPreassignmentTotals ===> CARG_VALOR y autorizado. idCarga={}, valor={}",
                idCarga,
                total);
    }

    private BigDecimal sumEffective(
            Long idCarga,
            CargaBudgetOverlay overlay) {
        BigDecimal total = BigDecimal.ZERO;
        List<CargaDocenteEntity> rows =
                cargaDocenteRepository.findByIdCarga(idCarga);
        int index = 0;
        while (index < rows.size()) {
            total = total.add(totalContratoOf(rows.get(index), overlay));
            index++;
        }
        return total;
    }

    private BigDecimal sumPreassignment(Long idCarga) {
        BigDecimal total = BigDecimal.ZERO;
        List<CargaDocenteEntity> rows =
                cargaDocenteRepository.findByIdCarga(idCarga);
        int index = 0;
        while (index < rows.size()) {
            total = total.add(
                    calculateInclusive(
                            fromCargaDocente(rows.get(index)))
                            .totalContrato());
            index++;
        }
        return total;
    }

    private BigDecimal totalContratoOf(
            CargaDocenteEntity docente,
            CargaBudgetOverlay overlay) {
        if (isOverlayFor(docente, overlay)) {
            return computeInclusive(overlay).totalContrato();
        }
        return calculateInclusive(effectiveSnapshot(docente)).totalContrato();
    }

    private boolean isOverlayFor(
            CargaDocenteEntity docente,
            CargaBudgetOverlay overlay) {
        return overlay != null
                && overlay.idCargaDocente() != null
                && overlay.idCargaDocente().equals(docente.getId());
    }

    private BudgetSnapshot effectiveSnapshot(CargaDocenteEntity docente) {
        Optional<NovedadCargaDocenteEntity> novelty =
                novedadCargaDocenteRepository
                        .findProfessorRecordToDuplicateNovelty(
                                docente.getId());
        if (novelty.isPresent()) {
            return fromNovelty(novelty.get());
        }
        return fromCargaDocente(docente);
    }

    private BudgetSnapshot fromNovelty(NovedadCargaDocenteEntity novelty) {
        BigDecimal horas = parseDecimal(novelty.getHoras());
        if (horas == null || horas.compareTo(BigDecimal.ZERO) <= 0) {
            horas = sumNovedadHours(novelty.getIdCargaDocente());
        }
        return new BudgetSnapshot(
                novelty.getIdModalidadContratacion(),
                novelty.getFechaInicio(),
                novelty.getFechaFin(),
                novelty.getSalario(),
                novelty.getValorHora(),
                parseDecimal(novelty.getSemanas()),
                horas,
                novelty.getPuntos(),
                novelty.getValorPunto());
    }

    private BudgetSnapshot fromCargaDocente(CargaDocenteEntity docente) {
        BigDecimal horas = parseDecimal(docente.getHoras());
        if (horas == null || horas.compareTo(BigDecimal.ZERO) <= 0) {
            horas = sumCargaHours(docente.getId());
        }
        return new BudgetSnapshot(
                docente.getIdModalidadContratacion(),
                docente.getFechaInicio(),
                docente.getFechaFin(),
                docente.getSalario(),
                docente.getValorHora(),
                parseDecimal(docente.getSemanas()),
                horas,
                docente.getPuntos(),
                docente.getValorPunto());
    }

    private BudgetSnapshot toSnapshot(CargaBudgetOverlay overlay) {
        return new BudgetSnapshot(
                overlay.idModalidadContratacion(),
                overlay.fechaInicio(),
                overlay.fechaFin(),
                overlay.asignacionSalarial(),
                overlay.valorHora(),
                overlay.semanas(),
                overlay.horasActividades(),
                overlay.puntos(),
                overlay.valorPunto());
    }

    private ValorContratacionDTO calculate(BudgetSnapshot snapshot) {
        BudgetKind kind = resolveKind(snapshot.idModalidadContratacion());
        if (kind == BudgetKind.PLANTA) {
            return ValorContratacionCalculator.zero();
        }
        if (kind == BudgetKind.CATEDRA) {
            return calculateCatedraBudget(snapshot);
        }
        return calculateTco(snapshot);
    }

    private ValorContratacionDTO calculateInclusive(BudgetSnapshot snapshot) {
        BudgetKind kind = resolveKind(snapshot.idModalidadContratacion());
        if (kind == BudgetKind.PLANTA) {
            return ValorContratacionCalculator.zero();
        }
        if (kind == BudgetKind.CATEDRA) {
            return calculateCatedraInclusive(snapshot);
        }
        return calculateTco(snapshot);
    }

    private ValorContratacionDTO calculateCatedraBudget(BudgetSnapshot snapshot) {
        if (snapshot.horasActividades() == null
                || snapshot.semanas() == null
                || snapshot.valorHora() == null) {
            return ValorContratacionCalculator.zero();
        }
        return ValorContratacionCalculator.calculateCatedraFromActivities(
                snapshot.horasActividades(),
                snapshot.semanas(),
                snapshot.valorHora());
    }

    private ValorContratacionDTO calculateCatedraInclusive(
            BudgetSnapshot snapshot) {
        if (snapshot.horasActividades() == null
                || snapshot.semanas() == null
                || snapshot.valorHora() == null
                || snapshot.fechaInicio() == null
                || snapshot.fechaFin() == null
                || snapshot.horasActividades()
                        .compareTo(BigDecimal.ZERO) <= 0) {
            return ValorContratacionCalculator.zero();
        }
        return ValorContratacionCalculator.calculateCatedra(
                snapshot.horasActividades(),
                snapshot.semanas(),
                snapshot.valorHora(),
                snapshot.fechaInicio(),
                snapshot.fechaFin());
    }

    private ValorContratacionDTO calculateTco(BudgetSnapshot snapshot) {
        BigDecimal asignacion = resolveAsignacion(snapshot);
        if (asignacion == null
                || snapshot.fechaInicio() == null
                || snapshot.fechaFin() == null) {
            return ValorContratacionCalculator.zero();
        }
        return ValorContratacionCalculator.calculate(
                asignacion,
                snapshot.fechaInicio(),
                snapshot.fechaFin());
    }

    private BigDecimal resolveAsignacion(BudgetSnapshot snapshot) {
        if (snapshot.asignacionSalarial() != null) {
            return snapshot.asignacionSalarial();
        }
        if (snapshot.valorPunto() == null
                || !StringUtils.hasText(snapshot.puntos())) {
            return null;
        }
        BigDecimal puntos = parseDecimal(snapshot.puntos());
        if (puntos == null) {
            return null;
        }
        return snapshot.valorPunto().multiply(puntos);
    }

    private BudgetKind resolveKind(Long idModalidad) {
        if (isPlanta(idModalidad)) {
            return BudgetKind.PLANTA;
        }
        if (isCatedra(idModalidad)) {
            return BudgetKind.CATEDRA;
        }
        return BudgetKind.TCO;
    }

    private boolean isPlanta(Long idModalidad) {
        if (idModalidad == null) {
            return false;
        }
        return modalidadContratacionRepository.findById(idModalidad)
                .map(this::isPlantaModalidad)
                .orElse(false);
    }

    private boolean isPlantaModalidad(ModalidadContratacionEntity modalidad) {
        return ValorContratacionCalculator.isPlanta(
                modalidad.getNombre(),
                modalidad.getSigla());
    }

    private boolean isCatedra(Long idModalidad) {
        if (idModalidad == null) {
            return false;
        }
        String formaPago = restriccionCargaRepository.findById(idModalidad)
                .map(RestriccionCargaEntity::getFormaPago)
                .orElse(null);
        return ValorContratacionCalculator.isCatedra(formaPago);
    }

    private BigDecimal sumCargaHours(Long idCargaDocente) {
        List<DetalleCargaDocenteEntity> detalles =
                detalleCargaDocenteRepository
                        .findAllByIdCargaDocente(idCargaDocente);
        BigDecimal total = BigDecimal.ZERO;
        int index = 0;
        while (index < detalles.size()) {
            total = total.add(parseDecimalOrZero(detalles.get(index).getHoras()));
            index++;
        }
        return total;
    }

    private BigDecimal sumNovedadHours(Long idCargaDocente) {
        List<DetalleNovedadCargaDocenteEntity> detalles =
                detalleNovedadCargaDocenteRepository
                        .findByIdNovedadCargaDocente(idCargaDocente);
        BigDecimal total = BigDecimal.ZERO;
        int index = 0;
        while (index < detalles.size()) {
            total = total.add(parseDecimalOrZero(
                    detalles.get(index).getHoras()));
            index++;
        }
        return total;
    }

    private CargaEntity findCarga(Long idCarga) {
        if (idCarga == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga es obligatoria");
        }
        return cargaRepository.findById(idCarga)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carga con id " + idCarga));
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

    private BigDecimal parseDecimalOrZero(String value) {
        BigDecimal parsed = parseDecimal(value);
        return parsed == null ? BigDecimal.ZERO : parsed;
    }

    private BigDecimal scale(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(ESCALA_MONETARIA);
        }
        return value.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
    }
}
