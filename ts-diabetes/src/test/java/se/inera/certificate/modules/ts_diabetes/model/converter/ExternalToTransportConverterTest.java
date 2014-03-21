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
package se.inera.certificate.modules.ts_diabetes.model.converter;

import iso.v21090.dt.v1.CD;

import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.ts_diabetes.utils.ModelAssert;
import se.inera.certificate.modules.ts_diabetes.utils.Scenario;
import se.inera.certificate.modules.ts_diabetes.utils.ScenarioFinder;
import se.inera.certificate.ts_diabetes.model.v1.RekommendationType;
import se.inera.certificate.ts_diabetes.model.v1.Utlatande;

public class ExternalToTransportConverterTest {

    private static ExternalToTransportConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new ExternalToTransportConverter();
    }

    @Test
    public void testConvertUtlatande() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            se.inera.certificate.modules.ts_diabetes.model.external.Utlatande extUtlatande = scenario.asExternalModel();

            Utlatande actual = converter.convert(extUtlatande);
                    
            Utlatande expected = scenario.asTransportModel();
            // We need to issue a get in order to create an empty list (and make the test pass)
            expected.getSkapadAv().getSpecialitets();
            expected.getSkapadAv().getBefattnings();
            expected.getObservationAktivitetRelations();
            expected.getAktivitets();
            for (RekommendationType rekommendation : actual.getRekommendations()) {
                rekommendation.getVardes();
            }

            resolveJAXBAnyTypes(expected);

            ModelAssert.assertEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }

    /**
     * Since JAXB has some trouble with xsd:anyType we have to manually convert some DOM elements into the correct java
     * types.
     * 
     * @param utlatande
     *            The utlatande to fix.
     * 
     * @throws JAXBException
     */
    protected void resolveJAXBAnyTypes(Utlatande utlatande) throws JAXBException {
        for (RekommendationType rekommendation : utlatande.getRekommendations()) {
            ArrayList<Object> newVardes = new ArrayList<>();
            for (Object varde : rekommendation.getVardes()) {
                newVardes.add(JAXBUtils.resolveAnyType(varde, Boolean.class, CD.class));
            }
            rekommendation.getVardes().clear();
            rekommendation.getVardes().addAll(newVardes);
        }
    }
}
