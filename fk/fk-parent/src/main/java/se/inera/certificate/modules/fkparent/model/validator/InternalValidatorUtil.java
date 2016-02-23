package se.inera.certificate.modules.fkparent.model.validator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.modules.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

import java.util.List;

/**
 * Created by BESA on 2016-02-23.
 */
public final class InternalValidatorUtil {

    @Autowired(required = false)
    private static WebcertModuleService moduleService;

    private static final Logger LOG = LoggerFactory.getLogger(InternalValidatorUtil.class);

    private static final int MIN_SIZE_PSYKISK_DIAGNOS = 4;
    private static final int MIN_SIZE_DIAGNOS = 3;

    public static void validateDiagnose(List<Diagnos> diagnoser, List<ValidationMessage> validationMessages) {

        if (diagnoser.size() == 0) {
            addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                    "lisu.validation.diagnos.missing");
        }
        for (Diagnos diagnos : diagnoser) {

            /* R8 För delfråga 6.2 ska diagnoskod anges med så många positioner som möjligt, men minst tre positioner (t.ex. F32).
               R9 För delfråga 6.2 ska diagnoskod anges med minst fyra positioner då en psykisk diagnos anges.
               Med psykisk diagnos avses alla diagnoser som börjar med Z73 eller med F (dvs. som tillhör F-kapitlet i ICD-10). */
            if (StringUtils.isBlank(diagnos.getDiagnosKod())) {
                addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                        "lisu.validation.diagnos.missing");
            } else {
                String trimDiagnoskod = StringUtils.trim(diagnos.getDiagnosKod()).toUpperCase();
                if ((trimDiagnoskod.startsWith("Z73") || trimDiagnoskod.startsWith("F"))
                        && trimDiagnoskod.length() < MIN_SIZE_PSYKISK_DIAGNOS) {
                    addValidationError(validationMessages, "diagnos", ValidationMessageType.INVALID_FORMAT,
                            "lisu.validation.diagnos.psykisk.length-4");
                } else if (trimDiagnoskod.length() < MIN_SIZE_DIAGNOS) {
                    addValidationError(validationMessages, "diagnos", ValidationMessageType.INVALID_FORMAT,
                            "lisu.validation.diagnos.length-3");
                } else {
                    validateDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), "diagnos",
                            "lisu.validation.diagnos.invalid", validationMessages);
                }
            }
            if (StringUtils.isBlank(diagnos.getDiagnosBeskrivning())) {
                addValidationError(validationMessages, "diagnos", ValidationMessageType.EMPTY,
                        "lisu.validation.diagnos.description.missing");
            }
        }
    }

    public static void validateDiagnosKod(String diagnosKod, String kodsystem, String field, String msgKey, List<ValidationMessage> validationMessages) {
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
     * Create a ValidationMessage and add it to the list of messages.
     *
     * @param validationMessages
     *            list collection messages
     * @param field
     *            a String with the name of the field
     * @param msg
     *            a String with an error code for the front end implementation
     */
    public static void addValidationError(List<ValidationMessage> validationMessages, String field, ValidationMessageType type, String msg) {
        validationMessages.add(new ValidationMessage(field, type, msg));
        LOG.debug(field + " " + msg);
    }

}
