package se.inera.certificate.modules.fk7263.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;

/**
 * @author andreaskaltenbach
 */
public class UtlatandeValidator {

    private Fk7263Intyg utlatande;

    private List<String> validationErrors = new ArrayList<>();

    public static final String PERSON_NUMBER_REGEX = "[0-9]{8}[-+][0-9]{4}";

    public UtlatandeValidator(Fk7263Intyg utlatande) {
        this.utlatande = utlatande;
    }

    public List<String> validate() {

        validatePatient(utlatande.getPatient());
        validateNonSmittskydd();

        validateRessatt();
        validateKommentar();

        return validationErrors;
    }

    private void validatePatient(Patient patient) {
        // Check format of patient id (has to be a valid personnummer)
        String personNumber = patient.getId().getExtension();
        if (personNumber == null || !Pattern.matches(PERSON_NUMBER_REGEX, personNumber)) {
            validationErrors.add("Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.");
        }
    }

    private void validateNonSmittskydd() {

        // Many fields are optional if smittskydd is checked, if not set validate these below
        boolean inSmittskydd = utlatande.getAvstangningEnligtSmittskyddslagen() != null;

        if (inSmittskydd) {
            return;
        }

        // Fält 2 - Check that we got a medicinsktTillstand element
        if (utlatande.getMedicinsktTillstand() == null) {
            validationErrors.add("No medicinsktTillstand found!");
            return;
        }

        // Fält 2 - Medicinskt tillstånd kod - mandatory
        if (utlatande.getMedicinsktTillstand().getObservationsKod() == null  || isNullOrEmpty(utlatande.getMedicinsktTillstand().getObservationsKod().getCode())) {
            validationErrors.add("No tillstandskod in medicinsktTillstand found!");
        }

        validateFunktionsnedsattning();
        validateArbetsformaga();
    }

    private void validateFunktionsnedsattning() {

        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        Observation funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (funktionsnedsattning == null) {
            validationErrors.add("No funktionsnedsattning element found!");
            return;
        }

        // Fält 4 - höger översta kryssrutan
        Vardkontakt inUndersokning = utlatande.getVardkontakt(Fk7263Intyg.Vardkontakt_Min_undersokning_av_patienten);

        // Fält 4 - höger näst översta kryssrutan
        Vardkontakt telefonkontakt = utlatande.getVardkontakt(Fk7263Intyg.Vardkontakt_Min_telefonkontakt_med_patienten);

        // Fält 4 - höger näst nedersta kryssrutan
        Referens journal = utlatande.getReferens(Fk7263Intyg.Referens_Journaluppgifter);

        // Fält 4 - höger nedersta kryssrutan
        Referens inAnnat = utlatande.getReferens(Fk7263Intyg.Referens_Annat);

        // Fält 4 - höger Check that we at least got one field set
        if (inUndersokning == null && telefonkontakt == null && journal == null && inAnnat == null) {
            validationErrors.add("No vardkontakt or referens element found ! At least one must be set!");
            return;
        }
    }

    private void validateArbetsformaga() {

        // Fält 8a - arbetsformoga
        List<Arbetsuppgift> arbetsuppgifts = utlatande.getPatient().getArbetsuppgifts();
        boolean hasArbetsuppgifts = (arbetsuppgifts != null && !arbetsuppgifts.isEmpty());

        // Fält 8a
        boolean nuvarandeArbete = utlatande.isArbetsformagaIForhallandeTillNuvarandeArbete();

        // Fält 8a - Check that we got a arbetsuppgift element if arbete is set
        if (nuvarandeArbete && !hasArbetsuppgifts) {
            validationErrors.add("No arbetsuppgift found when arbete set in field 8a!.");
            return;
        }
    }

    private void validateRessatt() {
        // Fält 11 - optional
        Aktivitet inForandratRessatt = utlatande.getForandratRessattAktuellt();
        Aktivitet inEjForandratRessatt = utlatande.getForandratRessattEjAktuellt();

        // Fält 11 - If set only one should be set
        if (inForandratRessatt != null && inEjForandratRessatt != null) {
            validationErrors.add("Only one forandrat ressatt could be set for field 11.");
        }
    }

    private void validateKommentar() {

        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        boolean hasKommentar = utlatande.getKommentars() != null && !utlatande.getKommentars().isEmpty();

        Referens annat = utlatande.getReferens(Fk7263Intyg.Referens_Annat);
        boolean garEjAttBedomma = utlatande.getArbetsformaga() != null && utlatande.getArbetsformaga().getObservationsKod() != null && Fk7263Intyg.Prognos_Det_gar_inte_att_bedomma.equals(utlatande.getArbetsformaga().getObservationsKod());

        if ((annat != null || garEjAttBedomma) && !hasKommentar) {
            validationErrors.add("Upplysningar should contain data as field 4 or fields 10 is checked.");
        }
    }
}
