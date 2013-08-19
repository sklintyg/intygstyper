package se.inera.certificate.modules.fk7263.validator;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.codes.Prognoskoder;
import se.inera.certificate.model.codes.Referenstypkoder;
import se.inera.certificate.model.codes.Vardkontakttypkoder;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

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
        Vardkontakt inUndersokning = utlatande.getVardkontakt(Vardkontakttypkoder.MIN_UNDERSOKNING_AV_PATIENTEN);

        // Fält 4 - höger näst översta kryssrutan
        Vardkontakt telefonkontakt = utlatande.getVardkontakt(Vardkontakttypkoder.MIN_TELEFONKONTAKT_MED_PATIENTEN);

        // Fält 4 - höger näst nedersta kryssrutan
        Referens journal = utlatande.getReferens(Referenstypkoder.JOURNALUPPGIFT);

        // Fält 4 - höger nedersta kryssrutan
        Referens inAnnat = utlatande.getReferens(Referenstypkoder.ANNAT);

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

        Referens annat = utlatande.getReferens(Referenstypkoder.ANNAT);
        boolean garEjAttBedomma = utlatande.getArbetsformaga() != null && utlatande.getArbetsformaga().getPrognos() != null &&
                Prognoskoder.DET_GAR_INTE_ATT_BEDOMA.equals(utlatande.getArbetsformaga().getPrognos().getPrognosKod());

        if ((annat != null || garEjAttBedomma) && !hasKommentar) {
            validationErrors.add("Upplysningar should contain data as field 4 or fields 10 is checked.");
        }
    }
}
