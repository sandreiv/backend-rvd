/**
 * Aplicación: rvd
 * Archivo: DocenteClaseSemanalEntity.java
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
@Table(name = "DOCENTECLASESEMANAL", schema = "ACADEMICO")
@IdClass(DocenteClaseSemanalEntityId.class)
public class DocenteClaseSemanalEntity {
    
    @Id
    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Id
    @Column(name = "CLSE_ID", nullable = false)
    private Long idClaseSemanal;

    @Column(name = "HCAR_ID")
    private Long idHcar;

    @Column(name = "DOCS_SOBRECARGA")
    private String sobrecarga;

    @Column(name = "DOCS_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "DOCS_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEGE_ID", insertable = false, updatable = false)
    private PersonaGeneralEntity personaGeneralEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLSE_ID", insertable = false, updatable = false)
    private ClaseSemanalEntity claseSemanal;

    /* RELACIONES ADICIONALES
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
        return "DocenteClaseSemanalEntity{" +
                "idPersonaGeneral=" + idPersonaGeneral +
                ", idClaseSemanal=" + idClaseSemanal +
                ", idHcar=" + idHcar +
                ", sobrecarga=" + sobrecarga +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
