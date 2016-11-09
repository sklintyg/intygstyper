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
package se.inera.intyg.intygstyper.fk7263.transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.xslt.SchematronResourceSCH;

import se.inera.intyg.common.support.xml.SchemaValidatorBuilder;
import se.inera.intyg.intygstyper.fk7263.rest.Fk7263ModuleApi;

@RunWith(MockitoJUnitRunner.class)
public class Fk7263TransformerTest {
    private static final String COMMON_UTLATANDE_SCHEMA = "core_components/MU7263-RIV_3.1.xsd";

    private static final String COMMON_UTLATANDE_TYPES_SCHEMA = "core_components/insuranceprocess_healthreporting_2.0.xsd";

    private static final String ISO_TYPES_SCHEMA = "core_components/iso_dt_subset_1.0.xsd";

    private static final String ROOT_LEVEL_UTLATANDE_SCHEMA = "interactions/RegisterMedicalCertificateInteraction/RegisterMedicalCertificateResponder_3.1.xsd";

    private static final String ROOT_LEVEL_FK7263SIT_SCHEMA = "interactions/RegisterCertificateInteraction/RegisterCertificateResponder_2.0.xsd";

    private static final String ROOT_LEVEL_FK7263_GENERAL_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_2.0.xsd";

    private static final String CLINICAL_UTLATANDE_TYPES_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_types_2.0.xsd";

    private static Schema lakarutlatandeInputSchema;
    private static Schema fk7263sitOutputSchema;

    @InjectMocks
    private Fk7263ModuleApi fk7263ModuleApi;

    @BeforeClass
    public static void initIntygstjansterSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(ROOT_LEVEL_UTLATANDE_SCHEMA);
        schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_SCHEMA);
        schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_TYPES_SCHEMA);
        schemaValidatorBuilder.registerResource(ISO_TYPES_SCHEMA);
        lakarutlatandeInputSchema = schemaValidatorBuilder.build(rootSource);
    }

    @BeforeClass
    public static void initGeneralSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(ROOT_LEVEL_FK7263SIT_SCHEMA);
        schemaValidatorBuilder.registerResource(ROOT_LEVEL_FK7263_GENERAL_SCHEMA);
        schemaValidatorBuilder.registerResource(CLINICAL_UTLATANDE_TYPES_SCHEMA);
        fk7263sitOutputSchema = schemaValidatorBuilder.build(rootSource);
    }

    @Test
    public void testTransformationFailsOnMissingFunktionstillstand() throws Exception {
        List<String> testFiles = Arrays.asList(
                "fk7263_utanfunktionstillstand.xml");

        for (String xmlFile : testFiles) {
            String xmlContentsInput = Resources.toString(getResource("Fk7263TransformerTest/" + xmlFile), Charsets.UTF_8);

            if (!validateIntygstjansterXSD(xmlContentsInput)) {
                fail();
            }
            String result = fk7263ModuleApi.transformToStatisticsService(xmlContentsInput);

            if (!validateIntygstjansterOutputXSD(result)) {
                fail();
            }

            SchematronOutputType output = getFk7263OutputSchematron(result);

            assertEquals(1, SVRLHelper.getAllFailedAssertions(output).size());
            assertTrue(SVRLHelper.getAllFailedAssertions(output).get(0).getText().contains("Ett 'MU' måste ha minst ett 'Behov av sjukskrivning'"));
        }
    }

    @Test
    public void testTransformationFailsOnMissingArbetsbegransning() throws Exception {
        List<String> testFiles = Arrays.asList(
                "fk7263_utanarbetsbegransning.xml");

        for (String xmlFile : testFiles) {
            String xmlContentsInput = Resources.toString(getResource("Fk7263TransformerTest/" + xmlFile), Charsets.UTF_8);

            if (!validateIntygstjansterXSD(xmlContentsInput)) {
                fail();
            }
            String result = fk7263ModuleApi.transformToStatisticsService(xmlContentsInput);

            if (!validateIntygstjansterOutputXSD(result)) {
                fail();
            }

            SchematronOutputType output = getFk7263OutputSchematron(result);

            assertEquals(1, SVRLHelper.getAllFailedAssertions(output).size());
            assertTrue(SVRLHelper.getAllFailedAssertions(output).get(0).getText().contains("Ett 'MU' måste ha minst ett 'Behov av sjukskrivning'"));
        }
    }

    @Test
    public void testTransformationAccepted() throws Exception {

        List<String> testFiles = Arrays.asList("fk7263.xml", "fk7263_utanvardkontakt.xml", "fk7263_utanreferens.xml",
                "fk7263_utanprognosangivelse.xml", "fk7263_utanmedicinskttillstand.xml",
                "fk7263_utanbedomttillstand.xml", "fk7263_utanaktivitet.xml", "fk7263_flerasysselsattningar.xml");

        for (String xmlFile : testFiles) {
            String xmlContentsInput = Resources.toString(getResource("Fk7263TransformerTest/" + xmlFile), Charsets.UTF_8);

            if (!validateIntygstjansterXSD(xmlContentsInput)) {
                fail();
            }
            String result = fk7263ModuleApi.transformToStatisticsService(xmlContentsInput);

            if (!validateIntygstjansterOutputXSD(result)) {
                fail();
            }

            if (!validateFk7263OutputSchematron(result)) {
                fail();
            }
        }
    }

    private boolean validateFk7263OutputSchematron(String xml) throws Exception {
        SchematronOutputType result = getFk7263OutputSchematron(xml);
        return SVRLHelper.getAllFailedAssertions(result).size() == 0;
    }

    private SchematronOutputType getFk7263OutputSchematron(String xml) throws Exception {
        SchematronResourceSCH schematronResource = SchematronResourceSCH.fromClassPath("fk7263sit.sch");
        if (!schematronResource.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
        return schematronResource
                .applySchematronValidationToSVRL((new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)))));

    }

    private boolean validateIntygstjansterXSD(String xml) {
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
        try {
            lakarutlatandeInputSchema.newValidator().validate(xmlSource);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean validateIntygstjansterOutputXSD(String xml) {
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
        try {
            fk7263sitOutputSchema.newValidator().validate(xmlSource);
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
