package co.edu.unipamplona.ciadti.rvd.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.service.FileService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/files")
    public ResponseEntity<Resource> getFile(
            @RequestParam String path)
            throws IOException {

        Resource resource =
                fileService.getFile(path);

        String contentType = null;

        try {

            contentType =
                    java.nio.file.Files.probeContentType(
                            resource.getFile()
                                    .toPath()
                    );

        } catch (Exception ignored) {
            // Se utilizará application/octet-stream.
        }

        if (
            contentType == null ||
            contentType.isBlank()
        ) {
            contentType =
                    MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\""
                                + resource.getFilename()
                                + "\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                contentType
                        )
                )
                .body(resource);
    }
}