/**
 * Aplicación: rvd
 * Archivo: ValorContratacionCalculator.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.util
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/09/2026
 * Modificaciones:
 * 17/09/2026 - Sebastian Jaimes - Creación inicial
 * 18/09/2026 - Sebastian Jaimes - Fórmula de contratación para cátedra
 * 18/09/2026 - Sebastian Jaimes - Cátedra de presupuesto y planta en cero
 */
package co.edu.unipamplona.ciadti.rvd.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;

public final class ValorContratacionCalculator {

    private static final int ESCALA_MONETARIA = 2;
    private static final BigDecimal CERO = BigDecimal.ZERO.setScale(
            ESCALA_MONETARIA);
    private static final BigDecimal DIAS_MES = new BigDecimal("30");
    private static final BigDecimal DIAS_ANIO = new BigDecimal("360");
    private static final BigDecimal DIAS_VACACIONES = new BigDecimal("720");
    private static final BigDecimal TASA_INTERES = new BigDecimal("0.12");

    private ValorContratacionCalculator() {
    }

    public static boolean isCatedra(String formaPago) {
        return formaPago != null
                && "CATEDRA".equalsIgnoreCase(formaPago.trim());
    }

    public static long countInclusiveDays(
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene fechas de inicio y fin");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha fin no puede ser anterior a la fecha inicio");
        }
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

    public static ValorContratacionDTO calculate(
            BigDecimal asignacionSalarial,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        if (asignacionSalarial == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene asignacion salarial");
        }
        BigDecimal dias = BigDecimal.valueOf(
                countInclusiveDays(fechaInicio, fechaFin));

        BigDecimal valorContrato = asignacionSalarial
                .divide(DIAS_MES, 8, RoundingMode.HALF_UP)
                .multiply(dias)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal valorCesantias = asignacionSalarial
                .multiply(dias)
                .divide(DIAS_ANIO, ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal valorIntereses = valorCesantias
                .multiply(dias)
                .divide(DIAS_ANIO, 8, RoundingMode.HALF_UP)
                .multiply(TASA_INTERES)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal valorPrimaLegal = valorCesantias;
        BigDecimal valorVacaciones = asignacionSalarial
                .multiply(dias)
                .divide(DIAS_VACACIONES, ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal totalPrestaciones = valorCesantias
                .add(valorIntereses)
                .add(valorPrimaLegal)
                .add(valorVacaciones)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal totalContrato = valorContrato
                .add(totalPrestaciones)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        return new ValorContratacionDTO(
                valorVacaciones,
                valorCesantias,
                valorIntereses,
                valorPrimaLegal,
                totalPrestaciones,
                valorContrato,
                totalContrato,
                asignacionSalarial.setScale(
                        ESCALA_MONETARIA, RoundingMode.HALF_UP));
    }

    public static ValorContratacionDTO calculateCatedra(
            BigDecimal horasSemanales,
            BigDecimal semanas,
            BigDecimal valorHora,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        if (horasSemanales == null
                || semanas == null
                || valorHora == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene horas, semanas o valor hora");
        }

        BigDecimal dias = BigDecimal.valueOf(
                countInclusiveDays(fechaInicio, fechaFin));

        BigDecimal valorSemestral = horasSemanales
                .multiply(semanas)
                .multiply(valorHora)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal promedioSalarioMensual = valorSemestral
                .multiply(DIAS_MES)
                .divide(dias, 8, RoundingMode.HALF_UP);

        BigDecimal valorCesantias = promedioSalarioMensual
                .multiply(dias)
                .divide(DIAS_ANIO, ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal valorIntereses = valorCesantias
                .multiply(dias)
                .divide(DIAS_ANIO, 8, RoundingMode.HALF_UP)
                .multiply(TASA_INTERES)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal valorPrimaLegal = valorCesantias;
        BigDecimal valorVacaciones = promedioSalarioMensual
                .multiply(dias)
                .divide(DIAS_VACACIONES, ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal totalPrestaciones = valorCesantias
                .add(valorIntereses)
                .add(valorPrimaLegal)
                .add(valorVacaciones)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        BigDecimal totalContrato = valorSemestral
                .add(totalPrestaciones)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);

        return new ValorContratacionDTO(
                valorVacaciones,
                valorCesantias,
                valorIntereses,
                valorPrimaLegal,
                totalPrestaciones,
                valorSemestral,
                totalContrato,
                promedioSalarioMensual.setScale(
                        ESCALA_MONETARIA, RoundingMode.HALF_UP));
    }

    public static ValorContratacionDTO calculateCatedraFromActivities(
            BigDecimal horasActividades,
            BigDecimal semanas,
            BigDecimal valorHora) {
        if (horasActividades == null
                || semanas == null
                || valorHora == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La carga docente no tiene horas, semanas o valor hora");
        }
        BigDecimal valorContrato = horasActividades
                .multiply(semanas)
                .multiply(valorHora)
                .setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        return new ValorContratacionDTO(
                CERO,
                CERO,
                CERO,
                CERO,
                CERO,
                valorContrato,
                valorContrato,
                CERO);
    }

    public static ValorContratacionDTO zero() {
        return new ValorContratacionDTO(
                CERO,
                CERO,
                CERO,
                CERO,
                CERO,
                CERO,
                CERO,
                CERO);
    }

    public static boolean isPlanta(String nombre, String sigla) {
        String nombreNorm = normalizeUpper(nombre);
        String siglaNorm = normalizeUpper(sigla);
        return "PLANTA".equals(siglaNorm)
                || "CARRERA".equals(siglaNorm)
                || "DOCENTE_CARRERA".equals(siglaNorm)
                || "DOCENTE DE CARRERA".equals(siglaNorm)
                || nombreNorm.contains("PLANTA")
                || nombreNorm.contains("CARRERA");
    }

    private static String normalizeUpper(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.trim().toUpperCase();
    }
}
