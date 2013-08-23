package se.inera.certificate.modules.rli.model.converters;

import se.inera.certificate.modules.rli.model.internal.Utlatande;

public interface ExternalToInternalConverter {

    Utlatande fromExternalToInternal(se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande);

}
