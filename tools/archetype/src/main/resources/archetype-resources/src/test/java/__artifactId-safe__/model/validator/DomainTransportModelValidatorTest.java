#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId-safe}.model.validator;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXB;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.junit.BeforeClass;
import org.junit.Test;

import ${package}.${artifactId-safe}.utils.Scenario;
import ${package}.${artifactId-safe}.utils.ScenarioFinder;
import se.inera.certificate.xml.SchemaValidatorBuilder;

public class DomainTransportModelValidatorTest {

    private static final String COMMON_UTLATANDE_SCHEMA = "/core_components/clinicalprocess_healthcond_certificate_0.9.xsd";

    private static final String COMMON_UTLATANDE_TYPES_SCHEMA = "/core_components/clinicalprocess_healthcond_types_0.9.xsd";

    private static Schema commonSchema;

    @BeforeClass
    public static void initCommonSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_SCHEMA);
        schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_TYPES_SCHEMA);

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
                fail("Expected schema validation error");
            } catch (Exception ignore) {
            }
        }
    }

    private void validateUtlatande(Scenario scenario) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            JAXB.marshal(scenario.asTransportModel(), output);

            Validator validator = commonSchema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(output.toByteArray())));

        } catch (Exception e) {
            throw new RuntimeException(String.format("Error in scenario %s: %s", scenario.getName(), e.getMessage()));
        }
    }
}
