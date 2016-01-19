package se.inera.certificate.modules.fkparent.rest;

import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public interface FKModuleApi extends ModuleApi {

    Utlatande getUtlatandeFromIntyg(Intyg intyg, String xml) throws Exception;

}
