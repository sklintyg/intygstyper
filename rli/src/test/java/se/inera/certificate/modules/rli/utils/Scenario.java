package se.inera.certificate.modules.rli.utils;

import se.inera.certificate.common.v1.Utlatande;
import se.inera.certificate.modules.rli.rest.dto.CertificateContentHolder;

public interface Scenario {

    String getName();

    Utlatande asTransportModel() throws ScenarioNotFoundException;

    se.inera.certificate.modules.rli.model.external.Utlatande asExternalModel() throws ScenarioNotFoundException;

    CertificateContentHolder asExternalModelWithHolder() throws ScenarioNotFoundException;

    se.inera.certificate.modules.rli.model.internal.mi.Utlatande asInternalMIModel() throws ScenarioNotFoundException;

    se.inera.certificate.modules.rli.model.internal.wc.Utlatande asInternalWCModel() throws ScenarioNotFoundException;
}
