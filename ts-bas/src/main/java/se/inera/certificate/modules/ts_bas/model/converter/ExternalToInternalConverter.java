package se.inera.certificate.modules.ts_bas.model.converter;

import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;

public class ExternalToInternalConverter {

    public Utlatande convert(se.inera.certificate.modules.ts_bas.model.external.Utlatande externalModel) throws ConverterException {
        ExternalToInternalConverterInstance instance = new ExternalToInternalConverterInstance();
        return instance.convert(externalModel);
    }
}
