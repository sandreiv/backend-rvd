/**
 * Aplicación: rvd
 * Archivo: SecurityAuthTokenValidator.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.config.security
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 04/08/2026
 * Modificaciones:
 * 04/08/2026 - Sebastian Jaimes - Creación inicial (adopción SecurityAuth)
 */
package co.edu.unipamplona.ciadti.rvd.config.security;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAuthTokenValidator {

    private final SecurityAuthProperties properties;
    private JwtDecoder decoder;

    @PostConstruct
    void init() {
        try {
            NimbusJwtDecoder built = buildDecoder();
            if (built == null) {
                log.info("SecurityAuth JWT: sin jwk-set-uri ni public-key-pem; deshabilitado");
                return;
            }
            applyIssuer(built);
            this.decoder = built;
        } catch (Exception e) {
            log.error("SecurityAuth JWT: no se pudo inicializar el decoder", e);
            this.decoder = null;
        }
    }

    private NimbusJwtDecoder buildDecoder() throws Exception {
        if (StringUtils.hasText(properties.jwkSetUri())) {
            log.info("SecurityAuth JWT: decoder JWKS activo ({})", properties.jwkSetUri());
            return NimbusJwtDecoder.withJwkSetUri(properties.jwkSetUri()).build();
        }
        if (StringUtils.hasText(properties.publicKeyPem())) {
            RSAPublicKey publicKey = parseRsaPublicKeyFromPem(properties.publicKeyPem());
            log.info("SecurityAuth JWT: decoder RSA (PEM) activo");
            return NimbusJwtDecoder.withPublicKey(publicKey).build();
        }
        return null;
    }

    private void applyIssuer(NimbusJwtDecoder d) {
        if (StringUtils.hasText(properties.issuer())) {
            d.setJwtValidator(JwtValidators.createDefaultWithIssuer(properties.issuer()));
        }
    }

    public boolean isActive() {
        return decoder != null;
    }

    public JwtDecoder decoder() {
        if (decoder == null) {
            throw new IllegalStateException("Decoder SecurityAuth no disponible");
        }
        return decoder;
    }

    public Jwt decode(String token) {
        return decoder().decode(token);
    }

    private static RSAPublicKey parseRsaPublicKeyFromPem(String pem) throws Exception {
        String body = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                .replace("-----END RSA PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(body);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
