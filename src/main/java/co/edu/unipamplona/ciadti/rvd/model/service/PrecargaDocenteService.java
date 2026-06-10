package co.edu.unipamplona.ciadti.rvd.model.service;

import java.util.List;

import co.edu.unipamplona.ciadti.rvd.model.dto.PeriodoTrabajoDTO;

public interface PrecargaDocenteService {
    
    List<PeriodoTrabajoDTO> findWorkPeriodList();
}
