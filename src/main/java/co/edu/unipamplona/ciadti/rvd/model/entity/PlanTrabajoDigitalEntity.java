/**
 * Aplicación: rvd
 * Archivo: PlanTrabajoDigitalEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 13/10/2026
 * Modificaciones:
 * 13/10/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "PlanTrabajoDigitalEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_PLANTRABAJODIGITAL",
    parameters = {
        @StoredProcedureParameter(name = "P_PTDI_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_PTDI_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "PLANTRABAJODIGITAL", schema = "RVD")
public class PlanTrabajoDigitalEntity implements Serializable, Cloneable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PTDI_ID", nullable = false)
    private Long id;

    @Column(name = "DECD_ID")
    private Long idDetalleCargaDocente;

    @Column(name = "DNCD_ID")
    private Long idDetalleNovedadCargaDocente;

    @Column(name = "TIAC_ID")
    private Long idTipoActividad;

    @Column(name = "PEGE_ID")
    private Long idPersonaGeneral;

    @Column(name = "PTDI_NIVEL")
    private String nivel;

    @Column(name = "PTDI_PONDERADO")
    private Long ponderado;

    @Column(name = "PTDI_ESTADO")
    private String estado;

    @Column(name = "PTDI_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "PTDI_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DECD_ID", insertable = false, updatable = false)
    private DetalleCargaDocenteEntity detalleCargaDocenteEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DNCD_ID", insertable = false, updatable = false)
    private DetalleNovedadCargaDocenteEntity detalleNovedadCargaDocenteEntity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIAC_ID", insertable = false, updatable = false)
    private TipoActividadesEntity tipoActividadesEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private PersonaGeneralEntity personaGeneralEntity;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PlanTrabajoDigitalEntity{" +
                "id=" + id +
                ", idDetalleCargaDocente=" + idDetalleCargaDocente +
                ", idDetalleNovedadCargaDocente=" + idDetalleNovedadCargaDocente +
                ", idTipoActividad=" + idTipoActividad +
                ", idPersonaGeneral=" + idPersonaGeneral +
                ", nivel=" + nivel +
                ", ponderado=" + ponderado +
                ", estado=" + estado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
