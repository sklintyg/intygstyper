package se.inera.certificate.modules.fk7263.model.codes;

import se.inera.certificate.model.Kod;

/**
 * @author andreaskaltenbach
 */
public final class ObservationsKoder {

    // Observationskategorier
    public static final Kod DIAGNOS = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "439401001");
    public static final Kod KROPPSFUNKTIONER = new Kod("1.2.752.116.1.1.3.1.1", "ICF", null, "b");
    public static final Kod AKTIVITETER_OCH_DELAKTIGHET = new Kod("1.2.752.116.1.1.3.1.1", "ICF", null, "d");

    // Observationskoder
    public static final Kod ARBETSFORMAGA = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "302119000");
    public static final Kod FORLOPP = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "288524001");
    public static final Kod SJUKDOM = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "39104002");
    public static final Kod GRAVIDITET = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "289908002");
    public static final Kod KOMPLIKATION_VID_GRAVIDITET = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "90821003");

    private ObservationsKoder() {
    }
}
