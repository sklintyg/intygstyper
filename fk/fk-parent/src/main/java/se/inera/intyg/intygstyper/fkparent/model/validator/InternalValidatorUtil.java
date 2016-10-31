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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;

import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;


/**
 * Created by BESA on 2016-02-23.
 */
public class InternalValidatorUtil {

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    private static final Logger LOG = LoggerFactory.getLogger(InternalValidatorUtil.class);

    private static final int MIN_SIZE_PSYKISK_DIAGNOS = 4;
    private static final int MIN_SIZE_DIAGNOS = 3;

    public enum GrundForMu {
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

    public void validateDiagnose(String intygsTyp, List<Diagnos> diagnoser, List<ValidationMessage> validationMessages) {

        if (diagnoser == null || diagnoser.isEmpty()) {
            addValidationError(validationMessages, "diagnos.diagnoser.0.diagnoskod", ValidationMessageType.EMPTY,
                    intygsTyp + ".validation.diagnos.missing");
        }

        for (int i = 0; i < diagnoser.size(); i++) {
            Diagnos diagnos = diagnoser.get(i);

            /* R8 För delfråga 6.2 ska diagnoskod anges med så många positioner som möjligt, men minst tre positioner (t.ex. F32).
               R9 För delfråga 6.2 ska diagnoskod anges med minst fyra positioner då en psykisk diagnos anges.
               Med psykisk diagnos avses alla diagnoser som börjar med Z73 eller med F (dvs. som tillhör F-kapitlet i ICD-10). */
            if (StringUtils.isBlank(diagnos.getDiagnosKod())) {
                addValidationError(validationMessages, "diagnos.diagnoser." + i + ".diagnoskod", ValidationMessageType.EMPTY,
                        intygsTyp + ".validation.diagnos" + i + ".missing");
            } else {
                String trimDiagnoskod = StringUtils.trim(diagnos.getDiagnosKod()).toUpperCase();
                if ((trimDiagnoskod.startsWith("Z73") || trimDiagnoskod.startsWith("F"))
                        && trimDiagnoskod.length() < MIN_SIZE_PSYKISK_DIAGNOS) {
                    addValidationError(validationMessages, "diagnos.diagnoser." + i + ".diagnoskod", ValidationMessageType.INVALID_FORMAT,
                            intygsTyp + ".validation.diagnos" + i + ".psykisk.length-4");
                } else if (trimDiagnoskod.length() < MIN_SIZE_DIAGNOS) {
                    addValidationError(validationMessages, "diagnos.diagnoser." + i + ".diagnoskod", ValidationMessageType.INVALID_FORMAT,
                            intygsTyp + ".validation.diagnos" + i + ".length-3");
                } else {
                    validateDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), "diagnos.diagnoser",
                            intygsTyp + ".validation.diagnos" + i + ".invalid", validationMessages);
                }
            }
            if (StringUtils.isBlank(diagnos.getDiagnosBeskrivning())) {
                addValidationError(validationMessages, "diagnos.diagnoser." + i + ".diagnosbeskrivning", ValidationMessageType.EMPTY,
                        intygsTyp + ".validation.diagnos" + i + ".description.missing");
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
            addValidationError(validationMessages, field, ValidationMessageType.INVALID_FORMAT, msgKey);
        }

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
                for (int j = i + 1; j < intervals.length; j++) {
                    // Overlap OR abuts(one intervals tom day == another's from day) is considered invalid
                    if (intervals[j] != null && intervals[i].overlaps(intervals[j])) {
                        addValidationError(validationMessages, fieldId, ValidationMessageType.OTHER,
                                "luae_na.validation.nedsattning.overlapping-date-interval");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean validateDate(InternalDate date, List<ValidationMessage> validationMessages, String field) {
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

    public void validateGrundForMuDate(InternalDate date, List<ValidationMessage> validationMessages, GrundForMu type) {
        String validationType = "grundformu." + type.getFieldName();
        validateDate(date, validationMessages, validationType);
    }

    public boolean isBlankButNotNull(String stringFromField) {
        return (!StringUtils.isEmpty(stringFromField)) && StringUtils.isBlank(stringFromField);
    }

    /**
     * Check if there are validation errors.
     *
     */
    public ValidationStatus getValidationStatus(List<ValidationMessage> validationMessages) {
        return (validationMessages.isEmpty()) ? ValidationStatus.VALID : ValidationStatus.INVALID;
    }

    public void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type, String msg, String dynamicLabel) {
        validationMessages.add(new ValidationMessage(field, type, msg, dynamicLabel));
        LOG.debug(field + " " + msg);
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
    public void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type, String msg) {
        validationMessages.add(new ValidationMessage(field, type, msg));
        LOG.debug(field + " " + msg);
    }

    public void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type) {
        validationMessages.add(new ValidationMessage(field, type));
        LOG.debug(field + " " + type.toString());
    }

    /**
     * @param intervals
     *            intervals
     * @return boolean
     */
    public boolean allNulls(InternalLocalDateInterval[] intervals) {
        for (InternalLocalDateInterval interval : intervals) {
            if (interval != null) {
                return false;
            }
        }
        return true;
    }

}
