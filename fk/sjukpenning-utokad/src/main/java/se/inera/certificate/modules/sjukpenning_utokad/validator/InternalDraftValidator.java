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

package se.inera.certificate.modules.sjukpenning_utokad.validator;

import static se.inera.certificate.modules.fkparent.model.validator.InternalValidatorUtil.addValidationError;
import static se.inera.certificate.modules.fkparent.model.validator.InternalValidatorUtil.validateDiagnose;
import static se.inera.certificate.modules.sjukpenning_utokad.model.internal.ArbetslivsinriktadeAtgarder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.sjukpenning_utokad.model.internal.*;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.StringValidator;

public class InternalDraftValidator {

    private static final int MAX_ARBETSLIVSINRIKTADE_ATGARDER = 10;

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidator.class);

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    public ValidateDraftResponse validateDraft(SjukpenningUtokadUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);
        // Kategori 2 – Sysselsättning
        validateSysselsattning(utlatande, validationMessages);
        // Kategori 3 – Diagnos
        validateDiagnose(utlatande.getDiagnoser(), validationMessages);
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

    private void validateKontakt(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // R8
        if (utlatande.getKontaktMedFk() != null && utlatande.getKontaktMedFk() && utlatande.getAnledningTillKontakt() == null) {
            addValidationError(validationMessages, "kontakt", ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.kontakt.anledning.missing");
        }
    }

    private void validateGrundForMU(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required
        if (utlatande.getUndersokningAvPatienten() == null
                && utlatande.getTelefonkontaktMedPatienten() == null
                && utlatande.getJournaluppgifter() == null
                && utlatande.getAnnatGrundForMU() == null) {
            addValidationError(validationMessages, "grundformu", ValidationMessageType.EMPTY,
                    "lisu.validation.grund-for-mu.missing");
        }

        // Validate dates
        if (utlatande.getUndersokningAvPatienten() != null && !utlatande.getUndersokningAvPatienten().isValidDate()) {
            addValidationError(validationMessages, "grundformu.undersokning", ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.grund-for-mu.undersokning.incorrect_format");
        }
        if (utlatande.getTelefonkontaktMedPatienten() != null && !utlatande.getTelefonkontaktMedPatienten().isValidDate()) {
            addValidationError(validationMessages, "grundformu.telefonkontakt", ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.grund-for-mu.telefonkontakt.incorrect_format");
        }
        if (utlatande.getJournaluppgifter() != null && !utlatande.getJournaluppgifter().isValidDate()) {
            addValidationError(validationMessages, "grundformu.journaluppgifter", ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.grund-for-mu.journaluppgifter.incorrect_format");
        }
        if (utlatande.getAnnatGrundForMU() != null && !utlatande.getAnnatGrundForMU().isValidDate()) {
            addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.INVALID_FORMAT,
                    "lisu.validation.grund-for-mu.annat.incorrect_format");
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "lisu.validation.grund-for-mu.annat.missing");
        }

        // R3
        if (utlatande.getAnnatGrundForMU() == null && !StringUtils.isBlank(utlatande.getAnnatGrundForMUBeskrivning())) {
            addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "lisu.validation.grund-for-mu.annat.beskrivning.invalid_combination");
        }
    }

    private void validateSysselsattning(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSysselsattning() == null) {
            addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                    "lisu.validation.sysselsattning.missing");
        } else {

            // R9
            if (utlatande.getSysselsattning().getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE
                    && utlatande.getNuvarandeArbete() == null) {
                addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.nuvarandearbete.missing");
            }

            // R10
            if (utlatande.getSysselsattning().getTyp() != Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE
                    && utlatande.getNuvarandeArbete() != null) {
                addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.nuvarandearbete.invalid_combination");
            }

            // R11
            if (utlatande.getSysselsattning().getTyp() == Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM
                    && utlatande.getArbetsmarknadspolitisktProgram() == null) {
                addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.ampolitisktprogram.missing");
            }

            // R12
            if (utlatande.getSysselsattning().getTyp() != Sysselsattning.SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM
                    && utlatande.getArbetsmarknadspolitisktProgram() != null) {
                addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisu.validation.sysselsattning.ampolitisktprogram.invalid_combination");
            }
        }
    }

    private void validateFunktionsnedsattning(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getFunktionsnedsattning() == null) {
            addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY,
                    "lisu.validation.funktionsnedsattning.missing");
        }
    }

    private void validateAktivitetsbegransning(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getAktivitetsbegransning() == null) {
            addValidationError(validationMessages, "aktivitetsbegransning", ValidationMessageType.EMPTY,
                    "lisu.validation.aktivitetsbegransning.missing");
        }
    }

    private void validateBedomning(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Sjukskrivningar
        validateSjukskrivningar(utlatande, validationMessages);

        // Prognos
        if (utlatande.getPrognos() == null) {
            addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                    "lisu.validation.bedomning.prognos.missing");
        } else {

            // R18, R19
            if (utlatande.getPrognos().getTyp() == Prognos.PrognosTyp.PROGNOS_OKLAR && utlatande.getPrognos().getFortydligande() == null) {
                addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                        "lisu.validation.bedomning.prognos.fortydligande.missing");
            } else if (utlatande.getPrognos().getTyp() != Prognos.PrognosTyp.PROGNOS_OKLAR && utlatande.getPrognos().getFortydligande() != null) {
                addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                        "lisu.validation.bedomning.prognos.fortydligande.invalidentry");
            }
        }
    }

    private void validateSjukskrivningar(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Check if there are any at all
        if (utlatande.getSjukskrivningar() == null) {
            addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
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
                    addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                            "lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing");
                } else {
                    if (utlatande.getArbetstidsforlaggning() && utlatande.getArbetstidsforlaggningMotivering() == null) {
                        addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                                "lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.missing");
                    }
                }
            } else if (isArbetstidsforlaggningMotiveringForbidden(utlatande) && utlatande.getArbetstidsforlaggningMotivering() != null) {
                addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                        "lisu.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalidentry");
            }
        }
    }

    private void validateSjukskrivningPeriodOverlap(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {
        utlatande.getSjukskrivningar()
                .stream()
                .filter(Objects::nonNull)
                .forEach(sjukskrivning -> checkSjukskrivningPeriodOverlapAgainstList(validationMessages, sjukskrivning, utlatande.getSjukskrivningar()));
    }

    private void checkSjukskrivningPeriodOverlapAgainstList(List<ValidationMessage> validationMessages, Sjukskrivning sjukskrivning, ImmutableList<Sjukskrivning> sjukskrivningar) {

        if(sjukskrivning == null) {
            return;
        }

        if(isPeriodIntervalsOverlapping(sjukskrivning, sjukskrivningar) && sjukskrivning.getSjukskrivningsgrad() != null) {
            addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                    "lisu.validation.bedomning.sjukskrivningar.period"+ sjukskrivning.getSjukskrivningsgrad().getId() + ".invalidentry");
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
            addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                    "lisu.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing");
        } else {
            if (sjukskrivning.getPeriod() == null) {
                addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                        "lisu.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId() + ".missing");
            } else {
                if (!sjukskrivning.getPeriod().isValid()) {
                    addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                            "lisu.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId() + ".invalid_format");
                }
            }
        }
    }

    private boolean isArbetstidsforlaggningMandatory(SjukpenningUtokadUtlatande utlatande) {
        return utlatande.getSjukskrivningar()
                .stream()
                .anyMatch(e -> e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_3_4
                        || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_HALFTEN
                        || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_1_4);
    }

    private boolean isArbetstidsforlaggningMotiveringForbidden(SjukpenningUtokadUtlatande utlatande) {
        return utlatande.getSjukskrivningar()
                .stream()
                .anyMatch(e -> e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT);
    }

    private void validateAtgarder(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Anything checked at all?
        if (utlatande.getArbetslivsinriktadeAtgarder() == null || utlatande.getArbetslivsinriktadeAtgarder().size() < 1) {
            addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                    "lisu.validation.atgarder.missing");
        } else {

            // R21 If INTE_AKTUELLT is checked it must be the only selection
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && utlatande.getArbetslivsinriktadeAtgarder().size() > 1) {
                addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.inte_aktuellt_no_combine");
            }

            // R23 If INTE_AKTUELLT is checked utlatande.getArbetslivsinriktadeAtgarderAktuelltBeskrivning() must not be answered
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && utlatande.getArbetslivsinriktadeAtgarderAktuelltBeskrivning() != null) {
                addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.invalid_combination");
            }

            // R24 If INTE_AKTUELLT is checked utlatande.getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning() must not be answered
            if (utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && utlatande.getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning() != null) {
                addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.invalid_combination");
            }

            // R22 If INTE_AKTUELLT is NOT checked beskrivning åtgärder aktuellt is required
            if (!utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && utlatande.getArbetslivsinriktadeAtgarderAktuelltBeskrivning() == null) {
                addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.aktuelltbeskrivning.missing");
            }

            // R25 If INTE_AKTUELLT is NOT checked beskrivning åtgärder ej aktuellt is not allowed
            if (!utlatande.getArbetslivsinriktadeAtgarder().stream().anyMatch(e -> e.getVal() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && utlatande.getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning() != null) {
                addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.invalid_combination");
            }

            // No more than 10 entries are allowed
            if (utlatande.getArbetslivsinriktadeAtgarder().size() > MAX_ARBETSLIVSINRIKTADE_ATGARDER) {
                addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisu.validation.atgarder.too-many");
            }
        }
    }

    private void validateVardenhet(SjukpenningUtokadUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostadress())) {
            addValidationError(validationMessages, "vardenhet.adress", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.postadress.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.postnummer.missing");
        } else if (!STRING_VALIDATOR.validateStringAsPostalCode(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostnummer())) {
            addValidationError(validationMessages, "vardenhet.postnummer", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.postnummer.incorrect-format");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getPostort())) {
            addValidationError(validationMessages, "vardenhet.postort", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.postort.missing");
        }

        if (StringUtils.isBlank(utlatande.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer())) {
            addValidationError(validationMessages, "vardenhet.telefonnummer", ValidationMessageType.EMPTY,
                    "lisu.validation.vardenhet.telefonnummer.missing");
        }
    }

    /**
     * Check if there are validation errors.
     */
    private ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return (validationMessages.isEmpty()) ? ValidationStatus.VALID : ValidationStatus.INVALID;
    }

    /**
     * @param validationMessages list collecting message
     * @param fieldId            field id
     * @param intervals          intervals
     * @return booleans
     */
    protected boolean validateIntervals(List<ValidationMessage> validationMessages, String fieldId, InternalLocalDateInterval... intervals) {
        if (intervals == null || allNulls(intervals)) {
            addValidationError(validationMessages, fieldId, ValidationMessageType.EMPTY,
                    "lisu.validation.nedsattning.choose-at-least-one");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                Interval oneInterval = createInterval(intervals[i].fromAsLocalDate(), intervals[i].tomAsLocalDate());
                if (oneInterval == null) {
                    addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                            "lisu.validation.nedsattning.incorrect-date-interval");
                    return false;
                }
                for (int j = i + 1; j < intervals.length; j++) {
                    if (intervals[j] != null) {
                        Interval anotherInterval = createInterval(intervals[j].fromAsLocalDate(), intervals[j].tomAsLocalDate());
                        if (anotherInterval == null) {
                            addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                    "lisu.validation.nedsattning.incorrect-date-interval");
                            return false;
                        }
                        // Overlap OR abuts(one intervals tom day== another's
                        // from day) is considered invalid
                        if (oneInterval.overlaps(anotherInterval) || oneInterval.abuts(anotherInterval)) {
                            addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                    "lisu.validation.nedsattning.overlapping-date-interval");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param intervals intervals
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
     * @param start start
     * @param end   end
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
