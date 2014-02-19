package se.inera.certificate.modules.ts_diabetes.model.internal;

import java.util.EnumSet;
import java.util.Set;

import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvserKategori;

public class IntygAvser {
    Set<IntygAvserKategori> korkortstyp;

    public Set<IntygAvserKategori> getKorkortstyp() {
        if (korkortstyp == null) {
            korkortstyp = EnumSet.noneOf(IntygAvserKategori.class);
        }
        return korkortstyp;
    }

}
