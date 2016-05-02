/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.ts_parent.integration;

import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponderService;

import com.google.common.base.Throwables;

public class SendTSClient {

    private static final String REGISTER_NAMESPACE = "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1:RegisterCertificate";

    private static final Logger LOGGER = LoggerFactory.getLogger(SendTSClient.class);

    private static final QName PORT_NAME =
            new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificate:1:rivtabp21", "RegisterCertificateResponderPort");

    private final RegisterCertificateResponderService service;

    private final MessageFactory messageFactory;
    private final DocumentBuilderFactory builderFactory;

    public SendTSClient(String url) {
        service = new RegisterCertificateResponderService();
        service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, url);
        try {
            messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
            builderFactory = DocumentBuilderFactory.newInstance();
        } catch (SOAPException e) {
            throw Throwables.propagate(e);
        }
    }

    public SOAPMessage registerCertificate(String message, String logicalAddress) {
        try {

            LOGGER.debug("Creating SoapMessage in sendTsBasClient");
            SOAPMessage soapReq1 = messageFactory.createMessage();
            soapReq1.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");

            SOAPEnvelope soapEnvelope = soapReq1.getSOAPPart().getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();
            SOAPHeader soapHeader = soapEnvelope.getHeader();
            SOAPElement address = soapHeader.addChildElement("To", "ns", "http://www.w3.org/2005/08/addressing");
            address.addTextNode(logicalAddress);

            // Create a Document from the message
            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(message)));
            soapBody.addDocument(doc);
            soapReq1.saveChanges();
            Dispatch<SOAPMessage> dispSOAPMsg = service.createDispatch(PORT_NAME, SOAPMessage.class, Service.Mode.MESSAGE);

            // Set the soap action
            dispSOAPMsg.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, true);
            dispSOAPMsg.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY, REGISTER_NAMESPACE);

            return dispSOAPMsg.invoke(soapReq1);

        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

}
