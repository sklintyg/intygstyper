package se.inera.certificate.modules.sjukersattning.integration;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.sjukersattning.model.converter.TransportToInternal;
import se.inera.certificate.modules.sjukersattning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.rest.SjukersattningModuleApi;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

import com.helger.schematron.svrl.SVRLWriter;

@WebServiceProvider
// TODO: this should be Service.Mode.PAYLOAD
@ServiceMode(value = Service.Mode.MESSAGE)
public class RegisterSjukersattningService implements Provider<Source> {

    RegisterSjukersattningValidator validator;

    @Autowired
    private SjukersattningModuleApi moduleApi;

    @Autowired
    private ConverterUtil converterUtil;

    public RegisterSjukersattningService() {
        try {
            validator = new RegisterSjukersattningValidator();
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
            StringWriter writer = new StringWriter();
            transformer.transform(request, new StreamResult(writer));
            String output = writer.toString();
            System.out.println(output + "\n\n\n");

            SchematronOutputType valResult = validator.validateSchematron(request);
            System.out.println(SVRLWriter.createXMLString(valResult));

            // TODO: In line with JIRA SIT-40, converting to json is probably not necessary
            JAXBContext context = JAXBContext.newInstance(RegisterCertificateType.class);
            JAXBElement<RegisterCertificateType> registerMedicalCertificate = context.createUnmarshaller().
                    unmarshal(request, RegisterCertificateType.class);
            RegisterCertificateType requestObject = registerMedicalCertificate.getValue();
            SjukersattningUtlatande utlatande = TransportToInternal.convert(requestObject.getIntyg());

            CertificateHolder certificateHolder = converterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(output);

            moduleApi.getModuleContainer().certificateReceived(certificateHolder);
            String responseString = "<result xmlns=\"urn:riv:clinicalprocess:healthcond:certificate:2\"><resultCode>OK</resultCode></result>";
            ByteArrayInputStream is = new ByteArrayInputStream(responseString.getBytes());
            SOAPMessage returnMessage = MessageFactory.newInstance().createMessage(null, is);
            is.close();
            response.setNode(returnMessage.getSOAPBody().extractContentAsDocument());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

}
