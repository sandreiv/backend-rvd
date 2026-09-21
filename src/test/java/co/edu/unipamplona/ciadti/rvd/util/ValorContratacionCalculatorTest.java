/**
 * Aplicación: rvd
 * Archivo: ValorContratacionCalculatorTest.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.util
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 17/09/2026
 * Modificaciones:
 * 17/09/2026 - Sebastian Jaimes - Creación inicial
 * 18/09/2026 - Sebastian Jaimes - Fórmula de contratación para cátedra
 */
package co.edu.unipamplona.ciadti.rvd.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;

class ValorContratacionCalculatorTest {

    @Test
    void countsStartAndEndDatesAsInclusiveRange() {
        LocalDate inicio = LocalDate.of(2026, 6, 8);
        LocalDate fin = LocalDate.of(2026, 11, 10);

        long dias = ValorContratacionCalculator.countInclusiveDays(
                inicio,
                fin);

        assertEquals(156, dias);
    }

    @Test
    void countsOneDayWhenStartEqualsEnd() {
        LocalDate fecha = LocalDate.of(2026, 6, 8);

        long dias = ValorContratacionCalculator.countInclusiveDays(
                fecha,
                fecha);

        assertEquals(1, dias);
    }

    @Test
    void calculatesContractWithInclusiveDays() {
        BigDecimal salario = new BigDecimal("2631640");
        LocalDate inicio = LocalDate.of(2026, 6, 8);
        LocalDate fin = LocalDate.of(2026, 11, 10);

        ValorContratacionDTO valor =
                ValorContratacionCalculator.calculate(
                        salario,
                        inicio,
                        fin);

        assertMoney("13684528.00", valor.valorContrato());
        assertMoney("1140377.33", valor.valorCesantias());
        assertMoney("570188.67", valor.valorVacaciones());
        assertMoney("59299.62", valor.valorIntereses());
        assertMoney("1140377.33", valor.valorPrimaLegal());
        assertMoney("2910242.95", valor.totalPrestaciones());
        assertMoney("16594770.95", valor.totalContrato());
        assertMoney("2631640.00", valor.salario());
    }

    @Test
    void rejectsEndDateBeforeStart() {
        LocalDate inicio = LocalDate.of(2026, 11, 10);
        LocalDate fin = LocalDate.of(2026, 6, 8);

        assertThrows(
                ApiException.class,
                () -> ValorContratacionCalculator.countInclusiveDays(
                        inicio,
                        fin));
    }

    @Test
    void calculatesCatedraFromWeeklyHoursWeeksAndHourValue() {
        LocalDate inicio = LocalDate.of(2026, 6, 8);
        LocalDate fin = LocalDate.of(2026, 11, 10);

        ValorContratacionDTO valor =
                ValorContratacionCalculator.calculateCatedra(
                        new BigDecimal("8"),
                        new BigDecimal("16"),
                        new BigDecimal("50000"),
                        inicio,
                        fin);

        assertMoney("6400000.00", valor.valorContrato());
        assertMoney("533333.33", valor.valorCesantias());
        assertMoney("27733.33", valor.valorIntereses());
        assertMoney("533333.33", valor.valorPrimaLegal());
        assertMoney("266666.67", valor.valorVacaciones());
        assertMoney("1361066.66", valor.totalPrestaciones());
        assertMoney("7761066.66", valor.totalContrato());
        assertMoney("1230769.23", valor.salario());
    }

    @Test
    void calculatesCatedraFromActivitiesWithoutTcoBenefits() {
        ValorContratacionDTO valor =
                ValorContratacionCalculator.calculateCatedraFromActivities(
                        new BigDecimal("8"),
                        new BigDecimal("16"),
                        new BigDecimal("50000"));

        assertMoney("6400000.00", valor.valorContrato());
        assertMoney("0.00", valor.totalPrestaciones());
        assertMoney("6400000.00", valor.totalContrato());
        assertMoney("0.00", valor.salario());
    }

    @Test
    void plantaContractTotalIsZero() {
        ValorContratacionDTO valor = ValorContratacionCalculator.zero();

        assertMoney("0.00", valor.valorContrato());
        assertMoney("0.00", valor.totalPrestaciones());
        assertMoney("0.00", valor.totalContrato());
        assertMoney("0.00", valor.salario());
    }

    @Test
    void identifiesCatedraPaymentFormIgnoringCase() {
        assertTrue(ValorContratacionCalculator.isCatedra("CATEDRA"));
        assertTrue(ValorContratacionCalculator.isCatedra("catedra"));
        assertFalse(ValorContratacionCalculator.isCatedra("SALARIO"));
        assertFalse(ValorContratacionCalculator.isCatedra(null));
    }

    @Test
    void rejectsCatedraWhenHourlyInputsAreMissing() {
        LocalDate fecha = LocalDate.of(2026, 6, 8);

        assertThrows(
                ApiException.class,
                () -> ValorContratacionCalculator.calculateCatedra(
                        null,
                        new BigDecimal("16"),
                        new BigDecimal("50000"),
                        fecha,
                        fecha));
    }

    private void assertMoney(String expected, BigDecimal actual) {
        assertEquals(
                0,
                new BigDecimal(expected).compareTo(actual),
                () -> "esperado " + expected + " pero fue " + actual);
    }
}
