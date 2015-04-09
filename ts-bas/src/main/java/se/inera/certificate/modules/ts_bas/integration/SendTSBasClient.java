package se.inera.certificate.modules.ts_bas.integration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import se.inera.certificate.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponderService;

import com.google.common.base.Throwables;

public class SendTSBasClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendTSBasClient.class);

    private static final QName PORT_NAME =
            new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificate:1:rivtabp21", "RegisterCertificateResponderPort");

    private final RegisterCertificateResponderService service;

    private final MessageFactory messageFactory;
    private final DocumentBuilderFactory builderFactory;

    public SendTSBasClient(String url) {
        service = new RegisterCertificateResponderService();
        service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, url);
        try {
            messageFactory = MessageFactory.newInstance();
            builderFactory = DocumentBuilderFactory.newInstance();
        } catch (SOAPException e) {
            throw Throwables.propagate(e);
        }
    }

    public SOAPMessage registerCertificate(String message) {
        try {
            LOGGER.debug("Creating SoapMessage in sendTsBasClient");
            SOAPMessage soapReq1 = messageFactory.createMessage();

            // Create a Document from the byte stream
            InputStream is = new ByteArrayInputStream(message.getBytes());
            builderFactory.setNamespaceAware(true);
            Document doc = builderFactory.newDocumentBuilder().parse(is); 
            
            // Add content to soap message body
            SOAPBody body = soapReq1.getSOAPBody();
            body.addDocument(doc);

            Dispatch<SOAPMessage> dispSOAPMsg = service.createDispatch(PORT_NAME, SOAPMessage.class, Service.Mode.MESSAGE);

            return dispSOAPMsg.invoke(soapReq1);

        } catch (SOAPException | SAXException | IOException | ParserConfigurationException e) {
            throw Throwables.propagate(e);
        }
    }

}
