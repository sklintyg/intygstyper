package se.inera.certificate.modules.ts_bas.model.converter;

import se.inera.certificate.modules.ts_bas.model.internal.mi.Utlatande;
import se.inera.certificate.modules.ts_bas.rest.dto.CertificateContentHolder;

public class ExternalToInternalConverter {

    public Utlatande convert(CertificateContentHolder certificateContentHolder) throws ConverterException {
        ExternalToInternalConverterInstance instance = new ExternalToInternalConverterInstance();
        return instance.convert(certificateContentHolder);
    }
}
