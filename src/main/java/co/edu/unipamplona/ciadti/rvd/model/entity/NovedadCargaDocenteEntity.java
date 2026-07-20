/**
 * Aplicación: rvd
 * Archivo: NovedadCargaDocenteEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 * 20/07/2026 - Sebastian Jaimes - Campos monetarios a BigDecimal
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "NOVEDADCARGADOCENTE", schema = "RVD")
public class NovedadCargaDocenteEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CADO_ID", nullable = false)
    private Long id;

    @Column(name = "CARG_ID")
    private Long idCarga;

    @Column(name = "PEGE_ID")
    private Long idPersonaGeneral;

    @Column(name = "MOCO_ID")
    private Long idModalidadContratacion;

    @Column(name = "CACA_ID")
    private Long idCategoriaCatedratico;

    @Column(name = "FECO_ID")
    private Long idFechasConvocatoria;

    @Column(name = "TINO_ID")
    private Long idTipoNovedad;

    @Column(name = "CADO_IDNOVEDAD")
    private Long idNovedad;

    @Column(name = "CADO_FECHANOVEDAD")
    private Date fechaNovedad;

    @Column(name = "CADO_OBSERVACIONNOVEDAD")
    private String observacionNovedad;

    @Column(name = "CADO_FECHAINICIO")
    private Date fechaInicio;

    @Column(name = "CADO_FECHAFIN")
    private Date fechaFin;

    @Column(name = "CADO_VALORCONTRATO")
    private BigDecimal valorContrato;

    @Column(name = "CADO_VALORPRESTACIONES")
    private BigDecimal valorPrestaciones;

    @Column(name = "CADO_SALARIO")
    private BigDecimal salario;

    @Column(name = "CADO_ESTADO")
    private String estado;

    @Column(name = "CADO_VIGENTE")
    private String vigente;

    @Column(name = "CADO_HORAS")
    private String horas;

    @Column(name = "CADO_VALORHORA")
    private BigDecimal valorHora;

    @Column(name = "CADO_PUNTOS")
    private String puntos;

    @Column(name = "CADO_VALORPUNTO")
    private BigDecimal valorPunto;

    @Column(name = "CADO_TOTALCONTRATO")
    private BigDecimal totalContrato;

    @Column(name = "CADO_SEMANAS")
    private String semanas;

    @Column(name = "CADO_NIVELFORMACION")
    private String nivelFormacion;

    @Column(name = "CADO_MOMENTO")
    private String momento;

    @Column(name = "CADO_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "CADO_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARG_ID", insertable = false, updatable = false)
    private CargaEntity carga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private PersonaGeneralEntity personaGeneral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOCO_ID", insertable = false, updatable = false)
    private ModalidadContratacionEntity modalidadContratacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CACA_ID", insertable = false, updatable = false)
    private CategoriaCatedraticoEntity categoriaCatedratico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FECO_ID", insertable = false, updatable = false)
    private FechasConvocatoriaEntity fechaConvocatoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TINO_ID", insertable = false, updatable = false)
    private TipoNovedadEntity tipoNovedad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CADO_IDNOVEDAD", referencedColumnName = "CADO_ID", insertable = false, updatable = false)
    private CargaDocenteEntity cargaDocenteNovedad;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "NovedadCargaDocenteEntity{" +
                "id=" + id +
                ", idCarga=" + idCarga +
                ", idPersonaGeneral=" + idPersonaGeneral +
                ", idModalidadContratacion=" + idModalidadContratacion +
                ", idCategoriaCatedratico=" + idCategoriaCatedratico +
                ", idFechasConvocatoria=" + idFechasConvocatoria +
                ", idTipoNovedad=" + idTipoNovedad +
                ", idNovedad=" + idNovedad +
                ", fechaNovedad=" + fechaNovedad +
                ", observacionNovedad=" + observacionNovedad +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", valorContrato=" + valorContrato +
                ", valorPrestaciones=" + valorPrestaciones +
                ", totalContrato=" + totalContrato +
                ", salario=" + salario +
                ", estado=" + estado +
                ", vigente=" + vigente +
                ", horas=" + horas +
                ", valorHora=" + valorHora +
                ", puntos=" + puntos +
                ", valorPunto=" + valorPunto +
                ", semanas=" + semanas +
                ", nivelFormacion=" + nivelFormacion +
                ", momento=" + momento +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
