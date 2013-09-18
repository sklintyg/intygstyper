package se.inera.certificate.modules.fk7263.validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.codes.Aktivitetskoder;
import se.inera.certificate.model.codes.ObservationsKoder;
import se.inera.certificate.model.codes.Sysselsattningskoder;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;

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
        utlatande.setKommentars(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingVardkontaktOrReferens() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove all vardkontakter
        utlatande.getVardkontakter().clear();

        // remove all referenser
        utlatande.getReferenser().clear();

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingMedicinsktTillstand() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();
        utlatande.getObservations().clear();
        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingArbetsbeskrivning() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        Sysselsattning sysselsattning = new Sysselsattning();
        sysselsattning.setSysselsattningsTyp(Sysselsattningskoder.NUVARANDE_ARBETE);
        utlatande.getPatient().getSysselsattnings().add(sysselsattning);

        utlatande.getPatient().getArbetsuppgifts().clear();

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testDoubleRessatt() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // set two activities with conflicting activity code
        utlatande.getAktiviteter().get(0)
                .setAktivitetskod(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
        utlatande.getAktiviteter().get(1)
                .setAktivitetskod(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientIdExtension() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove patiend id (personnr)
        utlatande.getPatient().getId().setExtension("10101010");

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientIdOID() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove patiend id (personnr)
        utlatande.getPatient().getId().setRoot("0.0.0");

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientName() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove all names
        utlatande.getPatient().setFornamn(null);
        utlatande.getPatient().setMellannamn(null);
        utlatande.getPatient().setEfternamn(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidHosPersonalIdExtension() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // break hos-personal id extension
        utlatande.getSkapadAv().getId().setExtension("");

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidHosPersonalIdOID() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // break hos-personal id root OID
        utlatande.getSkapadAv().getId().setRoot("0.0.0");

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidHosPersonalNamn() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // break hos-personal id extension
        utlatande.getSkapadAv().setNamn(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidFunktionsnedsattning() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove all field 4: based on (vardkontakter/referenser)
        utlatande.setVardkontakter(new ArrayList<Vardkontakt>());
        utlatande.setReferenser(new ArrayList<Referens>());

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testNuvarandeArbeteCheckedButNoArbetsuppgifts() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove arbetsuppgifts (but let 8a: nuvarande arbete be checked)
        utlatande.getPatient().setArbetsuppgifts(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testAtLeastOneNedsattArbetsformagaExists() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove all arbetsformage observations
        List<Observation> filtered = new ArrayList<Observation>();
        for (Observation o : utlatande.getObservations()) {
            if (!ObservationsKoder.ARBETSFORMAGA.equals(o.getObservationsKod())) {
                filtered.add(o);
            }
        }
        utlatande.setObservations(filtered);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testNedsattArbetsformagaInvalidInterval() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // Screw up one of the intervals so from > tom
        PartialInterval reversedPeriod = utlatande.getNedsattning25percent().getObservationsPeriod();
        reversedPeriod.setFrom(new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(),
                DateTimeFieldType.monthOfYear() }, new int[] { 2011, 4 }));
        reversedPeriod.setTom(new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(),
                DateTimeFieldType.monthOfYear() }, new int[] { 2011, 2 }));

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingSigneringsDatum() throws Exception {
        Fk7263Intyg utlatande = getValidUtlatande();

        // remove signeringsdatum
        utlatande.setSigneringsDatum(null);

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
