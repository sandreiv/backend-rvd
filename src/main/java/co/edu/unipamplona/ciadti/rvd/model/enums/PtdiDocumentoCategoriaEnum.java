package co.edu.unipamplona.ciadti.rvd.model.enums;

public enum PtdiDocumentoCategoriaEnum {

    CERTIFICADO_SGSST(
            "Certificado SG-SST - 50 horas"
    ),

    CLAUSULA_CONFIDENCIALIDAD(
            "Cláusula de confidencialidad"
    ),

    COMPROMISO_ANTICORRUPCION(
            "Compromiso anticorrupción y antisoborno"
    ),

    INHABILIDADES_DELITOS_SEXUALES(
            "Consulta de inhabilidades por delitos sexuales"
    ),

    CONTRALORIA(
            "Contraloría"
    ),

    DECLARACION_BIENES_CONFLICTOS(
            "Declaración de bienes y rentas / conflictos de interés"
    ),

    DECLARACION_JURAMENTADA_BIENES(
            "Declaración juramentada de bienes y rentas"
    ),

    DECLARACION_INHABILIDADES(
            "Declaración sobre causales de inhabilidad e incompatibilidad"
    ),

    REDAM(
            "REDAM"
    ),

    POLICIA_NACIONAL(
            "Policía Nacional"
    ),

    PROCURADURIA(
            "Procuraduría"
    ),

    MEDIDAS_CORRECTIVAS(
            "Sistema Nacional de Medidas Correctivas"
    ),

    EXPERIENCIA_LABORAL(
            "Certificados experiencia laboral"
    ),

    DIPLOMAS_EDUCACION_NO_FORMAL(
            "Diplomas educación no formal"
    ),

    DOCUMENTO_IDENTIDAD(
            "Fotocopia documento de identidad"
    ),

    HOJA_VIDA_DAFP(
            "Hoja de vida DAFP"
    ),

    LIBRETA_MILITAR(
            "Libreta militar"
    ),

    TARJETA_PROFESIONAL(
            "Tarjeta profesional"
    );

    private final String nombre;

    PtdiDocumentoCategoriaEnum(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return name();
    }

    public String getNombre() {
        return nombre;
    }
}