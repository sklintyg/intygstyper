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

package se.inera.intyg.intygstyper.luse.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.StringValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.intygstyper.fkparent.model.internal.Underlag;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalDraftValidator;
import se.inera.intyg.intygstyper.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;

public class InternalDraftValidatorImpl implements InternalDraftValidator<LuseUtlatande> {

    private static final int MAX_UNDERLAG = 3;

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidatorImpl.class);

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    @Autowired
    ValidatorUtil validatorUtilCommon;

    @Autowired
    ValidatorUtilFK validatorUtilFK;

    @Override
    public ValidateDraftResponse validateDraft(LuseUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Patientens adressuppgifter
        PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);
        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);
        // Kategori 2 – Andra medicinska utredningar och underlag
        validateUnderlag(utlatande, validationMessages);
        // Kategori 3 – Sjukdomsförlopp
        validateSjukdomsforlopp(utlatande, validationMessages);
        // Kategori 4 – Diagnos
        validatorUtilFK.validateDiagnose(utlatande.getDiagnoser(), validationMessages);
        // Diagnosgrund
        validateDiagnosgrund(utlatande, validationMessages);
        // Kategori 5 – Funktionsnedsättning
        validateFunktionsnedsattning(utlatande, validationMessages);
        // Kategori 6 – Aktivitetsbegränsning
        validateAktivitetsbegransning(utlatande, validationMessages);
        // Kategori 7 – Medicinska behandlingar/åtgärder
        // Kategori 8 – Medicinska förutsättningar för arbete
        validateMedicinskaForutsattningarForArbete(utlatande, validationMessages);
        // Kategori 9 – Övrigt
        // Kategori 10 – Kontakt
        validateKontaktMedFk(utlatande, validationMessages);
        // vårdenhet
        validatorUtilCommon.validateVardenhet(utlatande.getGrundData(), validationMessages);

        validateBlanksForOptionalFields(utlatande, validationMessages);

        return new ValidateDraftResponse(validatorUtilCommon.getValidationStatus(validationMessages), validationMessages);
    }

    private void validateGrundForMU(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getJournaluppgifter() == null
                && utlatande.getAnhorigsBeskrivningAvPatienten() == null && utlatande.getAnnatGrundForMU() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.baserasPa", ValidationMessageType.EMPTY);
        }

        if (utlatande.getUndersokningAvPatienten() != null) {
            validatorUtilFK.validateGrundForMuDate(utlatande.getUndersokningAvPatienten(), validationMessages, ValidatorUtilFK.GrundForMu.UNDERSOKNING);
        }
        if (utlatande.getJournaluppgifter() != null) {
            validatorUtilFK.validateGrundForMuDate(utlatande.getJournaluppgifter(), validationMessages, ValidatorUtilFK.GrundForMu.JOURNALUPPGIFTER);
        }
        if (utlatande.getAnhorigsBeskrivningAvPatienten() != null) {
            validatorUtilFK.validateGrundForMuDate(utlatande.getAnhorigsBeskrivningAvPatienten(), validationMessages, ValidatorUtilFK.GrundForMu.ANHORIGSBESKRIVNING);
        }
        if (utlatande.getAnnatGrundForMU() != null) {
            validatorUtilFK.validateGrundForMuDate(utlatande.getAnnatGrundForMU(), validationMessages, ValidatorUtilFK.GrundForMu.ANNAT);
        }

        // INTYG-2949
        if (utlatande.getUndersokningAvPatienten() == null && (utlatande.getJournaluppgifter() != null
                || utlatande.getAnhorigsBeskrivningAvPatienten() != null || utlatande.getAnnatGrundForMU() != null)
                && StringUtils.isBlank(utlatande.getMotiveringTillInteBaseratPaUndersokning())) {
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.motiveringTillInteBaseratPaUndersokning", ValidationMessageType.EMPTY);
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.annatGrundForMUBeskrivning", ValidationMessageType.EMPTY);
        }
        // R3
        if (utlatande.getAnnatGrundForMU() == null && !StringUtils.isEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.INCORRECT_COMBINATION,
                    "luse.validation.grund-for-mu.incorrect_combination_annat_beskrivning");
        }

        if (utlatande.getKannedomOmPatient() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.EMPTY);
        } else {
            boolean dateIsValid = validatorUtilCommon.validateDate(utlatande.getKannedomOmPatient(), validationMessages, "grundformu.kannedomOmPatient");
            if (dateIsValid) {
                if (utlatande.getUndersokningAvPatienten() != null && utlatande.getUndersokningAvPatienten().isValidDate()
                        && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getUndersokningAvPatienten().asLocalDate())) {
                    validatorUtilCommon.addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.OTHER,
                            "luse.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.UNDERSOKNING.RBK");
                }
                if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()
                        && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getAnhorigsBeskrivningAvPatienten().asLocalDate())) {
                    validatorUtilCommon.addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.OTHER,
                            "luse.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.ANHORIG.RBK");
                }
            }

        }
    }

    private void validateUnderlag(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getUnderlagFinns() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.underlagFinns", ValidationMessageType.EMPTY);
        } else if (utlatande.getUnderlagFinns() && utlatande.getUnderlag().isEmpty()) {
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.underlag", ValidationMessageType.EMPTY);
        } else if (!utlatande.getUnderlagFinns() && !utlatande.getUnderlag().isEmpty()) {
            // R6
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.underlagFinns", ValidationMessageType.OTHER,
                    "luse.validation.underlagfinns.incorrect_combination");
        }

        if (utlatande.getUnderlag().size() > MAX_UNDERLAG) {
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.underlag", ValidationMessageType.OTHER, "luse.validation.underlag.too_many");
        }
        for (int i = 0; i < utlatande.getUnderlag().size(); i++) {
            Underlag underlag = utlatande.getUnderlag().get(i);
            // Alla underlagstyper är godkända här utom Underlag från skolhälsovård
            if (underlag.getTyp() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "grundformu.underlag." + i + ".typ", ValidationMessageType.EMPTY,
                        "luse.validation.underlag.missing");
            } else if (!underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_HABILITERINGEN.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_LOGOPED.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANPSYKOLOG.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANFORETAGSHALSOVARD.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getId())
                    && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getId())
                    && !(underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.OVRIGT.getId()))) {
                validatorUtilCommon.addValidationError(validationMessages, "grundformu.underlag." + i + ".typ", ValidationMessageType.INVALID_FORMAT,
                        "luse.validation.underlag.incorrect_format");
            }
            if (underlag.getDatum() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "grundformu.underlag." + i + ".datum", ValidationMessageType.EMPTY,
                        "luse.validation.underlag.date.missing");
            } else if (!underlag.getDatum().isValidDate()) {
                validatorUtilCommon.addValidationError(validationMessages, "grundformu.underlag." + i + ".datum", ValidationMessageType.INVALID_FORMAT,
                        "luse.validation.underlag.date.incorrect_format");
            }
            if (StringUtils.isBlank(underlag.getHamtasFran())) {
                validatorUtilCommon.addValidationError(validationMessages, "grundformu.underlag." + i + ".hamtasFran", ValidationMessageType.EMPTY,
                        "luse.validation.underlag.hamtas-fran.missing");
            }
        }
    }

    private void validateSjukdomsforlopp(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getSjukdomsforlopp())) {
            validatorUtilCommon.addValidationError(validationMessages, "sjukdomsforlopp", ValidationMessageType.EMPTY);
        }
    }

    private void validateAktivitetsbegransning(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getAktivitetsbegransning())) {
            validatorUtilCommon.addValidationError(validationMessages, "aktivitetsbegransning", ValidationMessageType.EMPTY);
        }
    }

    private void validateMedicinskaForutsattningarForArbete(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getMedicinskaForutsattningarForArbete())) {
            validatorUtilCommon.addValidationError(validationMessages, "medicinskaforutsattningarforarbete", ValidationMessageType.EMPTY);
        }
    }

    private void validateFunktionsnedsattning(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        if (StringUtils.isBlank(utlatande.getFunktionsnedsattningAnnan())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningBalansKoordination())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningIntellektuell())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningKommunikation())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningKoncentration())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningPsykisk())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningSynHorselTal())) {
            validatorUtilCommon.addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY);
        }
    }

    private void validateDiagnosgrund(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (StringUtils.isBlank(utlatande.getDiagnosgrund())) {
            validatorUtilCommon.addValidationError(validationMessages, "diagnos.diagnosgrund", ValidationMessageType.EMPTY);
        }

        if (utlatande.getNyBedomningDiagnosgrund() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "diagnos.nyBedomningDiagnosgrund", ValidationMessageType.EMPTY);
        }

        // R13
        if (utlatande.getNyBedomningDiagnosgrund() != null && utlatande.getNyBedomningDiagnosgrund()
                && StringUtils.isBlank(utlatande.getDiagnosForNyBedomning())) {
            validatorUtilCommon.addValidationError(validationMessages, "diagnos.diagnosForNyBedomning", ValidationMessageType.EMPTY);
        }
        // R14 Inverted test of R13
        if ((utlatande.getNyBedomningDiagnosgrund() == null || !utlatande.getNyBedomningDiagnosgrund())
                && !Strings.isNullOrEmpty(utlatande.getDiagnosForNyBedomning())) {
            validatorUtilCommon.addValidationError(validationMessages, "diagnos.nyBedomningDiagnosgrund", ValidationMessageType.INCORRECT_COMBINATION,
                    "luse.validation.diagnosfornybedomning.incorrect_combination");
        }
    }

    private void validateKontaktMedFk(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // R11
        if ((utlatande.getKontaktMedFk() == null || !utlatande.getKontaktMedFk()) && !StringUtils.isBlank(utlatande.getAnledningTillKontakt())) {
            validatorUtilCommon.addValidationError(validationMessages, "Kontakt", ValidationMessageType.INCORRECT_COMBINATION,
                    "luse.validation.kontakt.incorrect_combination");
        }
    }

    private void validateBlanksForOptionalFields(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (validatorUtilCommon.isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            validatorUtilCommon.addValidationError(validationMessages, "anledningtillkontakt.blanksteg", ValidationMessageType.BLANK);
        }
        if (validatorUtilCommon.isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtilCommon.addValidationError(validationMessages, "grundformu.annat.blanksteg", ValidationMessageType.BLANK);
        }
        if (validatorUtilCommon.isBlankButNotNull(utlatande.getAvslutadBehandling())) {
            validatorUtilCommon.addValidationError(validationMessages, "avslutadBehandling.blanksteg", ValidationMessageType.BLANK);
        }
        if (validatorUtilCommon.isBlankButNotNull(utlatande.getFormagaTrotsBegransning())) {
            validatorUtilCommon.addValidationError(validationMessages, "formagatrotsbegransning.blanksteg", ValidationMessageType.BLANK);
        }
        if (validatorUtilCommon.isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            validatorUtilCommon.addValidationError(validationMessages, "pagaendebehandling.blanksteg", ValidationMessageType.BLANK);
        }
        if (validatorUtilCommon.isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            validatorUtilCommon.addValidationError(validationMessages, "planeradbehandling.blanksteg", ValidationMessageType.BLANK);
        }
        if (validatorUtilCommon.isBlankButNotNull(utlatande.getSubstansintag())) {
            validatorUtilCommon.addValidationError(validationMessages, "substansintag.blanksteg", ValidationMessageType.BLANK);
        }
        if (validatorUtilCommon.isBlankButNotNull(utlatande.getOvrigt())) {
            validatorUtilCommon.addValidationError(validationMessages, "ovrigt.blanksteg", ValidationMessageType.BLANK);
        }
    }

}
