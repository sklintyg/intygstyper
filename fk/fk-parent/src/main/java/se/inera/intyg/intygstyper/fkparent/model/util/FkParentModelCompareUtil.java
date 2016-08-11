package se.inera.intyg.intygstyper.fkparent.model.util;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.fkparent.model.internal.SitUtlatande;

public abstract class FkParentModelCompareUtil {

    @Autowired(required = false)
    protected WebcertModuleService moduleService;

    public boolean diagnosesAreValid(SitUtlatande utlatande) {
        for(Diagnos newDiagnos :  utlatande.getDiagnoser()) {
            if (!diagnoseCodeValid(newDiagnos)) {
                return false;
            }
        }
        return true;
    }

    public boolean datesAreValid(InternalDate... dates) {
        for (InternalDate date : dates) {
            if(!isValid(date)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValid(InternalDate date) {
        return date == null || date.isValidDate();
    }

    private boolean diagnoseCodeValid(Diagnos diagnosKod) {
        return moduleService.validateDiagnosisCode(diagnosKod.getDiagnosKod(), diagnosKod.getDiagnosKodSystem());
    }
}
