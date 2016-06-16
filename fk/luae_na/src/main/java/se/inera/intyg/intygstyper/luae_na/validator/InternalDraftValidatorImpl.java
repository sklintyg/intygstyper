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

package se.inera.intyg.intygstyper.luae_na.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.validate.StringValidator;
import se.inera.intyg.intygstyper.fkparent.model.internal.Underlag;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalDraftValidator;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalValidatorUtil;
import se.inera.intyg.intygstyper.luae_na.model.internal.LuaenaUtlatande;

public class InternalDraftValidatorImpl implements InternalDraftValidator<LuaenaUtlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidatorImpl.class);

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    @Autowired
    InternalValidatorUtil validatorUtil;

    @Override
    public ValidateDraftResponse validateDraft(LuaenaUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

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
        // vårdenhet
        validateVardenhet(utlatande, validationMessages);

        return new ValidateDraftResponse(getValidationStatus(validationMessages), validationMessages);
    }

    private void validateGrundForMU(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getJournaluppgifter() == null
                && utlatande.getAnhorigsBeskrivningAvPatienten() == null && utlatande.getAnnatGrundForMU() == null) {
            addValidationError(validationMessages, "grundformu", ValidationMessageType.EMPTY,
                    "luae_na.validation.grund-for-mu.missing");
        }

        if (utlatande.getUndersokningAvPatienten() != null && !utlatande.getUndersokningAvPatienten().isValidDate()) {
            addValidationError(validationMessages, "grundformu.undersokning", ValidationMessageType.INVALID_FORMAT,
                    "luae_na.validation.grund-for-mu.undersokning.incorrect_format");
        }
        if (utlatande.getJournaluppgifter() != null && !utlatande.getJournaluppgifter().isValidDate()) {
            addValidationError(validationMessages, "grundformu.journaluppgifter", ValidationMessageType.INVALID_FORMAT,
                    "luae_na.validation.grund-for-mu.journaluppgifter.incorrect_format");
        }
        if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && !utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()) {
            addValidationError(validationMessages, "grundformu.anhorigsbeskrivning", ValidationMessageType.INVALID_FORMAT,
                    "luae_na.validation.grund-for-mu.anhorigsbeskrivning.incorrect_format");
        }
        if (utlatande.getAnnatGrundForMU() != null && !utlatande.getAnnatGrundForMU().isValidDate()) {
            addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.INVALID_FORMAT,
                    "luae_na.validation.grund-for-mu.annat.incorrect_format");
        }

        //R2
        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "luae_na.validation.grund-for-mu.annat.missing");
        }
        // R3
        if (utlatande.getAnnatGrundForMU() == null && !StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "luae_na.validation.grund-for-mu.incorrect_combination_annat_beskrivning");
        }

        if (utlatande.getKannedomOmPatient() == null) {
            addValidationError(validationMessages, "grundformu.kannedom", ValidationMessageType.EMPTY,
                    "luae_na.validation.grund-for-mu.kannedom.missing");
        } else if (!utlatande.getKannedomOmPatient().isValidDate()) {
            addValidationError(validationMessages, "grundformu.kannedom", ValidationMessageType.INVALID_FORMAT,
                    "luae_na.validation.grund-for-mu.kannedom.incorrect_format");
        } else {
            if (utlatande.getUndersokningAvPatienten() != null && utlatande.getUndersokningAvPatienten().isValidDate()
                    && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getUndersokningAvPatienten().asLocalDate())) {
                addValidationError(validationMessages, "grundformu.kannedom", ValidationMessageType.OTHER,
                        "luae_na.validation.grund-for-mu.kannedom.after.undersokning");
            }
            if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()
                    && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getAnhorigsBeskrivningAvPatienten().asLocalDate())) {
                addValidationError(validationMessages, "grundformu.kannedom", ValidationMessageType.OTHER,
                        "luae_na.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning");
            }
        }

    }

    private void validateUnderlag(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getUnderlagFinns() == null) {
            addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                    "luae_na.validation.underlagfinns.missing");
        } else if (utlatande.getUnderlagFinns() && utlatande.getUnderlag().isEmpty()) {
            addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                    "luae_na.validation.underlagfinns.missing");
        } else if (!utlatande.getUnderlagFinns() && !utlatande.getUnderlag().isEmpty()) {
            // R6
            addValidationError(validationMessages, "underlag", ValidationMessageType.INVALID_FORMAT,
                    "luae_na.validation.underlagfinns.incorrect_combination");
        }

        for (Underlag underlag : utlatande.getUnderlag()) {
            // Alla underlagstyper är godkända här utom Underlag från skolhälsovård
            if (underlag.getTyp() == null) {
                addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                        "luae_na.validation.underlag.missing");
            } else if (underlag.getTyp().getId() != Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRAN_HABILITERINGEN.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRAN_LOGOPED.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRANPSYKOLOG.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRANFORETAGSHALSOVARD.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UNDERLAG_FRANSKOLHALSOVARD.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getId()
                && underlag.getTyp().getId() != Underlag.UnderlagsTyp.OVRIGT.getId()) {
                addValidationError(validationMessages, "underlag", ValidationMessageType.INVALID_FORMAT,
                        "luae_na.validation.underlag.incorrect_format");
            }
            if (underlag.getDatum() == null) {
                addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                        "luae_na.validation.underlag.date.missing");
            } else if (!underlag.getDatum().isValidDate()) {
                addValidationError(validationMessages, "underlag", ValidationMessageType.INVALID_FORMAT,
                        "luae_na.validation.underlag.date.incorrect_format");
            }
            if (underlag.getHamtasFran() == null) {
                addValidationError(validationMessages, "underlag", ValidationMessageType.EMPTY,
                        "luae_na.validation.underlag.hamtas-fran.missing");
            }
        }
    }

    private void validateSjukdomsforlopp(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getSjukdomsforlopp())) {
            addValidationError(validationMessages, "sjukdomsforlopp", ValidationMessageType.EMPTY,
                    "luae_na.validation.sjukdomsforlopp.missing");
        }
    }

    private void validateVardenhet(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostadress())) {
            addValidationError(validationMessages, "vardenhet.adress", ValidationMessageType.EMPTY,
                    "luae_na.validation.vardenhet.postadress.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "luae_na.validation.vardenhet.postnummer.missing");
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "luae_na.validation.vardenhet.postnummer.incorrect-format");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostort())) {
            addValidationError(validationMessages, "vardenhet.postort", ValidationMessageType.EMPTY,
                    "luae_na.validation.vardenhet.postort.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer())) {
            addValidationError(validationMessages, "vardenhet.telefonnummer", ValidationMessageType.EMPTY,
                    "luae_na.validation.vardenhet.telefonnummer.missing");
        }
    }

    private void validateAktivitetsbegransning(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getAktivitetsbegransning())) {
            addValidationError(validationMessages, "aktivitetsbegransning", ValidationMessageType.EMPTY,
                    "luae_na.validation.aktivitetsbegransning.missing");
        }
    }

    private void validateMedicinskaForutsattningarForArbete(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getMedicinskaForutsattningarForArbete())) {
            addValidationError(validationMessages, "medicinskaforutsattningarforarbete", ValidationMessageType.EMPTY,
                    "luae_na.validation.medicinskaforutsattningarforarbete.missing");
        }
    }

    private void validateFunktionsnedsattning(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        if (StringUtils.isBlank(utlatande.getFunktionsnedsattningAnnan())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningBalansKoordination())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningIntellektuell())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningKommunikation())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningKoncentration())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningPsykisk())
                && StringUtils.isBlank(utlatande.getFunktionsnedsattningSynHorselTal())) {
            addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY,
                    "luae_na.validation.funktionsnedsattning.missing");
        }
    }

    private void validateDiagnosgrund(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (StringUtils.isBlank(utlatande.getDiagnosgrund())) {
            addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                    "luae_na.validation.diagnosgrund.missing");
        }

        if (utlatande.getNyBedomningDiagnosgrund() == null) {
            addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                    "luae_na.validation.nybedomningdiagnosgrund.missing");
        }

        // R13
        if (utlatande.getNyBedomningDiagnosgrund() != null && utlatande.getNyBedomningDiagnosgrund()
                && StringUtils.isBlank(utlatande.getDiagnosForNyBedomning())) {
            addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                    "luae_na.validation.diagnosfornybedomning.missing");
        }
        // R14 Inverted test of R13
        if ((utlatande.getNyBedomningDiagnosgrund() == null || !utlatande.getNyBedomningDiagnosgrund())
                && !Strings.isNullOrEmpty(utlatande.getDiagnosForNyBedomning())) {
            addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                    "luae_na.validation.diagnosfornybedomning.incorrect_combination");
        }
    }

    private void validateKontaktMedFk(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // R11
        if ((utlatande.getKontaktMedFk() == null || !utlatande.getKontaktMedFk()) && !StringUtils.isBlank(utlatande.getAnledningTillKontakt())) {
            addValidationError(validationMessages, "Kontakt", ValidationMessageType.INVALID_FORMAT,
                    "luae_na.validation.kontakt.incorrect_combination");
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
                    "luae_na.validation.nedsattning.choose-at-least-one");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                Interval oneInterval = createInterval(intervals[i].fromAsLocalDate(), intervals[i].tomAsLocalDate());
                if (oneInterval == null) {
                    addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                            "luae_na.validation.nedsattning.incorrect-date-interval");
                    return false;
                }
                for (int j = i + 1; j < intervals.length; j++) {
                    if (intervals[j] != null) {
                        Interval anotherInterval = createInterval(intervals[j].fromAsLocalDate(), intervals[j].tomAsLocalDate());
                        if (anotherInterval == null) {
                            addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                    "luae_na.validation.nedsattning.incorrect-date-interval");
                            return false;
                        }
                        // Overlap OR abuts(one intervals tom day== another's
                        // from day) is considered invalid
                        if (oneInterval.overlaps(anotherInterval) || oneInterval.abuts(anotherInterval)) {
                            addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                    "luae_na.validation.nedsattning.overlapping-date-interval");
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