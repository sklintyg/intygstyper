package se.inera.certificate.modules.rli.model.converters;

import se.inera.certificate.modules.rli.model.external.Utlatande;

public interface TransportToExternalConverter {
	Utlatande transportToExternal(se.inera.certificate.common.v1.Utlatande source);
}
