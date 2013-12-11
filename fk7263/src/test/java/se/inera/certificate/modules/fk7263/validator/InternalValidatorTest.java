package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;

/**
 * @author andreaskaltenbach, marced
 */
public class InternalValidatorTest {

    private Fk7263Intyg getValidUtlatande() throws IOException {
        // read valid certificate from file
        return new CustomObjectMapper().readValue(
                new ClassPathResource("InternalValidatorTest/utlatande.json").getFile(), Fk7263Intyg.class);
    }

    @Test
    public void testHappyCase() throws Exception {
        assertEquals(0, new InternalValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testSmittskyddRelaxesOtherMandatoryInfo() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();
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
        Fk7263Intyg utlatande = getValidUtlatande();
        utlatande.setAvstangningSmittskydd(false);
        utlatande.setDiagnosKod("");

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingComment() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();
        utlatande.setKommentar(null);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingCommentOkIfField4And10NotFilled() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();
        utlatande.setKommentar(null);
        utlatande.setAnnanReferens(null);
        utlatande.setArbetsformataPrognosGarInteAttBedoma(false);
        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingVardkontaktOrReferens() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

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
        Fk7263Intyg utlatande = getValidUtlatande();
        utlatande.setAvstangningSmittskydd(true);
        // remove all vardkontakter
        utlatande.setUndersokningAvPatienten(null);
        utlatande.setTelefonkontaktMedPatienten(null);

        // remove all referenser
        utlatande.setJournaluppgifter(null);
        utlatande.setAnnanReferens(null);

        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testDoubleRessatt() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // set two activities with conflicting activity code
        utlatande.setRessattTillArbeteAktuellt(true);
        utlatande.setRessattTillArbeteEjAktuellt(true);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testMultipleRehabilitering() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // set conflicting values
        utlatande.setRehabiliteringAktuell(true);
        utlatande.setRehabiliteringEjAktuell(true);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testMultiplearbetsformagaprognos() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // set conflicting values
        utlatande.setArbetsformataPrognosGarInteAttBedoma(true);
        utlatande.setArbetsformataPrognosJa(true);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testNoArbetsformagas() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();
        utlatande.setAvstangningSmittskydd(false);
        // set conflicting values
        utlatande.setNuvarandeArbetsuppgifter("");
        utlatande.setArbetsloshet(false);
        utlatande.setForaldrarledighet(false);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testRessattTillArbeteAktuellt() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        utlatande.setRessattTillArbeteAktuellt(true);
        utlatande.setRessattTillArbeteEjAktuellt(false);

        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testRessattTillArbeteEjAktuellt() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        utlatande.setRessattTillArbeteAktuellt(false);
        utlatande.setRessattTillArbeteEjAktuellt(true);

        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testAtLeastOneNedsattArbetsformagaExists() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove all arbetsformage observations
        utlatande.setNedsattMed100(null);
        utlatande.setNedsattMed75(null);
        utlatande.setNedsattMed50(null);
        utlatande.setNedsattMed25(null);

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testNedsattArbetsformagaInvalidInterval() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // Screw up one of the intervals so from > tom
        LocalDateInterval reversedPeriod = utlatande.getNedsattMed25();
        reversedPeriod.setStart(new LocalDate(2011, 4, 1));
        reversedPeriod.setEnd(new LocalDate(2011, 2, 1));

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testDatumIntervallSammaDag() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove skickatdatum
        utlatande.getNedsattMed100().setEnd(utlatande.getNedsattMed100().getStart());

        assertEquals(0, new InternalValidator(utlatande).validate().size());
    }

    @Test
    public void testFelaktigtDatumIntervall() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove skickatdatum
        utlatande.getNedsattMed100().setEnd(utlatande.getNedsattMed100().getStart().minusDays(1));

        assertEquals(1, new InternalValidator(utlatande).validate().size());
    }
}
