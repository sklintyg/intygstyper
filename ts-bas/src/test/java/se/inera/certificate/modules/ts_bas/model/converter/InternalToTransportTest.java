/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.ts_bas.model.converter;

import org.junit.Test;

import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.utils.ModelAssert;
import se.inera.certificate.modules.ts_bas.utils.Scenario;
import se.inera.certificate.modules.ts_bas.utils.ScenarioFinder;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;

/**
 * Unit test for InternalToExternalConverter.
 *
 * @author erik
 *
 */
public class InternalToTransportTest {

    @Test
    public void testConvertUtlatandeFromInternalToExternal() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Utlatande intUtlatande = scenario.asInternalModel();

            TSBasIntyg actual = InternalToTransport.convert(intUtlatande).getIntyg();

            TSBasIntyg expected = scenario.asTransportModel().getIntyg();

            ModelAssert.assertEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }

}
