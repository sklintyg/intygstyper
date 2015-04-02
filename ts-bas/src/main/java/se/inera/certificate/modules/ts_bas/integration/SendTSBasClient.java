package se.inera.certificate.modules.ts_bas.integration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import se.inera.certificate.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponderService;

public class SendTSBasClient {

    private static final QName PORT_NAME =
            new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificate:1:rivtabp21", "RegisterCertificateResponderPort");

    private final RegisterCertificateResponderService service;

    private final MessageFactory messageFactory;

    public SendTSBasClient(String url) {
        service = new RegisterCertificateResponderService();
        service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, url);
        try {
            messageFactory = MessageFactory.newInstance();
        } catch (SOAPException e) {
            throw Throwables.propagate(e);
        }
    }

    public SOAPMessage registerCertificate(String message) {
        try {
            SOAPMessage soapReq1 = messageFactory.createMessage(null, new ByteArrayInputStream(message.getBytes(Charsets.UTF_8)));

            Dispatch<SOAPMessage> dispSOAPMsg = service.createDispatch(PORT_NAME, SOAPMessage.class, Service.Mode.MESSAGE);

            return dispSOAPMsg.invoke(soapReq1);

        } catch (SOAPException | IOException e) {
            throw Throwables.propagate(e);
        }
    }

}
