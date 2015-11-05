package se.inera.certificate.modules.sjukersattning.integration;

import org.springframework.beans.factory.annotation.Autowired;
import riv.clinicalprocess.healthcond.certificate.getcertificateresponder._1.GetCertificateType;
import se.inera.certificate.modules.sjukersattning.rest.SjukersattningModuleApi;
import se.inera.certificate.modules.support.api.CertificateHolder;

import java.io.ByteArrayInputStream;

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

@WebServiceProvider
// TODO: this should be Service.Mode.PAYLOAD
@ServiceMode(value = Service.Mode.MESSAGE)
public class GetSjukersattningService implements Provider<Source> {

    @Autowired
    private SjukersattningModuleApi moduleApi;

    @Override
    public Source invoke(Source request) {
        DOMSource response = new DOMSource();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(System.out);
            transformer.transform(request, result);
            System.out.println("\n\n\n");

            JAXBContext context = JAXBContext.newInstance(GetCertificateType.class);
            JAXBElement<GetCertificateType> getCertificateRequest = context.createUnmarshaller().
                    unmarshal(request, GetCertificateType.class);
            GetCertificateType requestObject = getCertificateRequest.getValue();
            String certificateId = requestObject.getIntygsId().getRoot();

            CertificateHolder certificate = moduleApi.getModuleContainer().getCertificate(certificateId, null, false);


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
