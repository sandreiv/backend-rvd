/**
 * Aplicación: rvd
 * Archivo: MateriaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/06/2026
 * Modificaciones:
 * 30/06/2026 - Sebastian Jaimes - Creación inicial
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
@Table(name = "MATERIA", schema = "ACADEMICO")
public class MateriaEntity implements Serializable, Cloneable {

    @Id
    @Column(name = "MATE_CODIGOMATERIA", nullable = false)
    private String codigoMateria;

    @Column(name = "MATE_NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "MATE_CAPACIDAD")
    private Long capacidad;

    @Column(name = "MATE_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "MATE_FECHACAMBIO")
    private Date fechaCambio;

    @Column(name = "UNID_ID")
    private Long idUnidad;

    @Column(name = "MATE_PONDERACIONACADEMICA")
    private Long ponderacionAcademica;

    @Column(name = "MATE_HABILITABLE")
    private String habilitable;

    @Column(name = "TICA_ID")
    private Long idTipoCategoria;

    @Column(name = "NATU_ID")
    private Long idNaturaleza;

    @Column(name = "MATE_ESOPCIONAL")
    private String esOpcional;

    @Column(name = "MATE_HOMOLOGABLE")
    private String homologable;

    @Column(name = "MATE_VALIDABLE")
    private String validable;

    @Column(name = "TIPA_ID")
    private Long idTipa;

    @Column(name = "MATE_BLOQUEHORASTEORICAS")
    private Long bloqueHorasTeoricas;

    @Column(name = "MATE_BLOQUEHORASPRACTICAS")
    private Long bloqueHorasPracticas;

    @Column(name = "MATE_CODIGOORIGINAL")
    private String codigoOriginal;

    @Column(name = "MATE_CUENTAPROMEDIO")
    private String cuentaPromedio;

    @Column(name = "MATE_HORASINDEPENDIENTES")
    private Long horasIndependientes;

    @Column(name = "MATE_HORASASESORIA")
    private Long horasAsesoria;

    @Column(name = "ARMA_ID")
    private Long idAreaMateria;

    @Column(name = "MATE_TIPO")
    private String tipo;

    @Column(name = "MATE_PROYECTODEGRADO")
    private String proyectoDeGrado;

    @Column(name = "MATE_DURACION")
    private Long duracion;

    @Column(name = "MATE_ESEXTENSION")
    private String esExtension;

    @Column(name = "MATE_ESEXTRACREDITO")
    private String esExtracredito;

    @Column(name = "MATE_HORASTEORICAS")
    private Long horasTeoricas;

    @Column(name = "MATE_HORASTEORICOPRACTICAS")
    private Long horasTeoricoPracticas;

    @Column(name = "MATE_HORASPRACTICAS")
    private Long horasPracticas;

    @Column(name = "MATE_ESPRACTICA")
    private String esPractica;

    @Column(name = "MATE_CODIGOSNIES")
    private String codigoSnies;

    @Column(name = "MATE_ESPROPEDEUTICA")
    private String esPropedeutica;

    @Column(name = "MATE_ESED")
    private String esEd;

    @Column(name = "MATE_ESCOMPETENCIA")
    private String esCompetencia;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "MateriaEntity{" +
                "codigoMateria=" + codigoMateria +
                ", nombre=" + nombre +
                ", capacidad=" + capacidad +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", idUnidad=" + idUnidad +
                ", ponderacionAcademica=" + ponderacionAcademica +
                ", habilitable=" + habilitable +
                ", idTipoCategoria=" + idTipoCategoria +
                ", idNaturaleza=" + idNaturaleza +
                ", esOpcional=" + esOpcional +
                ", homologable=" + homologable +
                ", validable=" + validable +
                ", idTipa=" + idTipa +
                ", bloqueHorasTeoricas=" + bloqueHorasTeoricas +
                ", bloqueHorasPracticas=" + bloqueHorasPracticas +
                ", codigoOriginal=" + codigoOriginal +
                ", cuentaPromedio=" + cuentaPromedio +
                ", horasIndependientes=" + horasIndependientes +
                ", horasAsesoria=" + horasAsesoria +
                ", idAreaMateria=" + idAreaMateria +
                ", tipo=" + tipo +
                ", proyectoDeGrado=" + proyectoDeGrado +
                ", duracion=" + duracion +
                ", esExtension=" + esExtension +
                ", esExtracredito=" + esExtracredito +
                ", horasTeoricas=" + horasTeoricas +
                ", horasTeoricoPracticas=" + horasTeoricoPracticas +
                ", horasPracticas=" + horasPracticas +
                ", esPractica=" + esPractica +
                ", codigoSnies=" + codigoSnies +
                ", esPropedeutica=" + esPropedeutica +
                ", esEd=" + esEd +
                ", esCompetencia=" + esCompetencia +
                '}';
    }
}
/* 30/06/2026 @:Sebastian Jaimes */
