package se.inera.certificate.modules.sjukersattning.model.converter;

import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public class TransportToInternal {

    public static SjukersattningUtlatande convert(Intyg source) throws ConverterException {
        SjukersattningUtlatande utlatande = new SjukersattningUtlatande();
        return utlatande;
    }

    public static CertificateMetaData getMetaData(Intyg source) {
        CertificateMetaData metaData = new CertificateMetaData();
        return metaData;
    }

}
