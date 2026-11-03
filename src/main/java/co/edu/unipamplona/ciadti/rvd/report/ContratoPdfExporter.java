/**
 * Aplicación: rvd
 * Archivo: ContratoPdfExporter.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.report
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/10/2026
 * Modificaciones:
 * 30/10/2026 - Creación inicial (anexo de contratación de 4 meses y con funciones administrativas)
 */
package co.edu.unipamplona.ciadti.rvd.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
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
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.ContratoDocenteReporteDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.contratacion.TipoContratoDocente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContratoPdfExporter {

    private static final String TEMPLATE_CUATRO_MESES = "Contratacion/contrato-4-meses";
    private static final String TEMPLATE_COORDINADOR = "Contratacion/contrato";
    private static final String CHECK_IMAGE = "templates/Contratacion/img/check-verde.png";

    private final SpringTemplateEngine templateEngine;

    public byte[] export(ContratoDocenteReporteDTO contrato, TipoContratoDocente tipo) {
        Context context = new Context(Locale.forLanguageTag("es-CO"));
        context.setVariable("contrato", contrato);
        context.setVariable("fmt", new ContratoPdfFormat());
        context.setVariable("checkImage", loadCheckDataUri());

        String template = tipo == TipoContratoDocente.CON_FUNCIONES_ADMINISTRATIVAS
                ? TEMPLATE_COORDINADOR
                : TEMPLATE_CUATRO_MESES;

        String html = templateEngine.process(template, context);
        return renderPdf(toXhtml(html), tipo);
    }

    /**
     * La viñeta de las funciones es decorativa: si el PNG no se puede leer el
     * anexo se genera sin ella en lugar de fallar.
     */
    private String loadCheckDataUri() {
        try {
            ClassPathResource resource = new ClassPathResource(CHECK_IMAGE);
            return "data:image/png;base64,"
                    + Base64.getEncoder().encodeToString(resource.getContentAsByteArray());
        } catch (IOException ex) {
            log.warn("loadCheckDataUri ===> No se pudo leer la viñeta del anexo", ex);
            return "";
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

    private byte[] renderPdf(String xhtml, TipoContratoDocente tipo) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(xhtml, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception ex) {
            log.error("renderPdf ===> Error generando el anexo ({})", tipo, ex);
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible generar el archivo PDF del contrato");
        }
    }
}
