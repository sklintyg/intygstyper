package se.inera.certificate.modules.ts_bas.validator.internal;

import se.inera.certificate.modules.ts_bas.model.codes.IntygAvserKod;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;

public class ValidationContext {

    private final Utlatande utlatande;

    public ValidationContext(Utlatande utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isPersontransportContext() {
        for (IntygAvserKategori intygAvser : utlatande.getIntygAvser().getKorkortstyp()) {
            IntygAvserKod intygAvserEnum = Enum.valueOf(IntygAvserKod.class, intygAvser.name());
            if (intygAvserEnum != null && IntygAvserKod.PERSONTRANSPORT.contains(intygAvserEnum)) {
                return true;
            }
        }
        return false;
    }
}
