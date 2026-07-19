package co.edu.unipamplona.ciadti.rvd.config.logs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Alerta en consola cuando el tamaño total de la carpeta de logs
 * supera el umbral configurado. No elimina archivos.
 */
@Slf4j
@Component
public class LogDirectorySizeMonitor {

    private final Path logPath;
    private final long thresholdBytes;

    public LogDirectorySizeMonitor(
            @Value("${logging.file.path:C:/rvd/logs}") String logPath,
            @Value("${logging.size-alert.threshold-bytes:1073741824}")
                    long thresholdBytes) {
        this.logPath = Paths.get(logPath);
        this.thresholdBytes = thresholdBytes;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkOnStartup() {
        checkLogDirectorySize();
    }

    @Scheduled(fixedDelayString = "${logging.size-alert.check-ms:3600000}")
    public void checkPeriodically() {
        checkLogDirectorySize();
    }

    private void checkLogDirectorySize() {
        if (!Files.isDirectory(logPath)) {
            return;
        }

        long totalBytes = calculateDirectorySize(logPath);
        if (totalBytes <= thresholdBytes) {
            return;
        }

        String totalGb = formatGb(totalBytes);
        String thresholdGb = formatGb(thresholdBytes);
        log.warn(
                "ALERTA: la carpeta de logs '{}' ocupa {} GB "
                        + "(umbral {} GB). Los archivos NO se eliminan; "
                        + "revise espacio en disco y archive manualmente "
                        + "si es necesario.",
                logPath.toAbsolutePath(),
                totalGb,
                thresholdGb);
    }

    private String formatGb(long bytes) {
        return String.format(Locale.US, "%.2f",
                bytes / (1024.0 * 1024.0 * 1024.0));
    }

    private long calculateDirectorySize(Path directory) {
        try (Stream<Path> walk = Files.walk(directory)) {
            return walk
                    .filter(Files::isRegularFile)
                    .mapToLong(this::safeSize)
                    .sum();
        } catch (IOException e) {
            log.debug("No se pudo calcular el tamaño de logs en {}: {}",
                    directory, e.getMessage());
            return 0L;
        }
    }

    private long safeSize(Path file) {
        try {
            return Files.size(file);
        } catch (IOException e) {
            return 0L;
        }
    }
}
