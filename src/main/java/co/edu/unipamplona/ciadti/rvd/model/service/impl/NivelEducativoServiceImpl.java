package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.mapper.NivelEducativoMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.NivelEducativoDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.NivelEducativoRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.NivelEducativoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NivelEducativoServiceImpl implements NivelEducativoService {

    private final NivelEducativoRepository nivelEducativoRepository;
    private final NivelEducativoMapper nivelEducativoMapper;

    @Override
    public List<NivelEducativoDTO> findEducationalLevelList() {
        return nivelEducativoMapper.toDtoList(
                nivelEducativoRepository.findAllEducationalLevels());
    }
}
