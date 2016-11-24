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

package se.inera.intyg.intygstyper.ts_bas.validator.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.intygstyper.ts_bas.model.internal.*;
import se.inera.intyg.intygstyper.ts_parent.codes.DiabetesKod;

/**
 * Class for validating drafts of the internal model.
 *
 * @author erik
 */
public class InternalValidatorInstance {

    private List<ValidationMessage> validationMessages;

    private ValidationContext context;

    public InternalValidatorInstance() {
        validationMessages = new ArrayList<>();
    }

    /**
     * Validates an internal draft of an {@link Utlatande} (this means the object being validated is not necessarily
     * complete).
     *
     * @param utlatande
     *            an internal {@link Utlatande}
     * @return a {@link ValidateDraftResponseHolder} with a status and a list of validationErrors
     */
    public ValidateDraftResponse validate(Utlatande utlatande) {

        if (utlatande == null) {
            ValidatorUtil.addValidationError(validationMessages, "utlatande", ValidationMessageType.EMPTY, "ts-bas.validation.utlatande.missing");

        } else {

            context = new ValidationContext(utlatande);

            // OBS! Utökas formuläret i framtiden, lägg in validering i rätt ordning nedan.
            PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);
            validateIntygAvser(utlatande.getIntygAvser());
            validateIdentitetStyrkt(utlatande.getVardkontakt());
            validateSyn(utlatande.getSyn()); // 1.
            validateHorselBalans(utlatande.getHorselBalans());  // 2.
            validateFunktionsnedsattning(utlatande.getFunktionsnedsattning()); // 3.
            validateHjartKarl(utlatande.getHjartKarl()); // 4.
            validateDiabetes(utlatande.getDiabetes());  // 5.
            validateNeurologi(utlatande.getNeurologi()); // 6.
            validateMedvetandestorning(utlatande.getMedvetandestorning());  // 7.
            validateNjurar(utlatande.getNjurar()); // 8.
            validateKognitivt(utlatande.getKognitivt()); // 9.
            validateSomnVakenhet(utlatande.getSomnVakenhet()); // 10.
            validateNarkotikaLakemedel(utlatande.getNarkotikaLakemedel()); // 11.
            validatePsykiskt(utlatande.getPsykiskt()); // 12.
            validateUtvecklingsstorning(utlatande.getUtvecklingsstorning()); // 13.
            validateSjukhusvard(utlatande.getSjukhusvard()); // 14.
            validateMedicinering(utlatande.getMedicinering()); // 15.
            validateBedomning(utlatande.getBedomning());
            ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);
        }

        return new ValidateDraftResponse(ValidatorUtil.getValidationStatus(validationMessages), validationMessages);
    }

    private void validateIdentitetStyrkt(Vardkontakt vardkontakt) {
        if (vardkontakt == null) {
            ValidatorUtil.addValidationError(validationMessages, "identitet", ValidationMessageType.EMPTY);
            return;
        }
        if (vardkontakt.getIdkontroll() == null) {
            ValidatorUtil.addValidationError(validationMessages, "identitet", ValidationMessageType.EMPTY);
        }
    }

    private void validateUtvecklingsstorning(Utvecklingsstorning utvecklingsstorning) {
        if (utvecklingsstorning == null) {
            ValidatorUtil.addValidationError(validationMessages, "utvecklingsstorning", ValidationMessageType.EMPTY,
                    "ts-bas.validation.utvecklingsstorning.missing");
            return;
        }
        if (utvecklingsstorning.getHarSyndrom() == null) {
            ValidatorUtil.addValidationError(validationMessages, "utvecklingsstorning.harSyndrom", ValidationMessageType.EMPTY);
        }
        if (utvecklingsstorning.getPsykiskUtvecklingsstorning() == null) {
            ValidatorUtil.addValidationError(validationMessages, "utvecklingsstorning.psykiskUtvecklingsstorning", ValidationMessageType.EMPTY);
        }

    }

    private void validatePsykiskt(Psykiskt psykiskt) {
        if (psykiskt == null) {
            ValidatorUtil.addValidationError(validationMessages, "psykiskt", ValidationMessageType.EMPTY,
                    "ts-bas.validation.psykiskt.missing");
            return;
        }

        if (psykiskt.getPsykiskSjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, "psykiskt.psykiskSjukdom", ValidationMessageType.EMPTY);
        }
    }

    private void validateSomnVakenhet(SomnVakenhet somnVakenhet) {
        if (somnVakenhet == null) {
            ValidatorUtil.addValidationError(validationMessages, "somnVakenhet", ValidationMessageType.EMPTY, "ts-bas.validation.somnvakenhet.missing");
            return;
        }
        if (somnVakenhet.getTeckenSomnstorningar() == null) {
            ValidatorUtil.addValidationError(validationMessages, "somnVakenhet.teckenSomnstorningar", ValidationMessageType.EMPTY);
        }
    }

    private void validateNjurar(Njurar njurar) {
        if (njurar == null) {
            ValidatorUtil.addValidationError(validationMessages, "njurar", ValidationMessageType.EMPTY,
                    "ts-bas.validation.njurar.missing");
            return;
        }
        if (njurar.getNedsattNjurfunktion() == null) {
            ValidatorUtil.addValidationError(validationMessages, "njurar.nedsattNjurfunktion", ValidationMessageType.EMPTY);
        }
    }

    private void validateNeurologi(Neurologi neurologi) {
        if (neurologi == null) {
            ValidatorUtil.addValidationError(validationMessages, "neurologi", ValidationMessageType.EMPTY,
                    "ts-bas.validation.neurologi.missing");
            return;
        }
        if (neurologi.getNeurologiskSjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, "neurologi.neurologiskSjukdom", ValidationMessageType.EMPTY,
                    "ts-bas.validation.neurologi.neurologisksjukdom.missing");
        }
    }

    private void validateSjukhusvard(Sjukhusvard sjukhusvard) {

        if (sjukhusvard == null) {
            ValidatorUtil.addValidationError(validationMessages, "sjukhusvard", ValidationMessageType.EMPTY,
                    "ts-bas.validation.sjukhusvard.missing");
            return;
        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt() == null) {
            ValidatorUtil.addValidationError(validationMessages, "sjukhusvard.sjukhusEllerLakarkontakt", ValidationMessageType.EMPTY);
            return;

        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, sjukhusvard.getTidpunkt(), "sjukhusvard.tidpunkt");
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, sjukhusvard.getVardinrattning(), "sjukhusvard.vardinrattning");
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, sjukhusvard.getAnledning(), "sjukhusvard.anledning");
        }
    }

    private void validateBedomning(final Bedomning bedomning) {

        if (bedomning == null) {
            ValidatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY,
                    "ts-bas.validation.bedomning.missing");
            return;
        }

        if ((bedomning.getKanInteTaStallning() == null || !bedomning.getKanInteTaStallning()) && bedomning.getKorkortstyp().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY);
        }
    }

    private void validateDiabetes(final Diabetes diabetes) {

        if (diabetes == null) {
            ValidatorUtil.addValidationError(validationMessages, "diabetes", ValidationMessageType.EMPTY,
                    "ts-bas.validation.diabetes.missing");
            return;
        }

        if (diabetes.getHarDiabetes() == null) {
            ValidatorUtil.addValidationError(validationMessages, "diabetes.harDiabetes", ValidationMessageType.EMPTY);
            return;
        }
        if (diabetes.getHarDiabetes()) {

            if (diabetes.getDiabetesTyp() == null) {
                ValidatorUtil.addValidationError(validationMessages, "diabetes.diabetesTyp", ValidationMessageType.EMPTY);

            } else if (diabetes.getDiabetesTyp().equals(DiabetesKod.DIABETES_TYP_2.name())) {
                if (isNullOrFalse(diabetes.getInsulin()) && isNullOrFalse(diabetes.getKost()) && isNullOrFalse(diabetes.getTabletter())) {
                    ValidatorUtil.addValidationError(validationMessages, "diabetes.diabetesTyp.behandlingsTyp", ValidationMessageType.EMPTY);
                }
            }
        }
    }

    private boolean isNullOrFalse(Boolean insulin) {
        return insulin == null || !insulin;
    }

    private void validateFunktionsnedsattning(final Funktionsnedsattning funktionsnedsattning) {

        if (funktionsnedsattning == null) {
            ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY,
                    "ts-bas.validation.funktionsnedsattning.missing");
            return;
        }

        if (funktionsnedsattning.getFunktionsnedsattning() == null) {
            ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning.funktionsnedsattning", ValidationMessageType.EMPTY);

        } else if (funktionsnedsattning.getFunktionsnedsattning()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, funktionsnedsattning.getBeskrivning(), "funktionsnedsattning.funktionsnedsattningBeskrivning");
        }

        if (context.isPersontransportContext()) {
            if (funktionsnedsattning.getOtillrackligRorelseformaga() == null) {
                ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning.otillrackligRorelseformaga", ValidationMessageType.EMPTY);
            }
        }
    }

    private void validateHjartKarl(final HjartKarl hjartKarl) {

        if (hjartKarl == null) {
            ValidatorUtil.addValidationError(validationMessages, "hjartKarl", ValidationMessageType.EMPTY,
                    "ts-bas.validation.hjartKarl.missing");
            return;
        }

        if (hjartKarl.getHjartKarlSjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, "hjartKarl.hjartKarlSjukdom", ValidationMessageType.EMPTY);
        }

        if (hjartKarl.getHjarnskadaEfterTrauma() == null) {
            ValidatorUtil.addValidationError(validationMessages, "hjartKarl.hjarnskadaEfterTrauma", ValidationMessageType.EMPTY);
        }

        if (hjartKarl.getRiskfaktorerStroke() == null) {
            ValidatorUtil.addValidationError(validationMessages, "hjartKarl.riskfaktorerStroke", ValidationMessageType.EMPTY);

        } else if (hjartKarl.getRiskfaktorerStroke()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, hjartKarl.getBeskrivningRiskfaktorer(), "hjartKarl.beskrivningRiskfaktorer");
        }
    }

    private void validateHorselBalans(final HorselBalans horselBalans) {

        if (horselBalans == null) {
            ValidatorUtil.addValidationError(validationMessages, "horselBalans", ValidationMessageType.EMPTY,
                    "ts-bas.validation.horselBalans.missing");
            return;
        }

        if (horselBalans.getBalansrubbningar() == null) {
            ValidatorUtil.addValidationError(validationMessages, "horselBalans.balansrubbningar", ValidationMessageType.EMPTY);
        }

        if (context.isPersontransportContext()) {
            if (horselBalans.getSvartUppfattaSamtal4Meter() == null) {
                ValidatorUtil.addValidationError(validationMessages, "horselBalans.svartUpfattaSamtal4Meter", ValidationMessageType.EMPTY);
            }
        }
    }

    private void validateIntygAvser(final IntygAvser intygAvser) {

        if (intygAvser == null) {
            ValidatorUtil.addValidationError(validationMessages, "intygAvser", ValidationMessageType.EMPTY);
            return;
        }

        if (intygAvser.getKorkortstyp().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "intygAvser", ValidationMessageType.EMPTY);
        }
    }

    private void validateKognitivt(final Kognitivt kognitivt) {

        if (kognitivt == null) {
            ValidatorUtil.addValidationError(validationMessages, "kognitivt", ValidationMessageType.EMPTY,
                    "ts-bas.validation.kognitivt.missing");
            return;
        }

        if (kognitivt.getSviktandeKognitivFunktion() == null) {
            ValidatorUtil.addValidationError(validationMessages, "kognitivt.sviktandeKognitivFunktion", ValidationMessageType.EMPTY);
        }
    }

    private void validateMedicinering(final Medicinering medicinering) {

        if (medicinering == null) {
            ValidatorUtil.addValidationError(validationMessages, "medicinering", ValidationMessageType.EMPTY,
                    "ts-bas.validation.medicinering.missing");
            return;
        }

        if (medicinering.getStadigvarandeMedicinering() == null) {
            ValidatorUtil.addValidationError(validationMessages, "medicinering.stadigvarandeMedicinering", ValidationMessageType.EMPTY);
        } else if (medicinering.getStadigvarandeMedicinering()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, medicinering.getBeskrivning(), "medicinering.medicineringBeskrivning");
        }
    }

    private void validateNarkotikaLakemedel(final NarkotikaLakemedel narkotikaLakemedel) {

        if (narkotikaLakemedel == null) {
            ValidatorUtil.addValidationError(validationMessages, "narkotikaLakemedel", ValidationMessageType.EMPTY,
                    "ts-bas.validation.narkotikaLakemedel.missing");
            return;
        }

        if (narkotikaLakemedel.getTeckenMissbruk() == null) {
            ValidatorUtil.addValidationError(validationMessages, "narkotikaLakemedel.teckenMissbruk", ValidationMessageType.EMPTY);
        }

        if (narkotikaLakemedel.getForemalForVardinsats() == null) {
            ValidatorUtil.addValidationError(validationMessages, "narkotikaLakemedel.vardinsats", ValidationMessageType.EMPTY);
        }

        if (BooleanUtils.isTrue(narkotikaLakemedel.getTeckenMissbruk()) || BooleanUtils.isTrue(narkotikaLakemedel.getForemalForVardinsats())) {
            if (narkotikaLakemedel.getProvtagningBehovs() == null) {
                ValidatorUtil.addValidationError(validationMessages, "narkotikaLakemedel.provtagningBehovs", ValidationMessageType.EMPTY);
            }
        }

        if (narkotikaLakemedel.getLakarordineratLakemedelsbruk() == null) {
            ValidatorUtil.addValidationError(validationMessages, "narkotikaLakemedel.lakarordineratLakemedelsbruk", ValidationMessageType.EMPTY);
            return;

        } else if (narkotikaLakemedel.getLakarordineratLakemedelsbruk()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, narkotikaLakemedel.getLakemedelOchDos(), "narkotikaLakemedel.getLakemedelOchDos");
        }
    }

    private void validateMedvetandestorning(final Medvetandestorning medvetandestorning) {

        if (medvetandestorning == null) {
            ValidatorUtil.addValidationError(validationMessages, "medvetandestorning", ValidationMessageType.EMPTY,
                    "ts-bas.validation.medvetandestorning.missing");
            return;
        }

        if (medvetandestorning.getMedvetandestorning() == null) {
            ValidatorUtil.addValidationError(validationMessages, "medvetandestorning.medvetandestorning", ValidationMessageType.EMPTY);
        }
    }

    private void validateSyn(final Syn syn) {

        if (syn == null) {
            ValidatorUtil.addValidationError(validationMessages, "syn", ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.missing");
            return;
        }

        if (syn.getSynfaltsdefekter() == null) {
            ValidatorUtil.addValidationError(validationMessages, "syn.teckenSynfaltsdefekter", ValidationMessageType.EMPTY);
        }

        if (syn.getNattblindhet() == null) {
            ValidatorUtil.addValidationError(validationMessages, "syn.nattblindhet", ValidationMessageType.EMPTY);
        }

        if (syn.getProgressivOgonsjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, "syn.progressivOgonsjukdom", ValidationMessageType.EMPTY);
        }

        if (syn.getDiplopi() == null) {
            ValidatorUtil.addValidationError(validationMessages, "syn.diplopi", ValidationMessageType.EMPTY);
        }

        if (syn.getNystagmus() == null) {
            ValidatorUtil.addValidationError(validationMessages, "syn.nystagmus", ValidationMessageType.EMPTY);
        }

        if (syn.getHogerOga() == null) {
            ValidatorUtil.addValidationError(validationMessages, "syn.hogerOga", ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.hogeroga.missing");
        } else {
            if (syn.getHogerOga().getUtanKorrektion() == null) {
                ValidatorUtil.addValidationError(validationMessages, "syn.hogerOga.utanKorrektion", ValidationMessageType.EMPTY);

            } else if (syn.getHogerOga().getUtanKorrektion() < 0.0 || syn.getHogerOga().getUtanKorrektion() > 2.0) {
                ValidatorUtil.addValidationError(validationMessages, "syn.hogerOga.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.out-of-bounds");
            }

            if (syn.getHogerOga().getMedKorrektion() != null) {
                if (syn.getHogerOga().getMedKorrektion() < 0.0 || syn.getHogerOga().getMedKorrektion() > 2.0) {
                    ValidatorUtil.addValidationError(validationMessages, "syn.hogerOga.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.out-of-bounds");
                }
            }
        }

        if (syn.getVansterOga() == null) {
            ValidatorUtil.addValidationError(validationMessages, "syn.vansterOga", ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.vansteroga.missing");
        } else {
            if (syn.getVansterOga().getUtanKorrektion() == null) {
                ValidatorUtil.addValidationError(validationMessages, "syn.vansterOga.utanKorrektion", ValidationMessageType.EMPTY);

            } else if (syn.getVansterOga().getUtanKorrektion() < 0.0 || syn.getVansterOga().getUtanKorrektion() > 2.0) {
                ValidatorUtil.addValidationError(validationMessages, "syn.vansterOga.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.out-of-bounds");
            }

            if (syn.getVansterOga().getMedKorrektion() != null) {
                if (syn.getVansterOga().getMedKorrektion() < 0.0 || syn.getVansterOga().getMedKorrektion() > 2.0) {
                    ValidatorUtil.addValidationError(validationMessages, "syn.vansterOga.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.out-of-bounds");
                }
            }
        }

        if (syn.getBinokulart() == null) {
            ValidatorUtil.addValidationError(validationMessages, "syn.binokulart", ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.binokulart.missing");
        } else {
            if (syn.getBinokulart().getUtanKorrektion() == null) {
                ValidatorUtil.addValidationError(validationMessages, "syn.binokulart.utanKorrektion", ValidationMessageType.EMPTY);

            } else if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
                ValidatorUtil.addValidationError(validationMessages, "syn.binokulart.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.out-of-bounds");
            }

            if (syn.getBinokulart().getMedKorrektion() != null) {
                if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                    ValidatorUtil.addValidationError(validationMessages, "syn.binokulart.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.out-of-bounds");
                }
            }
        }
    }
}
