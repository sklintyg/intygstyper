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

package se.inera.intyg.intygstyper.ts_diabetes.transformation;

import static java.util.Arrays.asList;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.common.support.xml.SchemaValidatorBuilder;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class TSDiabetesTransformerTest {

    private static final String COMMON_UTLATANDE_SCHEMA = "core_components/se_intygstjanster_services_1.0.xsd";

    private static final String COMMON_UTLATANDE_TYPES_SCHEMA = "core_components/se_intygstjanster_services_types_1.0.xsd";

    private static final String COMMON_REGISTER_SCHEMA = "interactions/RegisterTSDiabetesInteraction/RegisterTSDiabetesResponder_1.0.xsd";

    private static final String CLINICAL_TS_DIABETES_SCHEMA = "specializations/TS-Diabetes/ts-diabetes_model.xsd";

    private static final String CLINICAL_CORE_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_1.0.xsd";

    private static final String CLINICAL_TYPES_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_types_1.0.xsd";

    private static final String CLINIAL_REGISTER_SCHEMA = "interactions/RegisterCertificateInteraction/RegisterCertificateResponder_1.0.xsd";

    private static Schema intygstjansterSchema;

    private static Schema clinicalSchema;

    @BeforeClass
    public static void initIntygstjansterSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(COMMON_REGISTER_SCHEMA);
        schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_SCHEMA);
        schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_TYPES_SCHEMA);
        intygstjansterSchema = schemaValidatorBuilder.build(rootSource);
    }

    @BeforeClass
    public static void initClinicalSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(CLINIAL_REGISTER_SCHEMA);
        schemaValidatorBuilder.registerResource(CLINICAL_CORE_SCHEMA);
        schemaValidatorBuilder.registerResource(CLINICAL_TYPES_SCHEMA);
        schemaValidatorBuilder.registerResource(CLINICAL_TS_DIABETES_SCHEMA);
        clinicalSchema = schemaValidatorBuilder.build(rootSource);
    }

    @Test
    public void testTransformation() throws Exception {
        List<String> testFiles = asList("xsl.xml", "xsl-kan-inte-ta-stallning.xml");

        XslTransformer transformer = new XslTransformer("xsl/transform-ts-diabetes.xsl");

        for (String xmlFile : testFiles) {
            String xmlContents = Resources.toString(getResource("scenarios/transport/" + xmlFile), Charsets.UTF_8);

            if (!validateIntygstjansterXSD(xmlContents)) {
                fail();
            }

            String result = transformer.transform(xmlContents);

            if (!validateClinicalXSD(result)) {
                fail();
            }
        }
    }

    private static boolean validateIntygstjansterXSD(String xml) {
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
        try {
            intygstjansterSchema.newValidator().validate(xmlSource);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean validateClinicalXSD(String xml) {
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
        try {
            clinicalSchema.newValidator().validate(xmlSource);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

}
