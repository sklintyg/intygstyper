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

package se.inera.intyg.intygstyper.fk7263.rest;

import static se.inera.intyg.intygstyper.fk7263.integration.RegisterMedicalCertificateResponderImpl.CERTIFICATE_ALREADY_EXISTS;
import static se.inera.intyg.intygstyper.fk7263.model.converter.UtlatandeToIntyg.BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32;
import static se.inera.intyg.intygstyper.fk7263.model.converter.UtlatandeToIntyg.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.w3.wsaddressing10.AttributedURIType;

import com.fasterxml.jackson.databind.ObjectMapper;

import iso.v21090.dt.v1.CD;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.*;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.*;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter.ModelConverter;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.*;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.intygstyper.fk7263.model.converter.*;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.model.util.Fk7263ModelCompareUtil;
import se.inera.intyg.intygstyper.fk7263.pdf.*;
import se.inera.intyg.intygstyper.fk7263.schemas.clinicalprocess.healthcond.certificate.converter.ClinicalProcessCertificateMetaTypeConverter;
import se.inera.intyg.intygstyper.fk7263.validator.InternalDraftValidator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

/**
 * @author andreaskaltenbach, marced
 */
public class Fk7263ModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);

    private static final Comparator<? super DatePeriodType> PERIOD_START = Comparator.comparing(DatePeriodType::getStart);

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    private InternalDraftValidator internalDraftValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    @Qualifier("registerMedicalCertificateClient")
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient;

    @Autowired
    private Fk7263ModelCompareUtil modelCompareUtil;

    @Autowired(required = false)
    private GetMedicalCertificateForCareResponderInterface getMedicalCertificateForCareResponderInterface;

    @Autowired(required = false)
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;

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
        try {
            Utlatande intyg = getInternal(internalModel);
            PdfDefaultGenerator pdfGenerator = new PdfDefaultGenerator(intyg, statuses, applicationOrigin);
            return new PdfResponse(pdfGenerator.getBytes(), pdfGenerator.generatePdfFilename(false));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (standard copy) PDF for certificate!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, List<String> optionalFields)
            throws ModuleException {
        try {
            Utlatande intyg = getInternal(internalModel);
            PdfEmployeeGenerator pdfGenerator = new PdfEmployeeGenerator(intyg, statuses, applicationOrigin, optionalFields);
            return new PdfResponse(pdfGenerator.getBytes(), pdfGenerator.generatePdfFilename(true));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (employer copy) PDF for certificate!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        try {
            return toInteralModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder));

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public String createNewInternalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, String template)
            throws ModuleException {
        try {
            Utlatande internal = getInternal(template);
            return toInteralModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {

        // Check that we got any data at all
        if (xmlBody == null) {
            throw new ModuleException("No xml body found in call to sendCertificateToRecipient!");
        }

        if (logicalAddress == null) {
            throw new ModuleException("No LogicalAddress found in call to sendCertificateToRecipient!");
        }

        RegisterMedicalCertificateType request = JAXB.unmarshal(new StringReader(xmlBody), RegisterMedicalCertificateType.class);
        sendCertificateToRecipient(request, logicalAddress, recipientId);
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException {
        GetMedicalCertificateForCareRequestType request = new GetMedicalCertificateForCareRequestType();
        request.setCertificateId(certificateId);

        GetMedicalCertificateForCareResponseType response = getMedicalCertificateForCareResponderInterface
                .getMedicalCertificateForCare(logicalAddress, request);

        switch (response.getResult().getResultCode()) {
        case INFO:
        case OK:
            return convert(response, false);
        case ERROR:
            ErrorIdType errorId = response.getResult().getErrorId();
            String resultText = response.getResult().getResultText();
            switch (errorId) {
            case REVOKED:
                return convert(response, true);
            default:
                LOG.error("Error of type {} occured when retrieving certificate '{}': {}", errorId, certificateId, resultText);
                throw new ModuleException("Error of type " + errorId + " occured when retrieving certificate " + certificateId + ", " + resultText);
            }
        default:
            LOG.error("An unidentified error occured when retrieving certificate '{}': {}", certificateId, response.getResult().getResultText());
            throw new ModuleException(
                    "An unidentified error occured when retrieving certificate " + certificateId + ", " + response.getResult().getResultText());
        }
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        // Check that we got any data at all
        if (internalModel == null) {
            throw new ModuleException("No internal model found in call to sendCertificateToRecipient!");
        }

        if (logicalAddress == null) {
            throw new ModuleException("No LogicalAddress found in call to sendCertificateToRecipient!");
        }
        RegisterMedicalCertificateType request;
        try {
            request = InternalToTransport.getJaxbObject(getUtlatandeFromJson(internalModel));
        } catch (IOException | ConverterException e) {
            throw new ModuleException(e.getMessage());
        }
        sendCertificateToRecipient(request, logicalAddress, null);
    }

    @Override
    public String transformToStatisticsService(String inputXml) throws ModuleException {
        Utlatande utlatande = getUtlatandeFromXml(inputXml);
        Intyg intyg = getIntygFromUtlatande(utlatande);
        RegisterCertificateType type = new RegisterCertificateType();
        type.setIntyg(intyg);
        StringWriter writer = new StringWriter();
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class, Boolean.class);
            jaxbContext.createMarshaller().marshal(wrapJaxb(type), writer);
        } catch (JAXBException e) {
            LOG.debug("Could not create JAXB context for conversion from fk7263 ti fk7263-sit.", e);
        }
        return writer.toString();
    }

    private JAXBElement<?> wrapJaxb(RegisterCertificateType ws) {
        JAXBElement<?> jaxbElement = new JAXBElement<>(
                new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2", "RegisterCertificate"),
                RegisterCertificateType.class, ws);
        return jaxbElement;
    }

    @Override
    public String updateBeforeSave(String internalModel, HoSPersonal hosPerson) throws ModuleException {
        return updateInternal(internalModel, hosPerson, null);
    }

    @Override
    public String updateBeforeSave(String internalModel, Patient patient) throws ModuleException {
        return updateInternal(internalModel, patient);
    }

    @Override
    public String updateBeforeSigning(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        return updateInternal(internalModel, hosPerson, signingDate);
    }

    @Override
    public boolean shouldNotify(String persistedState, String currentState) throws ModuleException {
        Utlatande oldUtlatande;
        Utlatande newUtlatande;

        try {
            oldUtlatande = objectMapper.readValue(persistedState, Utlatande.class);
            newUtlatande = objectMapper.readValue(currentState, Utlatande.class);
        } catch (IOException e) {
            throw new ModuleException(e);
        }

        return modelCompareUtil.modelDiffers(oldUtlatande, newUtlatande);
    }

    /*
     * (non-Javadoc)
     *
     * Hard code code system name to ICD-10.
     *
     * This is a special case to solve JIRA issue https://inera-certificate.atlassian.net/browse/WEBCERT-1442.
     * It should be removed when Forsakringskassan can handle code system name correctly.
     */
    RegisterMedicalCertificateType whenFkIsRecipientThenSetCodeSystemToICD10(final RegisterMedicalCertificateType request) throws ModuleException {

        LOG.debug("Recipient of RegisterMedicalCertificate certificate is Försäkringskassan");
        LOG.debug("Set element 'lakarutlatande/medicinsktTillstand/tillstandsKod/codeSystemName' to value 'ICD-10'");

        // Check that we got a lakarutlatande element
        if (request.getLakarutlatande() == null) {
            throw new ModuleException("No Lakarutlatande element found in request data!");
        }

        LakarutlatandeType lakarutlatande = request.getLakarutlatande();

        // Decide if this certificate has smittskydd checked
        boolean inSmittskydd = findAktivitetWithCode(request.getLakarutlatande().getAktivitet(),
                Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA) != null ? true : false;

        if (!inSmittskydd) {
            // Check that we got a medicinsktTillstand element
            if (lakarutlatande.getMedicinsktTillstand() == null) {
                throw new ModuleException("No medicinsktTillstand element found in request data. Cannot set codeSystemName to 'ICD-10'!");
            }

            MedicinsktTillstandType medicinsktTillstand = lakarutlatande.getMedicinsktTillstand();

            // Check that we got a tillstandskod element
            if (medicinsktTillstand.getTillstandskod() == null) {
                throw new ModuleException("No tillstandskod element found in request data. Cannot set codeSystemName to 'ICD-10'!");
            }

            CD tillstandskod = medicinsktTillstand.getTillstandskod();
            tillstandskod.setCodeSystemName(Diagnoskodverk.ICD_10_SE.getCodeSystemName());

            // Update request
            request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(tillstandskod);

        } else {
            try {
                // tillstandskod is not mandatory when smittskydd is true, just try to set it.
                request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().setCodeSystemName(Diagnoskodverk.ICD_10_SE.getCodeSystemName());

            } catch (NullPointerException npe) {
                LOG.debug("No tillstandskod element found in request data. "
                        + "Element is not mandatory when Smittskydd is checked. "
                        + "Cannot set codeSystemName to 'ICD-10'");
            }
        }

        return request;
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws IOException {
        return objectMapper.readValue(utlatandeJson, Utlatande.class);
    }

    @Override
    public Utlatande getUtlatandeFromXml(String xml) throws ModuleException {
        RegisterMedicalCertificateType jaxbObject = JAXB.unmarshal(new StringReader(xml),
                RegisterMedicalCertificateType.class);
        try {
            return TransportToInternal.convert(jaxbObject.getLakarutlatande());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    private CertificateResponse convert(GetMedicalCertificateForCareResponseType response, boolean revoked) throws ModuleException {
        try {
            Utlatande utlatande = TransportToInternal.convert(response.getLakarutlatande());
            String internalModel = objectMapper.writeValueAsString(utlatande);
            CertificateMetaData metaData = ClinicalProcessCertificateMetaTypeConverter.toCertificateMetaData(response.getMeta());
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    private AktivitetType findAktivitetWithCode(List<AktivitetType> aktiviteter, Aktivitetskod aktivitetskod) throws ModuleException {
        AktivitetType foundAktivitet = null;

        try {
            if (aktiviteter != null) {
                for (int i = 0; i < aktiviteter.size(); i++) {
                    AktivitetType listAktivitet = aktiviteter.get(i);
                    if (listAktivitet.getAktivitetskod() != null && listAktivitet.getAktivitetskod().compareTo(aktivitetskod) == 0) {
                        foundAktivitet = listAktivitet;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new ModuleException(e.getMessage(), e);
        }

        return foundAktivitet;
    }

    private void sendCertificateToRecipient(RegisterMedicalCertificateType originalRequest, final String logicalAddress, final String recipientId)
            throws ModuleException {
        RegisterMedicalCertificateType request = originalRequest;
        // This is a special case when recipient is Forsakringskassan. See JIRA issue WEBCERT-1442.
        if (!StringUtils.isEmpty(recipientId) && recipientId.equalsIgnoreCase(PartKod.FKASSA.getValue())) {
            request = whenFkIsRecipientThenSetCodeSystemToICD10(request);
        }

        AttributedURIType address = new AttributedURIType();
        address.setValue(logicalAddress);

        try {
            RegisterMedicalCertificateResponseType response = registerMedicalCertificateClient.registerMedicalCertificate(address, request);

            // check whether call was successful or not
            if (response.getResult().getResultCode() != ResultCodeEnum.OK) {
                boolean info = response.getResult().getResultCode() == ResultCodeEnum.INFO;

                // This monstrosity is required because we want to handle when the certificate already exists in
                // Intygstjänsten. When this happens Intygstjänsten will return an INFO result with a specified
                // InfoText. To make sure we do not try to resend this request we need to throw an exception with
                // ErrorIdEnum of ValidationError.
                if (recipientId == null && info && CERTIFICATE_ALREADY_EXISTS.equals(response.getResult().getInfoText())) {
                    LOG.warn("Tried to register certificate ({}) which already exist in Intygstjänsten",
                            request.getLakarutlatande().getLakarutlatandeId());
                    throw new ExternalServiceCallException(response.getResult().getInfoText(), ErrorIdEnum.VALIDATION_ERROR);
                } else {
                    String message = info
                            ? response.getResult().getInfoText()
                            : response.getResult().getErrorId() + " : " + response.getResult().getErrorText();
                    LOG.error("Error occured when sending certificate '{}': {}",
                            request.getLakarutlatande() != null ? request.getLakarutlatande().getLakarutlatandeId() : null,
                            message);
                    throw new ExternalServiceCallException(message);
                }
            }
        } catch (SOAPFaultException e) {
            throw new ExternalServiceCallException(e);
        }

    }

    // - - - - - Private transformation methods for building responses - - - - - //

    private se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande getInternal(String internalModel)
            throws ModuleException {

        try {
            se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande utlatande = objectMapper.readValue(internalModel,
                    se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande.class);

            // Explicitly populate the giltighet interval since it is information derived from
            // the arbetsformaga but needs to be serialized into the Utkast model.
            utlatande.setGiltighet(ArbetsformagaToGiltighet.getGiltighetFromUtlatande(utlatande));
            return utlatande;

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private String updateInternal(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        try {
            Utlatande intyg = getInternal(internalModel);
            WebcertModelFactoryUtil.updateSkapadAv(intyg, hosPerson, signingDate);
            return toInteralModelResponse(intyg);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model skapadAv", e);
        }
    }

    private String updateInternal(String internalModel, Patient patient)
            throws ModuleException {
        try {
            Utlatande intyg = getInternal(internalModel);
            WebcertModelFactoryUtil.populateWithPatientInfo(intyg.getGrundData(), patient);
            return toInteralModelResponse(intyg);
        } catch (ModuleException | ConverterException e) {
            throw new ModuleException("Error while updating internal model with patient", e);
        }
    }

    private String toInteralModelResponse(
            se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return writer.toString();
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> getModuleSpecificArendeParameters(se.inera.intyg.common.support.model.common.internal.Utlatande utlatande,
            List<String> frageIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, String internalModelHolder)
            throws ModuleException {
        try {
            Utlatande internal = getInternal(internalModelHolder);
            internal.setKontaktMedFk(false);
            internal.setNedsattMed100(null);
            internal.setNedsattMed25(null);
            internal.setNedsattMed50(null);
            internal.setNedsattMed75(null);
            internal.setNedsattMed25Beskrivning(null);
            internal.setNedsattMed50Beskrivning(null);
            internal.setNedsattMed75Beskrivning(null);
            internal.setTjanstgoringstid(null);
            internal.setTelefonkontaktMedPatienten(null);
            internal.setUndersokningAvPatienten(null);
            internal.setJournaluppgifter(null);
            internal.setAnnanReferens(null);
            internal.setAnnanReferensBeskrivning(null);
            se.inera.intyg.common.support.model.common.internal.Relation relation = draftCopyHolder.getRelation();
            relation.setSistaGiltighetsDatum(internal.getGiltighet().getTom());
            draftCopyHolder.setRelation(relation);

            return toInteralModelResponse(webcertModelFactory.createCopy(draftCopyHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public Intyg getIntygFromUtlatande(se.inera.intyg.common.support.model.common.internal.Utlatande utlatande) throws ModuleException {
        try {
            return UtlatandeToIntyg.convert((Utlatande) utlatande);
        } catch (Exception e) {
            LOG.error("Could not get intyg from utlatande: {}", e.getMessage());
            throw new ModuleException("Could not get intyg from utlatande", e);
        }
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        List<DatePeriodType> periods = new ArrayList<>();
        try {
            for (Svar svar : intyg.getSvar()) {
                if (BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32.equals(svar.getId())) {
                    for (Delsvar delsvar : svar.getDelsvar()) {
                        if (BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32.equals(delsvar.getId())) {
                            DatePeriodType dtp = TransportConverterUtil.getDatePeriodTypeContent(delsvar);
                            if (dtp != null) {
                                periods.add(dtp);
                            }
                        }
                    }
                }
            }
        } catch (ConverterException e) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: {}", intyg.getIntygsId().getExtension(), e.getMessage());
            return null;
        }

        if (periods.isEmpty()) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: Found no periods.", intyg.getIntygsId().getExtension());
            return null;
        }

        Collections.sort(periods, PERIOD_START);

        return periods.get(0).getStart().toString() + " - " + periods.get(periods.size() - 1).getEnd().toString();
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        AttributedURIType uri = new AttributedURIType();
        uri.setValue(logicalAddress);

        StringBuffer sb = new StringBuffer(xmlBody);
        RevokeMedicalCertificateRequestType request = JAXB.unmarshal(new StreamSource(new StringReader(sb.toString())),
                RevokeMedicalCertificateRequestType.class);
        RevokeMedicalCertificateResponseType response = revokeCertificateClient.revokeMedicalCertificate(uri, request);
        if (!response.getResult().getResultCode().equals(ResultCodeEnum.OK)) {
            String message = "Could not send revoke to " + logicalAddress;
            LOG.error(message);
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public String createRevokeRequest(se.inera.intyg.common.support.model.common.internal.Utlatande utlatande,
            se.inera.intyg.common.support.model.common.internal.HoSPersonal skapatAv, String meddelande) throws ModuleException {
        RevokeMedicalCertificateRequestType request = new RevokeMedicalCertificateRequestType();
        request.setRevoke(ModelConverter.buildRevokeTypeFromUtlatande(utlatande, meddelande));

        StringWriter writer = new StringWriter();
        JAXB.marshal(request, writer);
        return writer.toString();
    }
}
