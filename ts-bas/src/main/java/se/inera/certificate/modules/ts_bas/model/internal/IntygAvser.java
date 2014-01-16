package se.inera.certificate.modules.ts_bas.model.internal;

import java.util.EnumSet;
import java.util.Set;

/**
 * The Korkortstyp[er] a specific Utlatande concerns
 * 
 * @author erik
 * 
 */
public class IntygAvser {

    Set<IntygAvserKategori> korkortstyp;

    public Set<IntygAvserKategori> getKorkortstyp() {
        if (korkortstyp == null) {
            korkortstyp = EnumSet.noneOf(IntygAvserKategori.class);
        }
        return korkortstyp;
    }

}
