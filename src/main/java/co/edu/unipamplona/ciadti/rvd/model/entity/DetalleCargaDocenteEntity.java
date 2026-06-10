/**
 * Aplicación: rvd
 * Archivo: DetalleCargaDocenteEntity.java
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
@Table(name = "DETALLECARGADOCENTE", schema = "RVD")
public class DetalleCargaDocenteEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DECD_ID", nullable = false)
    private Long id;

    @Column(name = "CADO_ID")
    private Long idCargaDocente;

    @Column(name = "PROG_ID")
    private Long idPrograma;

    @Column(name = "GRUP_ID")
    private Long idGrupo;

    @Column(name = "TIAC_ID")
    private Long idTipoActividad;

    @Column(name = "CECO_ID")
    private Long idCentroCosto;

    @Column(name = "DECD_HORAS")
    private String horas;

    @Column(name = "DECD_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "DECD_FECHACAMBIO")
    private Date fechaCambio;

    @OneToMany
    @JoinColumn(name = "CADO_ID", insertable = false, updatable = false)
    private List<CargaDocenteEntity> cargasDocente;

    @OneToMany
    @JoinColumn(name = "PROG_ID", insertable = false, updatable = false)
    private List<ProgramaEntity> programas;

    @OneToMany
    @JoinColumn(name = "GRUP_ID", insertable = false, updatable = false)
    private List<GrupoEntity> grupos;

    @OneToMany
    @JoinColumn(name = "TIAC_ID", insertable = false, updatable = false)
    private List<TipoActividadesEntity> tiposActividad;

    @OneToMany
    @JoinColumn(name = "CECO_ID", insertable = false, updatable = false)
    private List<CentroCostoEntity> centrosCosto;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "DetalleCargaDocenteEntity{" +
                "id=" + id +
                ", idCargaDocente=" + idCargaDocente +
                ", idPrograma=" + idPrograma +
                ", idGrupo=" + idGrupo +
                ", idTipoActividad=" + idTipoActividad +
                ", idCentroCosto=" + idCentroCosto +
                ", horas=" + horas +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
