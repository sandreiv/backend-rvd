/**
 * Aplicación: rvd
 * Archivo: ClasificacionDocenteEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 05/11/2026
 * Modificaciones:
 * 05/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CLASIFICACIONDOCENTE", schema = "ACADEMICO")
public class ClasificacionDocenteEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLDO_ID", nullable = false)
    private Long id;

    @Column(name = "TINO_ID", nullable = true)
    private Long tinoId;

    @Column(name = "CLDO_DESCRIPCION")
    private String descripcion;

    @Column(name = "CLDO_DOCENTEOCASIONAL")
    private String docenteOcasional;

    @Column(name = "CLDO_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "CLDO_FECHACAMBIO")
    private Date fechaCambio;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TINO_ID", insertable = false, updatable = false)
    private TinoEntity tino;
    */

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ClasificacionDocenteEntity{" +
                "id=" + id +
                ", tinoId=" + tinoId +
                ", descripcion=" + descripcion +
                ", docenteOcasional=" + docenteOcasional +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
