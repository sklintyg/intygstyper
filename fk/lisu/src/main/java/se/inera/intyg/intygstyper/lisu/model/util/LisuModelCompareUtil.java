package se.inera.intyg.intygstyper.lisu.model.util;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.intygstyper.fkparent.model.util.FkParentModelCompareUtil;
import se.inera.intyg.intygstyper.lisu.model.internal.LisuUtlatande;
import se.inera.intyg.intygstyper.lisu.model.internal.Sjukskrivning;

public class LisuModelCompareUtil extends FkParentModelCompareUtil implements ModelCompareUtil<LisuUtlatande> {

    @Override
    public boolean isValidForNotification(LisuUtlatande utlatande) {
        return diagnosesAreValid(utlatande)
                && datesAreValid(utlatande.getUndersokningAvPatienten(), utlatande.getTelefonkontaktMedPatienten(), utlatande.getJournaluppgifter(),
                        utlatande.getAnnatGrundForMU())
                && sjukskrivningarAreValid(utlatande);
    }

    private boolean sjukskrivningarAreValid(LisuUtlatande utlatande) {
        for (Sjukskrivning s : utlatande.getSjukskrivningar()) {
            if (!isValid(s.getPeriod())) {
                return false;
            }
        }
        return true;
    }

    private boolean isValid(InternalLocalDateInterval period) {
        return period == null || period.isValid();
    }
}
