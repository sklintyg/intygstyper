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

package se.inera.intyg.intygstyper.ts_diabetes.validator.internal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.StringValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.*;

/**
 * Class for validating drafts of the internal model.
 *
 * @author erik
 */
public class InternalValidatorInstance {

    @Autowired
    ValidatorUtil validatorUtilCommon;

    private static final Logger LOG = LoggerFactory.getLogger(InternalValidatorInstance.class);

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    private List<ValidationMessage> validationMessages;

    private ValidationContext context;

    public InternalValidatorInstance() {
        validationMessages = new ArrayList<>();
    }

    /**
     * Validates an internal draft of an {@link Utlatande} (this means the object being validated is not necessarily
     * complete).
     *
     * @param utlatande an internal {@link Utlatande}
     * @return a {@link ValidateDraftResponseHolder} with a status and a list of validationErrors
     */
    public ValidateDraftResponse validate(Utlatande utlatande) {

        if (utlatande == null) {
            validatorUtilCommon.addValidationError(validationMessages, "utlatande", ValidationMessageType.OTHER, "ts-diabetes.validation.utlatande.missing");
        } else {
            context = new ValidationContext(utlatande);
            PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);
            validateIntygAvser(utlatande.getIntygAvser());
            validateIdentitetStyrkt(utlatande.getVardkontakt());
            validateDiabetes(utlatande.getDiabetes());
            validateHypoglykemi(utlatande.getHypoglykemier());
            validateSyn(utlatande.getSyn());
            validateBedomning(utlatande.getBedomning());
            validatorUtilCommon.validateVardenhet(utlatande.getGrundData(), validationMessages);
        }

        ValidateDraftResponse response = new ValidateDraftResponse(validatorUtilCommon.getValidationStatus(validationMessages), validationMessages);

