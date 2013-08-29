package se.inera.certificate.modules.rli.model.converters;

import se.inera.certificate.modules.rli.model.internal.Utlatande;
import se.inera.certificate.modules.rli.rest.dto.CertificateContentHolder;

public interface ExternalToInternalConverter {

    Utlatande fromExternalToInternal(CertificateContentHolder certificateContentHolder);

}
