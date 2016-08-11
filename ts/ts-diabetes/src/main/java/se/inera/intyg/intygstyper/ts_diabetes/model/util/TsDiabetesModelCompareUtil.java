package se.inera.intyg.intygstyper.ts_diabetes.model.util;

import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;

public class TsDiabetesModelCompareUtil implements ModelCompareUtil<Utlatande> {

    @Override
    public boolean isValidForNotification(Utlatande utlatande) {
        // Until someone actually specifies metrics for this, we assume TS-diabetes is always valid for change notifications.
        return true;
    }

}
