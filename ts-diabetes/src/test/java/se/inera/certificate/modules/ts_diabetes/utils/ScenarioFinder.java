package se.inera.certificate.modules.ts_diabetes.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import se.inera.certificate.ts_diabetes.model.v1.Utlatande;

/**
 * Finds and creates scenarios based on scenario files placed in src/test/resources.
 */
public class ScenarioFinder {

    private static final String TRANSPORT_MODEL_PATH = "classpath:/scenarios/transport/";

    private static final String EXTERNAL_MODEL_PATH = "classpath:/scenarios/external/";

    private static final String INTERNAL_MODEL_PATH = "classpath:/scenarios/internal/";

    private static final String TRANSPORT_MODEL_EXT = ".xml";

    private static final String EXTERNAL_MODEL_EXT = ".json";

    private static final String INTERNAL_MODEL_EXT = ".json";

    /**
     * Finds the specified transport scenarios that matches the wildcard string.
     * 
     * @param scenarioWithWildcards
     *            A wildcard string matching scenarios. '*' and '?' can be used.
     * @return A list of matching transport scenarios.
     * @throws ScenarioNotFoundException
     *             If no scenarios could be found.
     */
    public static List<Scenario> getTransportScenarios(String scenarioWithWildcards) throws ScenarioNotFoundException {
        return getScenarios(scenarioWithWildcards + TRANSPORT_MODEL_EXT, TRANSPORT_MODEL_PATH, "transport");
    }

    /**
     * Finds the specified external scenarios that matches the wildcard string.
     * 
     * @param scenarioWithWildcards
     *            A wildcard string matching scenarios. '*' and '?' can be used.
     * @return A list of matching external scenarios.
     * @throws ScenarioNotFoundException
     *             If no scenarios could be found.
     */
    public static List<Scenario> getExternalScenarios(String scenarioWithWildcards) throws ScenarioNotFoundException {
        return getScenarios(scenarioWithWildcards + EXTERNAL_MODEL_EXT, EXTERNAL_MODEL_PATH, "external");
    }

    /**
     * Finds the specified internal Mina Intyg scenarios that matches the wildcard string.
     * 
     * @param scenarioWithWildcards
     *            A wildcard string matching scenarios. '*' and '?' can be used.
     * @return A list of matching internal Mina Intyg scenarios.
     * @throws ScenarioNotFoundException
     *             If no scenarios could be found.
     */
    public static List<Scenario> getInternalScenarios(String scenarioWithWildcards) throws ScenarioNotFoundException {
        return getScenarios(scenarioWithWildcards + INTERNAL_MODEL_EXT, INTERNAL_MODEL_PATH, "internal");
    }

    public static List<Scenario> getScenarios(String scenarioWithWildcards, String scenarioPath, String model)
            throws ScenarioNotFoundException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        try {
            Resource[] resources = context.getResources(scenarioPath + scenarioWithWildcards);
            ArrayList<Scenario> result = new ArrayList<>();
            if (resources.length < 1) {
                throw new ScenarioNotFoundException(scenarioPath + scenarioWithWildcards, model);
            }
            for (Resource r : resources) {
                System.err.println(r.getFile());
                result.add(new FileBasedScenario(r.getFile()));
            }
            return result;
        } catch (IOException e) {
            throw new ScenarioNotFoundException(scenarioPath + scenarioWithWildcards, model);
        }
    }

    /**
     * Finds the specified transport scenario matching the name.
     * 
     * @param filename
     *            A name matching a scenario.
     * @return A matching transport scenario.
     * @throws ScenarioNotFoundException
     *             If no scenario could be found.
     */
    public static Scenario getTransportScenario(String filename) throws ScenarioNotFoundException {
        return getScenario(filename + TRANSPORT_MODEL_EXT, TRANSPORT_MODEL_PATH, "transport");
    }

    /**
     * Finds the specified external scenario matching the name.
     * 
     * @param filename
     *            A name matching a scenario.
     * @return A matching external scenario.
     * @throws ScenarioNotFoundException
     *             If no scenario could be found.
     */
    public static Scenario getExternalScenario(String filename) throws ScenarioNotFoundException {
        return getScenario(filename + EXTERNAL_MODEL_EXT, EXTERNAL_MODEL_PATH, "external");
    }

    /**
     * Finds the specified internal Mina Intyg scenario matching the name.
     * 
     * @param filename
     *            A name matching a scenario.
     * @return A matching internal Mina Intyg scenario.
     * @throws ScenarioNotFoundException
     *             If no scenario could be found.
     */
    public static Scenario getInternalScenario(String filename) throws ScenarioNotFoundException {
        return getScenario(filename + INTERNAL_MODEL_EXT, INTERNAL_MODEL_PATH, "internal");
    }

    private static Scenario getScenario(String filename, String scenarioPath, String model)
            throws ScenarioNotFoundException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        try {
            return new FileBasedScenario(context.getResource(scenarioPath + filename).getFile());
        } catch (IOException e) {
            throw new ScenarioNotFoundException(filename, model);
        }
    }

    /**
     * Scenario implementation using files.
     */
    private static class FileBasedScenario implements Scenario {

        /** The file that represents the current scenario. */
        private final File scenarioFile;

        private FileBasedScenario(File scenarioFile) {
            this.scenarioFile = scenarioFile;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return FilenameUtils.getBaseName(scenarioFile.getName());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Utlatande asTransportModel() throws ScenarioNotFoundException {
            try {
                return ResourceConverterUtils.toTransport(getTransportModelFor(scenarioFile));
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "transport", e);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public se.inera.certificate.modules.ts_diabetes.model.external.Utlatande asExternalModel()
                throws ScenarioNotFoundException {
            try {
                return ResourceConverterUtils.toExternal(getExternalModelFor(scenarioFile));
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "external", e);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande asInternalModel()
                throws ScenarioNotFoundException {
            try {
                return ResourceConverterUtils.toInternal(getInternalModelFor(scenarioFile));
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "internal MI", e);
            }
        }

    }

    private static File getTransportModelFor(File otherModel) throws IOException {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        return context.getResource(TRANSPORT_MODEL_PATH + filenameWithoutExt + TRANSPORT_MODEL_EXT).getFile();
    }

    private static File getExternalModelFor(File otherModel) throws IOException {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        return context.getResource(EXTERNAL_MODEL_PATH + filenameWithoutExt + EXTERNAL_MODEL_EXT).getFile();
    }

    private static File getInternalModelFor(File otherModel) throws IOException {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        return context.getResource(INTERNAL_MODEL_PATH + filenameWithoutExt + INTERNAL_MODEL_EXT).getFile();
    }

}
