package se.inera.certificate.modules.ts_diabetes.model.converter;

import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;

public class ExternalToInternalConverter {

    public Utlatande convert(se.inera.certificate.modules.ts_diabetes.model.external.Utlatande externalModel) throws ConverterException {
        ExternalToInternalConverterInstance instance = new ExternalToInternalConverterInstance();
        return instance.convert(externalModel);
    }
}
