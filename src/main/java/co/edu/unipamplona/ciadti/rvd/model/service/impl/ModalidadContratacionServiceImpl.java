package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.mapper.ModalidadContratacionMapper;
import co.edu.unipamplona.ciadti.rvd.model.dto.ModalidadContratacionDTO;
import co.edu.unipamplona.ciadti.rvd.model.repository.ModalidadContratacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ModalidadContratacionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModalidadContratacionServiceImpl implements ModalidadContratacionService {

    private final ModalidadContratacionRepository modalidadContratacionRepository;
    private final ModalidadContratacionMapper modalidadContratacionMapper;

    @Override
    public List<ModalidadContratacionDTO> findModalityList() {
        return modalidadContratacionMapper.toDtoList(
                modalidadContratacionRepository.findAllModalities());
    }
}
