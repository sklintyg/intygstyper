package se.inera.certificate.modules.support.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ModuleFeaturesFactory {

    public static Map<String, Boolean> getFeatures(String featurePropertiesFile) {

        Properties features = loadFeaturePropertiesFile(featurePropertiesFile);
        
        Map<String, Boolean> moduleFeaturesMap = new HashMap<String, Boolean>();

        for (ModuleFeature feature : ModuleFeature.values()) {

            Boolean featureState = Boolean.parseBoolean(features.getProperty(feature.getName()));

            if (featureState == null) {
                moduleFeaturesMap.put(feature.getName(), Boolean.FALSE);
                continue;
            }

            moduleFeaturesMap.put(feature.getName(), featureState);
        }

        return moduleFeaturesMap;
    }

    private static Properties loadFeaturePropertiesFile(String featurePropertiesFile) {
        try {
            Resource resource = new ClassPathResource(featurePropertiesFile);
            return PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw new IllegalArgumentException("Feature file not found");
        }
    }

}
