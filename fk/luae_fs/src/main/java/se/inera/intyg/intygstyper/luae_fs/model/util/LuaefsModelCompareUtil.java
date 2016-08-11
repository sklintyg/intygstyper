package se.inera.intyg.intygstyper.luae_fs.model.util;

import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.intygstyper.fkparent.model.util.FkParentModelCompareUtil;
import se.inera.intyg.intygstyper.luae_fs.model.internal.LuaefsUtlatande;

public class LuaefsModelCompareUtil extends FkParentModelCompareUtil implements ModelCompareUtil<LuaefsUtlatande>{

    public boolean isValidForNotification(LuaefsUtlatande utlatande) {
        return diagnosesAreValid(utlatande)
                && datesAreValid(utlatande.getAnnatGrundForMU(), utlatande.getJournaluppgifter(), utlatande.getAnhorigsBeskrivningAvPatienten(),
                        utlatande.getUndersokningAvPatienten(), utlatande.getKannedomOmPatient());
    }

}
