/**
 * Aplicación: rvd
 * Archivo: AuthUserDetails.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial (adopción SecurityAuth)
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUserDetails implements UserDetails {

    private final Long idPersonaGeneral;
    private final String username;
    private final List<String> roles;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUserDetails(
            Long idPersonaGeneral,
            String username,
            List<String> roles,
            Collection<? extends GrantedAuthority> authorities) {
        this.idPersonaGeneral = idPersonaGeneral;
        this.username = username;
        this.roles = roles == null ? List.of() : List.copyOf(roles);
        this.authorities = authorities;
    }

    public Long getIdPersonaGeneral() {
        return idPersonaGeneral;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
