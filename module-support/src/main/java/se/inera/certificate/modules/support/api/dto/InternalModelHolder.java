package se.inera.certificate.modules.support.api.dto;

public class InternalModelHolder {

    private final Object internalModel;

    private InternalModelHolder(Object internalModel) {
        this.internalModel = internalModel;
    }

    public Object getInternalModel() {
        return internalModel;
    }
}
