package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.exception.ApiException;
import co.edu.unipamplona.ciadti.rvd.model.service.FileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl
        implements FileService {

    @Value("${file.cdp.root-path}")
    private String cdpRootPath;

    @Override
    public Resource getFile(
            String path) {

        if (path == null || path.isBlank()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "La ruta del archivo es obligatoria"
            );
        }

        try {

            Path root =
            Paths.get(cdpRootPath)
                    .toAbsolutePath()
                    .normalize();

            Path filePath =
                    root.resolve(path)
                            .normalize();

            /*
             * Impide rutas como:
             *
             * ../../archivo
             *
             * y evita salir de la carpeta permitida.
             */
            if (!filePath.startsWith(root)) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "La ruta del archivo no es válida"
                );
            }

            if (
                !Files.exists(filePath) ||
                !Files.isRegularFile(filePath)
            ) {
                throw new ApiException(
                        HttpStatus.NOT_FOUND,
                        "El archivo solicitado no existe"
                );
            }

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (!resource.isReadable()) {
                throw new ApiException(
                        HttpStatus.NOT_FOUND,
                        "El archivo solicitado no está disponible"
                );
            }

            return resource;

        } catch (ApiException ex) {
            throw ex;

        } catch (Exception ex) {

            log.error(
                    "getFile ===> Error obteniendo archivo path={}",
                    path,
                    ex
            );

            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No fue posible obtener el archivo solicitado"
            );
        }
    }
}