package se.inera.intyg.intygstyper.ts_bas.model.util;

import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;

public class TsBasModelCompareUtil implements ModelCompareUtil<Utlatande>{

    @Override
    public boolean isValidForNotification(Utlatande utlatande) {
        // Until someone actually specifies metrics for this, we assume TS-Bas is always valid for change notifications.
        return true;
    }

}
