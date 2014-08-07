package se.inera.certificate.modules.ts_bas.validator.transport;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.ts_bas.model.v1.ObservationType;
import se.inera.certificate.ts_bas.model.v1.Utlatande;

public class ProgrammaticTransportValidatorInstance {
    private List<String> validationErrors;

    public ProgrammaticTransportValidatorInstance() {
        validationErrors = new ArrayList<>();
    }

    public List<String> validate(se.inera.certificate.ts_bas.model.v1.Utlatande utlatande) {
        validateUtlatande(utlatande);
        validateObservationer(utlatande.getObservations());
        return validationErrors;
    }

    private void validateUtlatande(Utlatande utlatande) {
        // TODO Do some stuff here
    }

    private void validateObservationer(List<ObservationType> observations) {
        for (ObservationType observation : observations) {
            switch (observation.getObservationskod().getCode()) {
            case "OBS4":
                if (observation.isForekomst() && isNullOrEmpty(observation.getBeskrivning())) {
                   validationError("OBS4.forekomst = true requires beskrivning");
                }
                break;
            case "OBS7":
                if (observation.isForekomst() && isNullOrEmpty(observation.getBeskrivning())) {
                    validationError("OBS7.forekomst = true requires beskrivning");
                }
                break;
            case "OBS15":
                if (observation.isForekomst() && isNullOrEmpty(observation.getBeskrivning())) {
                    validationError("OBS15.forekomst = true requires beskrivning");
                }
                break;
            case "G40.9":
                if (observation.isForekomst() && isNullOrEmpty(observation.getBeskrivning())) {
                    validationError("G40.9.forekomst = true requires beskrivning");
                }
                break;
            case "OBS18":
                if (observation.isForekomst() && isNullOrEmpty(observation.getBeskrivning())) {
                    validationError("OBS18.forekomst = true requires beskrivning");
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

}
