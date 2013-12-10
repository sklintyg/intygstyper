package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.modules.fk7263.model.codes.Aktivitetskoder;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
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

        Observation huvudDiagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        boolean removed = utlatande.getObservationer().remove(huvudDiagnos);
        assertTrue(removed);
        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }
    
    @Test
    public void testMissingDiagnoseCodeButHasSmittSkydd() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();
        Observation huvudDiagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        utlatande.getObservationer().remove(huvudDiagnos);
       
        Aktivitet smittskyddsAktivitet = new Aktivitet();
        smittskyddsAktivitet.setAktivitetskod(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
        utlatande.getAktiviteter().add(smittskyddsAktivitet);
       
        assertEquals(0, new ExternalValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidDiagnoseCodeSystem() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();
        Observation huvudDiagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        utlatande.getObservationer().remove(huvudDiagnos);
        huvudDiagnos.setObservationskod(new Kod("non-existing-code-system", "a value"));
        utlatande.getObservationer().add(huvudDiagnos);

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }
    
    @Test
    public void testSmittskyddButInvalidDiagnoseCodeSystem() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();
        Observation huvudDiagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        utlatande.getObservationer().remove(huvudDiagnos);
        huvudDiagnos.setObservationskod(new Kod("non-existing-code-system", "a value"));
        utlatande.getObservationer().add(huvudDiagnos);
        Aktivitet smittskyddsAktivitet = new Aktivitet();
        smittskyddsAktivitet.setAktivitetskod(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
        utlatande.getAktiviteter().add(smittskyddsAktivitet);

        assertEquals(1, new ExternalValidator(utlatande).validate().size());
    }
}
