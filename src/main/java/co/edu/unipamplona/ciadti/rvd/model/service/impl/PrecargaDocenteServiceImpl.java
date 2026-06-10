package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoTrabajoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.PrecargaDocenteService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrecargaDocenteServiceImpl implements PrecargaDocenteService {
    
    @Override
    public List<PeriodoTrabajoDTO> findWorkPeriodList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findWorkPeriodList'");
    }
    
}
