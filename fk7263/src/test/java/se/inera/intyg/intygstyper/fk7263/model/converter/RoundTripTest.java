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
package se.inera.intyg.intygstyper.fk7263.model.converter;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.bind.*;

import org.custommonkey.xmlunit.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.*;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.w3c.dom.Node;

import com.fasterxml.jackson.databind.JsonNode;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.utils.*;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.PartialDateType;

@RunWith(Parameterized.class)
public class RoundTripTest {

    private Scenario scenario;

    private CustomObjectMapper objectMapper = new CustomObjectMapper();
    private ObjectFactory objectFactory = new ObjectFactory();
    private se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory rivtav2ObjectFactory = new se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory();
    private static Marshaller marshaller;

    private static final List<String> IGNORED_JSON_PROPERTIES = Arrays.asList("arbetsformagaPrognosGarInteAttBedomaBeskrivning",
            "annanReferensBeskrivning", "diagnosBeskrivning", "diagnosBeskrivning1", "diagnosBeskrivning2", "diagnosBeskrivning3", "diagnosKod2",
            "diagnosKod3", "samsjuklighet");

    static {
        try {
            marshaller = JAXBContext
                    .newInstance(RegisterMedicalCertificateType.class, RegisterCertificateType.class, DatePeriodType.class, PartialDateType.class)
                    .createMarshaller();
        } catch (JAXBException e) {
        }
    }

    private String name;

    public RoundTripTest(String name, Scenario scenario) {
        this.scenario = scenario;
        this.name = name;
    }

    @Parameters(name = "{index}: Scenario: {0}")
    public static Collection<Object[]> data() throws ScenarioNotFoundException {
        return ScenarioFinder.getInternalScenarios("valid-*").stream()
                .map(u -> new Object[] { u.getName(), u })
                .collect(Collectors.toList());
    }

    @Test
    public void testRoundTrip() throws Exception {
        RegisterMedicalCertificateType transport = InternalToTransport.getJaxbObject(scenario.asInternalModel());

        StringWriter expected = new StringWriter();
        StringWriter actual = new StringWriter();
        marshaller.marshal(objectFactory.createRegisterMedicalCertificate(scenario.asTransportModel()), expected);
        marshaller.marshal(objectFactory.createRegisterMedicalCertificate(transport), actual);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expected.toString(), actual.toString());
        diff.overrideElementQualifier(new ElementNameAndAttributeQualifier("id"));
        assertTrue(diff.toString(), diff.similar());

        JsonNode tree = objectMapper.valueToTree(TransportToInternal.convert(transport.getLakarutlatande()));
        JsonNode expectedTree = objectMapper.valueToTree(scenario.asInternalModel());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), new IgnoreCertainValuesComparator(JSONCompareMode.LENIENT));
    }

    @Test
    public void testConvertToRivtaV2() throws Exception {
        Utlatande internal = TransportToInternal.convert(scenario.asTransportModel().getLakarutlatande());
        RegisterCertificateType actual = new RegisterCertificateType();
        actual.setIntyg(UtlatandeToIntyg.convert(internal));

        StringWriter expected = new StringWriter();
        StringWriter actualSw = new StringWriter();
        marshaller.marshal(rivtav2ObjectFactory.createRegisterCertificate(scenario.asRivtaV2TransportModel()), expected);
        marshaller.marshal(rivtav2ObjectFactory.createRegisterCertificate(actual), actualSw);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expected.toString(), actualSw.toString());
        diff.overrideElementQualifier(new ElementNameAndAttributeQualifier("id"));
        diff.overrideDifferenceListener(new IgnoreNamespacePrexifDifferenceListener());
        assertTrue(name + " " + diff.toString(), diff.similar());
    }

    private class IgnoreNamespacePrexifDifferenceListener implements DifferenceListener {
        @Override
        public int differenceFound(Difference difference) {
            if (difference.getId() == DifferenceConstants.NAMESPACE_PREFIX_ID) {
                return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            }
            return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
        }

        @Override
        public void skippedComparison(Node control, Node test) {
        }
    }

    private class IgnoreCertainValuesComparator extends DefaultComparator {

        public IgnoreCertainValuesComparator(JSONCompareMode mode) {
            super(mode);
        }

        @Override
        public void checkJsonObjectKeysExpectedInActual(String prefix, JSONObject expected, JSONObject actual, JSONCompareResult result)
                throws JSONException {
            if (!IGNORED_JSON_PROPERTIES.stream().anyMatch(p -> expected.has(p))) {
                super.checkJsonObjectKeysExpectedInActual(prefix, expected, actual, result);
            }
        }

    }
}
