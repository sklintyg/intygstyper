/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.luse.utils;

import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

/**
 * Defines a scenario that can be tested. The following models (as POJOs) can be extracted from a scenario:
 * <ul>
 * <li>Transport model
 * <li>Export model (with and without a CertificateContentHolder)
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
    RegisterCertificateType asTransportModel() throws ScenarioNotFoundException;

    /**
     * Returns the scenario as a internal Mina Intyg model.
     *
     * @return The scenario as a internal Mina Intyg model.
     * @throws ScenarioNotFoundException
     *             if the scenario wasn't found.
     */
    LuseUtlatande asInternalModel() throws ScenarioNotFoundException;
}
