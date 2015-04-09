package se.inera.certificate.modules.ts_diabetes.model.converter;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.utils.Scenario;
import se.inera.certificate.modules.ts_diabetes.utils.ScenarioFinder;
import se.inera.certificate.modules.ts_diabetes.utils.ScenarioNotFoundException;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class ScenarioTest {

    private List<Scenario> internalScenarios;
    private List<Scenario> transportScenarios;

    @Before
    public void setUp() throws Exception {
        internalScenarios = ScenarioFinder.getInternalScenarios("diabetes-*");
        transportScenarios = ScenarioFinder.getTransportScenarios("diabetes-*");
    }

    @Test
    public void testInternalToTransport() throws ScenarioNotFoundException {
        for (Scenario internalScenario : internalScenarios) {
            Scenario transportScenario = getScenarioByName(internalScenario.getName(), transportScenarios);
            
            TSDiabetesIntyg expected = transportScenario.asTransportModel();
            TSDiabetesIntyg actual = InternalToTransportConverter.convert(internalScenario.asInternalModel());
            
            ReflectionAssert.assertLenientEquals(expected, actual);
        }
    }
    
    @Test
    public void testTransportToInternal() throws ScenarioNotFoundException{
        for (Scenario scenario : transportScenarios) {
            Scenario internalScenario = getScenarioByName(scenario.getName(), internalScenarios);
            
            Utlatande expected = internalScenario.asInternalModel();
            Utlatande actual = TransportToInternalConverter.convert(scenario.asTransportModel());
            
            ReflectionAssert.assertLenientEquals(expected, actual);
        }
    }

    private Scenario getScenarioByName(String name, List<Scenario> scenarios) {
        for (Scenario scenario : scenarios) {
            if(name.equalsIgnoreCase(scenario.getName())){
                return scenario;
            }
        }
        
        throw new IllegalArgumentException("No such scenario found");
    }
}
