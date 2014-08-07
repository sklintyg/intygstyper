package se.inera.certificate.modules.ts_diabetes.validator.transport;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_diabetes.model.codes.IntygAvserKod;
import se.inera.certificate.modules.ts_diabetes.model.converter.IsoTypeConverter;
import se.inera.certificate.ts_diabetes.iso.v21090.dt.v1.CD;
import se.inera.certificate.ts_diabetes.model.v1.Utlatande;

public class ValidationContext {
    
    private final Utlatande utlatande;
    
    ValidationContext(Utlatande utlatande) {
        this.utlatande = utlatande;
    }
    public boolean isHogrePersontransportContext() {
        for (CD intygAvser : utlatande.getIntygAvsers()) {
            Kod kod = IsoTypeConverter.toKod(intygAvser);
            IntygAvserKod intygAvserEnum = CodeConverter.fromCode(kod, IntygAvserKod.class);
            if (intygAvserEnum != null && IntygAvserKod.HOGRE_KORKORTSBEHORIGHET.contains(intygAvserEnum)) {
                return true;
            }
        }

        return false;
    }
}
