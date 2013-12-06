#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${artifactId}.model.converter;

import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;

import org.junit.Before;
import org.junit.Test;

import ${package}.${artifactId}.model.external.Utlatande;
import ${package}.${artifactId}.utils.Scenario;
import ${package}.${artifactId}.utils.ScenarioFinder;

/**
 * Test class for TransportToExternal, contains methods for setting up Utlatande using both the transport model and the
 * external model, and populating each with mock data
 * 
 * @author erik
 * 
 */

public class TransportToExternalConverterTest {

    private TransportToExternalConverter converter;

    @Before
    public void setUp() {
        converter = new TransportToExternalConverter();
    }

    @Test
    public void testTransportToExternal() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalWCScenarios("valid-*")) {
            se.inera.certificate.${artifactId}.model.v1.Utlatande utlatande = scenario.asTransportModel();

            Utlatande actual = converter.convert(utlatande);

            Utlatande expected = scenario.asExternalModel();
            assertLenientEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }
}
