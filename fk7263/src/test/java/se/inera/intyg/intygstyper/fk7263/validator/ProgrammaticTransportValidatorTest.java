package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;
import static se.inera.certificate.modules.fk7263.validator.ProgrammaticTransportValidator.findAktivitetWithCode;
import static se.inera.certificate.modules.fk7263.validator.ProgrammaticTransportValidator.findFunktionsTillstandType;
import iso.v21090.dt.v1.II;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXB;

import org.joda.time.LocalDate;
import org.junit.Test;

import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.TypAvFunktionstillstand;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning;


/**
 * @author andreaskaltenbach, marced
 */
public class ProgrammaticTransportValidatorTest {

    private LakarutlatandeType getValidUtlatande() throws IOException {
        // read valid certificate from file
        return JAXB.unmarshal(new File("src/test/resources/ProgrammaticLegacyTransportSchemaValidatorTest/legacy-maximalt-fk7263-transport.xml"),
                LakarutlatandeType.class);
    }

    @Test
    public void testHappyCase() throws Exception {
        assertEquals(0, new ProgrammaticTransportValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testMissingUtlatandeTyp() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        utlatande.setTypAvUtlatande(null);
        assertEquals(0, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingUtlatandeId() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();

        utlatande.setLakarutlatandeId(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingUtlatandeIdRoot() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();

        utlatande.setLakarutlatandeId("");

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingSigneringsDatum() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();

        // remove signeringsdatum
        utlatande.setSigneringsdatum(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingSkickatDatum() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();

        // remove skickatdatum
        utlatande.setSkickatDatum(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingPatient() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();

        utlatande.setPatient(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingPatientId() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();

        utlatande.getPatient().setPersonId(null);
        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientIdRoot() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        II ii = new II();
        ii.setRoot("1111111");
        ii.setExtension("19121212-1212");
        utlatande.getPatient().setPersonId(ii);

        System.out.println(new ProgrammaticTransportValidator(utlatande).validate());
        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientIdExtension() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        II ii = new II();
        ii.setRoot("1.2.752.129.2.1.3.1");
        ii.setExtension("1233333");
        utlatande.getPatient().setPersonId(ii);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientName() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();

        utlatande.getPatient().setFullstandigtNamn(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidHosPersonalNamn() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();

        utlatande.getSkapadAvHosPersonal().setFullstandigtNamn(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidAktivitetOvrigtrekommendationBeskrivning() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        AktivitetType aktivitet = findAktivitetWithCode(utlatande.getAktivitet(), Aktivitetskod.OVRIGT);
        aktivitet.setBeskrivning(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidAktivitetPlanAnnanAtgardBeskrivning() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        AktivitetType aktivitet = findAktivitetWithCode(utlatande.getAktivitet(), Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        aktivitet.setBeskrivning(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidAktivitetPlanSjukvardBeskrivning() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        AktivitetType aktivitet = findAktivitetWithCode(utlatande.getAktivitet(),
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
        LakarutlatandeType utlatande = getValidUtlatande();
        BedomtTillstandType tillstand = new BedomtTillstandType();
        tillstand.setBeskrivning(null);
        utlatande.setBedomtTillstand(tillstand);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidObservationKroppFunktionsBeskrivning() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        FunktionstillstandType funktion = findFunktionsTillstandType(utlatande.getFunktionstillstand(), TypAvFunktionstillstand.KROPPSFUNKTION);
        funktion.setBeskrivning(null);

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testSaknadSysselsattning() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.getArbetsformaga().getSysselsattning().clear();

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testSaknadSysselsattningSmittskydd() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        AktivitetType smittskydd = new AktivitetType();
        smittskydd.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        utlatande.getAktivitet().add(smittskydd);
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.getArbetsformaga().getSysselsattning().clear();

        assertEquals(0, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testTomArbetsuppgiftVidSysselsattningArbete() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.getArbetsformaga().getSysselsattning().clear();
        SysselsattningType type = new SysselsattningType();
        type.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);

        inAktivitetFunktion.getArbetsformaga().getSysselsattning().add(type);
        inAktivitetFunktion.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift("");

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testTomKommentarVidReferensAnnat() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        ReferensType annat = new ReferensType();
        annat.setReferenstyp(Referenstyp.ANNAT);
        annat.setDatum(LocalDate.now());
        utlatande.getReferens().clear();
        utlatande.getReferens().add(annat);
        utlatande.setKommentar("");

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testTomKommentarVidPrognosGarEjAttBedomma() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        ReferensType journalUppgifter = new ReferensType();
        journalUppgifter.setReferenstyp(Referenstyp.JOURNALUPPGIFTER);
        journalUppgifter.setDatum(LocalDate.now());
        utlatande.getReferens().clear();
        utlatande.getReferens().add(journalUppgifter);
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.getArbetsformaga().setPrognosangivelse(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA);
        utlatande.setKommentar("");

        assertEquals(1, new ProgrammaticTransportValidator(utlatande).validate().size());
    }

    @Test
    public void testTomSysselsattningSmittskydd() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        AktivitetType smittskydd = new AktivitetType();
        smittskydd.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        utlatande.getAktivitet().add(smittskydd);
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.getArbetsformaga().getSysselsattning().clear();

        SysselsattningType type = new SysselsattningType();
        type.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);
        inAktivitetFunktion.getArbetsformaga().getSysselsattning().add(type);
        inAktivitetFunktion.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift("");

        assertEquals(0, new ProgrammaticTransportValidator(utlatande).validate().size());

    }

    @Test
    public void testValidMissingAktivitetsbegransningBeskrivning() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.setBeskrivning(null);

        assertEquals(0, new ProgrammaticTransportValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testValidEmptyAktivitetsbegransningBeskrivning() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
        inAktivitetFunktion.setBeskrivning("");

        assertEquals(0, new ProgrammaticTransportValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testValidObservationKroppFunktionsBeskrivningSmittskydd() throws Exception {
        LakarutlatandeType utlatande = getValidUtlatande();

        AktivitetType smittskydd = new AktivitetType();
        smittskydd.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        utlatande.getAktivitet().add(smittskydd);

        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(utlatande.getFunktionstillstand(), TypAvFunktionstillstand.KROPPSFUNKTION);
        inAktivitetFunktion.setBeskrivning(null);

        assertEquals(0, new ProgrammaticTransportValidator(getValidUtlatande()).validate().size());
    }
}
