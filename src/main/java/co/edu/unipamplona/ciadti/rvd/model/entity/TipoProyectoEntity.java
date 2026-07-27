/**
 * Aplicación: rvd
 * Archivo: TipoProyectoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@NamedStoredProcedureQuery(
    name = "TipoProyectoEntity.deleteByProcedure",
    procedureName = "RVD.PR_RVD_D_TIPOPROYECTO",
    parameters = {
        @StoredProcedureParameter(name = "P_TIPR_ID", mode = ParameterMode.IN, type = Long.class),
        @StoredProcedureParameter(name = "P_TIPR_REGISTRADOPOR", mode = ParameterMode.IN, type = String.class),
        @StoredProcedureParameter(name = "P_EXITO", mode = ParameterMode.OUT, type = BigDecimal.class)
    }
)
@Getter
@Setter
@Entity
@Table(name = "TIPOPROYECTO", schema = "RVD")
public class TipoProyectoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIPR_ID", nullable = false)
    private Long id;

    @Column(name = "TIPR_NOMBRE")
    private String nombre;

    @Column(name = "TIPR_DESCRIPCION")
    private String descripcion;

    @Column(name = "TIPR_MINIMOPARTICIPANTES")
    private String minimoParticipantes;

    @Column(name = "TIPR_MAXIMOPARTICIPANTES")
    private String maximoParticipantes;

    @Column(name = "TIPR_MONTOMAXIMO")
    private String montoMaximo;

    @Column(name = "TIPR_MINIMOPRODUCTOS")
    private String minimoProductos;

    @Column(name = "TIPR_MINIMOCONOCIMIENTOTI")
    private String minimoConocimientoTi;

    @Column(name = "TIPR_TIPO")
    private String tipo;

    @Column(name = "TIPR_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "TIPR_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TipoProyectoEntity{" +
                "id=" + id +
                ", nombre=" + nombre +
                ", descripcion=" + descripcion +
                ", minimoParticipantes=" + minimoParticipantes +
                ", maximoParticipantes=" + maximoParticipantes +
                ", montoMaximo=" + montoMaximo +
                ", minimoProductos=" + minimoProductos +
                ", minimoConocimientoTi=" + minimoConocimientoTi +
                ", tipo=" + tipo +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
