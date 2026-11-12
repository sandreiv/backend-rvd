/**
 * Aplicación: rvd
 * Archivo: EstadoTrabajadorEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 12/11/2026
 * Modificaciones:
 * 12/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

import java.util.Date;

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
@Table(name = "ESTADOTRABAJADOR", schema = "TALENTOV3")
public class EstadoTrabajadorEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTR_ID", nullable = false)
    private Long id;

    @Column(name = "ESTR_DESCRIPCION")
    private String descripcion;

    @Column(name = "ESTR_TIPORETIRO")
    private String tipoRetiro;

    @Column(name = "ESTR_INICIAL")
    private String inicial;

    @Column(name = "ESTR_REGISTRADOPOR")
    private String registradoPor;
    
    @Column(name = "ESTR_FECHACAMBIO")
    private Date fechaCambio;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "EstadoTrabajadorEntity{" +
                "id=" + id +
                ", descripcion=" + descripcion +
                ", tipoRetiro=" + tipoRetiro +
                ", inicial=" + inicial +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                '}';
    }
}
