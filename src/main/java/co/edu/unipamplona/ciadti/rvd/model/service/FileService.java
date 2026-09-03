package co.edu.unipamplona.ciadti.rvd.model.service;

import org.springframework.core.io.Resource;

public interface FileService {

    Resource getFile(String path);
}