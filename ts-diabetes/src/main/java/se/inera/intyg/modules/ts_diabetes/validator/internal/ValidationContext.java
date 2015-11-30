package se.inera.certificate.modules.ts_diabetes.validator.internal;

import se.inera.certificate.modules.ts_diabetes.model.codes.KorkortsKod;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;

public class ValidationContext {

    private final Utlatande utlatande;

    public ValidationContext(Utlatande utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isHogreBehorighetContext() {
        for (IntygAvserKategori intygAvser : utlatande.getIntygAvser().getKorkortstyp()) {
            KorkortsKod intygAvserEnum = Enum.valueOf(KorkortsKod.class, intygAvser.name());
            if (intygAvserEnum != null && KorkortsKod.HOGRE_KORKORTSBEHORIGHET.contains(intygAvserEnum)) {
                return true;
            }
        }
        return false;
    }
}
