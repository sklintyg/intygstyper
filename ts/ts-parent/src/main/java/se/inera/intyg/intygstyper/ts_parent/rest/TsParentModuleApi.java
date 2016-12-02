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

package se.inera.intyg.intygstyper.ts_parent.rest;

import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.INTYG_AVSER_SVAR_ID_1;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter.ModelConverter;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.*;
import se.inera.intyg.intygstyper.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.intygstyper.ts_parent.model.converter.WebcertModelFactory;
import se.inera.intyg.intygstyper.ts_parent.pdf.PdfGenerator;
import se.inera.intyg.intygstyper.ts_parent.pdf.PdfGeneratorException;
import se.inera.intyg.intygstyper.ts_parent.validator.InternalDraftValidator;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public abstract class TsParentModuleApi<T extends Utlatande> implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(TsParentModuleApi.class);

    @Autowired
    private ModelCompareUtil<T> modelCompareUtil;

    @Autowired
    private InternalDraftValidator<T> validator;

    @Autowired
    private PdfGenerator<T> pdfGenerator;

    @Autowired
    private WebcertModelFactory<T> webcertModelFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;

    private Class<T> type;

    public TsParentModuleApi(Class<T> type) {
        this.type = type;
    }

    @Override
    public ValidateDraftResponse validateDraft(String internalModel) throws ModuleException {
        return validator.validateDraft(getInternal(internalModel));
    }

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
            T internal = getInternal(template);
            return toInternalModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, String template)
          throws ModuleException {
        return createNewInternalFromTemplate(draftCopyHolder, template);
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
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        try {
            return new PdfResponse(pdfGenerator.generatePDF(getInternal(internalModel), applicationOrigin),
                    pdfGenerator.generatePdfFilename(getInternal(internalModel)));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate PDF for certificate!", e);
        }
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, List<String> optionalFields)
            throws ModuleException {
        throw new ModuleException("Feature not supported");
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws IOException {
        return objectMapper.readValue(utlatandeJson, type);
    }

    @Override
    public boolean shouldNotify(String persistedState, String currentState) throws ModuleException {
        T newUtlatande;
        newUtlatande = getInternal(currentState);

        return modelCompareUtil.isValidForNotification(newUtlatande);
    }

    @Override
    public String transformToStatisticsService(String inputXml) throws ModuleException {
        return inputXml;
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> getModuleSpecificArendeParameters(se.inera.intyg.common.support.model.common.internal.Utlatande utlatande, List<String> frageIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Intyg getIntygFromUtlatande(Utlatande utlatande) throws ModuleException {
        try {
            return utlatandeToIntyg(type.cast(utlatande));
        } catch (Exception e) {
            LOG.error("Could not get intyg from utlatande: {}", e.getMessage());
            throw new ModuleException("Could not get intyg from utlatande", e);
        }
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        List<CVType> types = new ArrayList<>();
        try {
            for (Svar svar : intyg.getSvar()) {
                if (INTYG_AVSER_SVAR_ID_1.equals(svar.getId())) {
                    for (Delsvar delsvar : svar.getDelsvar()) {
                        if (INTYG_AVSER_DELSVAR_ID_1.equals(delsvar.getId())) {
                            CVType cv = TransportConverterUtil.getCVSvarContent(delsvar);
                            if (cv != null) {
                                types.add(cv);
                            }
                        }
                    }
                }
            }
        } catch (ConverterException e) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: {}", intyg.getIntygsId().getExtension(), e.getMessage());
            return null;
        }

        if (types.isEmpty()) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: Found no types.", intyg.getIntygsId().getExtension());
            return null;
        }

        return types.stream()
                .map(cv -> IntygAvserKod.fromCode(cv.getCode()))
                .map(IntygAvserKod::name)
                .collect(Collectors.joining(", "));
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
    public String createRevokeRequest(Utlatande utlatande,
            se.inera.intyg.common.support.model.common.internal.HoSPersonal skapatAv, String meddelande) throws ModuleException {
        RevokeMedicalCertificateRequestType request = new RevokeMedicalCertificateRequestType();
        request.setRevoke(ModelConverter.buildRevokeTypeFromUtlatande(utlatande, meddelande));

        StringWriter writer = new StringWriter();
        JAXB.marshal(request, writer);
        return writer.toString();
    }

    protected abstract Intyg utlatandeToIntyg(T utlatande) throws ConverterException;

    protected T getInternal(String internalModel) throws ModuleException {
        try {
            return objectMapper.readValue(internalModel, type);
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    protected String toInternalModelResponse(Utlatande internalModel) throws ModuleException {
        try {
            return objectMapper.writeValueAsString(internalModel);
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    private String updateInternal(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        T utlatande = getInternal(internalModel);
        WebcertModelFactoryUtil.updateSkapadAv(utlatande, hosPerson, signingDate);
        return toInternalModelResponse(utlatande);
    }

    private String updateInternal(String internalModel, Patient patient) throws ModuleException {
        T utlatande = getInternal(internalModel);
        try {
            WebcertModelFactoryUtil.populateWithPatientInfo(utlatande.getGrundData(), patient);
        } catch (ConverterException e) {
            throw new ModuleException("Failed to update internal model with patient", e);
        }
        return toInternalModelResponse(utlatande);
    }
}
