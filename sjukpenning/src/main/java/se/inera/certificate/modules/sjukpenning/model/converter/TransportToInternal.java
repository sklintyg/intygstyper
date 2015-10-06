package se.inera.certificate.modules.sjukpenning.model.converter;

import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public class TransportToInternal {

    public static SjukpenningUtlatande convert(Intyg source) throws ConverterException {
        SjukpenningUtlatande utlatande = new SjukpenningUtlatande();
        return utlatande;
    }

    public static CertificateMetaData getMetaData(Intyg source) {
        CertificateMetaData metaData = new CertificateMetaData();
        return metaData;
    }

}
