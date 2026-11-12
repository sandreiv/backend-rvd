/**
 * Aplicación: rvd
 * Archivo: TrabajadorLaborUnidadEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.util.Date;

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

@Getter
@Setter
@Entity
@Table(name = "TRABAJADORLABORUNIDAD", schema = "TALENTOV3")
public class TrabajadorLaborUnidadEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRLU_ID", nullable = false)
    private Long id;

    @Column(name = "PEGE_ID")
    private Long idPersonaGeneral;

    @Column(name = "LABO_ID")
    private Long idLabor;

    @Column(name = "UNID_ID")
    private Long idUnidad;

    @Column(name = "DOVI_ID")
    private Long idDocumentoVinculacion;

    @Column(name = "TRLU_FECHAINICIOESTADO")
    private Date fechaInicioEstado;

    @Column(name = "TRLU_TIPOASIGNACION")
    private String tipoAsignacion;

    @Column(name = "TRLU_ESTADO")
    private String estado;

    @Column(name = "TRLU_FECHAFINAL")
    private Date fechaFinal;

    @Column(name = "TRLU_FECHAINICIAL")
    private Date fechaInicial;

    @Column(name = "TRLU_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "TRLU_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private PersonaGeneralEntity personaGeneral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LABO_ID", insertable = false, updatable = false)
    private LaborEntity labor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNID_ID", insertable = false, updatable = false)
    private UnidadEntity unidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOVI_ID", insertable = false, updatable = false)
    private DocumentoVinculacionEntity documentoVinculacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TrabajadorLaborUnidadEntity{" +
                "id=" + id +
                ", idPersonaGeneral=" + idPersonaGeneral +
                ", idLabor=" + idLabor +
                ", idUnidad=" + idUnidad +
                ", idDocumentoVinculacion=" + idDocumentoVinculacion +
                ", fechaInicioEstado=" + fechaInicioEstado +
                ", tipoAsignacion=" + tipoAsignacion +
                ", estado=" + estado +
                ", fechaFinal=" + fechaFinal +
                ", fechaInicial=" + fechaInicial +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
