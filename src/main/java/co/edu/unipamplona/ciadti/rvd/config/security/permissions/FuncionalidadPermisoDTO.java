/**
 * Aplicación: rvd
 * Archivo: FuncionalidadPermisoDTO.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security.permissions
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 * 12/08/2026 - Sebastian Jaimes - nombreFuncion (LISTAR|GUARDAR|…)
 */
package co.edu.unipamplona.ciadti.rvd.config.security.permissions;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuncionalidadPermisoDTO {
    private String codigo;
    private String nombre;
    private String urlRecurso;
    private String metodo;
    @JsonAlias({ "nombrefuncion", "func_nombrefuncion", "nombreFuncion" })
    private String nombreFuncion;
    private Long idFuncionalidadPadre;
}
