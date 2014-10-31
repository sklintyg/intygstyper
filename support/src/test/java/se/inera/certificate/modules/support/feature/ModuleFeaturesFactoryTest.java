package se.inera.certificate.modules.support.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

public class ModuleFeaturesFactoryTest {

    private static final String TEST_FILE = "/Features/test-features.properties";
    
    @Test
    public void testFactory() {
        Map<String, Boolean> features = ModuleFeaturesFactory.getFeatures(TEST_FILE);
        assertNotNull(features);
        assertEquals(5, features.size());
        assertTrue(features.get(ModuleFeature.HANTERA_FRAGOR.getName()));
        assertFalse(features.get(ModuleFeature.MAKULERA_INTYG.getName()));
        assertFalse(features.get(ModuleFeature.SKICKA_INTYG.getName()));
    }
    
    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testFactoryWithNoFile() {
        ModuleFeaturesFactory.getFeatures("");
    }
}
