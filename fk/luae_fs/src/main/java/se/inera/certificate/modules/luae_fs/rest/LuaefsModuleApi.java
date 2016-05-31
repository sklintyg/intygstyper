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

package se.inera.certificate.modules.luae_fs.rest;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.soap.SOAPFaultException;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.converter.InternalToRevoke;
import se.inera.certificate.modules.fkparent.model.internal.Diagnos;
import se.inera.certificate.modules.fkparent.model.validator.XmlValidator;
import se.inera.certificate.modules.luae_fs.model.converter.*;
import se.inera.certificate.modules.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luae_fs.validator.InternalDraftValidator;
import se.inera.intyg.common.support.common.util.StringUtil;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.*;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.ResultCodeType;

public class LuaefsModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(LuaefsModuleApi.class);

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    private InternalDraftValidator internalDraftValidator;

    @Autowired
    @Qualifier("luae_fs-objectMapper")
    private ObjectMapper objectMapper;

    private ModuleContainerApi moduleContainer;

    @Autowired(required = false)
    @Qualifier("registerCertificateClient")
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Autowired(required = false)
    private GetCertificateResponderInterface getCertificateResponderInterface;

    private RegisterCertificateValidator validator = new RegisterCertificateValidator("luae_fs.sch");

    @Autowired(required = false)
    private RevokeCertificateResponderInterface revokeCertificateClient;

    // - - - API - - -

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidateDraftResponse validateDraft(String internalModel) throws ModuleException {
        return internalDraftValidator.validateDraft(getInternal(internalModel));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin)
            throws ModuleException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        try {
            return toInternalModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public String createNewInternalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, String template)
            throws ModuleException {
        try {
            LuaefsUtlatande internal = getInternal(template);
            return toInternalModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public ModuleContainerApi getModuleContainer() {
        return moduleContainer;
    }

    @Override
    public void setModuleContainer(ModuleContainerApi moduleContainer) {
        this.moduleContainer = moduleContainer;
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        if (xmlBody == null || StringUtil.isNullOrEmpty(logicalAddress)) {
            throw new ModuleException("Request does not contain the original xml");
        }
        StringBuffer sb = new StringBuffer(xmlBody);
        RegisterCertificateType request = JAXB.unmarshal(new StreamSource(new StringReader(sb.toString())), RegisterCertificateType.class);

        sendCertificateToRecipient(request, logicalAddress);
    }

    private void sendCertificateToRecipient(RegisterCertificateType request, final String logicalAddress) throws ExternalServiceCallException {
        try {
            RegisterCertificateResponseType response = registerCertificateResponderInterface.registerCertificate(logicalAddress, request);

            if (response.getResult() != null && response.getResult().getResultCode() != ResultCodeType.OK) {
                String message = response.getResult().getResultText();
                LOG.error("Error occured when sending certificate '{}': {}",
                        request.getIntyg() != null ? request.getIntyg().getIntygsId() : null,
                        message);
                throw new ExternalServiceCallException(message);
            }
        } catch (SOAPFaultException e) {
            throw new ExternalServiceCallException(e);
        }
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException {
        GetCertificateType request = new GetCertificateType();
        request.setIntygsId(getIntygsId(certificateId));

        try {
            return convert(getCertificateResponderInterface.getCertificate(logicalAddress, request));
        } catch (SOAPFaultException e) {
            String error = String.format("Could not get certificate with id %s and type luae_fs from Intygstjansten. SOAPFault: %s",
                    certificateId, e.getMessage());
            LOG.error(error);
            throw new ModuleException(error);
        }
    }

    private IntygId getIntygsId(String certificateId) {
        IntygId intygId = new IntygId();
        intygId.setRoot("SE5565594230-B31");
        intygId.setExtension(certificateId);
        return intygId;
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        RegisterCertificateType request;
        try {
            request = InternalToTransport.convert(objectMapper.readValue(internalModel, LuaefsUtlatande.class));
        } catch (ConverterException | IOException e) {
            LOG.error("Failed to convert to transport format during registerCertificate", e);
            throw new ExternalServiceCallException("Failed to convert to transport format during registerCertificate", e);
        }

        RegisterCertificateResponseType response2 = registerCertificateResponderInterface.registerCertificate(logicalAddress, request);

        // check whether call was successful or not
        if (response2.getResult().getResultCode() != ResultCodeType.OK) {
            String message = response2.getResult().getResultCode() == ResultCodeType.INFO
                    ? response2.getResult().getResultText()
                    : response2.getResult().getErrorId() + " : " + response2.getResult().getResultText();
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public String updateBeforeSave(String internalModel, HoSPersonal hosPerson) throws ModuleException {
        return updateInternal(internalModel, hosPerson, null);
    }

    @Override
    public String updateBeforeSigning(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        return updateInternal(internalModel, hosPerson, signingDate);
    }

    @Override
    public boolean isModelChanged(String persistedState, String currentState) throws ModuleException {
        return false;
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws IOException {
        return objectMapper.readValue(utlatandeJson, LuaefsUtlatande.class);
    }

    @Override
    public Utlatande getUtlatandeFromXml(String xml) throws ModuleException {
        RegisterCertificateType jaxbObject = JAXB.unmarshal(new StringReader(xml),
                RegisterCertificateType.class);
        try {
            return TransportToInternal.convert(jaxbObject.getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    @Override
    public String transformToStatisticsService(String inputXml) throws ModuleException {
        return inputXml;
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        return XmlValidator.validate(validator, inputXml);
    }

    @Override
    public Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande) {
        return TransportToArendeApi.getModuleSpecificArendeParameters(utlatande);
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, String template)
            throws ModuleException {
        try {
            LuaefsUtlatande internal = getInternal(template);
            return toInternalModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public Intyg getIntygFromUtlatande(Utlatande utlatande) throws ModuleException {
        try {
            return UtlatandeToIntyg.convert((LuaefsUtlatande) utlatande);
        } catch (Exception e) {
            LOG.error("Could not get intyg from utlatande: {}", e.getMessage());
            throw new ModuleException("Could not get intyg from utlatande", e);
        }
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        // currently not used
        return null;
    }

    // - - - private scope - - -

    private CertificateResponse convert(GetCertificateResponseType response) throws ModuleException {
        try {
            LuaefsUtlatande utlatande = TransportToInternal.convert(response.getIntyg());
            String internalModel = objectMapper.writeValueAsString(utlatande);
            CertificateMetaData metaData = TransportToInternal.getMetaData(response.getIntyg());
            boolean revoked = response.getIntyg().getStatus().stream()
                    .anyMatch(status -> StatusKod.CANCEL.name().equals(status.getStatus().getCode()));
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    private LuaefsUtlatande getInternal(String internalModel)
            throws ModuleException {

        try {
            LuaefsUtlatande utlatande = objectMapper.readValue(internalModel,
                    LuaefsUtlatande.class);

            // Explicitly populate the giltighet interval since it is information derived from
            // the arbetsformaga but needs to be serialized into the Utkast model.
            // TODO
            // utlatande.setGiltighet(ArbetsformagaToGiltighet.getGiltighetFromUtlatande(utlatande));
            return utlatande;

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private String updateInternal(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        try {
            LuaefsUtlatande intyg = getInternal(internalModel);
            List<Diagnos> decoratedDiagnoser = intyg.getDiagnoser().stream()
                    .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                            moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
                    .collect(Collectors.toList());
            intyg = intyg.toBuilder().setDiagnoser(decoratedDiagnoser).build();
            webcertModelFactory.updateSkapadAv(intyg, hosPerson, signingDate);
            return toInternalModelResponse(intyg);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model", e);
        }
    }

    private String toInternalModelResponse(LuaefsUtlatande internalModel) throws ModuleException {
        try {
            return toJsonString(internalModel);
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    private String toJsonString(Object internalModel) throws IOException {
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, internalModel);
        return writer.toString();
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        StringBuffer sb = new StringBuffer(xmlBody);
        RevokeCertificateType request = JAXB.unmarshal(new StreamSource(new StringReader(sb.toString())), RevokeCertificateType.class);
        RevokeCertificateResponseType response = revokeCertificateClient.revokeCertificate(logicalAddress, request);
        if (!response.getResult().getResultCode().equals(ResultCodeType.OK)) {
            String message = "Could not send revoke to " + logicalAddress;
            LOG.error(message);
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public String createRevokeRequest(Utlatande utlatande, se.inera.intyg.common.support.model.common.internal.HoSPersonal skapatAv,
            String meddelande) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            JAXB.marshal(InternalToRevoke.convert(utlatande, skapatAv, meddelande), writer);
            return writer.toString();
        } catch (ConverterException e) {
            throw new ModuleException(e.getMessage());
        }
    }
}
