package se.inera.certificate.modules.fk7263.validator;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;

/**
 * @author andreaskaltenbach
 */
public class UtlatandeValidatorTest {

    private Fk7263Intyg utlatande() throws IOException {
        // read request from file
        return new CustomObjectMapper().readValue(new ClassPathResource("fk7263.json").getFile(), Fk7263Intyg.class);
    }

    @Test
    public void testHappyCase() throws Exception {
        assertEquals(0, new UtlatandeValidator(utlatande()).validate().size());
    }

    @Test
    public void testMissingComment() throws Exception {
        Fk7263Intyg utlatande= utlatande();
        utlatande.setKommentars(null);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingVardkontaktOrReferens() throws Exception {
        Fk7263Intyg utlatande= utlatande();

        // remove all vardkontakter
        utlatande.getVardkontakter().clear();

        // remove all referenser
        utlatande.getReferenser().clear();

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingMedicinsktTillstand() throws Exception {
        Fk7263Intyg utlatande= utlatande();
        utlatande.getObservations().clear();
        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingArbetsbeskrivning() throws Exception {
        Fk7263Intyg utlatande= utlatande();

        Sysselsattning sysselsattning = new Sysselsattning();
        sysselsattning.setSysselsattningsTyp(Fk7263Intyg.Sysselsattning_Nuvarande_arbete);
        utlatande.getPatient().getSysselsattnings().add(sysselsattning);

        utlatande.getPatient().getArbetsuppgifts().clear();

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }

    @Test
    public void testDoubleRessatt() throws Exception {
        Fk7263Intyg utlatande= utlatande();

        // set two activities with conflicting activity code
        utlatande.getAktiviteter().get(0).setAktivitetskod(Fk7263Intyg.Aktivitet_Forandrat_ressatt_till_arbetsplatsen_ar_aktuellt);
        utlatande.getAktiviteter().get(1).setAktivitetskod(Fk7263Intyg.Aktivitet_Forandrat_ressatt_till_arbetsplatsen_ar_ej_aktuellt);

        assertEquals(1, new UtlatandeValidator(utlatande).validate().size());
    }
}
