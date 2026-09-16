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
import java.time.LocalDate;
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

    @Column(name = "NOVE_ID")
    private Long idNovedadCatalogo;

    @Column(name = "NOCD_FECHANOVEDAD")
    private Date fechaNovedad;

    @Column(name = "NOCD_OBSERVACIONNOVEDAD")
    private String observacionNovedad;

    @Column(name = "NOCD_FECHAINICIO")
    private LocalDate fechaInicio;

    @Column(name = "NOCD_FECHAFIN")
    private LocalDate fechaFin;

    @Column(name = "NOCD_VALORCONTRATO")
    private BigDecimal valorContrato;

    @Column(name = "NOCD_VALORPRESTACIONES")
    private BigDecimal valorPrestaciones;

    @Column(name = "NOCD_SALARIO")
    private BigDecimal salario;

    @Column(name = "NOCD_ESTADO")
    private String estado;

    @Column(name = "NOCD_VIGENTE")
    private String vigente;

    @Column(name = "NOCD_HORAS")
    private String horas;

    @Column(name = "NOCD_HORASDEEXCEPCION")
    private String horasDeExcepcion;

    @Column(name = "NOCD_VALORHORA")
    private BigDecimal valorHora;

    @Column(name = "NOCD_PUNTOS")
    private String puntos;

    @Column(name = "NOCD_VALORPUNTO")
    private BigDecimal valorPunto;

    @Column(name = "NOCD_TOTALCONTRATO")
    private BigDecimal totalContrato;

    @Column(name = "NOCD_SEMANAS")
    private String semanas;

    @Column(name = "NOCD_NIVELFORMACION")
    private String nivelFormacion;

    @Column(name = "NOCD_MOMENTO")
    private String momento;

    @Column(name = "NOCD_ONCEMESES")
    private String onceMeses;

    @Column(name = "NOCD_ESTADONOVEDAD")
    private String estadoNovedad;

    @Column(name = "NOCD_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "NOCD_FECHACAMBIO")
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
    @JoinColumn(
        name = "NOVE_ID",
        insertable = false,
        updatable = false
    )
    private NovedadEntity novedad;

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
                ", horasDeExcepcion=" + horasDeExcepcion +
                ", valorHora=" + valorHora +
                ", puntos=" + puntos +
                ", valorPunto=" + valorPunto +
                ", semanas=" + semanas +
                ", nivelFormacion=" + nivelFormacion +
                ", momento=" + momento +
                ", onceMeses=" + onceMeses +
                ", estadoNovedad=" + estadoNovedad +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", idNovedadCatalogo=" + idNovedadCatalogo +
                '}';
    }
}
