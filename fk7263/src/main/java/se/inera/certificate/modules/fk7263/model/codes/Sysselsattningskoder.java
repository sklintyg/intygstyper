package se.inera.certificate.modules.fk7263.model.codes;

import se.inera.certificate.model.Kod;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class Sysselsattningskoder {
    public static final Kod NUVARANDE_ARBETE = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "224375002");
    public static final Kod ARBETSLOSHET = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "73438004");
    public static final Kod PAPPALEDIG = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "224458009");
    public static final Kod MAMMALEDIG = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "224457004");

    public static final Map<Kod, String> MAP_TO_FK7263 = new HashMap<>();

    static {
        MAP_TO_FK7263.put(NUVARANDE_ARBETE, "Nuvarande_arbete");
        MAP_TO_FK7263.put(ARBETSLOSHET, "Arbetsloshet");
        MAP_TO_FK7263.put(PAPPALEDIG, "Foraldraledighet");
        MAP_TO_FK7263.put(MAMMALEDIG, "Foraldraledighet");
    }

    private Sysselsattningskoder() {
    }
}
