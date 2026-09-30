/**
 * Aplicación: btaa
 * Archivo: NanoIdGenerator.java
 * Paquete: co.edu.unipamplona.ciadti.btaa.util
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 18/06/2026
 * Modificaciones:
 * 18/06/2026 - Implementación inline de NanoID (mismo alfabeto y misma
 *                longitud que la librería oficial com.aventrix.nanoid),
 *                sin agregar dependencias externas al pom.xml.
 *
 *  - Alfabeto: 64 chars (A-Z, a-z, 0-9, "-", "_") → log2(64) = 6 bits/char.
 *  - Longitud por defecto: 21 → 21 * 6 = 126 bits de entropía.
 *    (La librería oficial usa 21 por default; alineamos con ese valor
 *    para mantener compatibilidad si en el futuro se agrega la dep.)
 *  - Generador: java.security.SecureRandom (CSPRNG del JDK).
 *  - Probabilidad de colisión con N identificadores y 126 bits ≈
 *    N^2 / 2^127 → con 1 millón de inscripciones ~10⁻²⁶ (despreciable).
 */
package co.edu.unipamplona.ciadti.rvd.util;

import java.security.SecureRandom;

public final class NanoIdGenerator {

    /** Alfabeto por default de NanoID (64 caracteres URL-safe). */
    public static final String DEFAULT_ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

    /** Longitud por default, alineada con com.aventrix.nanoid 0.6.x. */
    public static final int DEFAULT_LENGTH = 21;

    private static final SecureRandom RANDOM = new SecureRandom();

    private NanoIdGenerator() {
        // Utility class.
    }

    /**
     * Genera un NanoID con el alfabeto y longitud por default.
     */
    public static String random() {
        return random(DEFAULT_LENGTH);
    }

    /**
     * Genera un NanoID con la longitud indicada, usando el alfabeto por default.
     */
    public static String random(int length) {
        return random(DEFAULT_ALPHABET, length);
    }

    /**
     * Genera un NanoID con el alfabeto y longitud dados.
     *
     * Implementación equivalente a com.aventrix.nanoid.NanoId.random(size):
     *   - Enmascara los bytes aleatorios con el tamaño del alfabeto
     *     (operación AND con 0x3F para alfabeto de 64 chars, garantizando
     *     un índice válido sin sesgo).
     *   - Mapea cada byte a un carácter del alfabeto.
     *
     * Para alfabetos con tamaños potencia de 2 esto es exacto; con
     * DEFAULT_ALPHABET (64) es exacto sin rechazo.
     */
    public static String random(String alphabet, int length) {
        if (length < 1) {
            throw new IllegalArgumentException("La longitud debe ser >= 1");
        }
        if (alphabet == null || alphabet.isEmpty()) {
            throw new IllegalArgumentException("El alfabeto no puede estar vacío");
        }

        final int alphabetSize = alphabet.length();
        // Para tamaños potencia de 2 (ej. 64), la máscara es (size - 1).
        final int mask = alphabetSize - 1;
        final boolean isPowerOfTwo = (alphabetSize & mask) == 0;

        final char[] result = new char[length];
        final byte[] buffer = new byte[length];

        for (int i = 0; i < length; i++) {
            byte b;
            if (isPowerOfTwo) {
                // Tamaño potencia de 2: un solo byte alcanza, sin sesgo.
                RANDOM.nextBytes(buffer);
                b = buffer[0];
                result[i] = alphabet.charAt(b & mask);
            } else {
                // Tamaño arbitrario: usa máscara del mismo tamaño de bits
                // y rechaza valores fuera de rango (módulo bias).
                int bitsPerChar = (int) Math.ceil(Math.log(alphabetSize) / Math.log(2));
                int maxValid = (1 << bitsPerChar) - 1;
                int randomValue;
                do {
                    RANDOM.nextBytes(buffer);
                    randomValue = 0;
                    for (int bit = 0; bit < bitsPerChar; bit++) {
                        randomValue = (randomValue << 1) | (buffer[0] & 1);
                        buffer[0] >>= 1;
                    }
                } while (randomValue > maxValid || randomValue >= alphabetSize);
                result[i] = alphabet.charAt(randomValue);
            }
        }
        return new String(result);
    }
}

/* 18/06/2026 @: Implementación inline de NanoID */
