package se.inera.intyg.intygstyper.fk7263.transformation;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.SVRLWriter;
import com.helger.schematron.xslt.SchematronResourceSCH;

import java.util.Arrays;
import java.util.List;

import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.common.support.xml.SchemaValidatorBuilder;

public class Fk7263TransformerTest {
    private static final String COMMON_UTLATANDE_SCHEMA = "core_components/MU7263-RIV_3.1.xsd";

    private static final String COMMON_UTLATANDE_TYPES_SCHEMA = "core_components/insuranceprocess_healthreporting_2.0.xsd";

    private static final String ISO_TYPES_SCHEMA = "core_components/iso_dt_subset_1.0.xsd";

    private static final String ROOT_LEVEL_UTLATANDE_SCHEMA = "interactions/RegisterMedicalCertificateInteraction/RegisterMedicalCertificateResponder_3.1.xsd";

    private static final String ROOT_LEVEL_SJUKPENNING_UTOKAT_SCHEMA = "interactions/RegisterCertificateInteraction/RegisterCertificateResponder_2.0.xsd";

    private static final String ROOT_LEVEL_SJUKPENNING_GENERAL_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_2.0.xsd";

    private static final String CLINICAL_UTLATANDE_TYPES_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_types_2.0.xsd";

    private static Schema lakarutlatandeInputSchema;
    private static Schema sjukpenningUtokatOutputSchema;

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
    public static void initGeneralSjukpenningUtokatSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(ROOT_LEVEL_SJUKPENNING_UTOKAT_SCHEMA);
        schemaValidatorBuilder.registerResource(ROOT_LEVEL_SJUKPENNING_GENERAL_SCHEMA);
        schemaValidatorBuilder.registerResource(CLINICAL_UTLATANDE_TYPES_SCHEMA);
        sjukpenningUtokatOutputSchema = schemaValidatorBuilder.build(rootSource);
    }

    //TODO: ÅTERINFÖR TESTET NÄR XSLTRANSFORMERING FUNGERAR
//    @Test
    public void testTransformation() throws Exception {

        List<String> testFiles = Arrays.asList("fk7263.xml", "fk7263_utanvardkontakt.xml", "fk7263_utanreferens.xml",
                "fk7263_utanprognosangivelse.xml", "fk7263_utanmedicinskttillstand.xml", "fk7263_utanfunktionstillstand.xml",
                "fk7263_utanbedomttillstand.xml", "fk7263_utanarbetsbegransning.xml", "fk7263_utanaktivitet.xml", "fk7263_flerasysselsattningar.xml");

        XslTransformer transformer = new XslTransformer("transform-to-statistics.xsl");

        for (String xmlFile : testFiles) {
            String xmlContentsInput = Resources.toString(getResource("Fk7263TransformerTest/" + xmlFile), Charsets.UTF_8);

            if (!validateIntygstjansterXSD(xmlContentsInput)) {
                fail();
            }

            String result = transformer.transform(xmlContentsInput);
System.out.println(result);
            if (!validateIntygstjansterOutputXSD(result)) {
                fail();
            }

            if (!validateSjukpenningUtokatOutputSchematron(result)) {
                fail();
            }
        }
    }

    private boolean validateSjukpenningUtokatOutputSchematron(String xml) throws Exception {
        SchematronResourceSCH schematronResource = SchematronResourceSCH.fromClassPath("sjukpenning-utokat.sch");
        if (!schematronResource.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
        SchematronOutputType result = schematronResource
                .applySchematronValidationToSVRL((new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)))));

        System.out.println(SVRLWriter.createXMLString(result));

        return SVRLHelper.getAllFailedAssertions(result).size() == 0;
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
            sjukpenningUtokatOutputSchema.newValidator().validate(xmlSource);
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
