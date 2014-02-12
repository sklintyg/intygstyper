package se.inera.certificate.modules.ts_bas.validator.internal;

import se.inera.certificate.modules.ts_bas.model.internal.Bedomning;
import se.inera.certificate.modules.ts_bas.model.internal.Diabetes;
import se.inera.certificate.modules.ts_bas.model.internal.Funktionsnedsattning;
import se.inera.certificate.modules.ts_bas.model.internal.HjartKarl;
import se.inera.certificate.modules.ts_bas.model.internal.HoSPersonal;
import se.inera.certificate.modules.ts_bas.model.internal.HorselBalans;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_bas.model.internal.Kognitivt;
import se.inera.certificate.modules.ts_bas.model.internal.Medicinering;
import se.inera.certificate.modules.ts_bas.model.internal.Medvetandestorning;
import se.inera.certificate.modules.ts_bas.model.internal.Syn;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.rest.dto.ValidateDraftResponseHolder;
import se.inera.certificate.modules.ts_bas.rest.dto.ValidationMessage;
import se.inera.certificate.modules.ts_bas.rest.dto.ValidationStatus;

/**
 * Class for validating drafts of the internal model
 * 
 * @author erik
 * 
 */
public class InternalValidatorInstance {

    private ValidateDraftResponseHolder validationResponse;

    public InternalValidatorInstance() {
        validationResponse = new ValidateDraftResponseHolder();
    }

    /**
     * Validates an internal draft of an {@link Utlatande} (this means the object being validated is not necessarily
     * complete)
     * 
     * @param utlatande
     *            an internal {@link Utlatande}
     * @return a {@link ValidateDraftResponseHolder} with a status and a list of validationErrors
     */
    public ValidateDraftResponseHolder validate(Utlatande utlatande) {

        validateBedomning(utlatande.getBedomning());
        validateDiabetes(utlatande.getDiabetes());
        validateFunktionsnedsattning(utlatande.getFunktionsnedsattning());
        validateHjartKarl(utlatande.getHjartKarl());
        validateHorselBalans(utlatande.getHorselBalans());
        validateHoSPersonal(utlatande.getSkapadAv());
        validateIntygAvser(utlatande.getIntygAvser());
        validateKognitivt(utlatande.getKognitivt());
        validateMedicinering(utlatande.getMedicinering());
        validateMedvetandestorning(utlatande.getMedvetandestorning());
        validateSyn(utlatande.getSyn());
        
        validationResponse.setStatus(getValidationStatus());

        return validationResponse;
    }

    private ValidationStatus getValidationStatus() {
        if (validationResponse.getValidationErrors().isEmpty()) {
            return ValidationStatus.COMPLETE;
        }
        return ValidationStatus.INCOMPLETE;
    }

    private void validateBedomning(final Bedomning bedomning) {
        // TODO Auto-generated method stub

    }

    private void validateDiabetes(final Diabetes diabetes) {
        // TODO Auto-generated method stub

    }

    private void validateFunktionsnedsattning(final Funktionsnedsattning funktionsnedsattning) {
        // TODO Auto-generated method stub

    }

    private void validateHjartKarl(final HjartKarl hjartKarl) {
        // TODO Auto-generated method stub

    }

    private void validateHorselBalans(final HorselBalans horselBalans) {
        // TODO Auto-generated method stub

    }

    private void validateHoSPersonal(final HoSPersonal skapadAv) {
        // TODO Auto-generated method stub

    }

    private void validateIntygAvser(final IntygAvser intygAvser) {
        // TODO Auto-generated method stub

    }

    private void validateKognitivt(final Kognitivt kognitivt) {
        // TODO Auto-generated method stub

    }

    private void validateMedicinering(final Medicinering medicinering) {
        // TODO Auto-generated method stub

    }

    private void validateMedvetandestorning(final Medvetandestorning medvetandestorning) {
        // TODO Auto-generated method stub

    }

    private void validateSyn(final Syn syn) {
        if (syn.getHogerOga().getUtanKorrektion() < 0.0 || syn.getHogerOga().getUtanKorrektion() > 2.0) {
            addValidationError("hogerOga.utanKorrektion", "ErrorCode");
        }

        if (syn.getHogerOga().getMedKorrektion() != null) {
            if (syn.getHogerOga().getMedKorrektion() < 0.0 || syn.getHogerOga().getMedKorrektion() > 2.0) {
                addValidationError("hogerOga.medKorrektion", "ErrorCode");
            }
        }

        if (syn.getVansterOga().getUtanKorrektion() < 0.0 || syn.getVansterOga().getUtanKorrektion() > 2.0) {
            addValidationError("vansterOga.utanKorrektion", "ErrorCode");
        }

        if (syn.getVansterOga().getMedKorrektion() != null) {
            if (syn.getVansterOga().getMedKorrektion() < 0.0 || syn.getVansterOga().getMedKorrektion() > 2.0) {
                addValidationError("vansterOga.medKorrektion", "ErrorCode");
            }
        }

        if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
            addValidationError("binokulart.utanKorrektion", "ErrorCode");
        }

        if (syn.getBinokulart().getMedKorrektion() != null) {
            if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                addValidationError("binokulart.medKorrektion", "ErrorCode");
            }
        }
    }

    /**
     * Create a ValidationMessage and add it to the {@link ValidateDraftResponseHolder}
     * 
     * @param field
     *            a String with the name of the field
     * @param msg
     *            a String with an error code for the front end implementation
     */
    private void addValidationError(String field, String msg) {
        ValidationMessage validationMesssage = new ValidationMessage();
        validationMesssage.setField(field);
        validationMesssage.setMessage(msg);

        validationResponse.addErrorMessage(validationMesssage);
    }
}
