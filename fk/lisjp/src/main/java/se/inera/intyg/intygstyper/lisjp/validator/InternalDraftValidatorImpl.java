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

package se.inera.intyg.intygstyper.lisjp.validator;

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
import se.inera.intyg.intygstyper.lisjp.model.internal.*;
import se.inera.intyg.intygstyper.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;

public class InternalDraftValidatorImpl implements InternalDraftValidator<LisjpUtlatande> {

    private static final int MAX_ARBETSLIVSINRIKTADE_ATGARDER = 10;
    private static final int MAX_SYSSELSATTNING = 5;

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    @Autowired
    private InternalValidatorUtil validatorUtil;

    @Override
    public ValidateDraftResponse validateDraft(LisjpUtlatande utlatande) {
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
        validatorUtil.validateDiagnose(utlatande.getDiagnoser(), validationMessages);

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

    private void validateGrundForMU(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required if not smittskydd
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getUndersokningAvPatienten() == null
                    && utlatande.getTelefonkontaktMedPatienten() == null
                    && utlatande.getJournaluppgifter() == null
                    && utlatande.getAnnatGrundForMU() == null) {
                validatorUtil.addValidationError(validationMessages, "grundformu.baserasPa", ValidationMessageType.EMPTY);
            }
        }

        if (utlatande.getUndersokningAvPatienten() != null) {
            validatorUtil.validateGrundForMuDate(utlatande.getUndersokningAvPatienten(), validationMessages, InternalValidatorUtil.GrundForMu.UNDERSOKNING);
        }
        if (utlatande.getJournaluppgifter() != null) {
            validatorUtil.validateGrundForMuDate(utlatande.getJournaluppgifter(), validationMessages, InternalValidatorUtil.GrundForMu.JOURNALUPPGIFTER);
        }
        if (utlatande.getTelefonkontaktMedPatienten() != null) {
            validatorUtil.validateGrundForMuDate(utlatande.getTelefonkontaktMedPatienten(), validationMessages, InternalValidatorUtil.GrundForMu.TELEFONKONTAKT);
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
                    "lisjp.validation.grund-for-mu.annat.beskrivning.invalid_combination");
        }
    }

    private void validateSysselsattning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (CollectionUtils.isEmpty(utlatande.getSysselsattning())
                || !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() != null)) {
            validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY);
        } else {

            // R9
            if (StringUtils.isBlank(utlatande.getNuvarandeArbete())
                    && utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning.nuvarandeArbete", ValidationMessageType.EMPTY);
            }

            // R10
            if (!StringUtils.isBlank(utlatande.getNuvarandeArbete())
                    && !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.nuvarandearbete.invalid_combination");
            }

            // R11
            if (StringUtils.isBlank(utlatande.getArbetsmarknadspolitisktProgram())
                    && utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM)) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning.arbetsmarknadspolitisktProgram", ValidationMessageType.EMPTY);
            }

            // R12
            if (!StringUtils.isBlank(utlatande.getArbetsmarknadspolitisktProgram())
                    && !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM)) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.ampolitisktprogram.invalid_combination");
            }

            // No more than 5 entries are allowed
            if (utlatande.getSysselsattning().size() > MAX_SYSSELSATTNING) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.too-many");
            }
        }
    }

    private void validateFunktionsnedsattning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getFunktionsnedsattning())) {
            validatorUtil.addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY);
        }
    }

    private void validateAktivitetsbegransning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getAktivitetsbegransning())) {
            validatorUtil.addValidationError(validationMessages, "funktionsnedsattning.aktivitetsbegransning", ValidationMessageType.EMPTY);
        }
    }

    private void validateBedomning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Sjukskrivningar
        validateSjukskrivningar(utlatande, validationMessages);

        // FMB
        if (utlatande.getForsakringsmedicinsktBeslutsstod() != null && StringUtils.isBlank(utlatande.getForsakringsmedicinsktBeslutsstod())) {
            validatorUtil.addValidationError(validationMessages, "bedomning.forsakringsmedicinsktBeslutsstod", ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.fmb.empty");
        }

        // Prognos
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getPrognos() == null || utlatande.getPrognos().getTyp() == null) {
                validatorUtil.addValidationError(validationMessages, "bedomning.prognos", ValidationMessageType.EMPTY);
            } else {
                // New rule since INTYG-2286
                if (utlatande.getPrognos().getTyp() == PrognosTyp.ATER_X_ANTAL_DGR && utlatande.getPrognos().getDagarTillArbete() == null) {
                    validatorUtil.addValidationError(validationMessages, "bedomning.prognos.dagarTillArbete", ValidationMessageType.EMPTY);
                } else if (utlatande.getPrognos().getTyp() != PrognosTyp.ATER_X_ANTAL_DGR && utlatande.getPrognos().getDagarTillArbete() != null) {
                    validatorUtil.addValidationError(validationMessages, "bedomning.prognos", ValidationMessageType.EMPTY,
                            "lisjp.validation.bedomning.prognos.dagarTillArbete.invalid_combination");
                }
            }
        }
    }

    private void validateSjukskrivningar(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Check if there are any at all
        if (utlatande.getSjukskrivningar() == null || utlatande.getSjukskrivningar().size() < 1) {
            validatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.sjukskrivningar.missing");
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
                    validatorUtil.addValidationError(validationMessages, "bedomning.arbetstidsforlaggning", ValidationMessageType.EMPTY,
                            "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing");
                } else {
                    if (utlatande.getArbetstidsforlaggning() && StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                        validatorUtil.addValidationError(validationMessages, "bedomning.arbetstidsforlaggningMotivering", ValidationMessageType.EMPTY);
                    } else if (!utlatande.getArbetstidsforlaggning() && !StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                        validatorUtil.addValidationError(validationMessages, "bedomning.arbetstidsforlaggning", ValidationMessageType.EMPTY,
                                "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect");
                    }
                }
            } else if (isArbetstidsforlaggningMotiveringForbidden(utlatande)
                    && !StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                validatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                        "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination");
            }
        }
    }

    private void validateSjukskrivningPeriodOverlap(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
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
            validatorUtil.addValidationError(validationMessages,
                    "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId(),
                    ValidationMessageType.PERIOD_OVERLAP);
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
            validatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing");
        } else {
            if (sjukskrivning.getPeriod() == null) {
                validatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                        "lisjp.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId() + ".missing");
            } else {
                if (!sjukskrivning.getPeriod().isValid()) {
                    validatorUtil.addValidationError(validationMessages,
                            "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId(),
                            ValidationMessageType.INVALID_FORMAT);
                } else {
                    validatorUtil.validateDate(sjukskrivning.getPeriod().getFrom(), validationMessages,
                            "bedomning.sjukskrivningar." + sjukskrivning.getSjukskrivningsgrad().getId() + ".from");
                    validatorUtil.validateDate(sjukskrivning.getPeriod().getTom(), validationMessages,
                            "bedomning.sjukskrivningar." + sjukskrivning.getSjukskrivningsgrad().getId() + ".from");
                }
            }
        }
    }

    private boolean isArbetstidsforlaggningMandatory(LisjpUtlatande utlatande) {
        return !isAvstangningSmittskydd(utlatande) && utlatande.getSjukskrivningar()
                .stream()
                .anyMatch(e -> e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_3_4
                        || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_HALFTEN
                        || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_1_4);
    }

    private boolean isArbetstidsforlaggningMotiveringForbidden(LisjpUtlatande utlatande) {
        return utlatande.getSjukskrivningar()
                .stream()
                .anyMatch(e -> e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT);
    }

    private void validateAtgarder(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // Anything checked at all?
        if (utlatande.getArbetslivsinriktadeAtgarder() == null || utlatande.getArbetslivsinriktadeAtgarder().size() < 1) {
            validatorUtil.addValidationError(validationMessages, "atgarder.arbetslivsinriktadeAtgarder", ValidationMessageType.EMPTY,
                    "lisjp.validation.atgarder.missing");
        } else {

            // R21 If INTE_AKTUELLT is checked it must be the only selection
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && utlatande.getArbetslivsinriktadeAtgarder().size() > 1) {
                validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisjp.validation.atgarder.inte_aktuellt_no_combine");
            }

            for (ArbetslivsinriktadeAtgarder atgard : utlatande.getArbetslivsinriktadeAtgarder()) {
                if (atgard.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT) {
                    // R36 Beskrivning must not be specified for atgard of type INTE_AKTUELLT
                    if (StringUtils.isNotBlank(atgard.getBeskrivning())) {
                        validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                                "lisjp.validation.atgarder." + atgard.getVal().getId() + ".invalid_combination");
                    }
                } else if (StringUtils.isBlank(atgard.getBeskrivning())) {
                    // R35 Beskrivning must be specified for each atgard that is not of type INTE_AKTUELLT
                    validatorUtil.addValidationError(validationMessages, "atgarder.arbetslivsinriktadeAtgarder."
                            + atgard.getVal().getId() + ".beskrivning", ValidationMessageType.EMPTY);
                }
            }

            // No more than 10 entries are allowed
            if (utlatande.getArbetslivsinriktadeAtgarder().size() > MAX_ARBETSLIVSINRIKTADE_ATGARDER) {
                validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisjp.validation.atgarder.too-many");
            }
        }
    }

    private void validateKontakt(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getKontaktMedFk() != null && !utlatande.getKontaktMedFk() && !StringUtils.isBlank(utlatande.getAnledningTillKontakt())) {
            validatorUtil.addValidationError(validationMessages, "kontakt", ValidationMessageType.EMPTY,
                    "lisjp.validation.kontakt.invalid_combination");
        }
    }

    private void validateVardenhet(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostadress())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postadress", ValidationMessageType.EMPTY);
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postnummer", ValidationMessageType.EMPTY);
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "lisjp.validation.vardenhet.postnummer.incorrect-format");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostort())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.postort", ValidationMessageType.EMPTY);
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer())) {
            validatorUtil.addValidationError(validationMessages, "vardenhet.grunddata.skapadAv.vardenhet.telefonnummer", ValidationMessageType.EMPTY);
        }
    }

    private void validateBlanksForOptionalFields(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            validatorUtil.addValidationError(validationMessages, "anledningtillkontakt.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            validatorUtil.addValidationError(validationMessages, "pagaendebehandling.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            validatorUtil.addValidationError(validationMessages, "planeradbehandling.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (isBlankButNotNull(utlatande.getOvrigt())) {
            validatorUtil.addValidationError(validationMessages, "ovrigt.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
    }

    private boolean isAvstangningSmittskydd(LisjpUtlatande utlatande) {
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
