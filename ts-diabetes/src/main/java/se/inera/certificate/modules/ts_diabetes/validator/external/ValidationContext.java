package se.inera.certificate.modules.ts_diabetes.validator.external;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_diabetes.model.codes.IntygAvserKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_diabetes.model.external.Observation;
import se.inera.certificate.modules.ts_diabetes.model.external.Utlatande;

public class ValidationContext {

    private final Utlatande utlatande;

    public ValidationContext(Utlatande utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isDiabetesContext() {
        for (Observation observation : utlatande.getObservationer()) {
            Kod observationskod = observation.getObservationskod();
            if (observationskod != null
                    && (observationskod.equals(ObservationsKod.DIABETES_TYP_1) || observationskod
                            .equals(ObservationsKod.DIABETES_TYP_2))) {
                if (observation.getForekomst() != null) {
                    return observation.getForekomst();
                }
            }
        }
        return false;
    }

    public boolean isPersontransportContext() {
        for (Kod intygAvser : utlatande.getIntygAvser()) {
            IntygAvserKod intygAvserEnum = CodeConverter.fromCode(intygAvser, IntygAvserKod.class);
            if (intygAvserEnum != null && IntygAvserKod.PERSONTRANSPORT.contains(intygAvserEnum)) {
                return true;
            }
        }

        return false;
    }
}
