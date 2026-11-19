/**
 * Aplicación: rvd
 * Archivo: MateriaDocenteUnidadEntity.java
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
@Table(name = "MATERIADOCENTEUNIDAD", schema = "ACADEMICO")
@IdClass(MateriaDocenteUnidadEntityId.class)
public class MateriaDocenteUnidadEntity {
    
    @Id
    @Column(name = "DOUN_ID", nullable = false)
    private Long idDocenteUnidad;

    @Id
    @Column(name = "MATE_CODIGOMATERIA", nullable = false)
    private String codigoMateria;

    @Column(name = "MADU_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "MADU_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOUN_ID", insertable = false, updatable = false)
    private DocenteUnidadEntity docenteUnidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATE_CODIGOMATERIA", insertable = false, updatable = false)
    private MateriaEntity materia;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "MateriaDocenteUnidadEntity{" +
                "idDocenteUnidad=" + idDocenteUnidad +
                ", codigoMateria=" + codigoMateria +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
