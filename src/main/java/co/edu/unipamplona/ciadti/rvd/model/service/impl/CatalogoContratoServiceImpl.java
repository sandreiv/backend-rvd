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
import co.edu.unipamplona.ciadti.rvd.model.repository.FormaInicioContratoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.FormaPagoRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.LaborRepository;
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
    private final FormaInicioContratoRepository formaInicioContratoRepository;
    private final LaborRepository laborRepository;

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
        
        List<CatalogoItemDTO> formasInicioContrato = formaInicioContratoRepository
                .findAllByOrderByNombreAsc()
                .stream()
                .map(formaInicio -> new CatalogoItemDTO(formaInicio.getId(), formaInicio.getNombre()))
                .toList();

        List<CatalogoItemDTO> labores = laborRepository
                .findAllLaboresDocente()
                .stream()
                .map(labor -> new CatalogoItemDTO(labor.getId(), labor.getNombre()))
                .toList();

        log.debug("findContractCatalogs ===> formasPago={}, tiposObjeto={}, formasInicioContrato={}",
                formasPago.size(), tiposObjeto.size(), formasInicioContrato.size());

        return new ContratoCatalogosDTO(formasPago, tiposObjeto, formasInicioContrato, labores);
    }
}
