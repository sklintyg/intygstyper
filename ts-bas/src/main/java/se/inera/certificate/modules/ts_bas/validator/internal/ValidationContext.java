package se.inera.certificate.modules.ts_bas.validator.internal;

import se.inera.certificate.modules.ts_bas.model.codes.KorkortsKod;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;

public class ValidationContext {

    private final Utlatande utlatande;

    public ValidationContext(Utlatande utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isPersontransportContext() {
        for (IntygAvserKategori intygAvser : utlatande.getIntygAvser().getKorkortstyp()) {
            KorkortsKod intygAvserEnum = Enum.valueOf(KorkortsKod.class, intygAvser.name());
            if (intygAvserEnum != null && KorkortsKod.PERSONTRANSPORT.contains(intygAvserEnum)) {
                return true;
            }
        }
        return false;
    }
}
