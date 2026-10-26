/**
 * Aplicación: rvd
 * Archivo: TipoDocumentoContratacionEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 26/10/2026
 * Modificaciones:
 * 26/10/2026 - CIADTI - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;

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
@Table(name = "TIPODOCUMENTOCONTRATACION", schema = "RVD")
public class TipoDocumentoContratacionEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIDC_ID", nullable = false)
    private Long id;

    @Column(name = "TIDC_NOMBRE", nullable = false, length = 150)
    private String nombre;

    @Column(name = "TIDC_DESCRIPCION", length = 250)
    private String descripcion;

    /**
     * Bandera '1' = obligatorio, '0' = opcional (CHECK CK_TIPODOCUMENTOCONTRATACION_OBL).
     */
    @Column(name = "TIDC_OBLIGATORIO", nullable = false, length = 1)
    private String obligatorio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TipoDocumentoContratacionEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", obligatorio='" + obligatorio + '\'' +
                '}';
    }
}