        return response;
    }

    private void validateHypoglykemi(Hypoglykemier hypoglykemier) {
        if (hypoglykemier == null) {
            validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier", ValidationMessageType.EMPTY,
                    "ts-diabetes.validation.hypoglykemier.missing");
            return;
        }

        if (hypoglykemier.getKunskapOmAtgarder() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier.kunskapOmAtgarder", ValidationMessageType.EMPTY);
        }

        if (hypoglykemier.getTeckenNedsattHjarnfunktion() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier.teckenNedsattHjarnfunktion", ValidationMessageType.EMPTY);
        }

        if (validatorUtilCommon.isNotNullTrue(hypoglykemier.getTeckenNedsattHjarnfunktion())) {
            if (hypoglykemier.getSaknarFormagaKannaVarningstecken() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier.saknarFormagaKannaVarningstecken", ValidationMessageType.EMPTY);
            }

            if (hypoglykemier.getAllvarligForekomst() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier.allvarligForekomst", ValidationMessageType.EMPTY);
            }

            if (hypoglykemier.getAllvarligForekomstTrafiken() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier.allvarligForekomstTrafiken", ValidationMessageType.EMPTY);
            }
        }

        if (validatorUtilCommon.isNotNullTrue(hypoglykemier.getAllvarligForekomst())) {
            validatorUtilCommon.assertDescriptionNotEmpty(validationMessages, hypoglykemier.getAllvarligForekomstBeskrivning(),
                    "hypoglykemier.allvarligForekomstBeskrivning");
        }

        if (validatorUtilCommon.isNotNullTrue(hypoglykemier.getAllvarligForekomstTrafiken())) {
            validatorUtilCommon.assertDescriptionNotEmpty(validationMessages, hypoglykemier.getAllvarligForekomstTrafikBeskrivning(),
                    "hypoglykemier.allvarligForekomstTrafikBeskrivning");
        }

        if (validatorUtilCommon.isNotNullTrue(hypoglykemier.getAllvarligForekomstVakenTid())) {
            if (hypoglykemier.getAllvarligForekomstVakenTidObservationstid() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier.allvarligForekomstVakenTidObservationstid", ValidationMessageType.EMPTY);
            } else if (hypoglykemier.getAllvarligForekomstVakenTidObservationstid().beforeMinDateOrInFuture(LocalDate.now().minusYears(1))) {
                validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier.allvarligForekomstVakenTidObservationstid", ValidationMessageType.INVALID_FORMAT,
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.incorrect-date");
            }
        }

        if (context.isHogreBehorighetContext()) {
            if (hypoglykemier.getEgenkontrollBlodsocker() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier.egenkontrollBlodsocker", ValidationMessageType.EMPTY);
            }

            if (hypoglykemier.getAllvarligForekomstVakenTid() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "hypoglykemier.allvarligForekomstVakenTid", ValidationMessageType.EMPTY);
            }
        }

    }

    private void validateIdentitetStyrkt(Vardkontakt vardkontakt) {
        if (vardkontakt == null) {
            validatorUtilCommon.addValidationError(validationMessages, "identitet", ValidationMessageType.EMPTY);
            return;
        }
        if (vardkontakt.getIdkontroll() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "identitet", ValidationMessageType.EMPTY);
        }
    }

    private void validateBedomning(final Bedomning bedomning) {

        if (bedomning == null) {
            validatorUtilCommon.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY);
            return;
        }
        if (bedomning.getKorkortstyp().isEmpty() && (bedomning.getKanInteTaStallning() == null || validatorUtilCommon.isNotNullFalse(bedomning.getKanInteTaStallning()))) {
            validatorUtilCommon.addValidationError(validationMessages, "bedomning", ValidationMessageType.EMPTY);
        }

        if (context.isHogreBehorighetContext()) {
            if (bedomning.getLamplighetInnehaBehorighet() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "bedomning.lamplighetInnehaBehorighet", ValidationMessageType.EMPTY);
            }
        }
    }

    private void validateDiabetes(final Diabetes diabetes) {

        if (diabetes == null) {
            validatorUtilCommon.addValidationError(validationMessages, "diabetes", ValidationMessageType.EMPTY,
                    "ts-diabetes.validation.diabetes.missing");
            return;
        }

        if (diabetes.getObservationsperiod() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "diabetes.observationsperiod", ValidationMessageType.EMPTY);
        } else if (!STRING_VALIDATOR.validateStringIsYear(diabetes.getObservationsperiod())) {
            validatorUtilCommon.addValidationError(validationMessages, "diabetes.observationsperiod", ValidationMessageType.INVALID_FORMAT,
                    "ts-diabetes.validation.diabetes.observationsperiod.incorrect-format");
        }

        if (diabetes.getDiabetestyp() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "diabetes.diabetesTyp", ValidationMessageType.EMPTY);
        }

        boolean annanBehandling = diabetes.getAnnanBehandlingBeskrivning() != null
                && !diabetes.getAnnanBehandlingBeskrivning().isEmpty();
        if (!(validatorUtilCommon.isNotNullTrue(diabetes.getEndastKost()) || validatorUtilCommon.isNotNullTrue(diabetes.getTabletter()) || validatorUtilCommon.isNotNullTrue(diabetes.getInsulin()) || annanBehandling)) {
            validatorUtilCommon.addValidationError(validationMessages, "diabetes.behandling", ValidationMessageType.EMPTY,
                    "ts-diabetes.validation.diabetes.behandling.missing");
        }

        if (validatorUtilCommon.isNotNullTrue(diabetes.getInsulin())) {
            if (diabetes.getInsulinBehandlingsperiod() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "diabetes.insulin", ValidationMessageType.EMPTY,
                        "ts-diabetes.validation.diabetes.insulin.behandlingsperiod.missing");
            } else if (!STRING_VALIDATOR.validateStringIsYear(diabetes.getInsulinBehandlingsperiod())) {
                validatorUtilCommon.addValidationError(validationMessages, "diabetes.insulin", ValidationMessageType.INVALID_FORMAT,
                        "ts-diabetes.validation.diabetes.insulin.behandlingsperiod.incorrect-format");
            }
        }
    }

    private void validateIntygAvser(final IntygAvser intygAvser) {

        if (intygAvser == null) {
            validatorUtilCommon.addValidationError(validationMessages, "intygAvser", ValidationMessageType.EMPTY);
            return;
        }

        if (intygAvser.getKorkortstyp().isEmpty()) {
            validatorUtilCommon.addValidationError(validationMessages, "intygAvser", ValidationMessageType.EMPTY);
        }
    }

    private void validateSyn(final Syn syn) {

        if (syn == null) {
            return;
        }

        if (syn.getSeparatOgonlakarintyg() == null) {
            validatorUtilCommon.addValidationError(validationMessages, "syn.separatOgonlakarintyg", ValidationMessageType.EMPTY);

        } else if (!syn.getSeparatOgonlakarintyg()) {

            if (syn.getSynfaltsprovningUtanAnmarkning() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "syn.provningUtanAnmarkning", ValidationMessageType.EMPTY);
            }

            if (syn.getHoger() == null || syn.getHoger().getUtanKorrektion() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "syn.hoger.utanKorrektion", ValidationMessageType.EMPTY);

            } else {
                if (syn.getHoger().getUtanKorrektion() < 0.0 || syn.getHoger().getUtanKorrektion() > 2.0) {
                    validatorUtilCommon.addValidationError(validationMessages, "syn.hoger.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-diabetes.validation.syn.out-of-bounds");
                }

                if (syn.getHoger().getMedKorrektion() != null) {
                    if (syn.getHoger().getMedKorrektion() < 0.0 || syn.getHoger().getMedKorrektion() > 2.0) {
                        validatorUtilCommon.addValidationError(validationMessages, "syn.hoger.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                                "ts-diabetes.validation.syn.out-of-bounds");
                    }
                }
            }

            if (syn.getVanster() == null || syn.getVanster().getUtanKorrektion() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "syn.vanster.utanKorrektion", ValidationMessageType.EMPTY);

            } else {

                if (syn.getVanster().getUtanKorrektion() < 0.0 || syn.getVanster().getUtanKorrektion() > 2.0) {
                    validatorUtilCommon.addValidationError(validationMessages, "syn.vanster.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                                "ts-diabetes.validation.syn.out-of-bounds");
                }

                if (syn.getVanster().getMedKorrektion() != null) {
                    if (syn.getVanster().getMedKorrektion() < 0.0 || syn.getVanster().getMedKorrektion() > 2.0) {
                        validatorUtilCommon.addValidationError(validationMessages, "syn.vanster.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                                "ts-diabetes.validation.syn.out-of-bounds");
                    }
                }
            }

            if (syn.getBinokulart() == null || syn.getBinokulart().getUtanKorrektion() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "syn.binokulart.utanKorrektion", ValidationMessageType.EMPTY);

            } else {
                if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
                    validatorUtilCommon.addValidationError(validationMessages, "syn.binokulart.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-diabetes.validation.syn.out-of-bounds");
                }

                if (syn.getBinokulart().getMedKorrektion() != null) {
                    if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                        validatorUtilCommon.addValidationError(validationMessages, "syn.binokulart.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                                "ts-diabetes.validation.syn.out-of-bounds");
                    }
                }
            }

            if (syn.getDiplopi() == null) {
                validatorUtilCommon.addValidationError(validationMessages, "syn.diplopi", ValidationMessageType.EMPTY);
            }
        }
    }
}
