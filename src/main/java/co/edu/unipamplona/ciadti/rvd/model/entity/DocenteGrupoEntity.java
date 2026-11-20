/**
 * Aplicación: rvd
 * Archivo: DocenteGrupoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/11/2026
 * Modificaciones:
 * 19/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DOCENTEGRUPO", schema = "ACADEMICO")
@IdClass(DocenteGrupoEntityId.class)
public class DocenteGrupoEntity {
    
    @Id
    @Column(name = "GRUP_ID", nullable = false)
    private Long idGrupo;

    @Id
    @Column(name = "DOUN_ID", nullable = false)
    private Long idDocenteUnidad;

    @Column(name = "TCVE_ID")
    private Long idTcve;

    @Column(name = "NORG_ID")
    private Long idNormaGeneral;

    @Column(name = "HCAR_ID")
    private Long idHcar;

    @Column(name = "DOGR_RESPONSABLE")
    private String responsable;

    @Column(name = "DOGR_PESOHORACLASE")
    private Long pesoHoraClase;

    @Column(name = "DOGR_HORASEMANADIRIGIDO")
    private Long horaSemanaDirigido;

    @Column(name = "DOGR_DICTA")
    private String dicta;

    @Column(name = "DOGR_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "DOGR_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOUN_ID", insertable = false, updatable = false)
    private DocenteUnidadEntity docenteUnidad;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TCVE_ID", insertable = false, updatable = false)
    private TcveEntity tcve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NORG_ID", insertable = false, updatable = false)
    private NormaGeneralEntity normaGeneral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HCAR_ID", insertable = false, updatable = false)
    private HcarEntity hcar;
    */

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "DocenteGrupoEntity{" +
                "idGrupo=" + idGrupo +
                ", idDocenteUnidad=" + idDocenteUnidad +
                ", idTcve=" + idTcve +
                ", idNormaGeneral=" + idNormaGeneral +
                ", idHcar=" + idHcar +
                ", responsable=" + responsable +
                ", pesoHoraClase=" + pesoHoraClase +
                ", horaSemanaDirigido=" + horaSemanaDirigido +
                ", dicta=" + dicta +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
