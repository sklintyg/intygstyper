package se.inera.certificate.modules.rli.model.converters;

import se.inera.certificate.modules.rli.model.external.Utlatande;

public interface InternalToExternalConverter {

    public abstract Utlatande convertUtlatandeFromInternalToExternal(
            se.inera.certificate.modules.rli.model.edit.Utlatande source);

}
