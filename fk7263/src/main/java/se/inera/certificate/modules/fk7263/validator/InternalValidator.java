package se.inera.certificate.modules.fk7263.validator;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;

import java.util.List;

import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

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

        validateDiagnose();
        validateFunktionsnedsattning();
        validateRehabilitering();
        validateArbetsformaga();
        validatePrognos();
        validateRessatt();
        validateOvrigaRekommendationer();

        return getValidationErrors();
    }

    private void validateDiagnose() {

        // Fält 2 - Medicinskt tillstånd kod - mandatory if not smittskydd
        if (!utlatande.isAvstangningSmittskydd() && isNullOrEmpty(utlatande.getDiagnosKod())) {
            addValidationError("Field 2: No tillstandskod in medicinsktTillstand found!");
        }

        // Fält 3 - always optional regardless of smittskydd

    }

    private void validateFunktionsnedsattning() {

        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        String funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (!utlatande.isAvstangningSmittskydd() && (funktionsnedsattning == null)) {
            addValidationError("Field 4: No funktionsnedsattning element found!");
        } else if (!utlatande.isAvstangningSmittskydd()) { // Fält 4 - höger Check that we at least got one field set if
                                                           // not smittskydd
            if (utlatande.getUndersokningAvPatienten() == null && utlatande.getTelefonkontaktMedPatienten() == null
                    && utlatande.getJournaluppgifter() == null && utlatande.getAnnanReferens() == null) {
                addValidationError("Field 4: At least 1 vardkontakt or referens element must be set!");
            }
        }
    }

    private void validateRehabilitering() {
        // Fält 7 - 3 optional checkboxes (but exclusive!)
        if (!hasMaxOneTruth(utlatande.isRehabiliteringAktuell(), utlatande.isRehabiliteringEjAktuell(),
                utlatande.isRehabiliteringGarInteAttBedoma())) {
            addValidationError("Field 7: Only one rehabilitering field can be checked!");
        }
    }

    private void validatePrognos() {
        // Fält 10 - 4 optional checkboxes (but exclusive!)
        if (!hasMaxOneTruth(utlatande.isArbetsformataPrognosGarInteAttBedoma(), utlatande.isArbetsformataPrognosJa(),
                utlatande.isArbetsformataPrognosJaDelvis(), utlatande.isArbetsformataPrognosNej())) {
            addValidationError("Field 10: Only one arbetsformaga.prognos field can be checked!");
        }
    }

    private void validateArbetsformaga() {

        // Fält 8a - arbetsformoga - sysselsattning - applies of not smittskydd is set
        if (!utlatande.isAvstangningSmittskydd()) {
            if (!utlatande.isNuvarandeArbete() && !utlatande.isArbetsloshet() && !utlatande.isForaldrarledighet()) {
                addValidationError("Field 8a: At least 1 sysselsattning must be set");
            } else if (utlatande.isNuvarandeArbete() && StringUtils.isEmpty(utlatande.getNuvarandeArbetsuppgifter())) {
                addValidationError("Field 8a: Arbetsuppgifter must be set");
            }
        }

        // validate 8b - regardless of smittskydd
        validateIntervals("Field 8b", utlatande.getNedsattMed100(), utlatande.getNedsattMed75(),
                utlatande.getNedsattMed50(), utlatande.getNedsattMed25());

    }

    private void validateOvrigaRekommendationer() {
        // Fält 6a - If Övrigt is checked, something must be entered.
        if (utlatande.isRekommendationOvrigtCheck() && StringUtils.isEmpty(utlatande.getRekommendationOvrigt())) {
            addValidationError("Field 6a: Övrigt must be entered if checkbox is checked");
        }
    }

    protected boolean validateIntervals(String fieldId, LocalDateInterval... intervals) {
        if (intervals == null || allNulls(intervals)) {
            addValidationError(fieldId + ": At least 1 interval must be filled");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                if (!isValidInterval(intervals[i].getFrom(), intervals[i].getTom())) {
                    addValidationError(fieldId + ": Invalid date interval (from " + intervals[i].getFrom() + ", tom "
                            + intervals[i].getTom());
                    return false;
                }
            }
        }
        return true;

    }

    private boolean allNulls(LocalDateInterval[] intervals) {
        for (LocalDateInterval interval : intervals) {
            if (interval != null) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidInterval(LocalDate start, LocalDate end) {
        return (start != null && end != null && !start.isAfter(end));
    }

    /**
     * Checks that the given Observation is about arbetsformaga and has a period where tom>from.
     *
     * @param nedsattning
     *            nedsattning
     * @return boolean
     */
    private boolean validArbetsformageNedsattning(LocalDateInterval nedsattning) {

        if (nedsattning == null) {
            return true;
        }

        if (nedsattning.getFrom() != null && nedsattning.getTom() != null) {
            if (nedsattning.getTom().isBefore(nedsattning.getFrom())) {
                // Must be something wrong with the observationPeriod:
                addValidationError("Field 8b: Invalid date interval (from " + nedsattning.getFrom() + ", tom "
                        + nedsattning.getTom());
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

    private boolean hasMaxOneTruth(boolean... values) {
        boolean found = false;
        for (boolean b : values) {
            if (b) {
                if (found) {
                    return false;
                }
                found = true;
            }
        }
        return true;
    }
}
