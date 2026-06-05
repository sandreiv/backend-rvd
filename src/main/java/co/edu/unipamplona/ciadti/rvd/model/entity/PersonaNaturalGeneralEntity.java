/**
 * Aplicación: rvd
 * Archivo: PersonaNaturalGeneralEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "PERSONANATURALGENERAL", schema = "GENERAL")
public class PersonaNaturalGeneralEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Column(name = "PENG_PRIMERAPELLIDO", nullable = false)
    private String primerApellido;

    @Column(name = "PENG_SEGUNDOAPELLIDO")
    private String segundoApellido;

    @Column(name = "PENG_PRIMERNOMBRE", nullable = false)
    private String primerNombre;

    @Column(name = "PENG_SEGUNDONOMBRE")
    private String segundoNombre;

    @Column(name = "PENG_SEXO", nullable = false)
    private String sexo;

    @Column(name = "PENG_LIBRETAMILITAR")
    private String libretaMilitar;

    @Column(name = "PENG_RH")
    private String rh;

    @Column(name = "PENG_DISTRITOMILITAR")
    private String distritoMilitar;

    @Column(name = "PENG_NUMEROPASAPORTE")
    private String numeroPasaporte;

    @Column(name = "PENG_OTRANACIONALIDAD")
    private String otraNacionalidad;

    @Column(name = "PENG_EMAILINSTITUCIONAL")
    private String emailInstitucional;

    @Column(name = "PENG_UBICACIONFISICAHOJAVIDA")
    private String ubicacionFisicaHojavida;

    @Column(name = "PENG_CLASELIBRETAMILITAR")
    private String claseLibretaMilitar;

    @Column(name = "PENG_VIVE")
    private String vive;

    @Column(name = "PENG_FAX")
    private String fax;

    @Column(name = "PENG_FECHAVIGENCIAPASAPORTE")
    private Date fechaVigenciaPasaporte;

    @Column(name = "PENG_FECHANACIMIENTO")
    private Date fechaNacimiento;

    @Column(name = "PENG_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "PENG_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Column(name = "ESCG_ID")
    private Long idEstadoCivil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCG_ID", insertable = false, updatable = false)
    private EstadoCivilGeneralEntity estadoCivil;

    @Column(name = "CIGE_IDLUGARNACIMIENTO")
    private Long idLugarNacimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CIGE_IDLUGARNACIMIENTO", insertable = false, updatable = false)
    private CiudadGeneralEntity lugarNacimiento;

    @Column(name = "PAGE_IDNACIONALIDAD")
    private String idNacionalidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAGE_IDNACIONALIDAD", insertable = false, updatable = false)
    private PaisGeneralEntity nacionalidad;

    @Column(name = "PAGE_IDNACIMIENTO")
    private String idNacimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAGE_IDNACIMIENTO", insertable = false, updatable = false)
    private PaisGeneralEntity nacimiento;

    @OneToOne
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private PersonaGeneralEntity personaGeneral;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PersonaNaturalGeneralEntity [idPersonaGeneral=" + idPersonaGeneral + ", primerApellido=" + primerApellido + ", segundoApellido=" + segundoApellido + ", primerNombre=" + primerNombre + ", segundoNombre=" + segundoNombre + ", sexo=" + sexo + ", ubicacionFisicaHojavida=" + ubicacionFisicaHojavida + ", claseLibretaMilitar=" + claseLibretaMilitar + ", vive=" + vive + ", fax=" + fax + ", fechaVigenciaPasaporte=" + fechaVigenciaPasaporte + ", fechaNacimiento=" + fechaNacimiento + ", registradoPor=" + registradoPor + ", fechaCambio=" + fechaCambio + ", idEstadoCivil=" + idEstadoCivil + "]";
    }
}

    