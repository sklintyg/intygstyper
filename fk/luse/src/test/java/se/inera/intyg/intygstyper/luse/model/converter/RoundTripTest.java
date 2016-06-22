package se.inera.intyg.intygstyper.luse.model.converter;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.JsonNode;

import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.luse.model.utils.*;

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

        JsonNode tree = objectMapper.valueToTree(TransportToInternal.convert(InternalToTransport.convert(scenario.asInternalModel()).getIntyg()));
        JsonNode expectedTree = objectMapper.valueToTree(scenario.asInternalModel());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }
}
