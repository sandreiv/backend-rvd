/**
 * Aplicación: rvd
 * Archivo: TrabajadorLaborEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/10/2026
 * Modificaciones:
 * 16/10/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TRABAJADORLABOR", schema = "TALENTOV3")
public class TrabajadorLaborEntity implements Serializable, Cloneable {

    @Id
    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Column(name = "LABO_ID", nullable = false)
    private Long idLabor;

    @Column(name = "DOVI_ID", nullable = false)
    private Long idDocumentoVinculacion;

    @Column(name = "SUTR_ID")
    private Long idSubtipoTrabajador;

    @Column(name = "CLDO_ID")
    private Long idClaseDocumento;

    @Column(name = "DEDO_ID")
    private Long idDedicacionDocente;

    @Column(name = "ESTR_ID")
    private Long idEstadoTrabajador;

    @Column(name = "TRLA_FECHAINICIAL", nullable = false)
    private Date fechaInicial;

    @Column(name = "TRLA_FECHAFINAL")
    private Date fechaFinal;

    @Column(name = "TRLA_FECHAPOSESION")
    private Date fechaPosesion;

    @Column(name = "TRLA_FECHAINICIOESTADO")
    private Date fechaInicioEstado;

    @Column(name = "TRLA_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "TRLA_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Column(name = "JORN_ID")
    private Long idJornada;

    @Column(name = "TNOM_ID")
    private Long idTipoNomina;

    @Column(name = "NOVS_ID")
    private Long idNovedad;

    @Column(name = "TIVD_ID")
    private Long idTipoVinculacionDocente;

    @Column(name = "TRLA_FECHALIQUIDACION")
    private Date fechaLiquidacion;

    @Column(name = "TRLA_CARGAACADEMICA")
    private Long cargaAcademica;

    @Column(name = "TRLA_FECHAHISTORICA")
    private Date fechaHistorica;

    @Column(name = "CAAS_ID")
    private Long idCargaAcademica;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TrabajadorLaborEntity{" +
                "idPersonaGeneral=" + idPersonaGeneral +
                ", idLabor=" + idLabor +
                ", idDocumentoVinculacion=" + idDocumentoVinculacion +
                ", idSubtipoTrabajador=" + idSubtipoTrabajador +
                ", idClaseDocumento=" + idClaseDocumento +
                ", idDedicacionDocente=" + idDedicacionDocente +
                ", idEstadoTrabajador=" + idEstadoTrabajador +
                ", fechaInicial=" + fechaInicial +
                ", fechaFinal=" + fechaFinal +
                ", fechaPosesion=" + fechaPosesion +
                ", fechaInicioEstado=" + fechaInicioEstado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", idJornada=" + idJornada +
                ", idTipoNomina=" + idTipoNomina +
                ", idNovedad=" + idNovedad +
                ", idTipoVinculacionDocente=" + idTipoVinculacionDocente +
                ", fechaLiquidacion=" + fechaLiquidacion +
                ", cargaAcademica=" + cargaAcademica +
                ", fechaHistorica=" + fechaHistorica +
                ", idCargaAcademica=" + idCargaAcademica +
                '}';
    }
}
/* 16/10/2026 @:Sebastian Jaimes */
