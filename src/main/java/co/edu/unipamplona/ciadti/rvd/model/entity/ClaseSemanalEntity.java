/**
 * Aplicación: rvd
 * Archivo: ClaseSemanalEntity.java
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
@Table(name = "CLASESEMANAL", schema = "ACADEMICO")
public class ClaseSemanalEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLSE_ID", nullable = false)
    private Long id;

    @Column(name = "GRUP_ID")
    private Long idGrupo;

    @Column(name = "BLHO_IDINICIAL")
    private Long idBlhoInicial;

    @Column(name = "BLHO_IDFINAL")
    private Long idBlhoFinal;

    @Column(name = "CLSE_DIA")
    private Long dia;

    @Column(name = "CLSE_FECHAINICIO")
    private Date fechaInicio;

    @Column(name = "CLSE_FECHAFINAL")
    private Date fechaFinal;

    @Column(name = "CLSE_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "CLSE_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRUP_ID", insertable = false, updatable = false)
    private GrupoEntity grupo;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BHLO_IDINICIAL", insertable = false, updatable = false)
    private BhloEntity bhloInicial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BHLO_IDFINAL", insertable = false, updatable = false)
    private BhloEntity bhloFinal;
    */

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ClaseSemanalEntity{" +
                "id=" + id +
                ", idGrupo=" + idGrupo +
                ", idBlhoInicial=" + idBlhoInicial +
                ", idBlhoFinal=" + idBlhoFinal +
                ", dia=" + dia +
                ", fechaInicio=" + fechaInicio +
                ", fechaFinal=" + fechaFinal +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
