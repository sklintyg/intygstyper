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

import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalDraftValidator;
import se.inera.intyg.intygstyper.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.intygstyper.lisjp.model.internal.*;
import se.inera.intyg.intygstyper.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;

public class InternalDraftValidatorImpl implements InternalDraftValidator<LisjpUtlatande> {

    private static final int MAX_ARBETSLIVSINRIKTADE_ATGARDER = 10;
    private static final int MAX_SYSSELSATTNING = 5;
    private static final int VARNING_FOR_TIDIG_SJUKSKRIVNING_ANTAL_DAGAR = 7;

    @Autowired
    private ValidatorUtilFK validatorUtilFK;

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
        validatorUtilFK.validateDiagnose(utlatande.getDiagnoser(), validationMessages);

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
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateGrundForMU(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required if not smittskydd
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getUndersokningAvPatienten() == null
                    && utlatande.getTelefonkontaktMedPatienten() == null
                    && utlatande.getJournaluppgifter() == null
                    && utlatande.getAnnatGrundForMU() == null) {
                ValidatorUtil.addValidationError(validationMessages, "grundformu.baserasPa", ValidationMessageType.EMPTY);
            }
        }

        if (utlatande.getUndersokningAvPatienten() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getUndersokningAvPatienten(), validationMessages, ValidatorUtilFK.GrundForMu.UNDERSOKNING);
        }
        if (utlatande.getJournaluppgifter() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getJournaluppgifter(), validationMessages, ValidatorUtilFK.GrundForMu.JOURNALUPPGIFTER);
        }
        if (utlatande.getTelefonkontaktMedPatienten() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getTelefonkontaktMedPatienten(), validationMessages, ValidatorUtilFK.GrundForMu.TELEFONKONTAKT);
        }
        if (utlatande.getAnnatGrundForMU() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getAnnatGrundForMU(), validationMessages, ValidatorUtilFK.GrundForMu.ANNAT);
        }

        // INTYG-3310
        if (utlatande.getUndersokningAvPatienten() == null && (utlatande.getJournaluppgifter() != null
                || utlatande.getTelefonkontaktMedPatienten() != null || utlatande.getAnnatGrundForMU() != null)
                && StringUtils.isBlank(utlatande.getMotiveringTillInteBaseratPaUndersokning())) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.motiveringTillInteBaseratPaUndersokning", ValidationMessageType.EMPTY);
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.annatGrundForMUBeskrivning", ValidationMessageType.EMPTY);
        }

        // R3
        if (utlatande.getAnnatGrundForMU() == null && !StringUtils.isEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "lisjp.validation.grund-for-mu.annat.beskrivning.invalid_combination");
        }
    }

    private void validateSysselsattning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (CollectionUtils.isEmpty(utlatande.getSysselsattning())
                || !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() != null)) {
            ValidatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY);
        } else {

            // R9
            if (StringUtils.isBlank(utlatande.getNuvarandeArbete())
                    && utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning.nuvarandeArbete", ValidationMessageType.EMPTY);
            }

            // R10
            if (!StringUtils.isBlank(utlatande.getNuvarandeArbete())
                    && !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.nuvarandearbete.invalid_combination");
            }

            // R11
            if (StringUtils.isBlank(utlatande.getArbetsmarknadspolitisktProgram())
                    && utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM)) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning.arbetsmarknadspolitisktProgram", ValidationMessageType.EMPTY);
            }

            // R12
            if (!StringUtils.isBlank(utlatande.getArbetsmarknadspolitisktProgram())
                    && !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM)) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.ampolitisktprogram.invalid_combination");
            }

            // No more than 5 entries are allowed
            if (utlatande.getSysselsattning().size() > MAX_SYSSELSATTNING) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.too-many");
            }
        }
    }

    private void validateFunktionsnedsattning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getFunktionsnedsattning())) {
            ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY);
        }
    }

    private void validateAktivitetsbegransning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getAktivitetsbegransning())) {
            ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning.aktivitetsbegransning", ValidationMessageType.EMPTY);
        }
    }

    private void validateBedomning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Sjukskrivningar
        validateSjukskrivningar(utlatande, validationMessages);

        // FMB
        if (utlatande.getForsakringsmedicinsktBeslutsstod() != null && StringUtils.isBlank(utlatande.getForsakringsmedicinsktBeslutsstod())) {
            ValidatorUtil.addValidationError(validationMessages, "bedomning.forsakringsmedicinsktBeslutsstod", ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.fmb.empty");
        }

        // Prognos
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getPrognos() == null || utlatande.getPrognos().getTyp() == null) {
                ValidatorUtil.addValidationError(validationMessages, "bedomning.prognos", ValidationMessageType.EMPTY);
            } else {
                // New rule since INTYG-2286
                if (utlatande.getPrognos().getTyp() == PrognosTyp.ATER_X_ANTAL_DGR && utlatande.getPrognos().getDagarTillArbete() == null) {
                    ValidatorUtil.addValidationError(validationMessages, "bedomning.prognos.dagarTillArbete", ValidationMessageType.EMPTY);
                } else if (utlatande.getPrognos().getTyp() != PrognosTyp.ATER_X_ANTAL_DGR && utlatande.getPrognos().getDagarTillArbete() != null) {
                    ValidatorUtil.addValidationError(validationMessages, "bedomning.prognos", ValidationMessageType.EMPTY,
                            "lisjp.validation.bedomning.prognos.dagarTillArbete.invalid_combination");
                }
            }
        }
    }

    private void validateSjukskrivningar(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Check if there are any at all
        if (utlatande.getSjukskrivningar() == null || utlatande.getSjukskrivningar().size() < 1) {
            ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.sjukskrivningar.missing");
        } else {

            // Validate sjukskrivningar, checks that dates exist and are valid
            utlatande.getSjukskrivningar()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(sjukskrivning -> validateSjukskrivning(validationMessages, sjukskrivning));

            // R17 Validate no sjukskrivningperiods overlap
            validateSjukskrivningPeriodOverlap(utlatande, validationMessages);

            // INTYG-3207: Show warning if any period starts earlier than 7 days before now
            if (utlatande.getSjukskrivningar()
                    .stream()
                    .filter(Objects::nonNull)
                    .anyMatch(sjukskrivning ->
                            sjukskrivning.getPeriod() != null && sjukskrivning.getPeriod().getFrom() != null
                                    && sjukskrivning.getPeriod().getFrom().isBeforeNumDays(VARNING_FOR_TIDIG_SJUKSKRIVNING_ANTAL_DAGAR))) {
                ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.WARN,
                        "lisjp.validation.bedomning.sjukskrivningar.tidigtstartdatum");
            }

            // Arbetstidsforlaggning R13, R14, R15, R16
            if (isArbetstidsforlaggningMandatory(utlatande)) {
                if (utlatande.getArbetstidsforlaggning() == null) {
                    ValidatorUtil.addValidationError(validationMessages, "bedomning.arbetstidsforlaggning", ValidationMessageType.EMPTY,
                            "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing");
                } else {
                    if (utlatande.getArbetstidsforlaggning() && StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                        ValidatorUtil.addValidationError(validationMessages, "bedomning.arbetstidsforlaggningMotivering", ValidationMessageType.EMPTY);
                    } else if (!utlatande.getArbetstidsforlaggning() && !StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                        ValidatorUtil.addValidationError(validationMessages, "bedomning.arbetstidsforlaggning", ValidationMessageType.EMPTY,
                                "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect");
                    }
                }
            } else if (isArbetstidsforlaggningMotiveringForbidden(utlatande)
                    && !StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
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
            ValidatorUtil.addValidationError(validationMessages,
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
            ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing");
        } else {
            if (sjukskrivning.getPeriod() == null) {
                ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                        "lisjp.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId() + ".missing");
            } else {
                if (!sjukskrivning.getPeriod().isValid()) {
                    ValidatorUtil.addValidationError(validationMessages,
                            "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId(),
                            ValidationMessageType.INVALID_FORMAT);
                } else {
                    ValidatorUtil.validateDate(sjukskrivning.getPeriod().getFrom(), validationMessages,
                            "bedomning.sjukskrivningar." + sjukskrivning.getSjukskrivningsgrad().getId() + ".from");
                    ValidatorUtil.validateDate(sjukskrivning.getPeriod().getTom(), validationMessages,
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
            ValidatorUtil.addValidationError(validationMessages, "atgarder.arbetslivsinriktadeAtgarder", ValidationMessageType.EMPTY,
                    "lisjp.validation.atgarder.missing");
        } else {

            // R21 If INTE_AKTUELLT is checked it must be the only selection
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getTyp() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && utlatande.getArbetslivsinriktadeAtgarder().size() > 1) {
                ValidatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisjp.validation.atgarder.inte_aktuellt_no_combine");
            }

            // R36 If INTE_AKTUELLT is checked utlatande.getArbetslivsinriktadeAtgarderBeskrivning() must not be answered
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getTyp() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && !StringUtils.isBlank(utlatande.getArbetslivsinriktadeAtgarderBeskrivning())) {
                ValidatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisjp.validation.atgarder.invalid_combination");
            }

            // R35 If other choices than INTE_AKTUELLT are checked beskrivning åtgärder is required
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getTyp() != null && e.getTyp() != ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && StringUtils.isBlank(utlatande.getArbetslivsinriktadeAtgarderBeskrivning())) {
                ValidatorUtil.addValidationError(validationMessages, "atgarder.arbetslivsinriktadeAtgarderBeskrivning", ValidationMessageType.EMPTY);
            }

            // No more than 10 entries are allowed
            if (utlatande.getArbetslivsinriktadeAtgarder().size() > MAX_ARBETSLIVSINRIKTADE_ATGARDER) {
                ValidatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisjp.validation.atgarder.too-many");
            }
        }
    }

    private void validateKontakt(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getKontaktMedFk() != null && !utlatande.getKontaktMedFk() && !StringUtils.isBlank(utlatande.getAnledningTillKontakt())) {
            ValidatorUtil.addValidationError(validationMessages, "kontakt", ValidationMessageType.EMPTY,
                    "lisjp.validation.kontakt.invalid_combination");
        }
    }

    private void validateBlanksForOptionalFields(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            ValidatorUtil.addValidationError(validationMessages, "anledningtillkontakt.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.annat.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, "pagaendebehandling.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, "planeradbehandling.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationError(validationMessages, "ovrigt.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
    }

    private boolean isAvstangningSmittskydd(LisjpUtlatande utlatande) {
        return utlatande.getAvstangningSmittskydd() != null && utlatande.getAvstangningSmittskydd();
    }
}
