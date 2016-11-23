/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.fk7263.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.validate.StringValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.intygstyper.fk7263.model.internal.PrognosBedomning;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;

public class InternalDraftValidator {

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidator.class);

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    public ValidateDraftResponse validateDraft(Utlatande utlatande) {
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
        // fält 8
        validateArbetsformaga(utlatande, validationMessages);
        // fält 6a
        validateOvrigaRekommendationer(utlatande, validationMessages);
        // fält 11
        validateRessatt(utlatande, validationMessages);
        // fält 13
        validateKommentar(utlatande, validationMessages);
        // vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return new ValidateDraftResponse(ValidatorUtil.getValidationStatus(validationMessages), validationMessages);
    }

    private void validateVardkontakter(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getTelefonkontaktMedPatienten() != null && !utlatande.getTelefonkontaktMedPatienten().isValidDate()) {
            ValidatorUtil.addValidationError(validationMessages, "intygbaseratpa.telefonkontakt", ValidationMessageType.INVALID_FORMAT);
        }
        if (utlatande.getUndersokningAvPatienten() != null && !utlatande.getUndersokningAvPatienten().isValidDate()) {
            ValidatorUtil.addValidationError(validationMessages, "intygbaseratpa.undersokning", ValidationMessageType.INVALID_FORMAT);
        }
    }

    private void validateReferenser(Utlatande utlatande, List<ValidationMessage> validationMessages) {

        // Fält 4b - höger Check that we at least got one field set regarding
        // what the certificate is based on if not smittskydd
        if (!utlatande.isAvstangningSmittskydd()) {

            if (utlatande.getUndersokningAvPatienten() == null && utlatande.getTelefonkontaktMedPatienten() == null
                    && utlatande.getJournaluppgifter() == null && utlatande.getAnnanReferens() == null) {
                ValidatorUtil.addValidationError(validationMessages, "intygbaseratpa", ValidationMessageType.EMPTY);
            }
        }

        if (utlatande.getAnnanReferens() != null && !utlatande.getAnnanReferens().isValidDate()) {
            ValidatorUtil.addValidationError(validationMessages, "intygbaseratpa.referenser", ValidationMessageType.INVALID_FORMAT);
        }
        if (utlatande.getAnnanReferens() != null && StringUtils.isBlank(utlatande.getAnnanReferensBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, "intygbaseratpa.annat", ValidationMessageType.EMPTY);
        }
        if (utlatande.getJournaluppgifter() != null && !utlatande.getJournaluppgifter().isValidDate()) {
            ValidatorUtil.addValidationError(validationMessages, "intygbaseratpa.journaluppgifter", ValidationMessageType.INVALID_FORMAT);
        }
    }

    private void validateKommentar(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        if (utlatande.getPrognosBedomning() == PrognosBedomning.arbetsformagaPrognosGarInteAttBedoma
                && StringUtils.isBlank(utlatande.getArbetsformagaPrognosGarInteAttBedomaBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, "prognos.arbetsformagaPrognosGarInteAttBedomaBeskrivning", ValidationMessageType.EMPTY);
        }
    }

    private void validateRessatt(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 11 - optional
        boolean inForandratRessatt = utlatande.isRessattTillArbeteAktuellt();
        boolean inEjForandratRessatt = utlatande.isRessattTillArbeteEjAktuellt();

        // Fält 11 - If set only one should be set
        if (inForandratRessatt && inEjForandratRessatt) {
            ValidatorUtil.addValidationError(validationMessages, "forandrat-ressatt", ValidationMessageType.OTHER,
                    "fk7263.validation.forandrat-ressatt.choose-one");
        }
    }

    private void validateArbetsformaga(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 8a - arbetsformoga - sysselsattning - applies of not smittskydd is set
        if (!utlatande.isAvstangningSmittskydd()) {
            if (!utlatande.isNuvarandeArbete() && !utlatande.isArbetsloshet() && !utlatande.isForaldrarledighet()) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY);
            } else if (utlatande.isNuvarandeArbete() && StringUtils.isBlank(utlatande.getNuvarandeArbetsuppgifter())) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning.nuvarandearbetsuppgifter", ValidationMessageType.EMPTY);
            }
        }

        // validate 8b - regardless of smittskydd
        if (utlatande.getTjanstgoringstid() != null && !STRING_VALIDATOR.validateStringIsNumber(utlatande.getTjanstgoringstid())) {
            ValidatorUtil.addValidationError(validationMessages, "nedsattning", ValidationMessageType.OTHER,
                    "fk7263.validation.nedsattning.tjanstgoringstid");
        }

        // Check that from and tom is valid in all present intervals before doing more checks
        if (isValidDateInIntervals(validationMessages, utlatande)) {
            validateIntervals(validationMessages, "nedsattning.arbetsformaga", utlatande.getNedsattMed100(), utlatande.getNedsattMed75(),
                    utlatande.getNedsattMed50(), utlatande.getNedsattMed25());
        }
    }

    private boolean isValidDateInIntervals(List<ValidationMessage> validationMessages, Utlatande utlatande) {
        boolean success = true;
        InternalLocalDateInterval[] intervals = { utlatande.getNedsattMed100(), utlatande.getNedsattMed75(), utlatande.getNedsattMed50(),
                utlatande.getNedsattMed25() };
        final int nedsattmed100Index = 0;
        final int nedsattmed75Index = 1;
        final int nedsattmed50Index = 2;
        final int nedsattmed25Index = 3;

        if (ValidatorUtil.allNulls(intervals)) {
            ValidatorUtil.addValidationError(validationMessages, "nedsattning.arbetsformaga", ValidationMessageType.EMPTY);
            return false;
        }
        // if the interval is not null and either from or tom is invalid, raise validation error
        // use independent conditions to check this to be able to give specific validation errors for each case
        if (intervals[nedsattmed100Index] != null && !intervals[nedsattmed100Index].isValid()) {
            ValidatorUtil.addValidationError(validationMessages, "nedsattning.nedsattMed100", ValidationMessageType.INVALID_FORMAT,
                    "fk7263.validation.nedsattning.nedsattmed100.incorrect-format");
            success = false;
        }
        if (intervals[nedsattmed75Index] != null && !intervals[nedsattmed75Index].isValid()) {
            ValidatorUtil.addValidationError(validationMessages, "nedsattning.nedsattMed75", ValidationMessageType.INVALID_FORMAT,
                    "fk7263.validation.nedsattning.nedsattmed75.incorrect-format");
            success = false;
        }
        if (intervals[nedsattmed50Index] != null && !intervals[nedsattmed50Index].isValid()) {
            ValidatorUtil.addValidationError(validationMessages, "nedsattning.nedsattMed50", ValidationMessageType.INVALID_FORMAT,
                    "fk7263.validation.nedsattning.nedsattmed50.incorrect-format");
            success = false;
        }
        if (intervals[nedsattmed25Index] != null && !intervals[nedsattmed25Index].isValid()) {
            ValidatorUtil.addValidationError(validationMessages, "nedsattning.nedsattMed25", ValidationMessageType.INVALID_FORMAT,
                    "fk7263.validation.nedsattning.nedsattmed25.incorrect-format");
            success = false;
        }
        return success;
    }

    private void validateAktivitetsbegransning(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 5 Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning
        String aktivitetsbegransning = utlatande.getAktivitetsbegransning();
        if (!utlatande.isAvstangningSmittskydd() && StringUtils.isBlank(aktivitetsbegransning)) {
            ValidatorUtil.addValidationError(validationMessages, "aktivitetsbegransning", ValidationMessageType.EMPTY);
        }
    }

    private void validateFunktionsnedsattning(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        String funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (!utlatande.isAvstangningSmittskydd() && StringUtils.isBlank(funktionsnedsattning)) {
            ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY);
        }
    }

    private void validateDiagnose(Utlatande utlatande, List<ValidationMessage> validationMessages) {

        // Fält 3 - always optional regardless of smittskydd

        // Fält 2 - Medicinskt tillstånd kod - mandatory if not smittskydd
        if (utlatande.isAvstangningSmittskydd()) {
            return;
        }

        if (!StringUtils.isBlank(utlatande.getDiagnosKod())) {
            String kodsystem = utlatande.getDiagnosKodsystem1();
            if (StringUtils.isBlank(kodsystem)) {
                // Default to ICD-10
                kodsystem = Diagnoskodverk.ICD_10_SE.name();
            }
            validateDiagnosKod(utlatande.getDiagnosKod(), kodsystem, "diagnos.diagnosKod", "fk7263.validation.diagnos.invalid", validationMessages);
        } else {
            ValidatorUtil.addValidationError(validationMessages, "diagnos.diagnosKod", ValidationMessageType.EMPTY,
                    "fk7263.validation.diagnos.missing");
        }

        // Validate bidiagnos 1
        if (!StringUtils.isBlank(utlatande.getDiagnosKod2())) {
            String kodsystem = utlatande.getDiagnosKodsystem2();
            if (StringUtils.isBlank(kodsystem)) {
                // Default to ICD-10
                kodsystem = Diagnoskodverk.ICD_10_SE.name();
            }
            validateDiagnosKod(utlatande.getDiagnosKod2(), kodsystem, "diagnos.diagnosKod2", "fk7263.validation.diagnos2.invalid", validationMessages);
        }

        // Validate bidiagnos 2
        if (!StringUtils.isBlank(utlatande.getDiagnosKod3())) {
            String kodsystem = utlatande.getDiagnosKodsystem3();
            if (StringUtils.isBlank(kodsystem)) {
                // Default to ICD-10
                kodsystem = Diagnoskodverk.ICD_10_SE.name();
            }
            validateDiagnosKod(utlatande.getDiagnosKod3(), kodsystem, "diagnos.diagnosKod3", "fk7263.validation.diagnos3.invalid", validationMessages);
        }

    }

    private void validateDiagnosKod(String diagnosKod, String kodsystem, String field, String msgKey, List<ValidationMessage> validationMessages) {
        // if moduleService is not available, skip this validation
        if (moduleService == null) {
            LOG.warn("Forced to skip validation of diagnosKod since an implementation of ModuleService is not available");
            return;
        }

        if (!moduleService.validateDiagnosisCode(diagnosKod, kodsystem)) {
            ValidatorUtil.addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT, msgKey);
        }
    }

    private void validateOvrigaRekommendationer(Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 6a - If Övrigt is checked, something must be entered.
        if (utlatande.isRekommendationOvrigtCheck() && StringUtils.isBlank(utlatande.getRekommendationOvrigt())) {
            ValidatorUtil.addValidationError(validationMessages, "rekommendationer.rekommendationovrigt", ValidationMessageType.EMPTY,
                    "fk7263.validation.rekommendationer.ovriga");
        }
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
        if (intervals == null || ValidatorUtil.allNulls(intervals)) {
            ValidatorUtil.addValidationError(validationMessages, fieldId, ValidationMessageType.EMPTY,
                    "fk7263.validation.nedsattning.choose-at-least-one");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                for (int j = i + 1; j < intervals.length; j++) {
                    // Overlap OR abuts(one intervals tom day == another's from day) is considered invalid
                    if (intervals[j] != null && intervals[i].overlaps(intervals[j])) {
                        ValidatorUtil.addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                "fk7263.validation.nedsattning.overlapping-date-interval");
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
