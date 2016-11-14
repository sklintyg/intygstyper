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
package se.inera.intyg.intygstyper.luae_fs.model.converter;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.xml.bind.*;
import javax.xml.namespace.QName;

import org.custommonkey.xmlunit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.JsonNode;

import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.luae_fs.utils.*;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.DatePeriodType;

@RunWith(Parameterized.class)
public class RoundTripTest {

    private Scenario scenario;

    @SuppressWarnings("unused") // It is actually used to name the test
    private String name;

    public RoundTripTest(String name, Scenario scenario) {
        this.scenario = scenario;
        this.name = name;
    }

    @Parameters(name = "{index}: Scenario: {0}")
    public static Collection<Object[]> data() throws ScenarioNotFoundException {
        return ScenarioFinder.getInternalScenarios("pass-*").stream()
                .map(u -> new Object[] { u.getName(), u })
                .collect(Collectors.toList());
    }

    @Test
    public void testRoundTrip() throws Exception {
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        RegisterCertificateType transport = InternalToTransport.convert(scenario.asInternalModel());

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter expected = new StringWriter();
        StringWriter actual = new StringWriter();
        marshaller.marshal(wrapJaxb(scenario.asTransportModel()), expected);
        marshaller.marshal(wrapJaxb(transport), actual);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expected.toString(), actual.toString());
        diff.overrideElementQualifier(new ElementNameAndAttributeQualifier("id"));
        assertTrue(diff.toString(), diff.similar());

        JsonNode tree = objectMapper.valueToTree(TransportToInternal.convert(transport.getIntyg()));
        JsonNode expectedTree = objectMapper.valueToTree(scenario.asInternalModel());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    private JAXBElement<?> wrapJaxb(RegisterCertificateType ws) {
        JAXBElement<?> jaxbElement = new JAXBElement<>(
                new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2", "RegisterCertificate"),
                RegisterCertificateType.class, ws);
        return jaxbElement;
    }
}
