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

package se.inera.intyg.intygstyper.lisu.validator;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableList;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.StringValidator;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalDraftValidator;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalValidatorUtil;
import se.inera.intyg.intygstyper.lisu.model.internal.*;
import se.inera.intyg.intygstyper.lisu.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;

public class InternalDraftValidatorImpl implements InternalDraftValidator<LisuUtlatande> {

    private static final int MAX_ARBETSLIVSINRIKTADE_ATGARDER = 10;
    private static final int MAX_SYSSELSATTNING = 5;

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    @Autowired
    private InternalValidatorUtil validatorUtil;

    @Override
    public ValidateDraftResponse validateDraft(LisuUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Patientens adressuppgifter
        PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);

        // Kategori 2 – Sysselsättning
        if (!isAvstangningSmittskydd(utlatande)) {
            validateSysselsattning(utlatande, validationMessages);
        }

        // Kategori 3 – Diagnos
        validatorUtil.validateDiagnose(utlatande.getTyp(), utlatande.getDiagnoser(), validationMessages);

        // Kategori 4 – Sjukdomens konsekvenser
        if (!isAvstangningSmittskydd(utlatande)) {
            validateFunktionsnedsattning(utlatande, validationMessages);
            validateAktivitetsbegransning(utlatande, validationMessages);
        }

        // Kategori 5 – Medicinska behandlingar/åtgärder

        // Kategori 6 – Bedömning
        validateBedomning(utlatande, validationMessages);

        // Kategori 7 – Åtgärder
        if (!isAvstangningSmittskydd(utlatande)) {
            validateAtgarder(utlatande, validationMessages);
        }

        // Kategori 8 – Övrigt

        // Kategori 9 – Kontakt
        validateKontakt(utlatande, validationMessages);

        validateBlanksForOptionalFields(utlatande, validationMessages);
        // vårdenhet
        validateVardenhet(utlatande, validationMessages);

