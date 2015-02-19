package se.inera.certificate.modules.fk7263.model.converter;

import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.CertificateStatusUpdateForCareType;
import se.inera.certificate.modules.fk7263.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.notification.NotificationMessage;

public class InternalToNotification {

    private ConverterUtil converterUtil;

    public CertificateStatusUpdateForCareType createCertificateStatusUpdateForCareType(NotificationMessage notificationMessage) throws ModuleException {
        
        Utlatande utlatande = converterUtil.fromJsonString(notificationMessage.getUtkast());
        
        CertificateStatusUpdateForCareType c = new CertificateStatusUpdateForCareType();
        
        

        return c;
    }

}
