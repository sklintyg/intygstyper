/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.ts_bas.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.commons.io.FilenameUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

/**
 * Finds and creates scenarios based on scenario files placed in src/test/resources.
 */
public final class ScenarioFinder {

    private ScenarioFinder() {
    }

    private static final String TRANSPORT_MODEL_PATH = "classpath:/scenarios/transport/";

    private static final String RIVTA_V2_TRANSPORT_MODEL_PATH = "classpath:/scenarios/rivtav2/";

    private static final String TRANSFORMED_TRANSPORT_MODEL_PATH = "classpath:/scenarios/transformed/";

    private static final String INTERNAL_MODEL_PATH = "classpath:/scenarios/internal/";

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
                result.add(new FileBasedScenario(r.getFile()));
            }
            return result;
        } catch (IOException e) {
            throw new ScenarioNotFoundException(scenarioPath + scenarioWithWildcards, model);
        } finally {
            context.close();
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

    private static Scenario getScenario(String filename, String scenarioPath, String model)
            throws ScenarioNotFoundException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        try {
            return new FileBasedScenario(context.getResource(scenarioPath + filename).getFile());
        } catch (IOException e) {
            throw new ScenarioNotFoundException(filename, model);
        } finally {
            context.close();
        }
    }

    /**
     * Scenario implementation using files.
     */
    private static final class FileBasedScenario implements Scenario {

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
        public RegisterTSBasType asTransportModel() throws ScenarioNotFoundException {
            try {
                return JAXB.unmarshal(getTransportModelFor(scenarioFile, TRANSPORT_MODEL_PATH), RegisterTSBasType.class);
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "transport", e);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RegisterCertificateType asRivtaV2TransportModel() throws ScenarioNotFoundException {
            try {
                return JAXB.unmarshal(getTransportModelFor(scenarioFile, RIVTA_V2_TRANSPORT_MODEL_PATH), RegisterCertificateType.class);
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "rivta v2 transport", e);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType asTransformedTransportModel()
                throws ScenarioNotFoundException {
            try {
                return JAXB.unmarshal(getTransportModelFor(scenarioFile, TRANSFORMED_TRANSPORT_MODEL_PATH),
                        se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType.class);
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "transformed transport", e);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande asInternalModel()
                throws ScenarioNotFoundException {
            try {
                return new CustomObjectMapper().readValue(getInternalModelFor(scenarioFile), Utlatande.class);
            } catch (IOException e) {
                throw new ScenarioNotFoundException(scenarioFile.getName(), "internal", e);
            }
        }

    }

    private static File getTransportModelFor(File otherModel, String path) throws IOException {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        File retFile = context.getResource(path + filenameWithoutExt + TRANSPORT_MODEL_EXT).getFile();
        context.close();
        return retFile;
    }

    private static File getInternalModelFor(File otherModel) throws IOException {
        String filenameWithoutExt = FilenameUtils.removeExtension(otherModel.getName());
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        File retFile = context.getResource(INTERNAL_MODEL_PATH + filenameWithoutExt + INTERNAL_MODEL_EXT).getFile();
        context.close();
        return retFile;
    }
}