        return new ValidateDraftResponse(getValidationStatus(validationMessages), validationMessages);
    }

    private void validateGrundForMU(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required if not smittskydd
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getUndersokningAvPatienten() == null
                    && utlatande.getTelefonkontaktMedPatienten() == null
                    && utlatande.getJournaluppgifter() == null
                    && utlatande.getAnnatGrundForMU() == null) {
                validatorUtil.addValidationError(validationMessages, "grundformu", ValidationMessageType.EMPTY,
                        "lisu.validation.grund-for-mu.missing");
            }
        }

        if (utlatande.getUndersokningAvPatienten() != null) {
            validateGrundForMuDate(utlatande.getUndersokningAvPatienten(), validationMessages, GrundForMu.UNDERSOKNING);
        }
        if (utlatande.getJournaluppgifter() != null) {
            validateGrundForMuDate(utlatande.getJournaluppgifter(), validationMessages, GrundForMu.JOURNALUPPGIFTER);
        }
        if (utlatande.getTelefonkontaktMedPatienten() != null) {
            validateGrundForMuDate(utlatande.getTelefonkontaktMedPatienten(), validationMessages, GrundForMu.TELEFONKONTAKT);
        }
        if (utlatande.getAnnatGrundForMU() != null) {
            validateGrundForMuDate(utlatande.getAnnatGrundForMU(), validationMessages, GrundForMu.ANNAT);
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "lisu.validation.grund-for-mu.annat.beskrivning.missing");
        }

        // R3
        if (utlatande.getAnnatGrundForMU() == null && !StringUtils.isEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "lisu.validation.grund-for-mu.annat.beskrivning.invalid_combination");
        }
    }

    private void validateSysselsattning(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (CollectionUtils.isEmpty(utlatande.getSysselsattning())
                || !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() != null)) {
            validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                    "lisu.validation.sysselsattning.missing");
        } else {

            // R9
            if (StringUtils.isBlank(utlatande.getNuvarandeArbete()) &&
                    utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.nuvarandearbete.missing");
            }

            // R10
            if (!StringUtils.isBlank(utlatande.getNuvarandeArbete()) &&
                    !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.nuvarandearbete.invalid_combination");
            }

            // R11
            if (StringUtils.isBlank(utlatande.getArbetsmarknadspolitisktProgram()) &&
                    utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM)) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.ampolitisktprogram.missing");
            }

            // R12
            if (!StringUtils.isBlank(utlatande.getArbetsmarknadspolitisktProgram()) &&
                    !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM)) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.ampolitisktprogram.invalid_combination");
            }

            // No more than 5 entries are allowed
            if (utlatande.getSysselsattning().size() > MAX_SYSSELSATTNING) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.too-many");
            }
        }
    }

    private void validateFunktionsnedsattning(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getFunktionsnedsattning())) {
            validatorUtil.addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY,
                    "lisu.validation.funktionsnedsattning.missing");
        }
    }

    private void validateAktivitetsbegransning(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getAktivitetsbegransning())) {
            validatorUtil.addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY,
                    "lisu.validation.aktivitetsbegransning.missing");
        }
    }

    private void validateBedomning(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Sjukskrivningar
        validateSjukskrivningar(utlatande, validationMessages);

        // FMB
        if (utlatande.getForsakringsmedicinsktBeslutsstod() != null && StringUtils.isBlank(utlatande.getForsakringsmedicinsktBeslutsstod())) {
            validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                    "lisu.validation.bedomning.fmb.empty");
        }

        // Prognos
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getPrognos() == null || utlatande.getPrognos().getTyp() == null) {
                validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                        "lisu.validation.bedomning.prognos.missing");
            } else {
                // New rule since INTYG-2286
                if (utlatande.getPrognos().getTyp() == PrognosTyp.ATER_X_ANTAL_DGR && utlatande.getPrognos().getDagarTillArbete() == null) {
                    validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                            "lisu.validation.bedomning.prognos.dagarTillArbete.missing");
                } else if (utlatande.getPrognos().getTyp() != PrognosTyp.ATER_X_ANTAL_DGR && utlatande.getPrognos().getDagarTillArbete() != null) {
                    validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                            "lisu.validation.bedomning.prognos.dagarTillArbete.invalid_combination");
                }
            }
        }
    }

    private void validateSjukskrivningar(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Check if there are any at all
        if (utlatande.getSjukskrivningar() == null || utlatande.getSjukskrivningar().size() < 1) {
            validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                    "lisu.validation.bedomning.sjukskrivningar.missing");
        } else {

            // Validate sjukskrivningar, checks that dates exist and are valid
            utlatande.getSjukskrivningar()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(sjukskrivning -> validateSjukskrivning(validationMessages, sjukskrivning));

            // R17 Validate no sjukskrivningperiods overlap
            validateSjukskrivningPeriodOverlap(utlatande, validationMessages);

            // Arbetstidsforlaggning R13, R14, R15, R16
            if (isArbetstidsforlaggningMandatory(utlatande)) {
                if (utlatande.getArbetstidsforlaggning() == null) {
                    validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                            "lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing");
                } else {
                    if (utlatande.getArbetstidsforlaggning() && StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                        validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                                "lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.missing");
                    } else if (!utlatande.getArbetstidsforlaggning() && !StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                        validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                                "lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect");
                    }
                }
            } else if (isArbetstidsforlaggningMotiveringForbidden(utlatande)
                    && !StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                        "lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination");
            }
        }
    }

    private void validateSjukskrivningPeriodOverlap(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        utlatande.getSjukskrivningar()
                .stream()
                .filter(Objects::nonNull)
                .forEach(sjukskrivning -> checkSjukskrivningPeriodOverlapAgainstList(validationMessages, sjukskrivning,
                        utlatande.getSjukskrivningar()));
    }

    private void checkSjukskrivningPeriodOverlapAgainstList(List<ValidationMessage> validationMessages, Sjukskrivning sjukskrivning,
            ImmutableList<Sjukskrivning> sjukskrivningar) {

        if (sjukskrivning == null) {
            return;
        }

        if (isPeriodIntervalsOverlapping(sjukskrivning, sjukskrivningar) && sjukskrivning.getSjukskrivningsgrad() != null) {
            validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                    "lisu.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId() + ".overlap");
        }
    }

    private boolean isPeriodIntervalsOverlapping(Sjukskrivning sjukskrivning, ImmutableList<Sjukskrivning> sjukskrivningar) {
        return sjukskrivningar
                .stream()
                .filter(Objects::nonNull)
                .filter(e -> e != sjukskrivning)
                .anyMatch(e -> e.getPeriod() != null && e.getPeriod().overlaps(sjukskrivning.getPeriod()));
    }

    private void validateSjukskrivning(List<ValidationMessage> validationMessages, Sjukskrivning sjukskrivning) {
        if (sjukskrivning.getSjukskrivningsgrad() == null) {
            // Should never happen but just in case
            validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                    "lisu.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing");
        } else {
            if (sjukskrivning.getPeriod() == null) {
                validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                        "lisu.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId() + ".missing");
            } else {
                String errorMessage = "lisu.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId()
                        + ".invalid_format";

                if (!sjukskrivning.getPeriod().isValid()) {
                    validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                            errorMessage);
                } else {
                    validateDate(sjukskrivning.getPeriod().getFrom(), validationMessages, "bedomning", errorMessage);
                    validateDate(sjukskrivning.getPeriod().getTom(), validationMessages, "bedomning", errorMessage);
                }
            }
        }
    }

    private boolean isArbetstidsforlaggningMandatory(LisuUtlatande utlatande) {
        return !isAvstangningSmittskydd(utlatande) && utlatande.getSjukskrivningar()
                .stream()
                .anyMatch(e -> e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_3_4
                        || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_HALFTEN
                        || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_1_4);
    }

    private boolean isArbetstidsforlaggningMotiveringForbidden(LisuUtlatande utlatande) {
        return utlatande.getSjukskrivningar()
                .stream()
                .anyMatch(e -> e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT);
    }

    private void validateAtgarder(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // Anything checked at all?
        if (utlatande.getArbetslivsinriktadeAtgarder() == null || utlatande.getArbetslivsinriktadeAtgarder().size() < 1) {
            validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                    "lisu.validation.atgarder.missing");
        } else {

            // R21 If INTE_AKTUELLT is checked it must be the only selection
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && utlatande.getArbetslivsinriktadeAtgarder().size() > 1) {
                validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.inte_aktuellt_no_combine");
            }

            for (ArbetslivsinriktadeAtgarder atgard : utlatande.getArbetslivsinriktadeAtgarder()) {
                if (atgard.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT) {
                    // R36 Beskrivning must not be specified for atgard of type INTE_AKTUELLT
                    if (StringUtils.isNotBlank(atgard.getBeskrivning())) {
                        validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                                "lisu.validation.atgarder." + atgard.getVal().getId() + ".invalid_combination");
                    }
                } else if (StringUtils.isBlank(atgard.getBeskrivning())) {
                    // R35 Beskrivning must be specified for each atgard that is not of type INTE_AKTUELLT
                    validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                            "lisu.validation.atgarder." + atgard.getVal().getId() + ".missing_description");
                }
            }

            // No more than 10 entries are allowed
            if (utlatande.getArbetslivsinriktadeAtgarder().size() > MAX_ARBETSLIVSINRIKTADE_ATGARDER) {
                validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.too-many");
            }
        }
    }

    private void validateKontakt(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getKontaktMedFk() != null && !utlatande.getKontaktMedFk() && !StringUtils.isBlank(utlatande.getAnledningTillKontakt())) {
            validatorUtil.addValidationError(validationMessages, "kontakt", ValidationMessageType.EMPTY,
                    "lisu.validation.kontakt.invalid_combination");
        }
    }

    private void validateVardenhet(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostadress())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.adress", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.postadress.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.postnummer.missing");
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.postnummer.incorrect-format");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostort())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.postort", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.postort.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.telefonnummer", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.telefonnummer.missing");
        }
    }

    private void validateBlanksForOptionalFields(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            validatorUtil.addValidationError(validationMessages, "anledningtillkontakt.blanksteg", ValidationMessageType.EMPTY,
                    "lisu.validation.blanksteg.otillatet");
        }
        if (isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat.blanksteg", ValidationMessageType.EMPTY,
                    "lisu.validation.blanksteg.otillatet");
        }
        if (isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            validatorUtil.addValidationError(validationMessages, "pagaendebehandling.blanksteg", ValidationMessageType.EMPTY,
                    "lisu.validation.blanksteg.otillatet");
        }
        if (isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            validatorUtil.addValidationError(validationMessages, "planeradbehandling.blanksteg", ValidationMessageType.EMPTY,
                    "lisu.validation.blanksteg.otillatet");
        }
        if (isBlankButNotNull(utlatande.getOvrigt())) {
            validatorUtil.addValidationError(validationMessages, "ovrigt.blanksteg", ValidationMessageType.EMPTY,
                    "lisu.validation.blanksteg.otillatet");
        }
    }

    private boolean validateDate(InternalDate date, List<ValidationMessage> validationMessages, String validationType, String validationMessage) {
        boolean valid = true;
        if (!date.isValidDate()) {
            validatorUtil.addValidationError(validationMessages, validationType, ValidationMessageType.INVALID_FORMAT,
                    validationMessage);
            return false;
        }

        if (!date.isReasonable()) {
            validatorUtil.addValidationError(validationMessages, validationType, ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.general.date_out_of_range");
            valid = false;
        }
        return valid;
    }

    private void validateGrundForMuDate(InternalDate date, List<ValidationMessage> validationMessages, GrundForMu type) {
        String validationType = "grundformu." + type.getMessage();
        String validationMessage = "lisu.validation.grund-for-mu." + type.getMessage() + ".incorrect_format";
        validateDate(date, validationMessages, validationType, validationMessage);

    }

    private enum GrundForMu {
        UNDERSOKNING,
        JOURNALUPPGIFTER,
        ANNAT,
        TELEFONKONTAKT;

        public String getMessage() {
            switch (this) {
            case UNDERSOKNING:
                return "undersokning";
            case TELEFONKONTAKT:
                return "telefonkontakt";
            case JOURNALUPPGIFTER:
                return "journaluppgifter";
            case ANNAT:
                return "annat";
            default:
                return "annat";
            }

        }

    }

    private boolean isAvstangningSmittskydd(LisuUtlatande utlatande) {
        return (utlatande.getAvstangningSmittskydd() != null && utlatande.getAvstangningSmittskydd());
    }

    private boolean isBlankButNotNull(String stringFromField) {
        return (!StringUtils.isEmpty(stringFromField)) && StringUtils.isBlank(stringFromField);
    }

    /**
     * Check if there are validation errors.
     */
    private ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return (validationMessages.isEmpty()) ? ValidationStatus.VALID : ValidationStatus.INVALID;
    }
}
