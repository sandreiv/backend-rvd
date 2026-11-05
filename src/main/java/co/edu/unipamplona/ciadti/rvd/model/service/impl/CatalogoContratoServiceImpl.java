/**
 * Aplicación: rvd
 * Archivo: CatalogoContratoServiceImpl.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.service.impl
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.model.dto.CatalogoItemDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.ContratoCatalogosDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.FormaPagoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.TipoObjetoRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.CatalogoContratoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogoContratoServiceImpl implements CatalogoContratoService {

    private final FormaPagoRepository formaPagoRepository;
    private final TipoObjetoRepository tipoObjetoRepository;

    @Override
    public ContratoCatalogosDTO findContractCatalogs() {
        List<CatalogoItemDTO> formasPago = formaPagoRepository
                .findAllByOrderByDescripcionAsc()
                .stream()
                .map(formaPago -> new CatalogoItemDTO(formaPago.getId(), formaPago.getDescripcion()))
                .toList();

        List<CatalogoItemDTO> tiposObjeto = tipoObjetoRepository
                .findAllByOrderByDescripcionAsc()
                .stream()
                .map(tipoObjeto -> new CatalogoItemDTO(tipoObjeto.getId(), tipoObjeto.getDescripcion()))
                .toList();

        log.debug("findContractCatalogs ===> formasPago={}, tiposObjeto={}",
                formasPago.size(), tiposObjeto.size());

        return new ContratoCatalogosDTO(formasPago, tiposObjeto);
    }
}
