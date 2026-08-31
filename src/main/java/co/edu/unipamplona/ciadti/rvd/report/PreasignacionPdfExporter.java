/**
 * Aplicación: rvd
 * Archivo: PreasignacionPdfExporter.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.report
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 31/08/2026
 * Modificaciones:
 * 31/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadPreasignacionReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ReportePreasignacionCargaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TotalesPreasignacionReporteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreasignacionPdfExporter {

    private static final String TEMPLATE = "reportes/preasignacion";
    private static final String HEADER_IMAGE =
            "templates/reportes/img/encabezado.png";
    private static final TotalesPreasignacionReporteDTO TOTALES_VACIOS =
            new TotalesPreasignacionReporteDTO(
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO);

    private final SpringTemplateEngine templateEngine;

    public byte[] export(ReportePreasignacionCargaDTO reporte) {
        Context context = new Context(Locale.forLanguageTag("es-CO"));
        context.setVariable("encabezado", reporte.encabezado());
        List<ModalidadPreasignacionReporteDTO> modalidades =
                reporte.modalidades() != null
                        ? reporte.modalidades()
                        : List.of();
        context.setVariable("modalidades", modalidades);
        context.setVariable(
                "resumen",
                reporte.resumen() != null ? reporte.resumen() : TOTALES_VACIOS);
        context.setVariable("fmt", new PreasignacionPdfFormat());
        context.setVariable("headerImage", loadHeaderDataUri());

        String html = templateEngine.process(TEMPLATE, context);
        return renderPdf(toXhtml(html));
    }

    private String loadHeaderDataUri() {
        try {
            ClassPathResource resource = new ClassPathResource(HEADER_IMAGE);
            byte[] bytes = resource.getContentAsByteArray();
            return "data:image/png;base64,"
                    + Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ex) {
            log.error("loadHeaderDataUri ===> No se pudo leer el encabezado", ex);
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible cargar el encabezado del reporte PDF");
        }
    }

    private String toXhtml(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .escapeMode(Entities.EscapeMode.xhtml)
                .prettyPrint(false);
        if (document.selectFirst("html") != null) {
            document.selectFirst("html")
                    .attr("xmlns", "http://www.w3.org/1999/xhtml");
        }
        return document.html();
    }

    private byte[] renderPdf(String xhtml) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(xhtml, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception ex) {
            log.error("renderPdf ===> Error generando PDF de preasignación", ex);
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible generar el archivo PDF de preasignación");
        }
    }
}
