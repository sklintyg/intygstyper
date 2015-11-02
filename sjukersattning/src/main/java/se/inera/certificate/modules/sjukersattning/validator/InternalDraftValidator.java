package se.inera.certificate.modules.sjukersattning.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.common.enumerations.Diagnoskodverk;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.modules.service.WebcertModuleService;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.dto.ValidationMessage;
import se.inera.certificate.modules.support.api.dto.ValidationMessageType;
import se.inera.certificate.modules.support.api.dto.ValidationStatus;
import se.inera.certificate.validate.StringValidator;

public class InternalDraftValidator {

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidator.class);

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    public ValidateDraftResponse validateDraft(SjukersattningUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // intyget baseras på
        validateVardkontakter(utlatande, validationMessages);
        validateReferenser(utlatande, validationMessages);
        // fält 2
        validateDiagnose(utlatande, validationMessages);
        // Falt 4
        validateFunktionsnedsattning(utlatande, validationMessages);
        // Falt 5
        validateAktivitetsbegransning(utlatande, validationMessages);
        // vårdenhet
        validateVardenhet(utlatande, validationMessages);

        return new ValidateDraftResponse(getValidationStatus(validationMessages), validationMessages);
    }

    private void validateVardkontakter(SjukersattningUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getTelefonkontaktMedPatienten() != null && !utlatande.getTelefonkontaktMedPatienten().isValidDate()) {
            addValidationError(validationMessages, "intygbaseratpa.telefonkontakt", ValidationMessageType.INVALID_FORMAT,
                    "sjukersattning.validation.intyg-baserat-pa.telefonkontakt.incorrect_format");
        }
        if (utlatande.getUndersokningAvPatienten() != null && !utlatande.getUndersokningAvPatienten().isValidDate()) {
            addValidationError(validationMessages, "intygbaseratpa.undersokning", ValidationMessageType.INVALID_FORMAT,
                    "sjukersattning.validation.intyg-baserat-pa.undersokning.incorrect_format");
        }
    }

    private void validateReferenser(SjukersattningUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getTelefonkontaktMedPatienten() == null
                && utlatande.getJournaluppgifter() == null) {
            addValidationError(validationMessages, "intygbaseratpa", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.intyg-baserat-pa.missing");
        }

        if (utlatande.getJournaluppgifter() != null && !utlatande.getJournaluppgifter().isValidDate()) {
            addValidationError(validationMessages, "intygbaseratpa.journaluppgifter", ValidationMessageType.INVALID_FORMAT,
                    "sjukersattning.validation.intyg-baserat-pa.journaluppgifter.incorrect_format");
        }
    }

    private void validateVardenhet(SjukersattningUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostadress())) {
            addValidationError(validationMessages, "vardenhet.adress", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.postadress.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.postnummer.missing");
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.postnummer.incorrect-format");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostort())) {
            addValidationError(validationMessages, "vardenhet.postort", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.postort.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer())) {
            addValidationError(validationMessages, "vardenhet.telefonnummer", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.vardenhet.telefonnummer.missing");
        }
    }

    private void validateAktivitetsbegransning(SjukersattningUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 5 Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning
        // TODO
//        String aktivitetsbegransning = utlatande.getAktivitetsbegransning();
//        if (StringUtils.isBlank(aktivitetsbegransning)) {
//            addValidationError(validationMessages, "aktivitetsbegransning", ValidationMessageType.EMPTY,
//                    "sjukersattning.validation.aktivitetsbegransning.missing");
//        }
    }

    private void validateFunktionsnedsattning(SjukersattningUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 4 - vänster Check that we got a funktionsnedsattning element
    }

    private void validateDiagnose(SjukersattningUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (!StringUtils.isBlank(utlatande.getDiagnosKod1())) {
            String kodsystem = utlatande.getDiagnosKodsystem1();
            if (StringUtils.isBlank(kodsystem)) {
                // Default to ICD-10
                kodsystem = Diagnoskodverk.ICD_10_SE.name();
            }
            validateDiagnosKod(utlatande.getDiagnosKod1(), kodsystem, "diagnos", "sjukersattning.validation.diagnos.invalid", validationMessages);
        } else {
            addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                    "sjukersattning.validation.diagnos.missing");
        }

        // Validate bidiagnos 1
        if (!StringUtils.isBlank(utlatande.getDiagnosKod2())) {
            String kodsystem = utlatande.getDiagnosKodsystem2();
            if (StringUtils.isBlank(kodsystem)) {
                // Default to ICD-10
                kodsystem = Diagnoskodverk.ICD_10_SE.name();
            }
            validateDiagnosKod(utlatande.getDiagnosKod2(), kodsystem, "diagnos", "sjukersattning.validation.diagnos2.invalid", validationMessages);
        }

        // Validate bidiagnos 2
        if (!StringUtils.isBlank(utlatande.getDiagnosKod3())) {
            String kodsystem = utlatande.getDiagnosKodsystem3();
            if (StringUtils.isBlank(kodsystem)) {
                // Default to ICD-10
                kodsystem = Diagnoskodverk.ICD_10_SE.name();
            }
            validateDiagnosKod(utlatande.getDiagnosKod3(), kodsystem, "diagnos", "sjukersattning.validation.diagnos3.invalid", validationMessages);
        }

    }

    private void validateDiagnosKod(String diagnosKod, String kodsystem, String field, String msgKey, List<ValidationMessage> validationMessages) {
        // if moduleService is not available, skip this validation
        if (moduleService == null) {
            LOG.warn("Forced to skip validation of diagnosKod since an implementation of ModuleService is not available");
            return;
        }

        if (!moduleService.validateDiagnosisCode(diagnosKod, kodsystem)) {
            addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT, msgKey);
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
        return (validationMessages.isEmpty()) ? ValidationStatus.VALID : ValidationStatus.INVALID;
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
    private void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type, String msg) {
        validationMessages.add(new ValidationMessage(field, type, msg));
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
            addValidationError(validationMessages, fieldId, ValidationMessageType.EMPTY,
                    "sjukersattning.validation.nedsattning.choose-at-least-one");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                Interval oneInterval = createInterval(intervals[i].fromAsLocalDate(), intervals[i].tomAsLocalDate());
                if (oneInterval == null) {
                    addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                            "sjukersattning.validation.nedsattning.incorrect-date-interval");
                    return false;
                }
                for (int j = i + 1; j < intervals.length; j++) {
                    if (intervals[j] != null) {
                        Interval anotherInterval = createInterval(intervals[j].fromAsLocalDate(), intervals[j].tomAsLocalDate());
                        if (anotherInterval == null) {
                            addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                    "sjukersattning.validation.nedsattning.incorrect-date-interval");
                            return false;
                        }
                        // Overlap OR abuts(one intervals tom day== another's
                        // from day) is considered invalid
                        if (oneInterval.overlaps(anotherInterval) || oneInterval.abuts(anotherInterval)) {
                            addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                    "sjukersattning.validation.nedsattning.overlapping-date-interval");
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
