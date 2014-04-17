package se.inera.certificate.modules.fk7263.validator;

import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.dto.ValidationMessage;
import se.inera.certificate.modules.support.api.dto.ValidationStatus;

public class InternalDraftValidator {
    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidator.class);

    public ValidateDraftResponse validateDraft(Fk7263Intyg utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        validateDiagnose(utlatande, validationMessages);
        validateFunktionsnedsattning(utlatande, validationMessages);
        validateArbetsformaga(utlatande, validationMessages);
        validatePrognos(utlatande, validationMessages);
        validateRessatt(utlatande, validationMessages);
        validateKommentar(utlatande, validationMessages);

        return new ValidateDraftResponse(getValidationStatus(validationMessages), validationMessages);
    }

    private void validateKommentar(Fk7263Intyg utlatande, List<ValidationMessage> validationMessages) {
        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        if (utlatande.getAnnanReferens() != null && isNullOrEmpty(utlatande.getAnnanReferensBeskrivning())) {
            addValidationError(validationMessages, "Field 4", "fk7263.validation.intyg-baserat-pa.annat.beskrivning.missing");
        }

        if ((utlatande.getPrognosis() != null && utlatande.getPrognosis().equals("UNKNOWN"))
                && isNullOrEmpty(utlatande.getArbetsformagaPrognosGarInteAttBedomBeskrivning())) {
            addValidationError(validationMessages, "Field 10", "fk7263.validation.prognos.gar-ej-att-bedomma.beskrivning.missing");
        }

    }

    private void validateRessatt(Fk7263Intyg utlatande, List<ValidationMessage> validationMessages) {
        // Fält 11 - optional
        boolean inForandratRessatt = utlatande.isRessattTillArbeteAktuellt();
        boolean inEjForandratRessatt = utlatande.isRessattTillArbeteEjAktuellt();

        // Fält 11 - If set only one should be set
        if (inForandratRessatt && inEjForandratRessatt) {
            addValidationError(validationMessages, "Field 11", "Only one forandrat ressatt could be set.");
        }
    }

    private void validatePrognos(Fk7263Intyg utlatande, List<ValidationMessage> validationMessages) {
        // Fält 10 - 4 optional checkboxes (but exclusive!)
        if (!hasMaxOneTruth(utlatande.isArbetsformataPrognosGarInteAttBedoma(), utlatande.isArbetsformataPrognosJa(),
                utlatande.isArbetsformataPrognosJaDelvis(), utlatande.isArbetsformataPrognosNej())) {
            addValidationError(validationMessages, "Field 10", "Only one arbetsformaga.prognos field can be checked!");
        }
    }

    private void validateArbetsformaga(Fk7263Intyg utlatande, List<ValidationMessage> validationMessages) {
        // Fält 8a - arbetsformoga - sysselsattning - applies of not smittskydd
        // is set
        if (!utlatande.isAvstangningSmittskydd()) {
            boolean hasArbetsuppgifts = !StringUtils.isEmpty(utlatande.getNuvarandeArbetsuppgifter());

            if (!hasArbetsuppgifts && !utlatande.isArbetsloshet() && !utlatande.isForaldrarledighet()) {
                addValidationError(validationMessages, "Field 8a", "fk7263.validation.sysselsattning.missing");
            }
        }

        // validate 8b - regardless of smittskydd
        validateIntervals(validationMessages, "Field 8b", utlatande.getNedsattMed100(), utlatande.getNedsattMed75(),
                utlatande.getNedsattMed50(), utlatande.getNedsattMed25());
    }

    private void validateFunktionsnedsattning(Fk7263Intyg utlatande, List<ValidationMessage> validationMessages) {
        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        String funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (!utlatande.isAvstangningSmittskydd() && (funktionsnedsattning == null)) {
            addValidationError(validationMessages, "Field 4", "fk7263.validation.funktionsnedsattning.missing");
            return;
        }

        // Fält 4 - höger Check that we at least got one field set regarding
        // what the certificate is based on if not smittskydd
        if (!utlatande.isAvstangningSmittskydd()) {

            if (utlatande.getUndersokningAvPatienten() == null && utlatande.getTelefonkontaktMedPatienten() == null
                    && utlatande.getJournaluppgifter() == null && utlatande.getAnnanReferens() == null) {
                addValidationError(validationMessages, "Field 4", "fk7263.validation.intyg-baserat-pa.missing");
            }

        }
    }

    private void validateDiagnose(Fk7263Intyg utlatande, List<ValidationMessage> validationMessages) {
        // Fält 2 - Medicinskt tillstånd kod - mandatory if not smittskydd
        if (!utlatande.isAvstangningSmittskydd() && isNullOrEmpty(utlatande.getDiagnosKod())) {
            addValidationError(validationMessages, "Field 2", "fk7263.validation.diagnos.missing");
        }

        // Fält 3 - always optional regardless of smittskydd
    }

    /**
     * Check if there are validation errors.
     *
     * @param validationMessages list of validation messages
     * @return {@link se.inera.certificate.modules.support.api.dto.ValidationStatus#VALID}
     * if there are no errors, and {@link se.inera.certificate.modules.support.api.dto.ValidationStatus#INVALID}
     * otherwise
     */
    private ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return (validationMessages.isEmpty()) ? se.inera.certificate.modules.support.api.dto.ValidationStatus.VALID
                : se.inera.certificate.modules.support.api.dto.ValidationStatus.INVALID;
    }

    /**
     * Create a ValidationMessage and add it to the list of messages.
     *
     * @param validationMessages list collection messages
     * @param field a String with the name of the field
     * @param msg   a String with an error code for the front end implementation
     */
    private void addValidationError(List<ValidationMessage> validationMessages, String field, String msg) {
        validationMessages.add(new ValidationMessage(field, msg));
        LOG.debug(field + " " + msg);
    }

    /**
     * @param values values
     * @return boolean
     */
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

    /**
     *
     * @param validationMessages list collecting message
     * @param fieldId field id
     * @param intervals intervals
     * @return booleans
     */
    protected boolean validateIntervals(List<ValidationMessage> validationMessages, String fieldId, LocalDateInterval... intervals) {
        if (intervals == null || allNulls(intervals)) {
            addValidationError(validationMessages, fieldId, "fk7263.validation.arbetsformaga.choose-at-least-one");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                Interval oneInterval = createInterval(intervals[i].getFrom(), intervals[i].getTom());
                if (oneInterval == null) {
                    addValidationError(validationMessages, fieldId, "fk7263.validation.arbetsformaga.incorrect-date-interval");
                    return false;
                }
                for (int j = i + 1; j < intervals.length; j++) {
                    if (intervals[j] != null) {
                        Interval anotherInterval = createInterval(intervals[j].getFrom(), intervals[j].getTom());
                        if (anotherInterval == null) {
                            addValidationError(validationMessages, fieldId, "fk7263.validation.arbetsformaga.incorrect-date-interval");
                            return false;
                        }
                        // Overlap OR abuts(one intervals tom day== another's
                        // from day) is considered invalid
                        if (oneInterval.overlaps(anotherInterval) || oneInterval.abuts(anotherInterval)) {
                            addValidationError(validationMessages, fieldId, "fk7263.validation.arbetsformaga.overlapping-date-interval");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param intervals intervals
     * @return boolean
     */
    private boolean allNulls(LocalDateInterval[] intervals) {
        for (LocalDateInterval interval : intervals) {
            if (interval != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param start start
     * @param end end
     * @return Interval
     */
    private Interval createInterval(LocalDate start, LocalDate end) {
        if ((start == null || end == null || start.isAfter(end))) {
            return null;
        } else {
            return new Interval(start.toDate().getTime(), end.toDate().getTime());
        }
    }

}
