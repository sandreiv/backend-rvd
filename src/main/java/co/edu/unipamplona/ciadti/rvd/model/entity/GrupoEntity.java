/**
 * Aplicación: rvd
 * Archivo: GrupoEntity.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.model.entity
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 10/06/2026
 * Modificaciones:
 * 10/06/2026 - Sebastian Jaimes - Creación inicial
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

@Getter
@Setter
@Entity
@Table(name = "GRUPO", schema = "ACADEMICO")
public class GrupoEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GRUP_ID", nullable = false)
    private Long id;

    @Column(name = "TIGR_ID", nullable = false)
    private Long idTipoGrupo;

    @Column(name = "MATE_CODIGOMATERIA", nullable = false)
    private String codigoMateria;

    @Column(name = "UNID_IDREGIONAL", nullable = false)
    private Long idUnidadRegional;

    @Column(name = "SIEV_ID")
    private Long idSistemaEvaluacion;

    @Column(name = "SUBM_ID")
    private Long idSubMateria;

    @Column(name = "PEUN_ID", nullable = false)
    private Long idPeriodoUniversidad;

    @Column(name = "GRUP_NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "GRUP_CAPACIDAD", nullable = false)
    private Long capacidad;

    @Column(name = "GRUP_REGISTRADOPOR", nullable = false)
    private String registradoPor;

    @Column(name = "GRUP_FECHACAMBIO", nullable = false)
    private Date fechaCambio;

    @Column(name = "GRUP_FECHAINICIAL", nullable = false)
    private Date fechaInicial;

    @Column(name = "GRUP_FECHAFINAL", nullable = false)
    private Date fechaFinal;

    @Column(name = "GRUP_ACTIVO", nullable = false)
    private String activo;

    @Column(name = "GRUP_HORARIOXDIA", nullable = false)
    private String horarioPorDia;

    @Column(name = "GRUP_CUPOS", nullable = false)
    private Long cupos;

    @Column(name = "GRUP_HISTORICO", nullable = false)
    private String historico;

    @Column(name = "GRUP_CUPOMINIMO", nullable = false)
    private Long cupoMinimo;

    @Column(name = "GRUP_IDPADRE")
    private Long idPadre;

    @Column(name = "GRUP_COPIADO")
    private String copiado;

    @Column(name = "GRUP_PEUNDESTINO")
    private Long idPeriodoUniversidadDestino;

    @Column(name = "MATE_CREADOPRE")
    private String matePre;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "GrupoEntity{" +
                "id=" + id +
                ", nombre=" + nombre +
                ", capacidad=" + capacidad +
                ", codigoMateria=" + codigoMateria +
                ", idTipoGrupo=" + idTipoGrupo +
                ", registradoPor=" + registradoPor +
                ", fechaCambio=" + fechaCambio +
                ", idUnidadRegional=" + idUnidadRegional +
                ", fechaInicial=" + fechaInicial +
                ", fechaFinal=" + fechaFinal +
                ", idSistemaEvaluacion=" + idSistemaEvaluacion +
                ", activo=" + activo +
                ", horarioPorDia=" + horarioPorDia +
                ", cupos=" + cupos +
                ", idSubMateria=" + idSubMateria +
                ", historico=" + historico +
                ", cupoMinimo=" + cupoMinimo +
                ", idPeriodoUniversidad=" + idPeriodoUniversidad +
                ", idPadre=" + idPadre +
                ", copiado=" + copiado +
                ", idPeriodoUniversidadDestino=" + idPeriodoUniversidadDestino +
                ", creadoPre=" + creadoPre +
                '}';
    }
}
