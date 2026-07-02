/**
 * Aplicación: rvd
 * Archivo: PensumMateriaEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 30/06/2026
 * Modificaciones:
 * 30/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(PensumMateriaEntityId.class)
@Table(name = "PENSUMMATERIA", schema = "ACADEMICO")
public class PensumMateriaEntity implements Serializable, Cloneable {

    @Id
    @Column(name = "MATE_CODIGOMATERIA", nullable = false)
    private String codigoMateria;

    @Id
    @Column(name = "PENS_ID", nullable = false)
    private Long idPensum;

    @Column(name = "PEMA_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "PEMA_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Column(name = "PEMA_CREDITOREQUISITO")
    private Long creditoRequisito;

    @Column(name = "PEMA_PERIODO")
    private Long periodo;

    @Column(name = "MATE_OBLIGATORIA")
    private String obligatoria;

    @Column(name = "CICU_ID")
    private Long idCicloCurricular;

    @Column(name = "PROG_ID")
    private Long idPrograma;

    @Column(name = "PEMA_CUENTAPROMEDIO")
    private String cuentaPromedio;

    @Column(name = "PEMA_HOMOLOGABLE")
    private String homologable;

    @Column(name = "PEMA_HABILITABLE")
    private String habilitable;

    @Column(name = "PEMA_VALIDABLE")
    private String validable;

    @Column(name = "PEMA_TIPO")
    private String tipo;

    @Column(name = "NORG_ID")
    private Long idNormaGeneral;

    @Column(name = "PEMA_ESTADO")
    private String estado;

    @Column(name = "NUFO_ID")
    private Long idNucleoFormacion;

    @Column(name = "PEMA_REMEDIABLE")
    private String remediable;

    @Column(name = "CAFO_ID")
    private Long idCategoriaFormacion;

    @Column(name = "NUIN_ID")
    private Long idNucleoIntegracion;

    @Column(name = "PEMA_PORCENTAJEPROMEDIO")
    private Long porcentajePromedio;

    @Column(name = "CAPM_ID")
    private Long idCategoriaPensumMateria;

    @Column(name = "PEMA_ESPROPEDEUTICA")
    private String esPropedeutica;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PensumMateriaEntity{" +
                "codigoMateria=" + codigoMateria +
                ", idPensum=" + idPensum +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", creditoRequisito=" + creditoRequisito +
                ", periodo=" + periodo +
                ", obligatoria=" + obligatoria +
                ", idCicloCurricular=" + idCicloCurricular +
                ", idPrograma=" + idPrograma +
                ", cuentaPromedio=" + cuentaPromedio +
                ", homologable=" + homologable +
                ", habilitable=" + habilitable +
                ", validable=" + validable +
                ", tipo=" + tipo +
                ", idNormaGeneral=" + idNormaGeneral +
                ", estado=" + estado +
                ", idNucleoFormacion=" + idNucleoFormacion +
                ", remediable=" + remediable +
                ", idCategoriaFormacion=" + idCategoriaFormacion +
                ", idNucleoIntegracion=" + idNucleoIntegracion +
                ", porcentajePromedio=" + porcentajePromedio +
                ", idCategoriaPensumMateria=" + idCategoriaPensumMateria +
                ", esPropedeutica=" + esPropedeutica +
                '}';
    }
}
/* 30/06/2026 @:Sebastian Jaimes */
