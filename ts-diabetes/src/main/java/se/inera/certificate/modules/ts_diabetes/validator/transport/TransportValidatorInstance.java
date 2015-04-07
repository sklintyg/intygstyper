package se.inera.certificate.modules.ts_diabetes.validator.transport;

import java.util.ArrayList;
import java.util.List;

import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class TransportValidatorInstance {

    private ValidationContext context;

    private final List<String> validationErrors;

    public TransportValidatorInstance() {
        validationErrors = new ArrayList<String>();
    }

    public List<String> validate(TSDiabetesIntyg utlatande) {
        context = new ValidationContext(utlatande);
        if (context.isHogreContext()) {
            validateHogreBehorighetContext(utlatande);
        }
        return validationErrors;
    }

    private ValidationContext getContext() {
        return this.context;
    }

    private void validateHogreBehorighetContext(TSDiabetesIntyg utlatande) {
        if (getContext().isHogreContext()) {
            if (utlatande.getHypoglykemier().isGenomforEgenkontrollBlodsocker() == null) {
                validationErrors
                        .add("Aktivitet AKT_308113006 (Egenkontroll av blodsocker) must be present when intygAvser contains any of [C1, C1E, C, CE, D1, D1E, D, DE or TAXI]");
            }

            if (utlatande.getHypoglykemier().isHarAllvarligForekomstVakenTid() == null) {
                validationErrors.add("Observation OBS24 must be present when intygAvser contains any of [C1, C1E, C, CE, D1, D1E, D, DE or TAXI]");
            }
        }
    }
}
