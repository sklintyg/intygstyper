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

package se.inera.intyg.intygstyper.ts_parent.integration.stub;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultType;
import se.riv.clinicalprocess.healthcond.certificate.v1.Utlatande;

import com.google.common.base.Throwables;

@WebServiceProvider
@ServiceMode(value = javax.xml.ws.Service.Mode.MESSAGE)
public final class RegisterCertificateResponderStub implements Provider<SOAPMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCertificateResponderStub.class);

    private boolean throwException = false;

    @Autowired
    private TSCertificateStore tsCertificatesStore;

    private MessageFactory messageFactory;

    private RegisterCertificateResponderStub() {
        try {
            messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        } catch (SOAPException e) {
            LOGGER.error("Failed to initialize RegisterCertificateResponderStub: {}", e.fillInStackTrace());
            throw Throwables.propagate(e);
        }
    }

    public RegisterCertificateResponseType registerCertificate(RegisterCertificateType request) {
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();

        if (throwException) {
            LOGGER.debug("Throwing fake exception");
            throw new RuntimeException();
        }

        Utlatande utlatande = request.getUtlatande();
        String id = utlatande.getUtlatandeId().getExtension();

        Map<String, String> props = new HashMap<>();
        props.put("Personnummer", utlatande.getPatient().getPersonId().getExtension());
        props.put("Makulerad", "NEJ");

        LOGGER.info("TS-STUB Received request");
        LOGGER.info("Request with id: {}", request.getUtlatande().getUtlatandeId().getExtension());
        tsCertificatesStore.addCertificate(id, props);
        response.setResult(okResult());
        return response;
    }

    private ResultType okResult() {
        ResultType result = new ResultType();
        result.setResultCode(ResultCodeType.OK);
        return result;
    }

    public boolean isThrowException() {
        return throwException;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

    @Override
    public SOAPMessage invoke(SOAPMessage request) {
        LOGGER.debug("Invoking response for incoming request in stub");
        javax.xml.soap.SOAPMessage response = null;
        SOAPBody soapBody = null;
        try {
            response = messageFactory.createMessage();
            soapBody = response.getSOAPPart().getEnvelope().getBody();

            SOAPElement result = soapBody.addChildElement(new QName("urn:riv:clinicalprocess:healthcond:certificate:1", "result"));

            if (request.getSOAPBody().hasFault()) {
                result.setTextContent("ERROR");
            } else {
                SOAPBody requestBody = request.getSOAPBody();
                Document doc = requestBody.extractContentAsDocument();
                Source source = new DOMSource(doc);
                StringWriter stringWriter = new StringWriter();
                Result streamResult = new StreamResult(stringWriter);
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                transformer.transform(source, streamResult);
                String xml = stringWriter.getBuffer().toString();
                InputStream is = new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8));

                RegisterCertificateType registerCertificateType = JAXB.unmarshal(is, RegisterCertificateType.class);
                registerCertificate(registerCertificateType);

                if (registerCertificateType != null) {
                    String id = registerCertificateType.getUtlatande().getUtlatandeId().getExtension();
                    LOGGER.debug("UtlatandeID received in stub: {}", id);
                }
                result.setTextContent("OK");
            }
            response.saveChanges();
        } catch (SOAPException e) {
            LOGGER.error("Error while invoking response in RegisterCertificateResponderStub: {}", e.fillInStackTrace());
            throw Throwables.propagate(e);
        } catch (TransformerException e) {
            LOGGER.error("Error while transforming response in RegisterCertificateResponderStub: {}", e.fillInStackTrace());
            throw Throwables.propagate(e);
        }
        return response;
    }

}
