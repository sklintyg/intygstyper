package se.inera.certificate.modules.rli.utils;

import se.inera.certificate.rli.model.v1.Utlatande;

/**
 * Defines a scenario that can be tested. The following models (as POJOs) can be extracted from a scenario:
 * <ul>
 * <li>Transport model
 * <li>Export model (with and without a {@link CertificateContentHolder})
 * <li>Internal model used by Mina Intyg
 * <li>Internal model used by WebCert
 * </ul>
 * 
 * @see ScenarioFinder
 */
public interface Scenario {

    /**
     * Returns the name of the scenario. Useful for assertion messages.
     * 
     * @return The scenario name.
     */
    String getName();

    /**
     * Returns the scenario as a transport model.
     * 
     * @return The scenario as a transport model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    Utlatande asTransportModel() throws ScenarioNotFoundException;

    /**
     * Returns the scenario as a external model.
     * 
     * @return The scenario as a external model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    se.inera.certificate.modules.rli.model.external.Utlatande asExternalModel() throws ScenarioNotFoundException;

    /**
     * Returns the scenario as a internal Mina Intyg model.
     * 
     * @return The scenario as a internal Mina Intyg model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    se.inera.certificate.modules.rli.model.internal.mi.Utlatande asInternalMIModel() throws ScenarioNotFoundException;

    /**
     * Returns the scenario as a internal WebCert model.
     * 
     * @return The scenario as a internal WebCert model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    se.inera.certificate.modules.rli.model.internal.wc.Utlatande asInternalWCModel() throws ScenarioNotFoundException;
}
