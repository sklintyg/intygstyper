package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;

public class InternalModelHolder {

    private final String internalModel;

    public InternalModelHolder(String internalModel) {
        hasText(internalModel, "'internalModel' must not be empty");
        this.internalModel = internalModel;
    }

    public String getInternalModel() {
        return internalModel;
    }
}
