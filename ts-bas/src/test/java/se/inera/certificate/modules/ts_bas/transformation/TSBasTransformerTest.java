package se.inera.certificate.modules.ts_bas.transformation;

import static java.util.Arrays.asList;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class TSBasTransformerTest {

    @Test
    public void testTransformation() throws Exception {
        List<String> testFiles = asList("ts-bas-max.xml", "valid-diabetes-typ2-kost.xml",
                "valid-korrigerad-synskarpa.xml", "valid-maximal.xml", "valid-minimal.xml",
                "valid-persontransport.xml", "valid-sjukhusvard.xml", "valid-utan-korrigerad-synskarpa.xml");

        XslTransformer transformer = new XslTransformer("xsl/transform-ts-bas.xsl");

        for (String xmlFile : testFiles) {
            String xmlContents = Resources.toString(getResource("scenarios/transport/" + xmlFile), Charsets.UTF_8);

            if (!validateAgainstXSD(xmlContents, "intygstjanster-services/core-components/se_intygstjanster_services_1.0.xsd")) {
                fail();
            }

            String result = transformer.transform(xmlContents);

            if (!validateAgainstXSD(result, "specializations/TS-Bas/ts-bas_model.xsd")) {
                fail();
            }
        }
    }

    private static boolean validateAgainstXSD(String xml, String xsdPath) {
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(Thread.currentThread().getContextClassLoader().getResource(xsdPath));
            schema.newValidator().validate(xmlSource);
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
