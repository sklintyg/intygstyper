package se.inera.certificate.modules.ts_diabetes.validator.transport;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.ts_diabetes.model.v1.ObservationType;
import se.inera.certificate.ts_diabetes.model.v1.Utlatande;

public class ProgrammaticTransportValidatorInstance {
    private List<String> validationErrors;
    private ValidationContext context;
    private AktiviteterValidationInstance aktivitetValidatorInstance;

    public ProgrammaticTransportValidatorInstance() {
        validationErrors = new ArrayList<>();
    }

    ProgrammaticTransportValidatorInstance(List<String> validationErrors, ValidationContext context) {
        this.validationErrors = validationErrors;
        this.context = context;
    }

    public List<String> validate(Utlatande utlatande) {
        context = new ValidationContext(utlatande);
        aktivitetValidatorInstance = new AktiviteterValidationInstance(this, utlatande.getAktivitets());
        
        validateUtlatande(utlatande);
        validateObservationer(utlatande.getObservations());

        aktivitetValidatorInstance.validateAktiviteter();
        return validationErrors;
    }

    private void validateObservationer(List<ObservationType> observations) {
        for (ObservationType observation : observations) {
            switch (observation.getObservationskod().getCode()){
                case "OBS22":
                    if (observation.isForekomst() && isNullOrEmpty(observation.getBeskrivning())) {
                        validationErrors.add("Observation description must be set for Observation with code OBS22");
                    }
                    break;
                case "OBS23":
                    if (observation.isForekomst() && isNullOrEmpty(observation.getBeskrivning())) {
                        validationErrors.add("Observation description must be set for Observation with code OBS23");
                    }
                    break;
                case "OBS24":
                    if (observation.isForekomst() && observation.getObservationstid() == null) {
                        validationErrors.add("Observation time must be set for Observation with code OBS24");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public ValidationContext getContext() {
        return context;
    }

    protected void validationError(String error) {
        validationErrors.add(error);
    }
    
    private boolean isNullOrEmpty(String beskrivning) {
        if (beskrivning == null) {
            return true;
        } else {
            return beskrivning.isEmpty();
        }
    }

    private void validateUtlatande(Utlatande utlatande) {
        // TODO Do some stuff here
    }

}
