package se.inera.intyg.intygstyper.luae_na.model.util;

import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.intygstyper.fkparent.model.util.FkParentModelCompareUtil;
import se.inera.intyg.intygstyper.luae_na.model.internal.LuaenaUtlatande;

public class LuaenaModelCompareUtil extends FkParentModelCompareUtil implements ModelCompareUtil<LuaenaUtlatande>{
    public boolean isValidForNotification(LuaenaUtlatande utlatande) {
        return diagnosesAreValid(utlatande)
                && datesAreValid(utlatande.getAnnatGrundForMU(), utlatande.getJournaluppgifter(), utlatande.getAnhorigsBeskrivningAvPatienten(),
                        utlatande.getUndersokningAvPatienten(), utlatande.getKannedomOmPatient());
    }
}
