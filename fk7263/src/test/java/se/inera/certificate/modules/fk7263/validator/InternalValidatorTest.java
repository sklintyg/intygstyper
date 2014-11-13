package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.PrognosBedomning;

/**
 * @author andreaskaltenbach, marced
 */
public class InternalValidatorTest {

    private Utlatande getValidUtlatande() throws IOException {
        // read valid certificate from file
        return new CustomObjectMapper().readValue(
                new ClassPathResource("InternalValidatorTest/utlatande.json").getFile(), Utlatande.class);
    }

    @Test
    public void testHappyCase() throws Exception {
        assertEquals(0, new InternalValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testSmittskyddRelaxesOtherMandatoryInfo() throws Exception {
        Utlatande utlatande = getValidUtlatande();
        utlatande.setAvstangningSmittskydd(true);
        utlatande.setDiagnosKod("");
        // remove all vardkontakter
        utlatande.setUndersokningAvPatienten(null);
        utlatande.setTelefonkontaktMedPatienten(null);
        utlatande.setJournaluppgifter(null);
        utlatande.setAnnanReferens(null);

        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testNoSmittskyddDiagnoseMandatory() throws Exception {
        Utlatande utlatande = getValidUtlatande();
        utlatande.setAvstangningSmittskydd(false);
        utlatande.setDiagnosKod("");

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingCommentOkIfField4And10NotFilled() throws Exception {
        Utlatande utlatande = getValidUtlatande();
        utlatande.setKommentar(null);
        utlatande.setAnnanReferens(null);
        utlatande.setPrognosBedomning(PrognosBedomning.arbetsformagaPrognosGarInteAttBedoma);
        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingVardkontaktOrReferens() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        // remove all vardkontakter
        utlatande.setUndersokningAvPatienten(null);
        utlatande.setTelefonkontaktMedPatienten(null);

        // remove all referenser
        utlatande.setJournaluppgifter(null);
        utlatande.setAnnanReferens(null);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testSmittskyddMissingVardkontaktOrReferens() throws Exception {
        Utlatande utlatande = getValidUtlatande();
        utlatande.setAvstangningSmittskydd(true);
        // remove all vardkontakter
        utlatande.setUndersokningAvPatienten(null);
        utlatande.setTelefonkontaktMedPatienten(null);

        // remove all referenser
        utlatande.setJournaluppgifter(null);
        utlatande.setAnnanReferens(null);
        List<String> result = new InternalValidator(utlatande).validate();
        assertEquals(0, result.size());
    }

    @Test
    public void testDoubleRessatt() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        // set two activities with conflicting activity code
        utlatande.setRessattTillArbeteAktuellt(true);
        utlatande.setRessattTillArbeteEjAktuellt(true);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testNoArbetsformaga() throws Exception {
        Utlatande utlatande = getValidUtlatande();
        utlatande.setAvstangningSmittskydd(false);
        // set conflicting values
        utlatande.setNuvarandeArbete(false);
        utlatande.setNuvarandeArbetsuppgifter(null);
        utlatande.setArbetsloshet(false);
        utlatande.setForaldrarledighet(false);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testNoArbetsuppgifter() throws Exception {
        Utlatande utlatande = getValidUtlatande();
        utlatande.setAvstangningSmittskydd(false);
        // set conflicting values
        utlatande.setNuvarandeArbete(true);
        utlatande.setNuvarandeArbetsuppgifter(null);
        utlatande.setArbetsloshet(false);
        utlatande.setForaldrarledighet(false);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testNoArbetsuppgifterAndOthersChecked() throws Exception {
        Utlatande utlatande = getValidUtlatande();
        utlatande.setAvstangningSmittskydd(false);
        // set conflicting values
        utlatande.setNuvarandeArbete(true);
        utlatande.setNuvarandeArbetsuppgifter("");
        utlatande.setArbetsloshet(true);
        utlatande.setForaldrarledighet(true);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }


    @Test
    public void testRessattTillArbeteAktuellt() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        utlatande.setRessattTillArbeteAktuellt(true);
        utlatande.setRessattTillArbeteEjAktuellt(false);

        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testRessattTillArbeteEjAktuellt() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        utlatande.setRessattTillArbeteAktuellt(false);
        utlatande.setRessattTillArbeteEjAktuellt(true);

        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }


    @Test
    public void testRekommendationOvrigtIngenBeskrivning() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        utlatande.setRekommendationOvrigtCheck(true);
        utlatande.setRekommendationOvrigt(null);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testAtLeastOneNedsattArbetsformagaExists() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        // remove all arbetsformage observations
        utlatande.setNedsattMed100(null);
        utlatande.setNedsattMed75(null);
        utlatande.setNedsattMed50(null);
        utlatande.setNedsattMed25(null);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testNedsattArbetsformagaInvalidIntervalStartEnd() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        // Screw up one of the intervals so from > tom
        InternalLocalDateInterval reversedPeriod = utlatande.getNedsattMed25();
        reversedPeriod.setFrom(new InternalDate("2011-04-01"));
        reversedPeriod.setTom(new InternalDate("2011-02-01"));

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testDatumIntervallSammaDag() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        // remove skickatdatum
        utlatande.getNedsattMed100().setTom(utlatande.getNedsattMed100().getFrom());

        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testFelaktigtDatumIntervall() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        // remove skickatdatum
        utlatande.getNedsattMed100().setTom(new InternalDate(utlatande.getNedsattMed100().fromAsLocalDate().minusDays(1).toString()));

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }
    @Test
    public void testFelaktigtDatumIntervallNoEndDate() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        // remove skickatdatum
        utlatande.getNedsattMed100().setTom(null);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testNedsattArbetsformagaInvalidIntervalStart() throws Exception {
        Utlatande utlatande = getValidUtlatande();

        // remove end date
        utlatande.getNedsattMed100().setTom(null);
        assertEquals(1, new InternalValidator(utlatande).validate().size());

    }

}
