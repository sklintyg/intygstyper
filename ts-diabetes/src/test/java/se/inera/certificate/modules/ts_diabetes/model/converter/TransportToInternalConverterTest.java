package se.inera.certificate.modules.ts_diabetes.model.converter;

import static org.junit.Assert.*;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TransportToInternalConverterTest {

    private Utlatande expected;
    private TSDiabetesIntyg source;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new CustomObjectMapper();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("diabetes-test.json")) {
            expected = mapper.readValue(is, Utlatande.class);
        }
        
        JAXBContext ctx = JAXBContext.newInstance(TSDiabetesIntyg.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("diabetes-test.xml")) {
            JAXBElement<TSDiabetesIntyg> unmarshaled = unmarshaller.unmarshal(new StreamSource(is), TSDiabetesIntyg.class);
            source = unmarshaled.getValue();
        }        
    }

    @Test
    public void testConvert() {
        Utlatande actual = TransportToInternalConverter.convert(source);
        ReflectionAssert.assertLenientEquals(expected, actual);
    }

}
