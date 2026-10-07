/**
 * Aplicación: rvd
 * Archivo: NovedadPdfExporter.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.report
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 06/10/2026
 * Modificaciones:
 * 06/10/2026 - Sebastian Jaimes - Creación inicial (comparativa de novedad)
 */
package co.edu.unipamplona.ciadti.rvd.report;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;

import javax.imageio.ImageIO;

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
import co.edu.unipamplona.ciadti.rvd.model.dto.ReporteNovedadCargaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NovedadPdfExporter {

    private static final String TEMPLATE = "reportes/novedad";
    private static final String LOGO_IMAGE =
            "templates/reportes/img/logo.png";

    private final SpringTemplateEngine templateEngine;

    public byte[] export(ReporteNovedadCargaDTO reporte) {
        Context context = new Context(Locale.forLanguageTag("es-CO"));
        context.setVariable("reporte", reporte);
        context.setVariable("fmt", new NovedadPdfFormat());
        context.setVariable("logoImage", loadLogoDataUri());

        String html = templateEngine.process(TEMPLATE, context);
        return renderPdf(toXhtml(html));
    }

    private String loadLogoDataUri() {
        try {
            ClassPathResource resource = new ClassPathResource(LOGO_IMAGE);
            byte[] bytes = flattenPngOnWhite(resource.getContentAsByteArray());
            return "data:image/png;base64,"
                    + Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ex) {
            log.error("loadLogoDataUri ===> No se pudo leer el escudo", ex);
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible cargar el encabezado del reporte PDF");
        }
    }

    private byte[] flattenPngOnWhite(byte[] pngBytes) throws IOException {
        BufferedImage source = ImageIO.read(new ByteArrayInputStream(pngBytes));
        if (source == null) {
            throw new IOException("El archivo de logo no es una imagen valida");
        }
        BufferedImage flattened = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = flattened.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, source.getWidth(), source.getHeight());
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(flattened, "png", out);
        return out.toByteArray();
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
            log.error("renderPdf ===> Error generando PDF de novedad", ex);
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible generar el archivo PDF de la novedad");
        }
    }
}
