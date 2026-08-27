/**
 * Aplicación: rvd
 * Archivo: PreasignacionExcelExporter.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.report
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.DocentePreasignacionReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EncabezadoCargaReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadPreasignacionReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ReportePreasignacionCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ValorContratacionDTO;

@Component
public class PreasignacionExcelExporter {

    private static final String[] CODIGOS_ACTIVIDAD = {
            "FAD", "FAI", "CTEI", "ISU", "AC"
    };

    private static final String[] HEADERS = {
            "Nombre del docente", "Documento", "Puntos", "Categoría",
            "Fecha inicio", "Fecha fin", "Semanas", "Asignación salarial",
            "Vacaciones", "Cesantías", "Intereses", "Prima legal",
            "Total prestaciones", "Valor contrato", "Total contrato",
            "FAD", "FAI", "CTEI", "ISU", "AC", "Total horas"
    };

    private static final int COL_DOCENTE_INICIO = 0;
    private static final int COL_DOCENTE_FIN = 7;
    private static final int COL_CONTRATO_INICIO = 8;
    private static final int COL_CONTRATO_FIN = 14;
    private static final int COL_ACT_INICIO = 15;
    private static final int COL_TOTAL_HORAS = 20;
    private static final int LAST_COL = HEADERS.length - 1;

    private static final int COL_WIDTH_MIN = 4000;
    private static final int COL_WIDTH_MAX = 16000;
    private static final int[] COL_WIDTHS = {
            9000, 5000, 4000, 5500,
            4500, 4500, 4000, 5500,
            5000, 5000, 5000, 5000,
            5500, 5500, 5500,
            4000, 4000, 4000, 4000, 4000, 4500
    };

    public byte[] export(ReportePreasignacionCargaDTO reporte) {
        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Preasignación");
            Styles styles = createStyles(workbook);

            int rowIdx = writeEncabezado(sheet, reporte.encabezado(), styles);
            rowIdx++;
            List<ModalidadPreasignacionReporteDTO> modalidades =
                    reporte.modalidades() != null
                            ? reporte.modalidades()
                            : List.of();
            if (modalidades.isEmpty()) {
                Row empty = sheet.createRow(rowIdx);
                Cell cell = empty.createCell(0);
                cell.setCellValue("No hay docentes preasignados en esta carga");
                cell.setCellStyle(styles.value);
            } else {
                for (ModalidadPreasignacionReporteDTO modalidad : modalidades) {
                    rowIdx = writeModalidadTable(sheet, modalidad, rowIdx, styles);
                    rowIdx++;
                }
            }
            autoSizeColumns(sheet);
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "No fue posible generar el archivo Excel de preasignación");
        }
    }

    private int writeEncabezado(
            Sheet sheet,
            EncabezadoCargaReporteDTO encabezado,
            Styles styles) {

        int rowIdx = 0;
        Row title = sheet.createRow(rowIdx++);
        Cell titleCell = title.createCell(0);
        titleCell.setCellValue("Resumen de preasignación");
        titleCell.setCellStyle(styles.title);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        rowIdx = writeKv(sheet, rowIdx, "Unidad", encabezado.unidad(), styles);
        rowIdx = writeKv(
                sheet, rowIdx, "Facultad", encabezado.facultad(), styles);
        rowIdx = writeKv(
                sheet,
                rowIdx,
                "Coordinación",
                encabezado.coordinacion(),
                styles);
        rowIdx = writeKv(
                sheet,
                rowIdx,
                "Periodo académico",
                encabezado.periodoAcademico(),
                styles);
        rowIdx = writeKv(
                sheet,
                rowIdx,
                "Convocatoria",
                encabezado.convocatoria(),
                styles);
        return rowIdx;
    }

    private int writeKv(
            Sheet sheet,
            int rowIdx,
            String campo,
            String valor,
            Styles styles) {
        Row row = sheet.createRow(rowIdx);
        Cell label = row.createCell(0);
        label.setCellValue(campo);
        label.setCellStyle(styles.label);
        Cell value = row.createCell(1);
        value.setCellValue(valor != null ? valor : "");
        value.setCellStyle(styles.value);
        return rowIdx + 1;
    }

    private int writeModalidadTable(
            Sheet sheet,
            ModalidadPreasignacionReporteDTO modalidad,
            int startRow,
            Styles styles) {

        int rowIdx = startRow;
        Row modalityTitle = sheet.createRow(rowIdx);
        Cell titleCell = modalityTitle.createCell(0);
        titleCell.setCellValue("Modalidad de contratación: " + nullToEmpty(modalidad.nombreModalidad()));
        titleCell.setCellStyle(styles.modalityTitle);
        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, LAST_COL));

        for (int col = 1; col <= LAST_COL; col++) {
            Cell filler = modalityTitle.createCell(col);
            filler.setCellStyle(styles.modalityTitle);
        }
        rowIdx++;

        rowIdx = writeGroupHeader(sheet, rowIdx, styles);
        rowIdx = writeColumnHeader(sheet, rowIdx, styles);

        List<DocentePreasignacionReporteDTO> docentes =
                modalidad.docentes() != null
                        ? modalidad.docentes()
                        : List.of();
        if (docentes.isEmpty()) {
            Row empty = sheet.createRow(rowIdx++);
            Cell cell = empty.createCell(0);
            cell.setCellValue("Sin docentes en esta modalidad");
            cell.setCellStyle(styles.value);
            sheet.addMergedRegion(
                    new CellRangeAddress(
                            empty.getRowNum(),
                            empty.getRowNum(),
                            0,
                            LAST_COL));
            return rowIdx;
        }

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (DocentePreasignacionReporteDTO docente : docentes) {
            writeDocenteRow(sheet.createRow(rowIdx++), docente, styles, dateFormat);
        }
        return rowIdx;
    }

    private int writeGroupHeader(Sheet sheet, int rowIdx, Styles styles) {
        Row row = sheet.createRow(rowIdx);

        writeGroupCell(row, COL_DOCENTE_INICIO, "Datos del docente", styles);
        writeGroupCell(row, COL_CONTRATO_INICIO, "Valores de contratación", styles);
        writeGroupCell(row, COL_ACT_INICIO, "Actividades (horas)", styles);

        for (int col = 0; col <= LAST_COL; col++) {
            if (row.getCell(col) == null) {
                Cell filler = row.createCell(col);
                filler.setCellStyle(styles.groupHeader);
            }
        }

        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, COL_DOCENTE_INICIO, COL_DOCENTE_FIN));
        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, COL_CONTRATO_INICIO, COL_CONTRATO_FIN));
        sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, COL_ACT_INICIO, LAST_COL));
        row.setHeightInPoints(22);
        return rowIdx + 1;
    }

    private void writeGroupCell(
            Row row,
            int col,
            String label,
            Styles styles) {

        Cell cell = row.createCell(col);
        cell.setCellValue(label);
        cell.setCellStyle(styles.groupHeader);
    }

    private int writeColumnHeader(Sheet sheet, int rowIdx, Styles styles) {
        Row row = sheet.createRow(rowIdx);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(styles.header);
        }
        row.setHeightInPoints(30);
        return rowIdx + 1;
    }

    private void writeDocenteRow(
            Row row,
            DocentePreasignacionReporteDTO docente,
            Styles styles,
            DateTimeFormatter dateFormat) {

        setText(row, 0, docente.nombreCompleto(), styles.data);
        setText(row, 1, docente.documento(), styles.data);
        setText(row, 2, docente.puntos(), styles.data);
        setText(row, 3, docente.categoria(), styles.data);
        setText(row, 4, formatDate(docente.fechaInicio(), dateFormat), styles.data);
        setText(row, 5, formatDate(docente.fechaFin(), dateFormat), styles.data);
        setText(row, 6, docente.semanas(), styles.data);
        setNumber(row, 7, docente.asignacionSalarial(), styles.money);

        ValorContratacionDTO valor = docente.valorContratacion();
        if (valor == null) {
            for (int col = COL_CONTRATO_INICIO; col <= COL_CONTRATO_FIN; col++) {
                setText(row, col, "", styles.data);
            }
        } else {
            setNumber(row, 8, valor.valorVacaciones(), styles.money);
            setNumber(row, 9, valor.valorCesantias(), styles.money);
            setNumber(row, 10, valor.valorIntereses(), styles.money);
            setNumber(row, 11, valor.valorPrimaLegal(), styles.money);
            setNumber(row, 12, valor.totalPrestaciones(), styles.money);
            setNumber(row, 13, valor.valorContrato(), styles.money);
            setNumber(row, 14, valor.totalContrato(), styles.money);
        }

        Map<String, BigDecimal> horas = docente.horasPorCodigo() != null
                ? docente.horasPorCodigo()
                : Map.of();
        BigDecimal totalHoras = BigDecimal.ZERO;
        for (int i = 0; i < CODIGOS_ACTIVIDAD.length; i++) {
            BigDecimal horasCodigo = horas.get(CODIGOS_ACTIVIDAD[i]);
            setNumber(
                    row,
                    COL_ACT_INICIO + i,
                    horasCodigo,
                    styles.number);
            if (horasCodigo != null) {
                totalHoras = totalHoras.add(horasCodigo);
            }
        }
        setNumber(row, COL_TOTAL_HORAS, totalHoras, styles.number);
    }

    private String formatDate(LocalDate date, DateTimeFormatter dateFormat) {
        if (date == null) {
            return "";
        }
        return date.format(dateFormat);
    }

    private String nullToEmpty(String value) {
        return value != null ? value : "";
    }

    private void setText(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private void setNumber(
            Row row,
            int col,
            BigDecimal value,
            CellStyle style) {
        Cell cell = row.createCell(col);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        } else {
            cell.setBlank();
        }
        cell.setCellStyle(style);
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i <= LAST_COL; i++) {
            int preferred = i < COL_WIDTHS.length
                    ? COL_WIDTHS[i]
                    : COL_WIDTH_MIN;
            sheet.setColumnWidth(
                    i,
                    Math.min(Math.max(preferred, COL_WIDTH_MIN), COL_WIDTH_MAX));
        }
    }

    private Styles createStyles(Workbook workbook) {
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);

        Font boldFont = workbook.createFont();
        boldFont.setBold(true);

        CellStyle title = workbook.createCellStyle();
        title.setFont(titleFont);

        CellStyle label = workbook.createCellStyle();
        label.setFont(boldFont);

        CellStyle value = workbook.createCellStyle();

        CellStyle modalityTitle = workbook.createCellStyle();
        modalityTitle.setFont(boldFont);
        modalityTitle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        modalityTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        modalityTitle.setAlignment(HorizontalAlignment.LEFT);
        modalityTitle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle groupHeader = workbook.createCellStyle();
        groupHeader.setFont(boldFont);
        groupHeader.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        groupHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBorder(groupHeader);
        groupHeader.setAlignment(HorizontalAlignment.CENTER);
        groupHeader.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle header = workbook.createCellStyle();
        header.setFont(boldFont);
        header.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBorder(header);
        header.setAlignment(HorizontalAlignment.CENTER);
        header.setVerticalAlignment(VerticalAlignment.CENTER);
        header.setWrapText(true);

        CellStyle data = workbook.createCellStyle();
        applyBorder(data);
        data.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle number = workbook.createCellStyle();
        number.cloneStyleFrom(data);
        number.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

        CellStyle money = workbook.createCellStyle();
        money.cloneStyleFrom(data);
        money.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));

        return new Styles(
                title,
                label,
                value,
                modalityTitle,
                groupHeader,
                header,
                data,
                number,
                money);
    }

    private void applyBorder(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private record Styles(
            CellStyle title,
            CellStyle label,
            CellStyle value,
            CellStyle modalityTitle,
            CellStyle groupHeader,
            CellStyle header,
            CellStyle data,
            CellStyle number,
            CellStyle money) {}
}
