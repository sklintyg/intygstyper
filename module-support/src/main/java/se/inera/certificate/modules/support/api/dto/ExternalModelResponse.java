package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;
import se.inera.certificate.model.Utlatande;

public class ExternalModelResponse {

    private final String externalModelJson;

    private final Utlatande externalModel;

    public ExternalModelResponse(String externalModelJson, Utlatande externalModel) {
        hasText(externalModelJson, "'externalModelJson' must not be empty");
        notNull(externalModel, "'externalModel' must not be null");
        this.externalModelJson = externalModelJson;
        this.externalModel = externalModel;
    }

    public String getExternalModelJson() {
        return externalModelJson;
    }

    public Utlatande getExternalModel() {
        return externalModel;
    }
}
