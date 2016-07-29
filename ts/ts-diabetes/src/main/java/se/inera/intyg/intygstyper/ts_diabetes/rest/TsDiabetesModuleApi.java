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

package se.inera.intyg.intygstyper.ts_diabetes.rest;

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
import se.inera.intyg.intygstyper.ts_diabetes.integration.RegisterTSDiabetesResponderImpl;
import se.inera.intyg.intygstyper.ts_diabetes.model.converter.*;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_diabetes.util.TSDiabetesCertificateMetaTypeConverter;
import se.inera.intyg.intygstyper.ts_parent.integration.SendTSClient;
import se.inera.intyg.intygstyper.ts_parent.rest.TsParentModuleApi;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.*;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.*;
import se.inera.intygstjanster.ts.services.v1.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten and Mina-Intyg).
 *
 * @author Gustav Norbäcker, R2M
 */
public class TsDiabetesModuleApi extends TsParentModuleApi<Utlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(TsDiabetesModuleApi.class);

    @Autowired(required = false)
    @Qualifier("sendTsDiabetesClient")
    private SendTSClient sendTsDiabetesClient;

    @Autowired(required = false)
    @Qualifier("diabetesGetClient")
    private GetTSDiabetesResponderInterface diabetesGetClient;

    @Autowired(required = false)
    @Qualifier("diabetesRegisterClient")
    private RegisterTSDiabetesResponderInterface diabetesRegisterClient;

    @Autowired(required = false)
    @Qualifier("tsDiabetesXslTransformer")
    private XslTransformer xslTransformer;

    public TsDiabetesModuleApi() {
        super(Utlatande.class);
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        RegisterTSDiabetesType request = InternalToTransportConverter.convert(getInternal(internalModel));

        RegisterTSDiabetesResponseType response = diabetesRegisterClient.registerTSDiabetes(logicalAddress, request);

        // check whether call was successful or not
        if (response.getResultat().getResultCode() == ResultCodeType.INFO) {
            throw new ExternalServiceCallException(response.getResultat().getResultText(),
                    RegisterTSDiabetesResponderImpl.CERTIFICATE_ALREADY_EXISTS.equals(response.getResultat().getResultText())
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
            SOAPMessage response = sendTsDiabetesClient.registerCertificate(transformedPayload, logicalAddress);
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
        GetTSDiabetesType type = new GetTSDiabetesType();
        type.setIntygsId(certificateId);

        GetTSDiabetesResponseType diabetesResponseType = diabetesGetClient.getTSDiabetes(logicalAddress, type);

        switch (diabetesResponseType.getResultat().getResultCode()) {
        case INFO:
        case OK:
            return convert(diabetesResponseType, false);
        case ERROR:
            switch (diabetesResponseType.getResultat().getErrorId()) {
            case REVOKED:
                return convert(diabetesResponseType, true);
            case VALIDATION_ERROR:
                throw new ModuleException("getMedicalCertificateForCare WS call: VALIDATION_ERROR :"
                        + diabetesResponseType.getResultat().getResultText());
            default:
                throw new ModuleException("getMedicalCertificateForCare WS call: ERROR :" + diabetesResponseType.getResultat().getResultText());
            }
        default:
            throw new ModuleException("getMedicalCertificateForCare WS call: ERROR :" + diabetesResponseType.getResultat().getResultText());
        }
    }

    @Override
    public Utlatande getUtlatandeFromXml(String xml) throws ModuleException {
        RegisterTSDiabetesType jaxbObject = JAXB.unmarshal(new StringReader(xml),
                RegisterTSDiabetesType.class);
        try {
            return TransportToInternalConverter.convert(jaxbObject.getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    private CertificateResponse convert(GetTSDiabetesResponseType diabetesResponseType, boolean revoked) throws ModuleException {
        try {
            Utlatande utlatande = TransportToInternalConverter.convert(diabetesResponseType.getIntyg());
            String internalModel = toInternalModelResponse(utlatande);
            CertificateMetaData metaData = TSDiabetesCertificateMetaTypeConverter.toCertificateMetaData(diabetesResponseType.getMeta(),
                    diabetesResponseType.getIntyg());
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
