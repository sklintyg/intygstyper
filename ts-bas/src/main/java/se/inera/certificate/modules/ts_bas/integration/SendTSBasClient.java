package se.inera.certificate.modules.ts_bas.integration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import se.inera.certificate.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponderService;

public class SendTSBasClient {

    public void sendStuff(byte[] bytes) {
        MessageFactory factory = null;
        try {
            QName portName = new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificate:1:rivtabp21",
                    "RegisterCertificateResponderPort");

            InputStream is1 = new ByteArrayInputStream(bytes);
            factory = MessageFactory.newInstance();
            SOAPMessage soapReq1 = factory.createMessage(null, is1);

            RegisterCertificateResponderService service = new RegisterCertificateResponderService();
            service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, "http://servername.com/foo");

            Dispatch<SOAPMessage> dispSOAPMsg = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);

            //SOAPMessage soapResp = dispSOAPMsg.invoke(soapReq1);

            //System.out.println("Response from server: " + soapResp.getSOAPBody().getTextContent());

        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
