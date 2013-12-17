package se.inera.certificate.modules.ts_bas.validator;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.IntygAvserKod;
import se.inera.certificate.modules.ts_bas.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_bas.model.external.Observation;
import se.inera.certificate.modules.ts_bas.model.external.Utlatande;

public class ValidationContext {

    private static final Object KOD_HAR_DIABETES = CodeConverter.toKod(ObservationsKod.HAR_DIABETES);

    private final Utlatande utlatande;

    public ValidationContext(Utlatande utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isDiabetesContext() {
        for (Observation observation : utlatande.getObservationer()) {
            Kod observationskod = observation.getObservationskod();
            if (observationskod != null && observationskod.equals(KOD_HAR_DIABETES)) {
                return true;
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
