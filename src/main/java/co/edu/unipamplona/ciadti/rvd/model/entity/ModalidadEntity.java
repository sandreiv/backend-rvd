/**
 * Aplicación: rvd
 * Archivo: ModalidadEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 16/04/2026
 * Modificaciones:
 * 16/04/2026 - Leonel Antonio Pérez Ríos - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.model.entity;

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
@Table(name = "MODALIDAD", schema = "ACADEMICO")
public class ModalidadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MODA_ID", nullable = false)
    private Long id;

    @Column(name = "MODA_DESCRIPCION", nullable = false)
    private String descripcion;

    @Column(name = "NIED_ID", nullable = false)
    private Long idNivelEducativo;

    @Column(name = "MODA_PUNTOS")
    private Double puntos;

    @Column(name = "MODA_CODIGO")
    private String codigo;

    @Column(name = "MODA_FECHACAMBIO", nullable = false)
    private String fechaCambio;

    @Column(name = "MODA_REGISTRADOPOR", nullable = false)
    private String registradoPor;
}

/* 16/04/2026 @:Leonel Antonio Pérez Ríos */
