/**
 * Aplicación: rvd
 * Archivo: RestriccionCargaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 02/06/2026
 * Modificaciones:
 * 02/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "RESTRICCIONCARGA", schema = "RVD")
public class RestriccionCargaEntity implements Serializable, Cloneable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOCO_ID", nullable = false)
    private Long idModalidadContratacion;

    @Column(name = "RECA_MAXIMO")
    private String maximo;

    @Column(name = "RECA_MINIMO")
    private String minimo;

    @Column(name = "RECA_INVESTIGACION")
    private String investigacion;

    @Column(name = "RECA_FORMAPAGO")
    private String formaPago;

    @Column(name = "RECA_TIPOHORAS")
    private String tipoHoras;

    @Column(name = "RECA_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "RECA_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOCO_ID", insertable = false, updatable = false)
    private ModalidadContratacionEntity modalidadContratacion;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RestriccionCargaEntity{" +
                "idModalidadContratacion=" + idModalidadContratacion +
                ", maximo=" + maximo +
                ", minimo=" + minimo +
                ", investigacion=" + investigacion +
                ", formaPago=" + formaPago +
                ", tipoHoras=" + tipoHoras +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }

}
/* 02/06/2026 @:Sebastian Jaimes */
