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
package se.inera.certificate.modules.sjukersattning.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.pattern.ConverterUtil;
import se.inera.certificate.modules.aktivitetsersattningna.model.converter.InternalToNotification;
import se.inera.certificate.modules.aktivitetsersattningna.model.converter.TransportToInternal;
import se.inera.certificate.modules.aktivitetsersattningna.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.aktivitetsersattningna.validator.InternalDraftValidator;
import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal;
import se.inera.intyg.common.support.modules.support.api.dto.InternalModelHolder;
import se.inera.intyg.common.support.modules.support.api.dto.InternalModelResponse;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.notification.NotificationMessage;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public class AktivitetsersattningNAModuleApi implements ModuleApi{


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
    @Qualifier("aktivitetsersattning-objectMapper")
    private ObjectMapper objectMapper;

    private ModuleContainerApi moduleContainer;

    @Autowired(required = false)
    @Qualifier("registerCertificateClient")
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Autowired(required = false)
    private GetCertificateResponderInterface getCertificateResponderInterface;

    private RegisterCertificateValidator validator = new RegisterCertificateValidator("aktivititetsersattning-na.sch");

    @Autowired
    private InternalToNotification internalToNotification;

    public void setModuleContainer(ModuleContainerApi moduleContainer) {
        // TODO Auto-generated method stub

    }

    public ModuleContainerApi getModuleContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    public ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public PdfResponse pdf(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public PdfResponse pdfEmployer(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin)
            throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public InternalModelResponse createNewInternalFromTemplate(CreateDraftCopyHolder draftCopyHolder, InternalModelHolder template)
            throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public void registerCertificate(InternalModelHolder internalModel, String logicalAddress) throws ModuleException {
        // TODO Auto-generated method stub

    }

    public void sendCertificateToRecipient(InternalModelHolder internalModel, String logicalAddress, String recipientId) throws ModuleException {
        // TODO Auto-generated method stub

    }

    public CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isModelChanged(String persistedState, String currentState) throws ModuleException {
        // TODO Auto-generated method stub
        return false;
    }

    public InternalModelResponse updateBeforeSave(InternalModelHolder internalModel, HoSPersonal hosPerson) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public InternalModelResponse updateBeforeSigning(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createNotification(NotificationMessage notificationMessage) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public String marshall(String jsonString) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public Utlatande getUtlatandeFromIntyg(Intyg intyg) throws ConverterException{
        return TransportToInternal.convert(intyg);

    }

    public String transformToStatisticsService(String inputXml) throws ModuleException {
        // TODO Auto-generated method stub
        return inputXml;
    }

    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande) {
        // TODO Auto-generated method stub
        return null;
    }

    public String decorateUtlatande(String utlatandeJson) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

}
