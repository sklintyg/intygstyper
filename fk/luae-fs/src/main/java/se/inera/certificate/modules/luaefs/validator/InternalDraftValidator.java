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

package se.inera.certificate.modules.luaefs.validator;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.modules.fkparent.model.validator.InternalValidatorUtil;
import se.inera.certificate.modules.luaefs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luaefs.model.internal.Underlag;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.StringValidator;

import java.util.ArrayList;
import java.util.List;

public class InternalDraftValidator {

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidator.class);

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    @Autowired
    InternalValidatorUtil validatorUtil;

    public InternalDraftValidator() {
    }

    @VisibleForTesting
    public InternalDraftValidator(InternalValidatorUtil validatorUtil) {
        this.validatorUtil = validatorUtil;
    }

    public ValidateDraftResponse validateDraft(LuaefsUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);

        // Kategori 2 - Andra medicinska utredningar och underlag
        validateUnderlag(utlatande, validationMessages);

        // Kategori 3 – Diagnos
        validatorUtil.validateDiagnose(utlatande.getTyp(), utlatande.getDiagnoser(), validationMessages);

        // Kategori 4 – Funktionsnedsättning
        validateFunktionsnedsattning(utlatande, validationMessages);

        // Kategori 8 – Övrigt

        // Kategori 9 – Kontakt
        validateKontakt(utlatande, validationMessages);

        // Vårdenhet
        validateVardenhet(utlatande, validationMessages);

        return new ValidateDraftResponse(getValidationStatus(validationMessages), validationMessages);
    }

    private void validateGrundForMU(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required
        if (utlatande.getUndersokningAvPatienten() == null
                && utlatande.getJournaluppgifter() == null
                && utlatande.getAnhorigsBeskrivningAvPatienten() == null
                && utlatande.getAnnatGrundForMU() == null) {
            validatorUtil.addValidationError(validationMessages, "grundformu", ValidationMessageType.EMPTY,
                    "luaefs.validation.grund-for-mu.missing");
        }

        // Validate dates
        if (utlatande.getUndersokningAvPatienten() != null && !utlatande.getUndersokningAvPatienten().isValidDate()) {
            validatorUtil.addValidationError(validationMessages, "grundformu.undersokning", ValidationMessageType.INVALID_FORMAT,
                    "luaefs.validation.grund-for-mu.undersokning.incorrect_format");
        }
        if (utlatande.getJournaluppgifter() != null && !utlatande.getJournaluppgifter().isValidDate()) {
            validatorUtil.addValidationError(validationMessages, "grundformu.journaluppgifter", ValidationMessageType.INVALID_FORMAT,
                    "luse.validation.grund-for-mu.journaluppgifter.incorrect_format");
        }
        if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && !utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()) {
            validatorUtil.addValidationError(validationMessages, "grundformu.anhorigsbeskrivning", ValidationMessageType.INVALID_FORMAT,
                    "luaefs.validation.grund-for-mu.anhorigsbeskrivning.incorrect_format");
        }
        if (utlatande.getAnnatGrundForMU() != null && !utlatande.getAnnatGrundForMU().isValidDate()) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.INVALID_FORMAT,
                    "luaefs.validation.grund-for-mu.annat.incorrect_format");
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "luaefs.validation.grund-for-mu.annat.beskrivning.missing");
        }

        // R3
        if (utlatande.getAnnatGrundForMU() == null && !StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "luaefs.validation.grund-for-mu.annat.beskrivning.invalid_combination");
        }

        if (utlatande.getKannedomOmPatient() == null) {
            validatorUtil.addValidationError(validationMessages, "grundformu.kannedom", ValidationMessageType.EMPTY,
                    "luaefs.validation.grund-for-mu.kannedom.missing");
        } else if (!utlatande.getKannedomOmPatient().isValidDate()) {
            validatorUtil.addValidationError(validationMessages, "grundformu.kannedom", ValidationMessageType.INVALID_FORMAT,
                    "luaefs.validation.grund-for-mu.kannedom.incorrect_format");
        } else {
            if (utlatande.getUndersokningAvPatienten() != null && utlatande.getUndersokningAvPatienten().isValidDate()
                    && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getUndersokningAvPatienten().asLocalDate())) {
                validatorUtil.addValidationError(validationMessages, "grundformu.kannedom", ValidationMessageType.OTHER,
                        "luaefs.validation.grund-for-mu.kannedom.after.undersokning");
            }
            if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()
                    && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getAnhorigsBeskrivningAvPatienten().asLocalDate())) {
                validatorUtil.addValidationError(validationMessages, "grundformu.kannedom", ValidationMessageType.OTHER,
                        "luaefs.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning");
            }
        }
    }

    private void validateUnderlag(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getUnderlagFinns() == null) {
            validatorUtil.addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                    "luse.validation.underlagfinns.missing");
        } else if (utlatande.getUnderlagFinns() && utlatande.getUnderlag().isEmpty()) {
            validatorUtil.addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                    "luse.validation.underlagfinns.missing");
        } else if (!utlatande.getUnderlagFinns() && !utlatande.getUnderlag().isEmpty()) {
            // R6
            validatorUtil.addValidationError(validationMessages, "underlag", ValidationMessageType.INVALID_FORMAT,
                    "luse.validation.underlagfinns.incorrect_combination");
        }

        for (Underlag underlag : utlatande.getUnderlag()) {
            // Alla underlagstyper är godkända här utom Underlag från företagshälsovård
            if (underlag.getTyp() == null) {
                validatorUtil.addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                        "luse.validation.underlag.missing");
            } else if (underlag.getTyp().getId() != Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE.getId()
                    && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRAN_HABILITERINGEN.getId()
                    && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT.getId()
                    && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT.getId()
                    && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRAN_LOGOPED.getId()
                    && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRANPSYKOLOG.getId()
                    && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRANSKOLHALSOVARD.getId()
                    && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getId()
                    && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getId()
                    && underlag.getTyp().getId() != Underlag.UnderlagsTyp.OVRIGT.getId()) {
                validatorUtil.addValidationError(validationMessages, "underlag", ValidationMessageType.INVALID_FORMAT,
                        "luse.validation.underlag.incorrect_format");
            }
            if (underlag.getDatum() == null) {
                validatorUtil.addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                        "luse.validation.underlag.date.missing");
            } else if (!underlag.getDatum().isValidDate()) {
                validatorUtil.addValidationError(validationMessages, "underlag", ValidationMessageType.INVALID_FORMAT,
                        "luse.validation.underlag.date.incorrect_format");
            }
            if (underlag.getHamtasFran() == null) {
                validatorUtil.addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                        "luse.validation.underlag.hamtas-fran.missing");
            }
        }
    }

    private void validateFunktionsnedsattning(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getFunktionsnedsattningDebut())) {
            validatorUtil.addValidationError(validationMessages, "funktionsnedsattning.debut", ValidationMessageType.EMPTY,
                    "luaefs.validation.funktionsnedsattning.debut.missing");
        }
        if (StringUtils.isBlank(utlatande.getFunktionsnedsattningPaverkan())) {
            validatorUtil.addValidationError(validationMessages, "funktionsnedsattning.paverkan", ValidationMessageType.EMPTY,
                    "luaefs.validation.funktionsnedsattning.paverkan.missing");
        }
    }

    private void validateKontakt(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getKontaktMedFk() != null && !utlatande.getKontaktMedFk() && !StringUtils.isBlank(utlatande.getAnledningTillKontakt())) {
            validatorUtil.addValidationError(validationMessages, "kontakt", ValidationMessageType.EMPTY,
                    "luaefs.validation.kontakt.invalid_combination");
        }
    }

    private void validateVardenhet(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostadress())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.adress", ValidationMessageType.EMPTY,
                    "luaefs.validation.vardenhet.postadress.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "luaefs.validation.vardenhet.postnummer.missing");
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "luaefs.validation.vardenhet.postnummer.incorrect-format");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostort())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.postort", ValidationMessageType.EMPTY,
                    "luaefs.validation.vardenhet.postort.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.telefonnummer", ValidationMessageType.EMPTY,
                    "luaefs.validation.vardenhet.telefonnummer.missing");
        }
    }

    /**
     * Check if there are validation errors.
     */
    private ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return (validationMessages.isEmpty()) ? ValidationStatus.VALID : ValidationStatus.INVALID;
    }
}