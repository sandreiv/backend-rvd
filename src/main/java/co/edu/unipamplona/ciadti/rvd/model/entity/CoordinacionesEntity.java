/**
 * Aplicación: rvd
 * Archivo: CoordinacionesEntity.java
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
@Table(name = "COORDINACIONES", schema = "RVD")
public class CoordinacionesEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COOR_ID", nullable = false)
    private Long id;

    @Column(name = "UNID_IDPADRE")
    private Long idUnidadPadre;

    @Column(name = "UNID_IDREGIONAL")
    private Long idRegional;

    @Column(name = "UNID_IDAREA")
    private Long idArea;

    @Column(name = "METO_ID")
    private Long idMetodologia;

    @Column(name = "MODA_ID")
    private Long idModalidad;

    @Column(name = "COOR_ESACADEMICA")
    private String esAcademica;

    @Column(name = "COOR_CODIGO")
    private String codigo;

    @Column(name = "COOR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "COOR_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "UNID_IDPADRE", insertable = false, updatable = false)
    private List<UnidadEntity> unidadesPadre;

    @OneToMany
    @JoinColumn(name = "UNID_IDREGIONAL", insertable = false, updatable = false)
    private List<UnidadEntity> unidadesRegional;

    @OneToMany
    @JoinColumn(name = "UNID_IDAREA", insertable = false, updatable = false)
    private List<UnidadEntity> unidadesArea;

    @OneToMany
    @JoinColumn(name = "METO_ID", insertable = false, updatable = false)
    private List<MetodologiaEntity> metodologias;

    @OneToMany
    @JoinColumn(name = "MODA_ID", insertable = false, updatable = false)
    private List<ModalidadEntity> modalidades;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CoordinacionesEntity{" +
                "id=" + id +
                ", idUnidadPadre=" + idUnidadPadre +
                ", idRegional=" + idRegional +
                ", idArea=" + idArea +
                ", idMetodologia=" + idMetodologia +
                ", idModalidad=" + idModalidad +
                ", esAcademica=" + esAcademica +
                ", codigo=" + codigo +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }

}
