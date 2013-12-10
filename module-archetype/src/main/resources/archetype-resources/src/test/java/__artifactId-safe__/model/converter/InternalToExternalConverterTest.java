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
package ${package}.${artifactId-safe}.model.converter;

import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;

import org.junit.Before;
import org.junit.Test;

import ${package}.${artifactId-safe}.model.external.Utlatande;
import ${package}.${artifactId-safe}.utils.Scenario;
import ${package}.${artifactId-safe}.utils.ScenarioFinder;

/**
 * Unit test for InternalToExternalConverter
 * 
 * @author erik
 * 
 */
public class InternalToExternalConverterTest {

    private InternalToExternalConverter converter;

    @Before
    public void setUp() throws Exception {
        this.converter = new InternalToExternalConverter();
    }

    @Test
    public void testConvertUtlatandeFromInternalToExternal() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalWCScenarios("valid-*")) {
            ${package}.${artifactId-safe}.model.internal.wc.Utlatande intUtlatande = scenario.asInternalWCModel();

            Utlatande actual = converter.convert(intUtlatande);

            Utlatande expected = scenario.asExternalModel();
            assertLenientEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
        
    }
}
