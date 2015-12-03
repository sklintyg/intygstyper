package se.inera.intyg.intygstyper.ts_diabetes.model.validator;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.intyg.common.support.xml.SchemaValidatorBuilder;
import se.inera.intyg.intygstyper.ts_diabetes.utils.Scenario;
import se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioFinder;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;

public class DomainTransportModelValidatorTest {

    private static final String COMMON_UTLATANDE_SCHEMA = "/core_components/se_intygstjanster_services_1.0.xsd";

    private static final String COMMON_UTLATANDE_TYPES_SCHEMA = "/core_components/se_intygstjanster_services_types_1.0.xsd";

    private static final String COMMON_REGISTER_SCHEMA = "/interactions/RegisterTSDiabetesInteraction/RegisterTSDiabetesResponder_1.0.xsd";

    private static Schema commonSchema;

    @BeforeClass
    public static void initCommonSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(COMMON_REGISTER_SCHEMA);
        schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_TYPES_SCHEMA);
        schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_SCHEMA);

        commonSchema = schemaValidatorBuilder.build(rootSource);
    }

    @Test
    public void testValidateTransportXmlAgainstDomainModel() throws Exception {
        // Check that valid scenarios validates against the common domain model
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            validateUtlatande(scenario);
        }

        // Also check that invalid scenarios doesn't validate
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("invalid-*")) {
            try {
                validateUtlatande(scenario);
                fail("Expected schema validation error in " + scenario.getName());
            } catch (Exception ignore) {
            }
        }
    }

    private void validateUtlatande(Scenario scenario) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            JAXBElement<RegisterTSDiabetesType> jaxbElement = new JAXBElement<RegisterTSDiabetesType>(new QName("ns3:RegisterTSDiabetes"), RegisterTSDiabetesType.class,
                    scenario.asTransportModel());
            JAXBContext context = JAXBContext.newInstance(RegisterTSDiabetesType.class);
            context.createMarshaller().marshal(jaxbElement, output);

            Validator validator = commonSchema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(output.toByteArray())));

        } catch (Exception e) {
            throw new RuntimeException(String.format("Error in scenario %s: %s", scenario.getName(), e.getMessage()));
        }
    }
}
