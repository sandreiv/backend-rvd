package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unipamplona.ciadti.rvd.model.entity.ConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.FechasConvocatoriaEntity;
import co.edu.unipamplona.ciadti.rvd.model.repository.ConvocatoriaRepository;
import co.edu.unipamplona.ciadti.rvd.model.repository.RestriccionPorCoordinacionRepository;
import co.edu.unipamplona.ciadti.rvd.model.service.ConvocatoriaEstadoService;
import co.edu.unipamplona.ciadti.rvd.util.FechasConvocatoriaCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConvocatoriaEstadoServiceImpl implements ConvocatoriaEstadoService {

    private final ConvocatoriaRepository convocatoriaRepository;
    private final RestriccionPorCoordinacionRepository restriccionPorCoordinacionRepository;

    @Override
    @Transactional
    public void syncEstadoConvocatoria(Long idConvocatoria) {
        if (idConvocatoria == null) {
            return;
        }

        ConvocatoriaEntity convocatoria = convocatoriaRepository.findById(idConvocatoria)
                .orElse(null);

        if (convocatoria == null) {
            log.warn("syncEstadoConvocatoria ===> Convocatoria no encontrada. idConvocatoria={}",
                    idConvocatoria);
            return;
        }

        FechasConvocatoriaEntity fechaConvocatoria =
                convocatoriaRepository.findFechaCnvByConvocatoriaId(idConvocatoria);

        boolean convocatoriaVencida = fechaConvocatoria != null
                && FechasConvocatoriaCalculator.isVencida(fechaConvocatoria.getFechaFin());

        Long totalRestricciones = restriccionPorCoordinacionRepository
                .countActiveNonExpiredRestrictionsByConvocatoria(idConvocatoria);

        boolean tieneRestriccionesNoVencidas = totalRestricciones != null && totalRestricciones > 0;

        String nuevoEstado = null;
        String motivo = null;

        if (tieneRestriccionesNoVencidas) {
            nuevoEstado = "1";
            motivo = "restricciones activas no vencidas";
        } else if (convocatoriaVencida) {
            nuevoEstado = "0";
            motivo = "convocatoria vencida sin restricciones activas no vencidas";
        }

        if (nuevoEstado == null) {
            log.debug("syncEstadoConvocatoria ===> No requiere cambio de estado. idConvocatoria={}, estadoActual={}",
                    idConvocatoria, convocatoria.getEstado());
            return;
        }

        if (Objects.equals(convocatoria.getEstado(), nuevoEstado)) {
            log.debug("syncEstadoConvocatoria ===> Estado ya sincronizado. idConvocatoria={}, estado={}",
                    idConvocatoria, nuevoEstado);
            return;
        }

        Date ahora = new Date();

        convocatoriaRepository.updateEstado(nuevoEstado, ahora, idConvocatoria);

        convocatoria.setEstado(nuevoEstado);
        convocatoria.setFechaCambio(ahora);

        log.info("syncEstadoConvocatoria ===> Estado de convocatoria sincronizado. idConvocatoria={}, estado={}, motivo={}",
                idConvocatoria, nuevoEstado, motivo);
    }

    @Override
    @Transactional
    public void syncEstadoConvocatoriaByFecha(Long idFechasConvocatoria) {
        if (idFechasConvocatoria == null) {
            return;
        }

        restriccionPorCoordinacionRepository
                .findConvocatoriaIdByFechaId(idFechasConvocatoria)
                .ifPresent(this::syncEstadoConvocatoria);
    }

    @Override
    @Transactional
    public void syncEstadosConvocatoriasConRestricciones() {
        restriccionPorCoordinacionRepository.findConvocatoriaIdsWithRestrictions()
                .forEach(this::syncEstadoConvocatoria);
    }
}