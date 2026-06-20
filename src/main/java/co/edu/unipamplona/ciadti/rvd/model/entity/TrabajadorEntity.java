/**
 * Aplicación: rvd
 * Archivo: TrabajadorEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 20/06/2026
 * Modificaciones:
 * 20/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TRABAJADOR", schema = "TALENTOV3")
public class TrabajadorEntity implements Serializable, Cloneable {

    @Id
    @Column(name = "PEGE_ID", nullable = false)
    private Long idPersonaGeneral;

    @Column(name = "TRAB_GRADO", nullable = false)
    private String grado;

    @Column(name = "TRAB_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "TRAB_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Column(name = "BAPA_ID")
    private Long idBancoPagaduria;

    @Column(name = "TICB_ID")
    private Long idTipoCuentaBancaria;

    @Column(name = "PERS_ID")
    private Long idPers;

    @Column(name = "TRAB_ACTIVO")
    private Long activo;

    @Column(name = "RECE_ID")
    private Long idRegimenCesantia;

    @Column(name = "EXPA_ID")
    private Long idExcensionParafiscal;

    @Column(name = "TRAB_SINDICATO")
    private String sindicato;

    @Column(name = "TRAB_TIPOPENSION")
    private String tipoPension;

    @Column(name = "TRAB_TRABAJADOREXTERNO")
    private String trabajadorExterno;

    @Column(name = "TRAB_MONITOR")
    private String monitor;

    @Column(name = "TICO_ID")
    private Long idTipoCotizante;

    @Column(name = "CACA_ID")
    private Long idCategoriaCatedratico;

    @Column(name = "TIPL_ID")
    private Long idTipoDePlantaSuip;

    @Column(name = "SUCO_ID")
    private Long idSubtipoCotizante;

    @Column(name = "TRAB_CODIGOISO")
    private String codigoIso;

    @Column(name = "TRAB_CONSECUTIVO")
    private Long consecutivo;

    @Column(name = "TRAB_ASIMILACIONOFICIAL")
    private String asimilacionOficial;

    @Column(name = "TRAB_ESPRACTICANTE")
    private String esPracticante;

    @Column(name = "TRAB_EXTRANJERONOCOTIZA")
    private String extranjeroNoCotiza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CACA_ID", insertable = false, updatable = false)
    private CategoriaCatedraticoEntity categoriaCatedratico;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TrabajadorEntity{" +
                "idPersonaGeneral=" + idPersonaGeneral +
                ", grado=" + grado +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", idBancoPagaduria=" + idBancoPagaduria +
                ", idTipoCuentaBancaria=" + idTipoCuentaBancaria +
                ", idPers=" + idPers +
                ", activo=" + activo +
                ", idRegimenCesantia=" + idRegimenCesantia +
                ", idExcensionParafiscal=" + idExcensionParafiscal +
                ", sindicato=" + sindicato +
                ", tipoPension=" + tipoPension +
                ", trabajadorExterno=" + trabajadorExterno +
                ", monitor=" + monitor +
                ", idTipoCotizante=" + idTipoCotizante +
                ", idCategoriaCatedratico=" + idCategoriaCatedratico +
                ", idTipoDePlantaSuip=" + idTipoDePlantaSuip +
                ", idSubtipoCotizante=" + idSubtipoCotizante +
                ", codigoIso=" + codigoIso +
                ", consecutivo=" + consecutivo +
                ", asimilacionOficial=" + asimilacionOficial +
                ", esPracticante=" + esPracticante +
                ", extranjeroNoCotiza=" + extranjeroNoCotiza +
                '}';
    }
}
/* 20/06/2026 @:Sebastian Jaimes */
