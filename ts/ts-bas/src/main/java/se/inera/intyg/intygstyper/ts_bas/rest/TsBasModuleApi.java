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
package se.inera.intyg.intygstyper.ts_bas.rest;

import java.io.StringReader;

import javax.xml.bind.JAXB;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.intygstyper.ts_bas.integration.RegisterTSBasResponderImpl;
import se.inera.intyg.intygstyper.ts_bas.model.converter.*;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_parent.integration.SendTSClient;
import se.inera.intyg.intygstyper.ts_parent.rest.TsParentModuleApi;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.*;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.*;
import se.inera.intygstjanster.ts.services.v1.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten, Mina-Intyg & Webcert).
 *
 */
public class TsBasModuleApi extends TsParentModuleApi<Utlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(TsBasModuleApi.class);

    @Autowired(required = false)
    private GetTSBasResponderInterface getTSBasResponderInterface;

    @Autowired(required = false)
    @Qualifier("registerTSBasClient")
    private RegisterTSBasResponderInterface registerTSBasResponderInterface;

    @Autowired(required = false)
    @Qualifier("tsBasSendCertificateClient")
    private SendTSClient sendTsBasClient;

    @Autowired(required = false)
    @Qualifier("tsBasXslTransformer")
    private XslTransformer xslTransformer;

    public TsBasModuleApi() {
        super(Utlatande.class);
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        RegisterTSBasType request;
        try {
            request = InternalToTransport.convert(getInternal(internalModel));
        } catch (ConverterException e) {
            LOG.error("Failed to convert to transport format during registerTSBas", e);
            throw new ExternalServiceCallException("Failed to convert to transport format during registerTSBas", e);
        }

        RegisterTSBasResponseType response = registerTSBasResponderInterface.registerTSBas(logicalAddress, request);

        // check whether call was successful or not
        if (response.getResultat().getResultCode() == ResultCodeType.INFO) {
            throw new ExternalServiceCallException(response.getResultat().getResultText(),
                    RegisterTSBasResponderImpl.CERTIFICATE_ALREADY_EXISTS.equals(response.getResultat().getResultText())
                            ? ErrorIdEnum.VALIDATION_ERROR
                            : ErrorIdEnum.APPLICATION_ERROR);
        } else if (response.getResultat().getResultCode() == ResultCodeType.ERROR) {
            throw new ExternalServiceCallException(response.getResultat().getErrorId() + " : " + response.getResultat().getResultText());
        }

    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        String transformedPayload = xslTransformer.transform(xmlBody);

        try {
            SOAPMessage response = sendTsBasClient.registerCertificate(transformedPayload, logicalAddress);
            SOAPEnvelope contents = response.getSOAPPart().getEnvelope();
            if (contents.getBody().hasFault()) {
                throw new ExternalServiceCallException(contents.getBody().getFault().getTextContent());
            }
        } catch (Exception e) {
            LOG.error("Error in sendCertificateToRecipient with msg: " + e.getMessage(), e);
            throw new ModuleException("Error in sendCertificateToRecipient.", e);
        }
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException {
        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(certificateId);

        GetTSBasResponseType response = getTSBasResponderInterface.getTSBas(logicalAddress, request);

        switch (response.getResultat().getResultCode()) {
        case INFO:
        case OK:
            return convert(response, false);
        case ERROR:
            switch (response.getResultat().getErrorId()) {
            case REVOKED:
                return convert(response, true);
            case VALIDATION_ERROR:
                throw new ModuleException("getTSBas WS call: VALIDATION_ERROR :" + response.getResultat().getResultText());
            default:
                throw new ModuleException("getTSBas WS call: ERROR :" + response.getResultat().getResultText());
            }
        default:
            throw new ModuleException("getTSBas WS call: ERROR :" + response.getResultat().getResultText());
        }
    }

    @Override
    public Utlatande getUtlatandeFromXml(String xml) throws ModuleException {
        RegisterTSBasType jaxbObject = JAXB.unmarshal(new StringReader(xml),
                RegisterTSBasType.class);
        try {
            return TransportToInternal.convert(jaxbObject.getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    private CertificateResponse convert(GetTSBasResponseType response, boolean revoked) throws ModuleException {
        try {
            Utlatande utlatande = TransportToInternal.convert(response.getIntyg());
            String internalModel = toInternalModelResponse(utlatande);

            CertificateMetaData metaData = TsBasMetaDataConverter.toCertificateMetaData(response.getMeta(), response.getIntyg());
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    @Override
    protected Intyg utlatandeToIntyg(Utlatande utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }
}
