package se.inera.certificate.modules.ts_diabetes.util;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Status;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;
import se.inera.intygstjanster.ts.services.v1.IntygMeta;
import se.inera.intygstjanster.ts.services.v1.IntygStatus;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class TSDiabetesCertificateMetaTypeConverter {

    public static CertificateMetaData toCertificateMetaData(IntygMeta intygMeta, TSDiabetesIntyg tsDiabetesIntyg) {
        CertificateMetaData metaData = new CertificateMetaData();
        metaData.setCertificateId(tsDiabetesIntyg.getIntygsId());
        metaData.setCertificateType(tsDiabetesIntyg.getIntygsTyp());
        //metaData.setValidFrom(intygMeta.getValidFrom());
        //metaData.setValidTo(intygMeta.getValidTo());
        metaData.setIssuerName(tsDiabetesIntyg.getGrundData().getSkapadAv().getFullstandigtNamn());
        metaData.setFacilityName(tsDiabetesIntyg.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
        metaData.setSignDate(LocalDateTime.parse(tsDiabetesIntyg.getGrundData().getSigneringsTidstampel()));
        metaData.setAdditionalInfo(intygMeta.getAdditionalInfo());
        metaData.setAvailable(intygMeta.getAvailable().toLowerCase().equals("true"));
        List<Status> statuses = toStatusList(intygMeta.getStatus());
        metaData.setStatus(statuses);
        return metaData;
    }
    
    public static List<Status> toStatusList(List<IntygStatus> certificateStatuses) {
        List<Status> statuses = new ArrayList<>(certificateStatuses.size());
        if (certificateStatuses != null) {
            for (IntygStatus certificateStatus : certificateStatuses) {
                if (certificateStatus != null) {
                    statuses.add(toStatus(certificateStatus));
                }
            }
        }
        return statuses;
    }

    public static Status toStatus(IntygStatus certificateStatus) {
        Status status = new Status(CertificateState.valueOf(certificateStatus.getType().value()),
                certificateStatus.getTarget(),
                LocalDateTime.parse(certificateStatus.getTimestamp()));
        return status;
    }
}
