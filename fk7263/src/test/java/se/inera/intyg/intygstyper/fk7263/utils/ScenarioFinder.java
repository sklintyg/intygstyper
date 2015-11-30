package se.inera.certificate.modules.fk7263.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * Finds and creates scenarios based on scenario files placed in src/test/resources.
 */
public class ScenarioFinder {

    private static final File TRANSPORT_MODEL_PATH = new File("src/test/resources/scenarios/transport");

    private static final File INTERNAL_MODEL_PATH = new File("src/test/resources/scenarios/internal");

    private static final String TRANSPORT_MODEL_EXT = ".xml";

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

    private static List<Scenario> getScenarios(String scenarioWithWildcards, File scenarioPath, String model)
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
     * Finds the specified internal Mina Intyg scenario matching the name.
     *
     * @param filename
     *            A name matching a scenario.
     * @return A matching internal Mina Intyg scenario.
     * @throws ScenarioNotFoundException
     *             If no scenario could be found.
     */
    public static Scenario getInternalScenario(String filename) throws ScenarioNotFoundException {
        return getScenario(filename + INTERNAL_MODEL_EXT, INTERNAL_MODEL_PATH, "internal ");
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
        public RegisterMedicalCertificateType asTransportModel() throws ScenarioNotFoundException {
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
        public se.inera.certificate.modules.fk7263.model.internal.Utlatande asInternalModel()
                throws ScenarioNotFoundException {
            try {
                return ResourceConverterUtils.toInternal(getInternalModelFor(scenarioFile));
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "internal", e);
            }
        }

    }

    private static File getTransportModelFor(File otherModel) {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        return new File(TRANSPORT_MODEL_PATH, filenameWithoutExt + TRANSPORT_MODEL_EXT);
    }

    private static File getInternalModelFor(File otherModel) {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        return new File(INTERNAL_MODEL_PATH, filenameWithoutExt + INTERNAL_MODEL_EXT);
    }
}
