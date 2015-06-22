package se.inera.certificate.modules.ts_diabetes.validator.transport;

import se.inera.certificate.modules.ts_diabetes.model.codes.KorkortsKod;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class ValidationContext {

    private final TSDiabetesIntyg utlatande;

    public ValidationContext(TSDiabetesIntyg utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isHogreContext() {
        for (KorkortsbehorighetTsDiabetes intygAvser : utlatande.getIntygAvser().getKorkortstyp()) {
            if (intygAvser != null && KorkortsKod.isHogreBehorighet(intygAvser.value().value())) {
                return true;
            }
        }
        return false;
    }
}
