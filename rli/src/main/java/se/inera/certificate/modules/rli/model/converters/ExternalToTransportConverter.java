package se.inera.certificate.modules.rli.model.converters;

import se.inera.certificate.common.v1.Utlatande;

public interface ExternalToTransportConverter {
	Utlatande externalToTransport(se.inera.certificate.modules.rli.model.external.Utlatande source);
}
