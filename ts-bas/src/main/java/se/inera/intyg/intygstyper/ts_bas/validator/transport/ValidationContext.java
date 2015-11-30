package se.inera.certificate.modules.ts_bas.validator.transport;

import se.inera.certificate.modules.ts_bas.model.codes.KorkortsKod;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsBas;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;

public class ValidationContext {

    private final TSBasIntyg utlatande;

    public ValidationContext(TSBasIntyg utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isPersontransportContext() {
        for (KorkortsbehorighetTsBas intygAvser : utlatande.getIntygAvser().getKorkortstyp()) {
            if (intygAvser != null && KorkortsKod.isPersontransport(intygAvser.value().value())) {
                return true;
            }
        }
        return false;
    }
}
