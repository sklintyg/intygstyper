package se.inera.certificate.modules.rli.model.factory;

import java.util.Map;

import se.inera.certificate.modules.rli.model.edit.Utlatande;

public interface EditModelFactory {

    public abstract Utlatande createEditableUtlatande(String certificateId, Map<String, Object> certificateData);

}
