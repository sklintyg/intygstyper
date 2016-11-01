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

package se.inera.intyg.intygstyper.luae_fs.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableList;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.StringValidator;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.fkparent.model.internal.Underlag;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalDraftValidator;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalValidatorUtil;
import se.inera.intyg.intygstyper.luae_fs.model.internal.LuaefsUtlatande;

public class InternalDraftValidatorImpl implements InternalDraftValidator<LuaefsUtlatande> {

    public static final int MAX_DIAGNOSER = 3;

    @Autowired
    InternalValidatorUtil validatorUtil;

    @Override
    public ValidateDraftResponse validateDraft(LuaefsUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Patientens adressuppgifter
        PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);

        // Kategori 2 - Andra medicinska utredningar och underlag
        validateUnderlag(utlatande, validationMessages);

        // Kategori 3 – Diagnos
        validateDiagnose(utlatande, validationMessages);

        // Kategori 4 – Funktionsnedsättning
        validateFunktionsnedsattning(utlatande, validationMessages);

        // Kategori 8 – Övrigt

        // Kategori 9 – Kontakt
        validateKontakt(utlatande, validationMessages);

        validateBlanksForOptionalFields(utlatande, validationMessages);

        // Vårdenhet
        validatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return new ValidateDraftResponse(validatorUtil.getValidationStatus(validationMessages), validationMessages);
    }

    void validateGrundForMU(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required
        if (utlatande.getUndersokningAvPatienten() == null
                && utlatande.getJournaluppgifter() == null
                && utlatande.getAnhorigsBeskrivningAvPatienten() == null
                && utlatande.getAnnatGrundForMU() == null) {
            validatorUtil.addValidationError(validationMessages, "grundformu.baserasPa", ValidationMessageType.EMPTY);
        }

        if (utlatande.getUndersokningAvPatienten() != null) {
            validatorUtil.validateGrundForMuDate(utlatande.getUndersokningAvPatienten(), validationMessages, InternalValidatorUtil.GrundForMu.UNDERSOKNING);
        }
        if (utlatande.getJournaluppgifter() != null) {
            validatorUtil.validateGrundForMuDate(utlatande.getJournaluppgifter(), validationMessages, InternalValidatorUtil.GrundForMu.JOURNALUPPGIFTER);
        }
        if (utlatande.getAnhorigsBeskrivningAvPatienten() != null) {
            validatorUtil.validateGrundForMuDate(utlatande.getAnhorigsBeskrivningAvPatienten(), validationMessages, InternalValidatorUtil.GrundForMu.ANHORIGSBESKRIVNING);
        }
        if (utlatande.getAnnatGrundForMU() != null) {
            validatorUtil.validateGrundForMuDate(utlatande.getAnnatGrundForMU(), validationMessages, InternalValidatorUtil.GrundForMu.ANNAT);
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annatGrundForMUBeskrivning", ValidationMessageType.EMPTY);
        }

        // R3
        if (utlatande.getAnnatGrundForMU() == null && !StringUtils.isEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "luae_fs.validation.grund-for-mu.annat.beskrivning.invalid_combination");
        }

        if (utlatande.getKannedomOmPatient() == null) {
            validatorUtil.addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.EMPTY);
        } else {
            boolean dateIsValid = validatorUtil.validateDate(utlatande.getKannedomOmPatient(), validationMessages, "grundformu.kannedomOmPatient");
            if (dateIsValid) {
                if (utlatande.getUndersokningAvPatienten() != null && utlatande.getUndersokningAvPatienten().isValidDate()
                        && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getUndersokningAvPatienten().asLocalDate())) {
                    validatorUtil.addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.OTHER,
                            "luae_fs.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.UNDERSOKNING.RBK");
                }
                if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()
                        && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getAnhorigsBeskrivningAvPatienten().asLocalDate())) {
                    validatorUtil.addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.OTHER,
                            "luae_fs.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.ANHORIG.RBK");
                }
            }
        }
    }

    void validateUnderlag(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getUnderlagFinns() == null) {
            validatorUtil.addValidationError(validationMessages, "grundformu.underlagFinns", ValidationMessageType.EMPTY);
            // If the flag is null, we cant determine whether underlag should be a list or not, so we can't do any
            // further validation..
            return;
        } else if (utlatande.getUnderlagFinns() && (utlatande.getUnderlag() == null || utlatande.getUnderlag().isEmpty())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.underlag", ValidationMessageType.EMPTY);
        } else if (!(utlatande.getUnderlagFinns() || utlatande.getUnderlag().isEmpty())) {
            // R6
            validatorUtil.addValidationError(validationMessages, "grundformu.underlagFinns", ValidationMessageType.INVALID_FORMAT,
                    "luae_fs.validation.underlagfinns.incorrect_combination");
        }

        for (int i = 0; i < utlatande.getUnderlag().size(); i++) {
            Underlag underlag = utlatande.getUnderlag().get(i);
            // Alla underlagstyper är godkända här utom Underlag från företagshälsovård
            if (underlag.getTyp() == null) {
                validatorUtil.addValidationError(validationMessages, "grundformu.underlag." + i + ".typ", ValidationMessageType.EMPTY,
                        "luae_fs.validation.underlag.missing");
            } else if (!underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_HABILITERINGEN.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_LOGOPED.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANPSYKOLOG.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANSKOLHALSOVARD.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.OVRIGT.getId())) {
                validatorUtil.addValidationError(validationMessages, "grundformu.underlag." + i + ".typ", ValidationMessageType.INVALID_FORMAT,
                        "luae_fs.validation.underlag.incorrect_format");
            }
            if (underlag.getDatum() == null) {
                validatorUtil.addValidationError(validationMessages, "grundformu.underlag." + i + ".datum", ValidationMessageType.EMPTY,
                        "luae_fs.validation.underlag.date.missing");
            } else if (!underlag.getDatum().isValidDate()) {
                validatorUtil.addValidationError(validationMessages, "grundformu.underlag." + i + ".datum", ValidationMessageType.INVALID_FORMAT,
                        "luae_fs.validation.underlag.date.incorrect_format");
            }
            if (underlag.getHamtasFran() == null || underlag.getHamtasFran().trim().isEmpty()) {
                validatorUtil.addValidationError(validationMessages, "grundformu.underlag." + i + ".hamtasFran", ValidationMessageType.EMPTY,
                        "luae_fs.validation.underlag.hamtas-fran.missing");
            }
        }
    }

    void validateDiagnose(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {
        validateNumberOfDiagnose(utlatande.getDiagnoser(), validationMessages);
        validatorUtil.validateDiagnose(utlatande.getDiagnoser(), validationMessages);
    }

    private void validateNumberOfDiagnose(ImmutableList<Diagnos> diagnoser, List<ValidationMessage> validationMessages) {
        if (diagnoser.size() > MAX_DIAGNOSER) {
            validatorUtil.addValidationError(validationMessages, "diagnos", ValidationMessageType.OTHER,
                    "luae_fs.validation.diagnos.max-diagnoser");
        }
    }

    void validateFunktionsnedsattning(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getFunktionsnedsattningDebut())) {
            validatorUtil.addValidationError(validationMessages, "funktionsnedsattning.funktionsnedsattningDebut", ValidationMessageType.EMPTY,
                    "luae_fs.validation.funktionsnedsattning.debut.missing");
        }
        if (StringUtils.isBlank(utlatande.getFunktionsnedsattningPaverkan())) {
            validatorUtil.addValidationError(validationMessages, "funktionsnedsattning.funktionsnedsattningPaverkan", ValidationMessageType.EMPTY,
                    "luae_fs.validation.funktionsnedsattning.paverkan.missing");
        }
    }

    void validateKontakt(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getKontaktMedFk() != null && !utlatande.getKontaktMedFk() && !StringUtils.isBlank(utlatande.getAnledningTillKontakt())) {
            validatorUtil.addValidationError(validationMessages, "kontakt", ValidationMessageType.EMPTY,
                    "luae_fs.validation.kontakt.invalid_combination");
        }
    }

    private void validateBlanksForOptionalFields(LuaefsUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (validatorUtil.isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            validatorUtil.addValidationError(validationMessages, "anledningtillkontakt.blanksteg", ValidationMessageType.EMPTY,
                    "luae_fs.validation.blanksteg.otillatet");
        }
        if (validatorUtil.isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat.blanksteg", ValidationMessageType.EMPTY,
                    "luae_fs.validation.blanksteg.otillatet");
        }
        if (validatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            validatorUtil.addValidationError(validationMessages, "ovrigt.blanksteg", ValidationMessageType.EMPTY,
                    "luae_fs.validation.blanksteg.otillatet");
        }
    }
}
