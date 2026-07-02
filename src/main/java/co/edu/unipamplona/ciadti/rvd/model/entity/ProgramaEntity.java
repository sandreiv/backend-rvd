/**
 * Aplicación: rvd
 * Archivo: ProgramaEntity.java
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
@Table(name = "PROGRAMA", schema = "ACADEMICO")
public class ProgramaEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROG_ID", nullable = false)
    private Long id;

    @Column(name = "MODA_ID")
    private Long idModalidad;

    @Column(name = "METO_ID")
    private Long idMetodologia;
    
    @Column(name = "PROM_ID")
    private Long idPromedio;

    @Column(name = "JORN_ID")
    private Long idJornada;

    @Column(name = "TPPA_ID")
    private Long idTipoPeriodoAcademico;

    @Column(name = "NORG_ID")
    private Long idNormaGeneral;
    
    @Column(name = "PROG_CODIGOICFES")
    private String codigoIcfes;

    @Column(name = "PROG_NUMPERIODOS", nullable = false)
    private Long numPeriodos;


    @Column(name = "PROG_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "PROG_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Column(name = "PROG_FECHAAPROBACIONICFES")
    private Date fechaAprobacionIcfes;

    @Column(name = "PROG_ESTADO")
    private String estado;

    @Column(name = "PROG_CODIGOPROGRAMA")
    private String codigoPrograma;

    @Column(name = "PROG_NOMBRE")
    private String nombre;

    @Column(name = "PROG_COMPLEJIDAD")
    private String complejidad;

    @Column(name = "PROG_TITULOOTORGA")
    private String tituloOtorga;

    @Column(name = "PROG_TIENECONVENIO")
    private String tieneConvenio;

    @Column(name = "PROG_TIPOPROGRAMA")
    private String tipoPrograma;

    @Column(name = "PROG_ABREVIATURA")
    private String abreviatura;

    @Column(name = "PROG_CODIGOSNIES")
    private String codigoSnies;

    @Column(name = "PROG_CICLOPROPEDEUTICO")
    private Long cicloPropedeutico;

    @OneToMany
    @JoinColumn(name = "MODA_ID", insertable = false, updatable = false)
    private List<ModalidadEntity> modalidades;

    @OneToMany
    @JoinColumn(name = "METO_ID", insertable = false, updatable = false)
    private List<MetodologiaEntity> metodologias;

    /*@OneToMany
    @JoinColumn(name = "PROM_ID", insertable = false, updatable = false)
    private List<PromedioEntity> promedios;

    @OneToMany
    @JoinColumn(name = "JORN_ID", insertable = false, updatable = false)
    private List<JornadaEntity> jornadas;

    @OneToMany
    @JoinColumn(name = "TPPA_ID", insertable = false, updatable = false)
    private List<TipoPeriodoAcademicoEntity> tipoPeriodoAcademico;

    @OneToMany
    @JoinColumn(name = "NORG_ID", insertable = false, updatable = false)
    private List<NormaGeneralEntity> normaGeneral;*/

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ProgramaEntity{" +
                "id=" + id +
                ", codigoIcfes=" + codigoIcfes +
                ", numPeriodos=" + numPeriodos +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", idModalidad=" + idModalidad +
                ", idMetodologia=" + idMetodologia +
                ", complejidad=" + complejidad +
                ", tituloOtorga=" + tituloOtorga +
                ", tieneConvenio=" + tieneConvenio +
                ", idJornada=" + idJornada +
                ", idTipoPeriodoAcademico=" + idTipoPeriodoAcademico +
                ", idNormaGeneral=" + idNormaGeneral +
                ", fechaAprobacionIcfes=" + fechaAprobacionIcfes +
                ", estado=" + estado +
                ", codigoPrograma=" + codigoPrograma +
                ", nombre=" + nombre +
                ", idPromedio=" + idPromedio +
                ", tipoPrograma=" + tipoPrograma +
                ", abreviatura=" + abreviatura +
                ", codigoSnies=" + codigoSnies +
                ", cicloPropedeutico=" + cicloPropedeutico +
                '}';
    }
}
