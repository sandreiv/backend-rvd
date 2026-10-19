/**
 * Aplicación: rvd
 * Archivo: RubroVigenciaEntity.java
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
@Table(name = "RUBROVIGENCIA", schema = "TESORERIAV3")
public class RubroVigenciaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RUVI_ID", nullable = false)
    private Long id;

    @Column(name = "VIGE_ID")
    private Long idVigencia;

    @Column(name = "RUBR_ID")
    private Long idRubro;

    @Column(name = "RGVI_ID")
    private Long idRubroGlobalVigencia;

    @Column(name = "RUVI_IDANCESTRO")
    private Long idAncestro;

    @Column(name = "RUVI_CODIGORUBRO")
    private String codigoRubro;

    @Column(name = "RUVI_DESCRIPCIONRUBRO")
    private String descripcionRubro;

    @Column(name = "RUVI_ESCUENTAMAYOR")
    private String esCuentaMayor;

    @Column(name = "RUVI_USAPLURIANUAL")
    private String usaPlurianual;

    @Column(name = "RUVI_RESERVA")
    private String reserva;

    @Column(name = "RUVI_NATURALEZA")
    private String naturaleza;

    @Column(name = "RUVI_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "RUVI_FECHACAMBIO")
    private Date fechaCambio;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VIGE_ID", insertable = false, updatable = false)
    private VigenciaEntity vigencia;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUBR_ID", insertable = false, updatable = false)
    private RubroEntity rubro;

    /* RELACIONES ADICIONALES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RGVI_ID", insertable = false, updatable = false)
    private RubroGlobalVigenciaEntity rubroGlobalVigencia;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUVI_IDANCESTRO", insertable = false, updatable = false)
    private RubroVigenciaEntity ancestro;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RubroVigenciaEntity{" +
                "id=" + id +
                ", idVigencia=" + idVigencia +
                ", idRubro=" + idRubro +
                ", idRubroGlobalVigencia=" + idRubroGlobalVigencia +
                ", idAncestro=" + idAncestro +
                ", codigoRubro=" + codigoRubro +
                ", descripcionRubro=" + descripcionRubro +
                ", esCuentaMayor=" + esCuentaMayor +
                ", usaPlurianual=" + usaPlurianual +
                ", reserva=" + reserva +
                ", naturaleza=" + naturaleza +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
