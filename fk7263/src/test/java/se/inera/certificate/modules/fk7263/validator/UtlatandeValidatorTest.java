package se.inera.certificate.modules.fk7263.validator;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;

/**
 * @author andreaskaltenbach, marced
 */
public class UtlatandeValidatorTest {

    private Fk7263Intyg getValidUtlatande() throws IOException {
        // read valid certificate from file
        return new CustomObjectMapper().readValue(
                new ClassPathResource("UtlatandeValidatorTest/utlatande.json").getFile(), Fk7263Intyg.class);
    }

    @Test
    public void testHappyCase() throws Exception {
        assertEquals(0, new UtlatandeValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testMissingComment() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();
        utlatande.setKommentar(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
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

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingMedicinsktTillstand() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();
        utlatande.setDiagnosKod(null);
        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testDoubleRessatt() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // set two activities with conflicting activity code
        utlatande.setRessattTillArbeteAktuellt(true);
        utlatande.setRessattTillArbeteEjAktuellt(true);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientIdExtension() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        utlatande.setPatientPersonnummer("10101010");

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientName() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove name
        utlatande.setPatientNamn(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidHosPersonalNamn() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // break hos-personal id extension
        utlatande.getVardperson().setNamn(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testAtLeastOneNedsattArbetsformagaExists() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove all arbetsformage observations
        utlatande.setNedsattMed100(null);
        utlatande.setNedsattMed75(null);
        utlatande.setNedsattMed50(null);
        utlatande.setNedsattMed25(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testNedsattArbetsformagaInvalidInterval() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // Screw up one of the intervals so from > tom
        LocalDateInterval reversedPeriod = utlatande.getNedsattMed25();
        reversedPeriod.setStart(new LocalDate(2011, 4, 1));
        reversedPeriod.setEnd(new LocalDate(2011, 2, 1));

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingSigneringsDatum() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove signeringsdatum
        utlatande.setSigneringsdatum(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingSkickatDatum() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove skickatdatum
        utlatande.setSkickatDatum(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

}
