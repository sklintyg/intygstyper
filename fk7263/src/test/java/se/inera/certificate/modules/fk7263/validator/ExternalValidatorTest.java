package se.inera.certificate.modules.fk7263.validator;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.modules.fk7263.model.codes.Aktivitetskoder;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.codes.Sysselsattningskoder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Aktivitet;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

/**
 * @author marced
 */
public class ExternalValidatorTest {

    private Fk7263Utlatande getValidUtlatande() throws IOException {
        // read valid certificate from file
        return new CustomObjectMapper().readValue(new ClassPathResource(
                "ExternalValidatorTest/external_model_utlatande.json").getFile(), Fk7263Utlatande.class);
    }

    @Test
    public void testHappyCase() throws Exception {
        assertEquals(0, new ExternalValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testMissingDiagnoseCode() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        Fk7263Observation huvudDiagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        boolean removed = utlatande.getObservationer().remove(huvudDiagnos);
        assertTrue(removed);
        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingDiagnoseCodeButHasSmittSkydd() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();
        Fk7263Observation huvudDiagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        utlatande.getObservationer().remove(huvudDiagnos);

        Fk7263Aktivitet smittskyddsAktivitet = new Fk7263Aktivitet();
        smittskyddsAktivitet.setAktivitetskod(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
        utlatande.getAktiviteter().add(smittskyddsAktivitet);

        assertEquals(0, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidSysselsattning() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();
        Fk7263Patient patient = utlatande.getPatient();
        for (Sysselsattning sysselsattning : utlatande.getPatient().getSysselsattningar()) {
            if (Sysselsattningskoder.NUVARANDE_ARBETE.equals(sysselsattning.getSysselsattningstyp())
                    && !patient.getArbetsuppgifter().isEmpty()) {
                patient.getArbetsuppgifter().get(0).setTypAvArbetsuppgift("");
            }
        }
        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testSmittskyddButInvalidDiagnoseCodeSystem() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();
        Fk7263Observation huvudDiagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        utlatande.getObservationer().remove(huvudDiagnos);
        huvudDiagnos.setObservationskod(new Kod("non-existing-code-system", "a value"));
        utlatande.getObservationer().add(huvudDiagnos);
        Fk7263Aktivitet smittskyddsAktivitet = new Fk7263Aktivitet();
        smittskyddsAktivitet.setAktivitetskod(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
        utlatande.getAktiviteter().add(smittskyddsAktivitet);

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidDiagnoseCodeSystem() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();
        Fk7263Observation huvudDiagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        utlatande.getObservationer().remove(huvudDiagnos);
        huvudDiagnos.setObservationskod(new Kod("non-existing-code-system", "a value"));
        utlatande.getObservationer().add(huvudDiagnos);

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidObservationForloppBeskrivning() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        Fk7263Observation observation = utlatande.findObservationByKod(ObservationsKoder.FORLOPP);
        observation.setBeskrivning(null);

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidObservationAktivitetsbegransningBeskrivning() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        Fk7263Observation observation = utlatande.findObservationByKategori(ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET);
        observation.setBeskrivning(null);

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidObservationKroppFunktionsBeskrivning() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        Fk7263Observation observation = utlatande.findObservationByKategori(ObservationsKoder.KROPPSFUNKTIONER);
        observation.setBeskrivning("");

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testValidObservationKroppFunktionsBeskrivningSmittskydd() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();
        Fk7263Aktivitet smittskydd = new Fk7263Aktivitet();
        smittskydd.setAktivitetskod(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
        utlatande.getAktiviteter().add(smittskydd);

        Fk7263Observation observation = utlatande.findObservationByKategori(ObservationsKoder.KROPPSFUNKTIONER);
        observation.setBeskrivning(null);

        assertEquals(0, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidAktivitetOvrigtRekommendationBeskrivning() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        Fk7263Aktivitet aktivitet = utlatande.getAktivitet(Aktivitetskoder.OVRIGT);
        aktivitet.setBeskrivning(null);

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidAktivitetPlanSjukvardBeskrivning() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        Fk7263Aktivitet aktivitet = utlatande
                .getAktivitet(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        aktivitet.setBeskrivning("");

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidAktivitetPlanAnnanAtgardBeskrivning() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        Fk7263Aktivitet aktivitet = utlatande.getAktivitet(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        aktivitet.setBeskrivning("");

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }
}
