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
package se.inera.certificate.modules.luae_na.rest;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import se.inera.certificate.modules.luae_na.model.converter.InternalToNotification;
import se.inera.certificate.modules.luae_na.model.converter.InternalToTransport;
import se.inera.certificate.modules.luae_na.model.converter.TransportToInternal;
import se.inera.certificate.modules.luae_na.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.luae_na.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.luae_na.model.internal.AktivitetsersattningNAUtlatande;
import se.inera.certificate.modules.luae_na.validator.InternalDraftValidator;
import se.inera.intyg.common.support.common.util.StringUtil;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal;
import se.inera.intyg.common.support.modules.support.api.dto.InternalModelHolder;
import se.inera.intyg.common.support.modules.support.api.dto.InternalModelResponse;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.modules.support.api.notification.NotificationMessage;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.ResultCodeType;

public class AktivitetsersattningNAModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(AktivitetsersattningNAModuleApi.class);

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    private InternalDraftValidator internalDraftValidator;

    @Autowired
    private ConverterUtil converterUtil;

    @Autowired
    @Qualifier("luae_na-objectMapper")
    private ObjectMapper objectMapper;

    private ModuleContainerApi moduleContainer;

    @Autowired(required = false)
    @Qualifier("registerCertificateClient")
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Autowired(required = false)
    private GetCertificateResponderInterface getCertificateResponderInterface;

    private RegisterCertificateValidator validator = new RegisterCertificateValidator("luae_na.sch");

    @Autowired
    private InternalToNotification internalToNotification;

    @Autowired(required = false)
    private RevokeCertificateResponderInterface revokeCertificateClient;

    @Override
    public void setModuleContainer(ModuleContainerApi moduleContainer) {
        this.moduleContainer = moduleContainer;
    }

    @Override
    public ModuleContainerApi getModuleContainer() {
        return this.moduleContainer;
    }

    @Override
    public ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException {
        return internalDraftValidator.validateDraft(getInternal(internalModel));
    }

    private AktivitetsersattningNAUtlatande getInternal(InternalModelHolder internalModel)
            throws ModuleException {

        try {
            AktivitetsersattningNAUtlatande utlatande = objectMapper.readValue(internalModel.getInternalModel(),
                    AktivitetsersattningNAUtlatande.class);

            // Explicitly populate the giltighet interval since it is information derived from
            // the arbetsformaga but needs to be serialized into the Utkast model.
            // TODO
            // utlatande.setGiltighet(ArbetsformagaToGiltighet.getGiltighetFromUtlatande(utlatande));
            return utlatande;

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    @Override
    public PdfResponse pdf(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PdfResponse pdfEmployer(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin)
            throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        try {
            return toInternalModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    private InternalModelResponse toInternalModelResponse(AktivitetsersattningNAUtlatande internalModel) throws ModuleException {
        try {
            return new InternalModelResponse(toJsonString(internalModel));
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
    public InternalModelResponse createNewInternalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, InternalModelHolder template)
            throws ModuleException {
        try {
            AktivitetsersattningNAUtlatande internal = getInternal(template);
            return toInternalModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public void registerCertificate(InternalModelHolder internalModel, String logicalAddress) throws ModuleException {
        RegisterCertificateType request;
        try {
            request = InternalToTransport.convert(converterUtil.fromJsonString(internalModel.getInternalModel()));
        } catch (ConverterException e) {
            LOG.error("Failed to convert to transport format during registerAktivitetsErsattningNA", e);
            throw new ExternalServiceCallException("Failed to convert to transport format during registerAktivitetsErsattningNA", e);
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
    public void sendCertificateToRecipient(InternalModelHolder internalModel, String logicalAddress, String recipientId) throws ModuleException {
        if (internalModel == null || internalModel.getXmlModel() == null || StringUtil.isNullOrEmpty(logicalAddress)) {
            throw new ModuleException("Internal model does not contain the original xml");
        }
        StringBuffer sb = new StringBuffer(internalModel.getXmlModel());
        RegisterCertificateType request = JAXB.unmarshal(new StreamSource(new StringReader(sb.toString())), RegisterCertificateType.class);

        sendCertificateToRecipient(request, logicalAddress);
    }

    private IntygId getIntygsId(String certificateId) {
        IntygId intygId = new IntygId();
        intygId.setRoot("SE5565594230-B31");
        intygId.setExtension(certificateId);
        return intygId;
    }

    private CertificateResponse convert(GetCertificateResponseType response) throws ModuleException {
        try {
            AktivitetsersattningNAUtlatande utlatande = TransportToInternal.convert(response.getIntyg());
            String internalModel = objectMapper.writeValueAsString(utlatande);
            CertificateMetaData metaData = TransportToInternal.getMetaData(response.getIntyg());
            boolean revoked = response.getIntyg().getStatus().stream()
                    .anyMatch(status -> StatusKod.CANCEL.name().equals(status.getStatus().getCode()));
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException {
        GetCertificateType request = new GetCertificateType();
        request.setIntygsId(getIntygsId(certificateId));

        try {
            return convert(getCertificateResponderInterface.getCertificate(logicalAddress, request));
        } catch (SOAPFaultException e) {
            String error = String.format("Could not get certificate with id %s and type LUAE_NA from Intygstjansten. SOAPFault: %s",
                    certificateId, e.getMessage());
            LOG.error(error);
            throw new ModuleException(error);
        }
    }

    @Override
    public boolean isModelChanged(String persistedState, String currentState) throws ModuleException {
        return false;
    }

    private InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        try {
            AktivitetsersattningNAUtlatande intyg = getInternal(internalModel);
            webcertModelFactory.updateSkapadAv(intyg, hosPerson, signingDate);
            return toInternalModelResponse(intyg);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model", e);
        }
    }

    @Override
    public InternalModelResponse updateBeforeSave(InternalModelHolder internalModel, HoSPersonal hosPerson) throws ModuleException {
        return updateInternal(internalModel, hosPerson, null);
    }

    @Override
    public InternalModelResponse updateBeforeSigning(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        return updateInternal(internalModel, hosPerson, signingDate);
    }

    @Override
    public Object createNotification(NotificationMessage notificationMessage) throws ModuleException {
        return internalToNotification.createCertificateStatusUpdateForCareType(notificationMessage);
    }

    @Override
    public String marshall(String jsonString) throws ModuleException {
        String xmlString = null;
        try {
            AktivitetsersattningNAUtlatande internal = objectMapper.readValue(jsonString, AktivitetsersattningNAUtlatande.class);
            RegisterCertificateType external = InternalToTransport.convert(internal);
            StringWriter writer = new StringWriter();
            JAXB.marshal(external, writer);
            xmlString = writer.toString();

        } catch (IOException | ConverterException e) {
            LOG.error("Error occured while marshalling: {}", e.getStackTrace().toString());
            throw new ModuleException(e);
        }
        return xmlString;
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws IOException {
        return objectMapper.readValue(utlatandeJson, AktivitetsersattningNAUtlatande.class);
    }

    @Override
    public Utlatande getUtlatandeFromIntyg(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);

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
    public String decorateUtlatande(String utlatandeJson) throws ModuleException {
        AktivitetsersattningNAUtlatande utlatande;
        try {
            utlatande = objectMapper.readValue(utlatandeJson,
                    AktivitetsersattningNAUtlatande.class);
            List<Diagnos> decoratedDiagnoser = new ArrayList<>();
            for (Diagnos diagnos : utlatande.getDiagnoser()) {
                String klartext = moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem());
                Diagnos decoratedDiagnos = Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                        klartext);
                decoratedDiagnoser.add(decoratedDiagnos);
            }
            AktivitetsersattningNAUtlatande result = utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
            return objectMapper.writeValueAsString(result);
        } catch (IOException e) {
            LOG.error("Failed to decorate Utlatande with diagnose descriptions.", e);
            throw new ModuleException("Could not convert json string into an Utlatande object. ");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see se.inera.intyg.common.support.modules.support.api.ModuleApi#createRenewalFromTemplate(se.inera.intyg.common.
     * support.modules.support.api.dto.CreateDraftCopyHolder,
     * se.inera.intyg.common.support.modules.support.api.dto.InternalModelHolder)
     */
    @Override
    public InternalModelResponse createRenewalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, InternalModelHolder template)
            throws ModuleException {
        try {
            AktivitetsersattningNAUtlatande internal = getInternal(template);
            return toInternalModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public Intyg getIntygFromCertificateHolder(CertificateHolder certificateHolder) throws ModuleException {
        try {
            RegisterCertificateType jaxbObject = JAXB.unmarshal(new StringReader(certificateHolder.getOriginalCertificate()),
                    RegisterCertificateType.class);
            return jaxbObject.getIntyg();
        } catch (Exception e) {
            LOG.error("Could not get intyg from certificate holder. {}", e);
            throw new ModuleException("Could not get intyg from certificate holder", e);
        }
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        // currently not used
        return null;
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
            String meddelande)
            throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            JAXB.marshal(InternalToRevoke.convert(utlatande, skapatAv, meddelande), writer);
            return writer.toString();
        } catch (ConverterException e) {
            throw new ModuleException(e.getMessage());
        }
    }
}
