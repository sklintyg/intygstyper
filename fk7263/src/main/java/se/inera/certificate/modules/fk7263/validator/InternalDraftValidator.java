package se.inera.certificate.modules.fk7263.validator;

import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.service.WebcertModuleService;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.dto.ValidationMessage;
import se.inera.certificate.modules.support.api.dto.ValidationStatus;
import se.inera.certificate.validate.StringValidator;

public class InternalDraftValidator {

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidator.class);

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    public ValidateDraftResponse validateDraft(Utlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        validateDiagnose(utlatande, validationMessages);
        // Falt 4
        validateFunktionsnedsattning(utlatande, validationMessages);
        // Falt 5
        validateAktivitetsbegransning(utlatande, validationMessages);
        validateArbetsformaga(utlatande, validationMessages);
        validateRessatt(utlatande, validationMessages);
        validateKommentar(utlatande, validationMessages);
        validateVardenhet(utlatande, validationMessages);
        validateOvrigaRekommendationer(utlatande, validationMessages);
        validateReferenser(utlatande, validationMessages);
        validateVardkontakter(utlatande, validationMessages);

        return new ValidateDraftResponse(getValidationStatus(validationMessages), validationMessages);
    }

    private void validateVardkontakter(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getTelefonkontaktMedPatienten() != null && !utlatande.getTelefonkontaktMedPatienten().isValidDate()) {
            addValidationError(validationMessages, "intygbaseratpa.telefonkontakt", "fk7263.validation.intyg-baserat-pa.telefonkontakt.incorrect_format");
        }
        if (utlatande.getUndersokningAvPatienten() != null && !utlatande.getUndersokningAvPatienten().isValidDate()) {
            addValidationError(validationMessages, "intygbaseratpa.undersokning", "fk7263.validation.intyg-baserat-pa.undersokning.incorrect_format");
        }
    }

    private void validateReferenser(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getAnnanReferens() != null && !utlatande.getAnnanReferens().isValidDate()) {
            addValidationError(validationMessages, "intygbaseratpa.referenser", "fk7263.validation.intyg-baserat-pa.annan.incorrect_format");
        }
        if (utlatande.getJournaluppgifter() != null && !utlatande.getJournaluppgifter().isValidDate()) {
            addValidationError(validationMessages, "intygbaseratpa.journaluppgifter", "fk7263.validation.intyg-baserat-pa.journaluppgifter.incorrect_format");
        }
    }

    private void validateVardenhet(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (isNullOrEmpty(utlatande.getIntygMetadata().getSkapadAv().getVardenhet().getPostadress())) {
            addValidationError(validationMessages, "vardperson.postadress", "fk7263.validation.vardenhet.postadress.missing");
        }

        if (isNullOrEmpty(utlatande.getIntygMetadata().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardperson.postnummer", "fk7263.validation.vardenhet.postnummer.missing");
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(utlatande.getIntygMetadata().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardperson.postnummer", "fk7263.validation.vardenhet.postnummer.incorrect-format");
        }

        if (isNullOrEmpty(utlatande.getIntygMetadata().getSkapadAv().getVardenhet().getPostort())) {
            addValidationError(validationMessages, "vardperson.postort", "fk7263.validation.vardenhet.postort.missing");
        }

        if (isNullOrEmpty(utlatande.getIntygMetadata().getSkapadAv().getVardenhet().getTelefonnummer())) {
            addValidationError(validationMessages, "vardperson.telefonnummer", "fk7263.validation.vardenhet.telefonnummer.missing");
        }
    }

    private void validateKommentar(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        if (utlatande.getAnnanReferens() != null && isNullOrEmpty(utlatande.getAnnanReferensBeskrivning())) {
            addValidationError(validationMessages, "intygbaseratpa.annat", "fk7263.validation.intyg-baserat-pa.annat.beskrivning.missing");
        }
    }

    private void validateRessatt(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 11 - optional
        boolean inForandratRessatt = utlatande.isRessattTillArbeteAktuellt();
        boolean inEjForandratRessatt = utlatande.isRessattTillArbeteEjAktuellt();

        // Fält 11 - If set only one should be set
        if (inForandratRessatt && inEjForandratRessatt) {
            addValidationError(validationMessages, "forandrat-ressatt", "fk7263.validation.forandrat-ressatt.choose-one");
        }
    }

    private void validateArbetsformaga(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 8a - arbetsformoga - sysselsattning - applies of not smittskydd is set
        if (!utlatande.isAvstangningSmittskydd()) {
            if (!utlatande.isNuvarandeArbete() && !utlatande.isArbetsloshet() && !utlatande.isForaldrarledighet()) {
                addValidationError(validationMessages, "sysselsattning", "fk7263.validation.sysselsattning.missing");
            } else if (utlatande.isNuvarandeArbete() && StringUtils.isEmpty(utlatande.getNuvarandeArbetsuppgifter())) {
                addValidationError(validationMessages, "sysselsattning", "fk7263.validation.sysselsattning.arbetsuppgifter.missing");
            }
        }

        // validate 8b - regardless of smittskydd
        if (utlatande.getTjanstgoringstid() != null && !STRING_VALIDATOR.validateStringIsNumber(utlatande.getTjanstgoringstid())) {
            addValidationError(validationMessages, "arbetsformaga", "fk7263.validation.arbetsformaga.tjanstgoringstid");
        }

        // Check that from and tom is valid in all present intervals before doing more checks
        if (isValidDateInIntervals(validationMessages, utlatande)) {
            validateIntervals(validationMessages, "arbetsformaga", utlatande.getNedsattMed100(), utlatande.getNedsattMed75(),
                    utlatande.getNedsattMed50(), utlatande.getNedsattMed25());
        }
    }

    private boolean isValidDateInIntervals(List<ValidationMessage> validationMessages, Utlatande utlatande) {
        boolean success = true;
        InternalLocalDateInterval[] intervals = {utlatande.getNedsattMed100(), utlatande.getNedsattMed75(), utlatande.getNedsattMed50(),
                utlatande.getNedsattMed25() };
        if (allNulls(intervals)) {
            return false;
        }
        // if the interval is not null and either from or tom is invalid, raise validation error
        // use independent conditions to check this to be able to give specific validation errors for each case
        if (intervals[0] != null && !intervals[0].isValid()) {
            addValidationError(validationMessages, "arbetsformaga.nedsattMed100", "fk7263.validation.arbetsformaga.nedsattmed100.incorrect-format");
            success = false;
        }
        if (intervals[1] != null && !intervals[1].isValid()) {
            addValidationError(validationMessages, "arbetsformaga.nedsattMed75", "fk7263.validation.arbetsformaga.nedsattmed75.incorrect-format");
            success = false;
        }
        if (intervals[2] != null && !intervals[2].isValid()) {
            addValidationError(validationMessages, "arbetsformaga.nedsattMed50", "fk7263.validation.arbetsformaga.nedsattmed50.incorrect-format");
            success = false;
        }
        if (intervals[3] != null && !intervals[3].isValid()) {
            addValidationError(validationMessages, "arbetsformaga.nedsattMed25", "fk7263.validation.arbetsformaga.nedsattmed25.incorrect-format");
            success = false;
        }
        return success;
    }

    private void validateAktivitetsbegransning(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 5 Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning
        String aktivitetsbegransning = utlatande.getAktivitetsbegransning();
        if (!utlatande.isAvstangningSmittskydd() && aktivitetsbegransning == null) {
            addValidationError(validationMessages, "aktivitetsbegransning", "fk7263.validation.aktivitetsbegransning.missing");
        }
    }

    private void validateFunktionsnedsattning(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        String funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (!utlatande.isAvstangningSmittskydd() && (funktionsnedsattning == null)) {
            addValidationError(validationMessages, "funktionsnedsattning", "fk7263.validation.funktionsnedsattning.missing");
        }

        // Fält 4 - höger Check that we at least got one field set regarding
        // what the certificate is based on if not smittskydd
        if (!utlatande.isAvstangningSmittskydd()) {

            if (utlatande.getUndersokningAvPatienten() == null && utlatande.getTelefonkontaktMedPatienten() == null
                    && utlatande.getJournaluppgifter() == null && utlatande.getAnnanReferens() == null) {
                addValidationError(validationMessages, "intygbaseratpa", "fk7263.validation.intyg-baserat-pa.missing");
            }

        }
    }

    private void validateDiagnose(Utlatande utlatande, List<ValidationMessage> validationMessages) {

        // Fält 3 - always optional regardless of smittskydd

        // Fält 2 - Medicinskt tillstånd kod - mandatory if not smittskydd
        if (utlatande.isAvstangningSmittskydd()) {
            return;
        }

        if (!isNullOrEmpty(utlatande.getDiagnosKod())) {
            validateDiagnosKod(utlatande.getDiagnosKod(), "diagnos", "fk7263.validation.diagnos.invalid", validationMessages);
        } else {
            addValidationError(validationMessages, "diagnos", "fk7263.validation.diagnos.missing");
        }

        // Validate bidiagnos 1
        if (!isNullOrEmpty(utlatande.getDiagnosKod2())) {
            validateDiagnosKod(utlatande.getDiagnosKod2(), "diagnos", "fk7263.validation.diagnos2.invalid", validationMessages);
        }

        // Validate bidiagnos 2
        if (!isNullOrEmpty(utlatande.getDiagnosKod3())) {
            validateDiagnosKod(utlatande.getDiagnosKod3(), "diagnos", "fk7263.validation.diagnos3.invalid", validationMessages);
        }

    }

    private void validateDiagnosKod(String diagnosKod, String field, String msgKey, List<ValidationMessage> validationMessages) {
        // if moduleService is not available, skip this validation
        if (moduleService == null) {
            LOG.warn("Forced to skip validation of diagnosKod since an implementation of ModuleService is not available");
            return;
        }

        if (!moduleService.validateDiagnosisCode(diagnosKod)) {
            addValidationError(validationMessages, field, msgKey);
        }
    }

    private void validateOvrigaRekommendationer(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 6a - If Övrigt is checked, something must be entered.
        if (utlatande.isRekommendationOvrigtCheck() && StringUtils.isEmpty(utlatande.getRekommendationOvrigt())) {
            addValidationError(validationMessages, "rekommendationer", "fk7263.validation.rekommendationer.ovriga");
        }
    }

    /**
     * Check if there are validation errors.
     *
     * @param validationMessages
     *            list of validation messages
     * @return {@link se.inera.certificate.modules.support.api.dto.ValidationStatus#VALID} if there are no errors, and
     *         {@link se.inera.certificate.modules.support.api.dto.ValidationStatus#INVALID} otherwise
     */
    private ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return (validationMessages.isEmpty()) ? se.inera.certificate.modules.support.api.dto.ValidationStatus.VALID
                : se.inera.certificate.modules.support.api.dto.ValidationStatus.INVALID;
    }

    /**
     * Create a ValidationMessage and add it to the list of messages.
     *
     * @param validationMessages
     *            list collection messages
     * @param field
     *            a String with the name of the field
     * @param msg
     *            a String with an error code for the front end implementation
     */
    private void addValidationError(List<ValidationMessage> validationMessages, String field, String msg) {
        validationMessages.add(new ValidationMessage(field, msg));
        LOG.debug(field + " " + msg);
    }

    /**
     *
     * @param validationMessages
     *            list collecting message
     * @param fieldId
     *            field id
     * @param intervals
     *            intervals
     * @return booleans
     */
    protected boolean validateIntervals(List<ValidationMessage> validationMessages, String fieldId, InternalLocalDateInterval... intervals) {
        if (intervals == null || allNulls(intervals)) {
            addValidationError(validationMessages, fieldId, "fk7263.validation.arbetsformaga.choose-at-least-one");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                Interval oneInterval = createInterval(intervals[i].fromAsLocalDate(), intervals[i].tomAsLocalDate());
                if (oneInterval == null) {
                    addValidationError(validationMessages, fieldId, "fk7263.validation.arbetsformaga.incorrect-date-interval");
                    return false;
                }
                for (int j = i + 1; j < intervals.length; j++) {
                    if (intervals[j] != null) {
                        Interval anotherInterval = createInterval(intervals[j].fromAsLocalDate(), intervals[j].tomAsLocalDate());
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
     * @param intervals
     *            intervals
     * @return boolean
     */
    private boolean allNulls(InternalLocalDateInterval[] intervals) {
        for (InternalLocalDateInterval interval : intervals) {
            if (interval != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param start
     *            start
     * @param end
     *            end
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
