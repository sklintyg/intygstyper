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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import se.inera.intyg.intygstyper.fkparent.model.validator.InternalValidatorUtil;
import se.inera.intyg.intygstyper.lisu.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.StringValidator;
import se.inera.intyg.intygstyper.lisu.model.internal.LisuUtlatande;
import se.inera.intyg.intygstyper.lisu.model.internal.PrognosTyp;
import se.inera.intyg.intygstyper.lisu.model.internal.Sjukskrivning;
import se.inera.intyg.intygstyper.lisu.model.internal.Sysselsattning;

public class InternalDraftValidator {

    private static final int MAX_ARBETSLIVSINRIKTADE_ATGARDER = 10;

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    @Autowired
    private InternalValidatorUtil validatorUtil;

    public InternalDraftValidator() {
    }

    @VisibleForTesting
    public InternalDraftValidator(InternalValidatorUtil validatorUtil) {
        this.validatorUtil = validatorUtil;
    }

    public ValidateDraftResponse validateDraft(LisuUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);

        // Kategori 2 – Sysselsättning
        validateSysselsattning(utlatande, validationMessages);

        // Kategori 3 – Diagnos
        validatorUtil.validateDiagnose(utlatande.getTyp(), utlatande.getDiagnoser(), validationMessages);

        // Kategori 4 – Sjukdomens konsekvenser
        validateFunktionsnedsattning(utlatande, validationMessages);
        validateAktivitetsbegransning(utlatande, validationMessages);

        // Kategori 5 – Medicinska behandlingar/åtgärder

        // Kategori 6 – Bedömning
        validateBedomning(utlatande, validationMessages);

        // Kategori 7 – Åtgärder
        validateAtgarder(utlatande, validationMessages);

        // Kategori 8 – Övrigt

        // Kategori 9 – Kontakt
        validateKontakt(utlatande, validationMessages);

        // vårdenhet
        validateVardenhet(utlatande, validationMessages);

        return new ValidateDraftResponse(getValidationStatus(validationMessages), validationMessages);
    }

    private void validateGrundForMU(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required
        if (utlatande.getUndersokningAvPatienten() == null
                && utlatande.getTelefonkontaktMedPatienten() == null
                && utlatande.getJournaluppgifter() == null
                && utlatande.getAnnatGrundForMU() == null) {
            validatorUtil.addValidationError(validationMessages, "grundformu", ValidationMessageType.EMPTY,
                    "lisu.validation.grund-for-mu.missing");
        }

        // Validate dates
        if (utlatande.getUndersokningAvPatienten() != null && !utlatande.getUndersokningAvPatienten().isValidDate()) {
            validatorUtil.addValidationError(validationMessages, "grundformu.undersokning", ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.grund-for-mu.undersokning.incorrect_format");
        }
        if (utlatande.getTelefonkontaktMedPatienten() != null && !utlatande.getTelefonkontaktMedPatienten().isValidDate()) {
            validatorUtil.addValidationError(validationMessages, "grundformu.telefonkontakt", ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.grund-for-mu.telefonkontakt.incorrect_format");
        }
        if (utlatande.getJournaluppgifter() != null && !utlatande.getJournaluppgifter().isValidDate()) {
            validatorUtil.addValidationError(validationMessages, "grundformu.journaluppgifter", ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.grund-for-mu.journaluppgifter.incorrect_format");
        }
        if (utlatande.getAnnatGrundForMU() != null && !utlatande.getAnnatGrundForMU().isValidDate()) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.grund-for-mu.annat.incorrect_format");
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "lisu.validation.grund-for-mu.annat.beskrivning.missing");
        }

        // R3
        if (utlatande.getAnnatGrundForMU() == null && !StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            validatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "lisu.validation.grund-for-mu.annat.beskrivning.invalid_combination");
        }
    }

    private void validateSysselsattning(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSysselsattning() == null || utlatande.getSysselsattning().getTyp() == null) {
            validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                    "lisu.validation.sysselsattning.missing");
        } else {

            // R9
            if (utlatande.getSysselsattning().getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE
                    && StringUtils.isBlank(utlatande.getNuvarandeArbete())) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.nuvarandearbete.missing");
            }

            // R10
            if (utlatande.getSysselsattning().getTyp() != Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE
                    && !StringUtils.isBlank(utlatande.getNuvarandeArbete())) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.nuvarandearbete.invalid_combination");
            }

            // R11
            if (utlatande.getSysselsattning().getTyp() == Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM
                    && StringUtils.isBlank(utlatande.getArbetsmarknadspolitisktProgram())) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.ampolitisktprogram.missing");
            }

            // R12
            if (utlatande.getSysselsattning().getTyp() != Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM
                    && !StringUtils.isBlank(utlatande.getArbetsmarknadspolitisktProgram())) {
                validatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.ampolitisktprogram.invalid_combination");
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

        // Förmåga trots begränsning
        if (utlatande.getFormagaTrotsBegransning() != null && StringUtils.isBlank(utlatande.getFormagaTrotsBegransning())) {
            validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                    "lisu.validation.bedomning.formagatrotsbegransning.empty");
        }

        // Prognos
        if (utlatande.getPrognos() == null) {
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
            } else if (isArbetstidsforlaggningMotiveringForbidden(utlatande) && !StringUtils.isBlank(utlatande.getArbetstidsforlaggningMotivering())) {
                validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                        "lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination");
            }
        }
    }

    private void validateSjukskrivningPeriodOverlap(LisuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        utlatande.getSjukskrivningar()
                .stream()
                .filter(Objects::nonNull)
                .forEach(sjukskrivning -> checkSjukskrivningPeriodOverlapAgainstList(validationMessages, sjukskrivning, utlatande.getSjukskrivningar()));
    }

    private void checkSjukskrivningPeriodOverlapAgainstList(List<ValidationMessage> validationMessages, Sjukskrivning sjukskrivning, ImmutableList<Sjukskrivning> sjukskrivningar) {

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
                if (!sjukskrivning.getPeriod().isValid()) {
                    validatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                            "lisu.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId() + ".invalid_format");
                }
            }
        }
    }

    private boolean isArbetstidsforlaggningMandatory(LisuUtlatande utlatande) {
        return utlatande.getSjukskrivningar()
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

            // R24 If INTE_AKTUELLT is checked, arbetslivsinriktadeAtgarderEjAktuelltBeskrivning must be specified
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && StringUtils.isBlank(utlatande.getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning())) {
                validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.inte_aktuellt_missing_description");
            }

            // R23 If INTE_AKTUELLT is checked utlatande.getArbetslivsinriktadeAtgarderAktuelltBeskrivning() must not be answered
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && !StringUtils.isBlank(utlatande.getArbetslivsinriktadeAtgarderAktuelltBeskrivning())) {
                validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.invalid_combination");
            }

            // R22 If other choices than INTE_AKTUELLT are checked beskrivning åtgärder aktuellt is required
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> ArbetslivsinriktadeAtgarderVal.ATGARD_AKTUELL.contains(e.getVal()))
                    && StringUtils.isBlank(utlatande.getArbetslivsinriktadeAtgarderAktuelltBeskrivning())) {
                validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.aktuelltbeskrivning.missing");
            }

            // R25 If INTE_AKTUELLT is NOT checked beskrivning åtgärder ej aktuellt is not allowed
            if (!utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && !StringUtils.isBlank(utlatande.getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning())) {
                validatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.invalid_combination");
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

    /**
     * Check if there are validation errors.
     */
    private ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return (validationMessages.isEmpty()) ? ValidationStatus.VALID : ValidationStatus.INVALID;
    }
}
