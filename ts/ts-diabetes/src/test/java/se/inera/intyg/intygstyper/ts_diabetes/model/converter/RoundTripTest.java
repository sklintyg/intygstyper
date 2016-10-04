package se.inera.intyg.intygstyper.ts_diabetes.model.converter;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.xml.bind.*;

import org.custommonkey.xmlunit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.w3c.dom.Node;

import com.fasterxml.jackson.databind.JsonNode;

import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_diabetes.utils.*;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.ObjectFactory;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.PartialDateType;

@RunWith(Parameterized.class)
public class RoundTripTest {

    private Scenario scenario;

    private CustomObjectMapper objectMapper = new CustomObjectMapper();
    private ObjectFactory objectFactory = new ObjectFactory();
    private se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory rivtav2ObjectFactory = new se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory();
    private se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.ObjectFactory transformedObjectFactory = new se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.ObjectFactory();
    private static Marshaller marshaller;
    private static XslTransformer transformer;

    static {
        try {
            marshaller = JAXBContext.newInstance(RegisterTSDiabetesType.class, RegisterCertificateType.class, DatePeriodType.class,
                    PartialDateType.class, se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType.class)
                    .createMarshaller();
            transformer = new XslTransformer("xsl/transform-ts-diabetes.xsl");
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
        return ScenarioFinder.getInternalScenarios("transform-valid-*").stream()
                .map(u -> new Object[] { u.getName(), u })
                .collect(Collectors.toList());
    }

    @Test
    public void testRoundTrip() throws Exception {
        RegisterTSDiabetesType transport = InternalToTransportConverter.convert(scenario.asInternalModel());

        StringWriter expected = new StringWriter();
        StringWriter actual = new StringWriter();
        marshaller.marshal(objectFactory.createRegisterTSDiabetes(scenario.asTransportModel()), expected);
        marshaller.marshal(objectFactory.createRegisterTSDiabetes(transport), actual);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expected.toString(), actual.toString());
        diff.overrideElementQualifier(new ElementNameAndAttributeQualifier("id"));
        assertTrue(diff.toString(), diff.similar());

        JsonNode tree = objectMapper.valueToTree(TransportToInternalConverter.convert(transport.getIntyg()));
        JsonNode expectedTree = objectMapper.valueToTree(scenario.asInternalModel());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    @Test
    public void testConvertToRivtav2() throws Exception {
        Utlatande internal = TransportToInternalConverter.convert(scenario.asTransportModel().getIntyg());
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
        assertTrue(name + " " + diff.toString(), diff.similar());
    }

    @Test
    public void testTransportTransform() throws Exception {
        StringWriter transformingString = new StringWriter();
        marshaller.marshal(objectFactory.createRegisterTSDiabetes(scenario.asTransportModel()), transformingString);
        String actual = transformer.transform(transformingString.toString());

        StringWriter expected = new StringWriter();
        marshaller.marshal(transformedObjectFactory.createRegisterCertificate(scenario.asTransformedTransportModel()), expected);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expected.toString(), actual);
        diff.overrideElementQualifier(new ElementNameAndAttributeQualifier("id"));
        diff.overrideDifferenceListener(new IgnoreNamespacePrefixDifferenceListener());
        assertTrue(name + " " + diff.toString(), diff.similar());
    }

    private class IgnoreNamespacePrefixDifferenceListener implements DifferenceListener {
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
}
