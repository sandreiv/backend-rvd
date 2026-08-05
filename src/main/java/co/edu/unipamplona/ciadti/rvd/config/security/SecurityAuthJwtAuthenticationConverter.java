/**
 * Aplicación: rvd
 * Archivo: SecurityAuthJwtAuthenticationConverter.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityAuthJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    private final ExternalJwtUserResolver externalJwtUserResolver;
    private final SecurityAuthProperties securityAuthProperties;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return externalJwtUserResolver.resolve(jwt)
                .map(user -> (AbstractAuthenticationToken)
                        new UsernamePasswordAuthenticationToken(
                                user, jwt, user.getAuthorities()))
                .orElseThrow(() -> new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_token",
                                "Sin roles para application-id "
                                        + securityAuthProperties.applicationId(),
                                null)));
    }
}
