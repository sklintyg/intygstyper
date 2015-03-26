package se.inera.certificate.modules.ts_diabetes.validator.internal;

import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;

public class ValidationContext {

    private final Utlatande utlatande;

    public ValidationContext(Utlatande utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isHogreBehorighetContext() {
        /*for (IntygAvserKategori intygAvser : utlatande.getIntygAvser().getKorkortstyp()) {
            IntygAvserKod intygAvserEnum = Enum.valueOf(IntygAvserKod.class, intygAvser.name());
            if (intygAvserEnum != null && IntygAvserKod.HOGRE_KORKORTSBEHORIGHET.contains(intygAvserEnum)) {
                return true;
            }
        }*/
        return false;
    }
}
