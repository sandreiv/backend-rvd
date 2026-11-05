/**
 * Aplicación: rvd
 * Archivo: FormaPagoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/11/2026
 * Modificaciones:
 * 04/11/2026 - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Forma de pago de los contratos (BIENESV3.FORMAPAGO).
 * Catálogo compartido con el módulo de bienes y contratos: aquí solo se lee.
 */
@Getter
@Setter
@Entity
@Table(name = "FORMAPAGO", schema = "BIENESV3")
public class FormaPagoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOPA_ID", nullable = false)
    private Long id;

    @Column(name = "FOPA_DESCRIPCION")
    private String descripcion;

    @Column(name = "FOPA_CANTDIA")
    private String cantidadDias;

    @Column(name = "FOPA_REGISTRADOPOR")
    private String registradoPor;

    @Column(name = "FOPA_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "FormaPagoEntity{" +
                "id=" + id +
                ", descripcion=" + descripcion +
                ", cantidadDias=" + cantidadDias +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
