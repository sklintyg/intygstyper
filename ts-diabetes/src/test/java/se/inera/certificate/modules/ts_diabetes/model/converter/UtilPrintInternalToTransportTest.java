package se.inera.certificate.modules.ts_diabetes.model.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.junit.Test;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UtilPrintInternalToTransportTest {
    
    @Test
    public void test() throws IOException, JAXBException{
        ObjectMapper mapper = new CustomObjectMapper();
        
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("scenarios/internal/diabetes-fel1.json")){
            Utlatande utlatande = mapper.readValue(is, Utlatande.class);
            
            TSDiabetesIntyg convert = InternalToTransportConverter.convert(utlatande);
            
            print(convert);
        }
    }

    private void print(TSDiabetesIntyg convert) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(TSDiabetesIntyg.class);
        Marshaller marshaller = ctx.createMarshaller();

        QName qName = new QName("urn:local:se:intygstjanster:services:1", "TSDiabetesIntyg");
        JAXBElement<TSDiabetesIntyg> t = new JAXBElement<TSDiabetesIntyg>(qName, TSDiabetesIntyg.class, convert);

        StringWriter wr = new StringWriter();
        marshaller.marshal(t, wr);

        System.out.println(wr.toString());
    }
}
