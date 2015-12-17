package se.inera.certificate.modules.sjukpenning.integration;

import java.io.ByteArrayInputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.helger.schematron.svrl.SVRLWriter;

@WebServiceProvider
@ServiceMode(value = Service.Mode.MESSAGE)
public class RegisterSjukpenningService implements Provider<Source> {

    RegisterSjukpenningValidator validator;

    public RegisterSjukpenningService() {
        try {
            validator = new RegisterSjukpenningValidator();
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }

    @Override
    public Source invoke(Source request) {
        DOMSource response = new DOMSource();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(System.out);
            transformer.transform(request, result);
            System.out.println("\n\n\n");

            SchematronOutputType valResult = validator.validateSchematron(request);
            System.out.println(SVRLWriter.createXMLString(valResult));

            String responseString = "<result xmlns=\"urn:riv:clinicalprocess:healthcond:certificate:2\"><resultCode>OK</resultCode></result>";
            ByteArrayInputStream is = new ByteArrayInputStream(responseString.getBytes());
            SOAPMessage greetMeResponse = MessageFactory.newInstance().createMessage(null, is);
            is.close();
            response.setNode(greetMeResponse.getSOAPBody().extractContentAsDocument());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

}
