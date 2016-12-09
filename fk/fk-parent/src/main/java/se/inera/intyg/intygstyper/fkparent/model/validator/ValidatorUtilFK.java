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
package se.inera.intyg.intygstyper.fkparent.model.validator;

import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;


/**
 * Created by BESA on 2016-02-23.
 */
public class ValidatorUtilFK {

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorUtilFK.class);

    private static final int MIN_SIZE_PSYKISK_DIAGNOS = 4;
    private static final int MIN_SIZE_DIAGNOS = 3;
    private static final int MAX_SIZE_DIAGNOS = 5;

    public enum GrundForMu {
        UNDERSOKNING,
        JOURNALUPPGIFTER,
        ANHORIGSBESKRIVNING,
        ANNAT,
        TELEFONKONTAKT;

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
                case TELEFONKONTAKT:
                    return GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
                default:
                    return "annat";
            }
        }
    }

    public void validateDiagnose(List<Diagnos> diagnoser, List<ValidationMessage> validationMessages) {

        if (diagnoser == null || diagnoser.isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "diagnos.diagnoser.0.diagnoskod", ValidationMessageType.EMPTY,
                    "common.validation.diagnos.missing");
            return;
        }

        for (int i = 0; i < diagnoser.size(); i++) {
            Diagnos diagnos = diagnoser.get(i);

            /* R8 För delfråga 6.2 ska diagnoskod anges med så många positioner som möjligt, men minst tre positioner (t.ex. F32).
               R9 För delfråga 6.2 ska diagnoskod anges med minst fyra positioner då en psykisk diagnos anges.
               Med psykisk diagnos avses alla diagnoser som börjar med Z73 eller med F (dvs. som tillhör F-kapitlet i ICD-10). */
            if (StringUtils.isBlank(diagnos.getDiagnosKod())) {
                ValidatorUtil.addValidationError(validationMessages, "diagnos.diagnoser." + i + ".diagnoskod", ValidationMessageType.EMPTY,
                        "common.validation.diagnos" + i + ".missing");
            } else {
                String trimDiagnoskod = StringUtils.trim(diagnos.getDiagnosKod()).toUpperCase();
                if ((trimDiagnoskod.startsWith("Z73") || trimDiagnoskod.startsWith("F"))
                        && trimDiagnoskod.length() < MIN_SIZE_PSYKISK_DIAGNOS) {
                    ValidatorUtil.addValidationError(validationMessages, "diagnos.diagnoser." + i + ".diagnoskod", ValidationMessageType.INVALID_FORMAT,
                            "common.validation.diagnos" + i + ".psykisk.length-4");
                } else if (trimDiagnoskod.length() < MIN_SIZE_DIAGNOS) {
                    ValidatorUtil.addValidationError(validationMessages, "diagnos.diagnoser." + i + ".diagnoskod", ValidationMessageType.INVALID_FORMAT,
                            "common.validation.diagnos" + i + ".length-3");
                } else if (trimDiagnoskod.length() > MAX_SIZE_DIAGNOS) {
                    ValidatorUtil.addValidationError(validationMessages, "diagnos.diagnoser." + i + ".diagnoskod", ValidationMessageType.INVALID_FORMAT,
                            "common.validation.diagnos" + i + ".length-5");
                } else {
                    validateDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), "diagnos.diagnoser",
                            "common.validation.diagnos" + i + ".invalid", validationMessages);
                }
            }
            if (StringUtils.isBlank(diagnos.getDiagnosBeskrivning())) {
                ValidatorUtil.addValidationError(validationMessages, "diagnos.diagnoser." + i + ".diagnosbeskrivning", ValidationMessageType.EMPTY,
                        "common.validation.diagnos" + i + ".description.missing");
            }
        }
    }

    public void validateDiagnosKod(String diagnosKod, String kodsystem, String field, String msgKey, List<ValidationMessage> validationMessages) {
        // if moduleService is not available, skip this validation
        if (moduleService == null) {
            LOG.warn("Forced to skip validation of diagnosKod since an implementation of ModuleService is not available");
            return;
        }

        if (!moduleService.validateDiagnosisCode(diagnosKod, kodsystem)) {
            ValidatorUtil.addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT, msgKey);
        }

    }

    public static void validateGrundForMuDate(InternalDate date, List<ValidationMessage> validationMessages, GrundForMu type) {
        String validationType = "grundformu." + type.getFieldName();
        ValidatorUtil.validateDateAndWarnIfFuture(date, validationMessages, validationType);
    }
}
