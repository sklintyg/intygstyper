package se.inera.certificate.modules.rli.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import se.inera.certificate.rli.model.v1.Utlatande;

/**
 * Finds and creates scenarios based on scenario files placed in src/test/resources.
 */
public class ScenarioFinder {

    private static final File TRANSPORT_MODEL_PATH = new File("src/test/resources/scenarios/transport");

    private static final File EXTERNAL_MODEL_PATH = new File("src/test/resources/scenarios/external");

    private static final File INTERNAL_MODEL_MI_PATH = new File("src/test/resources/scenarios/internal-mi");

    private static final File INTERNAL_MODEL_WC_PATH = new File("src/test/resources/scenarios/internal-wc");

    private static final String TRANSPORT_MODEL_EXT = ".xml";

    private static final String EXTERNAL_MODEL_EXT = ".json";

    private static final String INTERNAL_MODEL_MI_EXT = ".json";

    private static final String INTERNAL_MODEL_WC_EXT = ".json";

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
    public static List<Scenario> getInternalMIScenarios(String scenarioWithWildcards) throws ScenarioNotFoundException {
        return getScenarios(scenarioWithWildcards + INTERNAL_MODEL_MI_EXT, INTERNAL_MODEL_MI_PATH, "internal MI");
    }

    /**
     * Finds the specified inernal WebCert scenarios that matches the wildcard string.
     * 
     * @param scenarioWithWildcards
     *            A wildcard string matching scenarios. '*' and '?' can be used.
     * @return A list of matching internal WebCert scenarios.
     * @throws ScenarioNotFoundException
     *             If no scenarios could be found.
     */
    public static List<Scenario> getInternalWCScenarios(String scenarioWithWildcards) throws ScenarioNotFoundException {
        return getScenarios(scenarioWithWildcards + INTERNAL_MODEL_WC_EXT, INTERNAL_MODEL_WC_PATH, "internal WC");
    }

    public static List<Scenario> getScenarios(String scenarioWithWildcards, File scenarioPath, String model)
            throws ScenarioNotFoundException {
        FilenameFilter filter = new WildcardFileFilter(scenarioWithWildcards);
        File[] files = scenarioPath.listFiles(filter);
        if (files == null || files.length == 0) {
            throw new ScenarioNotFoundException(scenarioWithWildcards, model);
        }

        ArrayList<Scenario> result = new ArrayList<>();
        for (File file : files) {
            result.add(new FileBasedScenario(file));
        }
        return result;
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
    public static Scenario getInternalMIScenario(String filename) throws ScenarioNotFoundException {
        return getScenario(filename + INTERNAL_MODEL_MI_EXT, INTERNAL_MODEL_MI_PATH, "internal MI");
    }

    /**
     * Finds the specified internal WebCert scenario matching the name.
     * 
     * @param filename
     *            A name matching a scenario.
     * @return A matching internal WebCert scenario.
     * @throws ScenarioNotFoundException
     *             If no scenario could be found.
     */
    public static Scenario getInternalWCScenario(String filename) throws ScenarioNotFoundException {
        return getScenario(filename + INTERNAL_MODEL_WC_EXT, INTERNAL_MODEL_WC_PATH, "internal WC");
    }

    private static Scenario getScenario(String filename, File scenarioPath, String model)
            throws ScenarioNotFoundException {
        File file = new File(scenarioPath, filename);
        if (!file.exists() || !file.isFile()) {
            throw new ScenarioNotFoundException(filename, model);
        }

        return new FileBasedScenario(file);
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
        public se.inera.certificate.modules.rli.model.external.Utlatande asExternalModel()
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
        public se.inera.certificate.modules.rli.model.internal.mi.Utlatande asInternalMIModel()
                throws ScenarioNotFoundException {
            try {
                return ResourceConverterUtils.toInternalMI(getInternalMIModelFor(scenarioFile));
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "internal MI", e);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public se.inera.certificate.modules.rli.model.internal.wc.Utlatande asInternalWCModel()
                throws ScenarioNotFoundException {
            try {
                return ResourceConverterUtils.toInternalWC(getInternalWCModelFor(scenarioFile));
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "internal WC", e);
            }
        }
    }

    private static File getTransportModelFor(File otherModel) {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        return new File(TRANSPORT_MODEL_PATH, filenameWithoutExt + TRANSPORT_MODEL_EXT);
    }

    private static File getExternalModelFor(File otherModel) {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        return new File(EXTERNAL_MODEL_PATH, filenameWithoutExt + EXTERNAL_MODEL_EXT);
    }

    private static File getInternalMIModelFor(File otherModel) {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        return new File(INTERNAL_MODEL_MI_PATH, filenameWithoutExt + INTERNAL_MODEL_MI_EXT);
    }

    private static File getInternalWCModelFor(File otherModel) {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        return new File(INTERNAL_MODEL_WC_PATH, filenameWithoutExt + INTERNAL_MODEL_WC_EXT);
    }
}
