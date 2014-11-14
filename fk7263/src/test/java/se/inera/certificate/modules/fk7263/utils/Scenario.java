package se.inera.certificate.modules.fk7263.utils;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;

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
    RegisterMedicalCertificate asTransportModel() throws ScenarioNotFoundException;

    /**
     * Returns the scenario as a internal Mina Intyg model.
     *
     * @return The scenario as a internal Mina Intyg model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    se.inera.certificate.modules.fk7263.model.internal.Utlatande asInternalModel() throws ScenarioNotFoundException;
}
