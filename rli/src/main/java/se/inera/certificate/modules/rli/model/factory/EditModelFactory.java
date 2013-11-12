package se.inera.certificate.modules.rli.model.factory;

import se.inera.certificate.modules.rli.model.converters.ConverterException;
import se.inera.certificate.modules.rli.model.edit.Utlatande;
import se.inera.certificate.modules.rli.rest.dto.CreateNewDraftCertificateHolder;

public interface EditModelFactory {

    public abstract Utlatande createEditableUtlatande(CreateNewDraftCertificateHolder newDraftData) throws ConverterException;

}
