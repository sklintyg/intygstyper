package se.inera.certificate.modules.ts_diabetes.model.converter;

import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.rest.dto.CertificateContentHolder;

public class ExternalToInternalConverter {

    public Utlatande convert(CertificateContentHolder certificateContentHolder) throws ConverterException {
        ExternalToInternalConverterInstance instance = new ExternalToInternalConverterInstance();
        return instance.convert(certificateContentHolder);
    }
}
