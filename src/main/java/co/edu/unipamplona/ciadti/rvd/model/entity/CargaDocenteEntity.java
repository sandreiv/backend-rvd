/**
 * Aplicación: rvd
 * Archivo: CargaDocenteEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CARGADOCENTE", schema = "RVD")
public class CargaDocenteEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CADO_ID", nullable = false)
    private Long id;

    @Column(name = "CARG_ID")
    private Long idCarga;

    @Column(name = "PEGE_ID")
    private Long idPersonaGeneral;

    @Column(name = "MODO_ID")
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
    private String valorContrato;

    @Column(name = "CADO_VALORPRESTACIONES")
    private String valorPrestaciones;

    @Column(name = "CADO_SALARIO")
    private String salario;

    @Column(name = "CADO_ESTADO")
    private String estado;

    @Column(name = "CADO_VIGENTE")
    private String vigente;

    @Column(name = "CADO_HORAS")
    private String horas;

    @Column(name = "CADO_VALORHORA")
    private String valorHora;

    @Column(name = "CADO_PUNTOS")
    private String puntos;

    @Column(name = "CADO_VALORPUNTO")
    private String valorPunto;

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

    @OneToMany
    @JoinColumn(name = "CARG_ID", insertable = false, updatable = false)
    private List<CargaEntity> cargas;

    @OneToMany
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private List<PersonaGeneralEntity> personasGenerales;

    @OneToMany
    @JoinColumn(name = "MODO_ID", insertable = false, updatable = false)
    private List<ModalidadContratacionEntity> modalidadesContratacion;

    @OneToMany
    @JoinColumn(name = "CACA_ID", insertable = false, updatable = false)
    private List<CategoriaCatedraticoEntity> categoriasCatedratico;

    @OneToMany
    @JoinColumn(name = "FECO_ID", insertable = false, updatable = false)
    private List<FechasConvocatoriaEntity> fechasConvocatoria;

    @OneToMany
    @JoinColumn(name = "TINO_ID", insertable = false, updatable = false)
    private List<TipoNovedadEntity> tiposNovedad;

    @OneToMany
    @JoinColumn(name = "CADO_IDNOVEDAD", insertable = false, updatable = false)
    private List<CargaDocenteEntity> novedades;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CargaDocenteEntity{" +
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
