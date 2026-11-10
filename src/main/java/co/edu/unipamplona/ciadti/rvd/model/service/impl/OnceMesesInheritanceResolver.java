package co.edu.unipamplona.ciadti.rvd.model.service.impl;

import java.util.Optional;

import co.edu.unipamplona.ciadti.rvd.model.entity.CargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.model.entity.NovedadCargaDocenteEntity;
import co.edu.unipamplona.ciadti.rvd.util.OnceMesesReporteFilter;

final class OnceMesesInheritanceResolver {

    private OnceMesesInheritanceResolver() {
    }

    static Optional<CargaDocenteEntity> resolve(
            CargaDocenteEntity carga,
            NovedadCargaDocenteEntity ultimaAprobada) {

        if (carga == null) {
            return Optional.empty();
        }


        NovedadCargaDocenteEntity novedad =
                ultimaAprobada != null
                && "1".equals(ultimaAprobada.getEstadoNovedad())
                        ? ultimaAprobada
                        : null;

        if ("1".equals(carga.getCreadoEnNovedad())
                && novedad == null) {

            return Optional.empty();
        }

        if (novedad != null
                && "1".equals(novedad.getEstadoEliminado())) {

            return Optional.empty();
        }

        String onceMeses = preferred(
                novedad != null
                        ? novedad.getOnceMeses()
                        : null,
                carga.getOnceMeses()
        );

        if (!OnceMesesReporteFilter.isOnceMeses(onceMeses)) {
            return Optional.empty();
        }

        CargaDocenteEntity efectivo = new CargaDocenteEntity();

        efectivo.setIdPersonaGeneral(
                preferred(
                        novedad != null
                                ? novedad.getIdPersonaGeneral()
                                : null,
                        carga.getIdPersonaGeneral()
                )
        );

        efectivo.setIdModalidadContratacion(
                preferred(
                        novedad != null
                                ? novedad.getIdModalidadContratacion()
                                : null,
                        carga.getIdModalidadContratacion()
                )
        );

        efectivo.setIdCategoriaCatedratico(
                preferred(
                        novedad != null
                                ? novedad.getIdCategoriaCatedratico()
                                : null,
                        carga.getIdCategoriaCatedratico()
                )
        );

        efectivo.setFechaInicio(
                preferred(
                        novedad != null
                                ? novedad.getFechaInicio()
                                : null,
                        carga.getFechaInicio()
                )
        );

        efectivo.setFechaFin(
                preferred(
                        novedad != null
                                ? novedad.getFechaFin()
                                : null,
                        carga.getFechaFin()
                )
        );

        efectivo.setValorContrato(
                preferred(
                        novedad != null
                                ? novedad.getValorContrato()
                                : null,
                        carga.getValorContrato()
                )
        );

        efectivo.setValorPrestaciones(
                preferred(
                        novedad != null
                                ? novedad.getValorPrestaciones()
                                : null,
                        carga.getValorPrestaciones()
                )
        );

        efectivo.setSalario(
                preferred(
                        novedad != null
                                ? novedad.getSalario()
                                : null,
                        carga.getSalario()
                )
        );

        efectivo.setTotalContrato(
                preferred(
                        novedad != null
                                ? novedad.getTotalContrato()
                                : null,
                        carga.getTotalContrato()
                )
        );

        efectivo.setVigente(
                preferred(
                        novedad != null
                                ? novedad.getVigente()
                                : null,
                        carga.getVigente()
                )
        );

        efectivo.setHoras(
                preferred(
                        novedad != null
                                ? novedad.getHoras()
                                : null,
                        carga.getHoras()
                )
        );

        efectivo.setHorasDeExcepcion(
                preferred(
                        novedad != null
                                ? novedad.getHorasDeExcepcion()
                                : null,
                        carga.getHorasDeExcepcion()
                )
        );

        efectivo.setValorHora(
                preferred(
                        novedad != null
                                ? novedad.getValorHora()
                                : null,
                        carga.getValorHora()
                )
        );

        efectivo.setPuntos(
                preferred(
                        novedad != null
                                ? novedad.getPuntos()
                                : null,
                        carga.getPuntos()
                )
        );

        efectivo.setValorPunto(
                preferred(
                        novedad != null
                                ? novedad.getValorPunto()
                                : null,
                        carga.getValorPunto()
                )
        );

        efectivo.setSemanas(
                preferred(
                        novedad != null
                                ? novedad.getSemanas()
                                : null,
                        carga.getSemanas()
                )
        );

        efectivo.setNivelFormacion(
                preferred(
                        novedad != null
                                ? novedad.getNivelFormacion()
                                : null,
                        carga.getNivelFormacion()
                )
        );

        efectivo.setMomento(
                preferred(
                        novedad != null
                                ? novedad.getMomento()
                                : null,
                        carga.getMomento()
                )
        );

        efectivo.setOnceMeses(onceMeses);

        return Optional.of(efectivo);
    }

    private static <T> T preferred(
            T valorNovedad,
            T valorCarga) {

        return valorNovedad != null
                ? valorNovedad
                : valorCarga;
    }
}