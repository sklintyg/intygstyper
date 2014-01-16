package se.inera.certificate.modules.ts_bas.model.internal.mi;

import java.util.EnumSet;
import java.util.Set;

/**
 * The Korkortstyp[er] a specific Utlatande concerns
 * 
 * @author erik
 * 
 */
public class IntygAvser {

    Set<IntygAvserKorkortstyp> korkortstyp;

    public Set<IntygAvserKorkortstyp> getKorkortstyp() {
        if (korkortstyp == null) {
            korkortstyp = EnumSet.noneOf(IntygAvserKorkortstyp.class);
        }
        return korkortstyp;
    }

}
