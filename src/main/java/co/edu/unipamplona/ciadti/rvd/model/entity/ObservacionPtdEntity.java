/**
 * Aplicación: rvd
 * Archivo: ObservacionPtdEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 14/10/2026
 * Modificaciones:
 * 14/10/2026 - Andrés Hernández - Creación inicial
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "OBSERVACIONPTD", schema = "RVD")
public class ObservacionPtdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OBPT_ID", nullable = false)
    private Long id;

    @Column(name = "CADO_ID")
    private Long idCargaDocente;

    @Column(name = "PEGE_IDREGISTRA")
    private Long idPersonaGeneralRegistra;

    @Column(name = "OBPT_TEXTO")
    private String texto;

    @Column(name = "OBPT_RETROALIMENTACION")
    private String retroalimentacion;

    @Column(name = "OBPT_PEGEROL")
    private String rolPersonaGeneralRegistra;

    @Column(name = "OBPT_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "OBPT_FECHACAMBIO")
    private Date fechaCambio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CADO_ID", insertable = false, updatable = false)
    private CargaDocenteEntity cargaDocenteEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEGE_IDREGISTRA", insertable = false, updatable = false)
    private PersonaGeneralEntity personaRegistra;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ObservacionPtdEntity{" +
                "id=" + id +
                ", idCargaDocente=" + idCargaDocente +
                ", idPersonaGeneralRegistra=" + idPersonaGeneralRegistra +
                ", texto=" + texto +
                ", retroalimentacion=" + retroalimentacion +
                ", rolPersonaGeneralRegistra=" + rolPersonaGeneralRegistra +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
