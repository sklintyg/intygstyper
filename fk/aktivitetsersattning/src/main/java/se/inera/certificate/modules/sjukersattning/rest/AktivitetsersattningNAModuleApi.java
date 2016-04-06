package se.inera.certificate.modules.sjukersattning.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
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
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public class AktivitetsersattningNAModuleApi implements ModuleApi{

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

    public Utlatande getUtlatandeFromIntyg(Intyg intyg) throws Exception {
        // TODO Auto-generated method stub
        return null;
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
