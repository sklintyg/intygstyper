package se.inera.intyg.intygstyper.luse.model.util;

import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.intygstyper.fkparent.model.util.FkParentModelCompareUtil;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;

public class LuseModelCompareUtil extends FkParentModelCompareUtil implements ModelCompareUtil<LuseUtlatande>{
    public boolean isValidForNotification(LuseUtlatande utlatande) {
        return diagnosesAreValid(utlatande)
                && datesAreValid(utlatande.getAnnatGrundForMU(), utlatande.getJournaluppgifter(), utlatande.getAnhorigsBeskrivningAvPatienten(),
                        utlatande.getUndersokningAvPatienten(), utlatande.getKannedomOmPatient());
    }
}
