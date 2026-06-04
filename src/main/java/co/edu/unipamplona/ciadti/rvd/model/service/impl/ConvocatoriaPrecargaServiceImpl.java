package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import co.edu.unipamplona.ciadti.rvd.mapper.PersonaAutorizaConvocatoriaMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PersonaAutorizaConvocatoriaDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.PersonaGeneralRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaPrecargaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConvocatoriaPrecargaServiceImpl implements ConvocatoriaPrecargaService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final PersonaGeneralRepository personaGeneralRepository;
    private final PersonaAutorizaConvocatoriaMapper personaAutorizaConvocatoriaMapper;

    @Override
    public List<ConvocatoriaDTO> findCallListWithDates() {
        return convocatoriaRepository.findCallListWithDates();
    }

    @Override
    public List<PersonaAutorizaConvocatoriaDTO> searchGeneralPerson(String nombre, String documento) {
        String nombreParam = normalizeParam(nombre);
        String documentoParam = normalizeParam(documento);
        if (nombreParam == null && documentoParam == null) {
            return Collections.emptyList();
        }
        return personaAutorizaConvocatoriaMapper.toDtoList(
                personaGeneralRepository.searchGeneralPerson(
                        nombreParam,
                        documentoParam));
    }

    private String normalizeParam(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
