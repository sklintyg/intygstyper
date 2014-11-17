package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;
import static se.inera.certificate.modules.fk7263.validator.ProgrammaticTransportValidator.findAktivitetWithCode;
import static se.inera.certificate.modules.fk7263.validator.ProgrammaticTransportValidator.findFunktionsTillstandType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import org.junit.Test;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.TypAvFunktionstillstand;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning;
import se.inera.certificate.fk7263.iso.v21090.dt.v1.II;

/**
 * @author andreaskaltenbach, marced
 */
public class ProgrammaticTransportValidatorTest {

    private Lakarutlatande getValidUtlatande() throws IOException {
        // read valid certificate from file
        return JAXB.unmarshal(new File("src/test/resources/ProgrammaticLegacyTransportSchemaValidatorTest/legacy-maximalt-fk7263-transport.xml"),
                Lakarutlatande.class);
    }

    @Test
    public void testHappyCase() throws Exception {
        assertEquals(0, new ProgrammaticTransportValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testMissingUtlatandeTyp() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        utlatande.setTypAvUtlatande(null);
        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingUtlatandeId() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();

        utlatande.setLakarutlatandeId(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingUtlatandeIdRoot() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();

        utlatande.setLakarutlatandeId("");

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingSigneringsDatum() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();

        // remove signeringsdatum
        utlatande.setSigneringsdatum(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingSkickatDatum() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();

        // remove skickatdatum
        utlatande.setSkickatDatum(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingPatient() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();

        utlatande.setPatient(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingPatientId() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();

        utlatande.getPatient().setPersonId(null);
        assertEquals(2, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientIdRoot() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        II ii = new II();
        ii.setRoot("1111111");
        ii.setExtension("19121212+1212");
        utlatande.getPatient().setPersonId(ii);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientIdExtension() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        II ii = new II();
        ii.setRoot("1.2.752.129.2.1.3.1");
        ii.setExtension("1233333");
        utlatande.getPatient().setPersonId(ii);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientName() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();

        utlatande.getPatient().setFullstandigtNamn(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidHosPersonalNamn() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();

        utlatande.getSkapadAvHosPersonal().setFullstandigtNamn(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidAktivitetOvrigtrekommendationBeskrivning() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        AktivitetType aktivitet = findAktivitetWithCode(utlatande.getAktivitets(), Aktivitetskod.OVRIGT);
        aktivitet.setBeskrivning(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidAktivitetPlanAnnanAtgardBeskrivning() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        AktivitetType aktivitet = findAktivitetWithCode(utlatande.getAktivitets(), Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        aktivitet.setBeskrivning(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidAktivitetPlanSjukvardBeskrivning() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        AktivitetType aktivitet = findAktivitetWithCode(utlatande.getAktivitets(),
                Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        aktivitet.setBeskrivning(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    /*
     * @Test
     * public void testInvalidDiagnoseCodeSystem() throws Exception {
     * assertEquals(0, new ProgrammaticLegacyTransportSchemaValidator(getValidUtlatande()).validate().size());
     * }
     */
    @Test
    public void testInvalidObservationForloppBeskrivning() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        BedomtTillstandType tillstand = new BedomtTillstandType();
        tillstand.setBeskrivning(null);
        utlatande.setBedomtTillstand(tillstand);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidObservationKroppFunktionsBeskrivning() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        FunktionstillstandType funktion = findFunktionsTillstandType(utlatande.getFunktionstillstands(), TypAvFunktionstillstand.KROPPSFUNKTION);
        funktion.setBeskrivning(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testSaknadSysselsattning() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstands(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.getArbetsformaga().getSysselsattnings().clear();

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testSaknadSysselsattningSmittskydd() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        AktivitetType smittskydd = new AktivitetType();
        smittskydd.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        utlatande.getAktivitets().add(smittskydd);
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstands(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.getArbetsformaga().getSysselsattnings().clear();

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testTomArbetsuppgiftVidSysselsattningArbete() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstands(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.getArbetsformaga().getSysselsattnings().clear();
        SysselsattningType type = new SysselsattningType();
        type.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);

        inAktivitetFunktion.getArbetsformaga().getSysselsattnings().add(type);
        inAktivitetFunktion.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift("");

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testTomSysselsattningSmittskydd() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        AktivitetType smittskydd = new AktivitetType();
        smittskydd.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        utlatande.getAktivitets().add(smittskydd);
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstands(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.getArbetsformaga().getSysselsattnings().clear();

        SysselsattningType type = new SysselsattningType();
        type.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);
        inAktivitetFunktion.getArbetsformaga().getSysselsattnings().add(type);
        inAktivitetFunktion.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift("");

        assertEquals(0, new ProgrammaticTransportValidator(utlatande).validate().size());

    }

    @Test
    public void testValidMissingAktivitetsbegransningBeskrivning() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstands(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.setBeskrivning(null);

        assertEquals(0, new ProgrammaticTransportValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testValidEmptyAktivitetsbegransningBeskrivning() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstands(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.setBeskrivning("");

        assertEquals(0, new ProgrammaticTransportValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testValidObservationKroppFunktionsBeskrivningSmittskydd() throws Exception {
        Lakarutlatande utlatande = getValidUtlatande();

        AktivitetType smittskydd = new AktivitetType();
        smittskydd.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        utlatande.getAktivitets().add(smittskydd);

        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstands(), TypAvFunktionstillstand.KROPPSFUNKTION);
        inAktivitetFunktion.setBeskrivning(null);

        assertEquals(0, new ProgrammaticTransportValidator(getValidUtlatande()).validate().size());
    }

}
