package se.inera.certificate.modules.fk7263.validator;

import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

import java.util.List;

import org.joda.time.LocalDate;

import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;

/**
 * Validates a fk7263 certificate's specific rules that's not covered by schema validation or external validation.
 * 
 * @author marced
 */
public class InternalValidator extends AbstractValidator {

    private Fk7263Intyg utlatande;

    public InternalValidator(Fk7263Intyg utlatande) {
        this.utlatande = utlatande;
    }

    public List<String> validate() {

        validateNonSmittskydd();
        validateRessatt();
        validateKommentar();

        return validationErrors;
    }

    private void validateNonSmittskydd() {

        // Many fields are optional if smittskydd is checked, if not set validate these below
        boolean inSmittskydd = utlatande.isAvstangningSmittskydd();

        if (inSmittskydd) {
            return;
        }

        // Fält 2 - Medicinskt tillstånd kod - mandatory if not smittskydd
        if (isNullOrEmpty(utlatande.getDiagnosKod())) {
            addValidationError("Field 2: No tillstandskod in medicinsktTillstand found!");
        }

        validateFunktionsnedsattning();
        validateArbetsformaga();
    }

    private void validateFunktionsnedsattning() {

        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        String funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (funktionsnedsattning == null) {
            addValidationError("Field 4: No funktionsnedsattning element found!");
            return;
        }

        // Fält 4 - höger Check that we at least got one field set
        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getTelefonkontaktMedPatienten() == null
                && utlatande.getJournaluppgifter() == null && utlatande.getAnnanReferens() == null) {
            addValidationError("Field 4: At least 1 vardkontakt or referens element must be set!");
            return;
        }
    }

    private void validateArbetsformaga() {

        // Fält 8a - arbetsformoga - sysselsattning
        boolean hasArbetsuppgifts = utlatande.getNuvarandeArbetsuppgifter() != null;

        if (!hasArbetsuppgifts && !utlatande.isArbetsloshet() && !utlatande.isForaldrarledighet()) {
            addValidationError("Field 8a: At least 1 sysselsattning must be set");
        }

        // validate 8b
        boolean valid100 = validArbetsformageNedsattning(utlatande.getNedsattMed100());
        boolean valid75 = validArbetsformageNedsattning(utlatande.getNedsattMed75());
        boolean valid50 = validArbetsformageNedsattning(utlatande.getNedsattMed50());
        boolean valid25 = validArbetsformageNedsattning(utlatande.getNedsattMed25());

        if (!valid100 && !valid75 && !valid50 && !valid25) {
            addValidationError("Field 8b: Invalid span in arbetsformaga nedsattningfield.");
        }

        if (utlatande.getNedsattMed100() == null && utlatande.getNedsattMed75() == null
                && utlatande.getNedsattMed50() == null && utlatande.getNedsattMed25() == null) {
            addValidationError("Field 8b: At least 1 nedsatt arbetsformaga field must be filled");
        }
    }

    /**
     * Checks that the given Observation is about arbetsformaga and has a period where tom>from
     * 
     * @param nedsattning
     * @return
     */
    private boolean validArbetsformageNedsattning(LocalDateInterval nedsattning) {

        if (nedsattning == null) {
            return true;
        }

        if (nedsattning.getStart() != null && nedsattning.getEnd() != null) {
            if (nedsattning.getEnd().isBefore(nedsattning.getStart())) {
                // Must be something wrong with the observationPeriod:
                addValidationError("Field 8b: Invalid date interval (from " + nedsattning.getStart() + ", tom "
                        + nedsattning.getEnd());
            } else {
                return true;
            }

        } else {
            // No period given
            addValidationError("Field 8b: No date interval given");
        }

        return false;
    }

    private void validateRessatt() {
        // Fält 11 - optional
        boolean inForandratRessatt = utlatande.isRessattTillArbeteAktuellt();
        boolean inEjForandratRessatt = utlatande.isRessattTillArbeteEjAktuellt();

        // Fält 11 - If set only one should be set
        if (inForandratRessatt && inEjForandratRessatt) {
            addValidationError("Field 11: Only one forandrat ressatt could be set.");
        }
    }

    private void validateKommentar() {

        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        boolean hasKommentar = !isNullOrEmpty(utlatande.getKommentar());

        LocalDate annat = utlatande.getAnnanReferens();

        boolean garEjAttBedomma = utlatande.isArbetsformataPrognosGarInteAttBedoma();

        if ((annat != null || garEjAttBedomma) && !hasKommentar) {
            addValidationError("Field 13: Field should contain data as field 4 or fields 10 are checked.");
        }
    }

}
