/**
 * Aplicación: rvd
 * Archivo: RubroEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 19/10/2026
 * Modificaciones:
 * 19/10/2026 - Andrés Hernández - Creación inicial
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
@Table(name = "RUBRO", schema = "TESORERIAV3")
public class RubroEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RUBR_ID", nullable = false)
    private Long id;

    @Column(name = "RUBR_IDANCESTRO")
    private Long idAncestro;

    @Column(name = "NIAX_ID")
    private String idNivelAuxiliar;

    @Column(name = "TIRU_ID")
    private String idTipoRubro;

    @Column(name = "RUGL_ID")
    private Long idRubroGlobal;

    @Column(name = "CLTR_ID")
    private Long idClaseTipoRubro;

    @Column(name = "RUBR_ESCUENTAMAYOR")
    private String esCuentaMayor;

    @Column(name = "RUBR_CODIGO")
    private String codigo;

    @Column(name = "RUBR_DESCRIPCION")
    private String descripcion;

    @Column(name = "RUBR_USAPLURIANUAL")
    private String usaPlurianual;

    @Column(name = "RUBR_CONTRIBUCION")
    private String contribucion;

    @Column(name = "RUBR_RESERVA")
    private String reserva;

    @Column(name = "RUBR_HOMOLOGACIONSIIF")
    private String homologacionSiif;

    @Column(name = "RUBR_CODIGOSNIES")
    private String codigoSnies;

    @Column(name = "RUBR_DESCRIPCIONSNIES")
    private String descripcionSnies;

    @Column(name = "RUBR_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "RUBR_FECHACAMBIO")
    private Date fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUBR_RUBRANCESTRO", insertable = false, updatable = false)
    private RubroEntity ancestro;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NIAX_ID", insertable = false, updatable = false)
    private NivelAuxiliarEntity nivelAuxiliar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIRU_ID", insertable = false, updatable = false)
    private TipoRubroEntity tipoRubro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUGL_ID", insertable = false, updatable = false)
    private RubroGlobalEntity rubroGlobal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLTR_ID", insertable = false, updatable = false)
    private ClaseTipoRubroEntity claseTipoRubro;
    */

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RubroEntity{" +
                "id=" + id +
                ", idAncestro=" + idAncestro +
                ", idNivelAuxiliar=" + idNivelAuxiliar +
                ", idTipoRubro=" + idTipoRubro +
                ", idRubroGlobal=" + idRubroGlobal +
                ", idClaseTipoRubro=" + idClaseTipoRubro +
                ", esCuentaMayor=" + esCuentaMayor +
                ", codigo=" + codigo +
                ", descripcion=" + descripcion +
                ", usaPlurianual=" + usaPlurianual +
                ", contribucion=" + contribucion +
                ", reserva=" + reserva +
                ", homologacionSiif=" + homologacionSiif +
                ", codigoSnies=" + codigoSnies +
                ", descripcionSnies=" + descripcionSnies +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
