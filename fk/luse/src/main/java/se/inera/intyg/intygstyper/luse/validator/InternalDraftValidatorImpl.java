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

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
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
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;

import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;

public class InternalDraftValidatorImpl implements InternalDraftValidator<LuseUtlatande> {

    private static final int MAX_UNDERLAG = 3;

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidatorImpl.class);

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    @Autowired
    InternalValidatorUtil validatorUtil;

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
        validatorUtil.validateDiagnose(utlatande.getTyp(), utlatande.getDiagnoser(), validationMessages);
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

        validateBlanksForOptionalFields(utlatande, validationMessages);
        // vårdenhet
        validateVardenhet(utlatande, validationMessages);

        return new ValidateDraftResponse(getValidationStatus(validationMessages), validationMessages);
    }

    private boolean validateDate(InternalDate date, List<ValidationMessage> validationMessages, String field) {
        boolean valid = true;
        if (!date.isValidDate()) {
            addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT);
            return false;
        }

        if (!date.isReasonable()) {
            addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT,
                    "luse.validation.general.date_out_of_range");
            valid = false;
        }
        return valid;
    }

    private void validateGrundForMuDate(InternalDate date, List<ValidationMessage> validationMessages, GrundForMu type) {
        String validationType = "grundformu." + type.getFieldName();
        validateDate(date, validationMessages, validationType);

    }

    private enum GrundForMu {
        UNDERSOKNING,
        JOURNALUPPGIFTER,
        ANHORIGSBESKRIVNING,
        ANNAT;

        public String getFieldName() {
            switch (this) {
            case UNDERSOKNING:
                return GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
            case ANHORIGSBESKRIVNING:
                return GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
            case JOURNALUPPGIFTER:
                return GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
            case ANNAT:
                return GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
            default:
                return "annat";
            }

        }

    }

    private void validateGrundForMU(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getJournaluppgifter() == null
                && utlatande.getAnhorigsBeskrivningAvPatienten() == null && utlatande.getAnnatGrundForMU() == null) {
            addValidationError(validationMessages, "grundformu.baserasPa", ValidationMessageType.EMPTY);
        }

        if (utlatande.getUndersokningAvPatienten() != null) {
            validateGrundForMuDate(utlatande.getUndersokningAvPatienten(), validationMessages, GrundForMu.UNDERSOKNING);
        }
        if (utlatande.getJournaluppgifter() != null) {
            validateGrundForMuDate(utlatande.getJournaluppgifter(), validationMessages, GrundForMu.JOURNALUPPGIFTER);
        }
        if (utlatande.getAnhorigsBeskrivningAvPatienten() != null) {
            validateGrundForMuDate(utlatande.getAnhorigsBeskrivningAvPatienten(), validationMessages, GrundForMu.ANHORIGSBESKRIVNING);
        }
        if (utlatande.getAnnatGrundForMU() != null) {
            validateGrundForMuDate(utlatande.getAnnatGrundForMU(), validationMessages, GrundForMu.ANNAT);
        }

        // INTYG-2949
        if (utlatande.getUndersokningAvPatienten() == null && 
                (utlatande.getJournaluppgifter() != null || utlatande.getAnhorigsBeskrivningAvPatienten() != null || utlatande.getAnnatGrundForMU() != null) &&
                StringUtils.isBlank(utlatande.getMotiveringTillInteBaseratPaUndersokning())) {
            addValidationError(validationMessages, "grundformu.motiveringTillInteBaseratPaUndersokning", ValidationMessageType.EMPTY);
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            addValidationError(validationMessages, "grundformu.annatGrundForMUBeskrivning", ValidationMessageType.EMPTY);
        }
        // R3
        if (utlatande.getAnnatGrundForMU() == null && !StringUtils.isEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.INCORRECT_COMBINATION,
                    "luse.validation.grund-for-mu.incorrect_combination_annat_beskrivning");
        }

        if (utlatande.getKannedomOmPatient() == null) {
            addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.EMPTY);
        } else {
            boolean dateIsValid = validateDate(utlatande.getKannedomOmPatient(), validationMessages, "grundformu.kannedomOmPatient");
            if (dateIsValid) {
                if (utlatande.getUndersokningAvPatienten() != null && utlatande.getUndersokningAvPatienten().isValidDate()
                        && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getUndersokningAvPatienten().asLocalDate())) {
                    addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.OTHER,
                            "luse.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.UNDERSOKNING.RBK");
                }
                if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()
                        && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getAnhorigsBeskrivningAvPatienten().asLocalDate())) {
                    addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.OTHER,
                            "luse.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.ANHORIG.RBK");
                }
            }

        }

    }

    private void validateUnderlag(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getUnderlagFinns() == null) {
            addValidationError(validationMessages, "grundformu.underlagFinns", ValidationMessageType.EMPTY);
        } else if (utlatande.getUnderlagFinns() && utlatande.getUnderlag().isEmpty()) {
            addValidationError(validationMessages, "grundformu.underlag", ValidationMessageType.EMPTY);
        } else if (!utlatande.getUnderlagFinns() && !utlatande.getUnderlag().isEmpty()) {
            // R6
            addValidationError(validationMessages, "grundformu.underlagFinns", ValidationMessageType.OTHER,
                    "luse.validation.underlagfinns.incorrect_combination");
        }

        if (utlatande.getUnderlag().size() > MAX_UNDERLAG) {
            addValidationError(validationMessages, "grundformu.underlag", ValidationMessageType.OTHER, "luse.validation.underlag.too_many");
        }
        for (int i = 0; i < utlatande.getUnderlag().size(); i++) {
            Underlag underlag = utlatande.getUnderlag().get(i);
            // Alla underlagstyper är godkända här utom Underlag från skolhälsovård
            if (underlag.getTyp() == null) {
                addValidationError(validationMessages, "grundformu.underlag." + i + ".typ", ValidationMessageType.EMPTY,
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
                addValidationError(validationMessages, "grundformu.underlag." + i + ".typ", ValidationMessageType.INVALID_FORMAT,
                        "luse.validation.underlag.incorrect_format");
            }
            if (underlag.getDatum() == null) {
                addValidationError(validationMessages, "grundformu.underlag." + i + ".datum", ValidationMessageType.EMPTY,
                        "luse.validation.underlag.date.missing");
            } else if (!underlag.getDatum().isValidDate()) {
                addValidationError(validationMessages, "grundformu.underlag." + i + ".datum", ValidationMessageType.INVALID_FORMAT,
                        "luse.validation.underlag.date.incorrect_format");
            }
            if (StringUtils.isBlank(underlag.getHamtasFran())) {
                addValidationError(validationMessages, "grundformu.underlag." + i + ".hamtasFran", ValidationMessageType.EMPTY,
                        "luse.validation.underlag.hamtas-fran.missing");
            }
        }
    }

    private void validateSjukdomsforlopp(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getSjukdomsforlopp())) {
            addValidationError(validationMessages, "sjukdomsforlopp", ValidationMessageType.EMPTY);
        }
    }

    private void validateVardenhet(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostadress())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postadress", ValidationMessageType.EMPTY);
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postnummer", ValidationMessageType.EMPTY);
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postnummer", ValidationMessageType.INVALID_FORMAT,
                    "common.validation.postnummer.incorrect-format");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostort())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postort", ValidationMessageType.EMPTY);
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer())) {
            addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.telefonnummer", ValidationMessageType.EMPTY);
        }
    }

    private void validateAktivitetsbegransning(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getAktivitetsbegransning())) {
            addValidationError(validationMessages, "aktivitetsbegransning", ValidationMessageType.EMPTY);
        }
    }

    private void validateMedicinskaForutsattningarForArbete(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getMedicinskaForutsattningarForArbete())) {
            addValidationError(validationMessages, "medicinskaforutsattningarforarbete", ValidationMessageType.EMPTY);
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
            addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY);
        }
    }

    private void validateDiagnosgrund(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (StringUtils.isBlank(utlatande.getDiagnosgrund())) {
            addValidationError(validationMessages, "diagnos.diagnosgrund", ValidationMessageType.EMPTY);
        }

        if (utlatande.getNyBedomningDiagnosgrund() == null) {
            addValidationError(validationMessages, "diagnos.nyBedomningDiagnosgrund", ValidationMessageType.EMPTY);
        }

        // R13
        if (utlatande.getNyBedomningDiagnosgrund() != null && utlatande.getNyBedomningDiagnosgrund()
                && StringUtils.isBlank(utlatande.getDiagnosForNyBedomning())) {
            addValidationError(validationMessages, "diagnos.diagnosForNyBedomning", ValidationMessageType.EMPTY);
        }
        // R14 Inverted test of R13
        if ((utlatande.getNyBedomningDiagnosgrund() == null || !utlatande.getNyBedomningDiagnosgrund())
                && !Strings.isNullOrEmpty(utlatande.getDiagnosForNyBedomning())) {
            addValidationError(validationMessages, "diagnos.nyBedomningDiagnosgrund", ValidationMessageType.INCORRECT_COMBINATION,
                    "luse.validation.diagnosfornybedomning.incorrect_combination");
        }
    }

    private void validateKontaktMedFk(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // R11
        if ((utlatande.getKontaktMedFk() == null || !utlatande.getKontaktMedFk()) && !StringUtils.isBlank(utlatande.getAnledningTillKontakt())) {
            addValidationError(validationMessages, "Kontakt", ValidationMessageType.INCORRECT_COMBINATION,
                    "luse.validation.kontakt.incorrect_combination");
        }
    }

    private boolean isBlankButNotNull(String stringFromField) {
        return (!StringUtils.isEmpty(stringFromField)) && StringUtils.isBlank(stringFromField);
    }

    private void validateBlanksForOptionalFields(LuseUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            addValidationError(validationMessages, "anledningtillkontakt.blanksteg", ValidationMessageType.BLANK);
        }
        if (isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            addValidationError(validationMessages, "grundformu.annat.blanksteg", ValidationMessageType.BLANK);
        }
        if (isBlankButNotNull(utlatande.getAvslutadBehandling())) {
            addValidationError(validationMessages, "avslutadBehandling.blanksteg", ValidationMessageType.BLANK);
        }
        if (isBlankButNotNull(utlatande.getFormagaTrotsBegransning())) {
            addValidationError(validationMessages, "formagatrotsbegransning.blanksteg", ValidationMessageType.BLANK);
        }
        if (isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            addValidationError(validationMessages, "pagaendebehandling.blanksteg", ValidationMessageType.BLANK);
        }
        if (isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            addValidationError(validationMessages, "planeradbehandling.blanksteg", ValidationMessageType.BLANK);
        }
        if (isBlankButNotNull(utlatande.getSubstansintag())) {
            addValidationError(validationMessages, "substansintag.blanksteg", ValidationMessageType.BLANK);
        }
        if (isBlankButNotNull(utlatande.getOvrigt())) {
            addValidationError(validationMessages, "ovrigt.blanksteg", ValidationMessageType.BLANK);
        }
    }

    /**
     * Check if there are validation errors.
     *
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
    private void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type, String msg, String dynamicLabel) {
        validationMessages.add(new ValidationMessage(field, type, msg, dynamicLabel));
        LOG.debug(field + " " + msg);
    }

    private void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type, String msg) {
        validationMessages.add(new ValidationMessage(field, type, msg));
        LOG.debug(field + " " + msg);
    }

    private void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type) {
        validationMessages.add(new ValidationMessage(field, type));
        LOG.debug(field + " " + type.toString());
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
                    "luse.validation.nedsattning.choose-at-least-one");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                for (int j = i + 1; j < intervals.length; j++) {
                    // Overlap OR abuts(one intervals tom day == another's from day) is considered invalid
                    if (intervals[j] != null && intervals[i].overlaps(intervals[j])) {
                        addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                "luse.validation.nedsattning.overlapping-date-interval");
                        return false;
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
}
