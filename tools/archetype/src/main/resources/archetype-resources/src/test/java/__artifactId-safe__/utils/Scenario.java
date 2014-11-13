#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId-safe}.utils;

import se.inera.certificate.${artifactId-safe}.model.v1.Utlatande;

/**
 * Defines a scenario that can be tested. The following models (as POJOs) can be extracted from a scenario:
 * <ul>
 * <li>Transport model
 * <li>Export model (with and without a {@link CertificateContentHolder})
 * <li>Internal model
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
    ${package}.${artifactId-safe}.model.external.Utlatande asExternalModel() throws ScenarioNotFoundException;

    /**
     * Returns the scenario as a internal Mina Intyg model.
     *
     * @return The scenario as a internal Mina Intyg model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    ${package}.${artifactId-safe}.model.internal.Utlatande asInternalModel() throws ScenarioNotFoundException;
}