package se.inera.certificate.modules.sjukpenning.model.converter;

import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningType;
import se.inera.intygstjanster.fk.services.v1.SjukpenningIntyg;

public class InternalToTransport {

    public static RegisterSjukpenningType convert(SjukpenningUtlatande source) throws ConverterException {

        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        RegisterSjukpenningType sjukpenningType = new RegisterSjukpenningType();
        SjukpenningIntyg sjukpenningIntyg = new SjukpenningIntyg();
        sjukpenningType.setIntyg(sjukpenningIntyg);

        return sjukpenningType;
    }

}
