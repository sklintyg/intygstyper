package se.inera.certificate.modules.ts_diabetes.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.certificate.integration.module.exception.InvalidCertificateException;
import se.inera.certificate.logging.LogMarkers;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.modules.support.api.CertificateStateHolder;
import se.inera.certificate.modules.ts_diabetes.model.converter.InternalToTransportConverter;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.rest.ModuleService;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesType;
import se.inera.intygstjanster.ts.services.utils.ResultTypeUtil;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;
import se.inera.intygstjanster.ts.services.v1.IntygMeta;
import se.inera.intygstjanster.ts.services.v1.IntygStatus;
import se.inera.intygstjanster.ts.services.v1.Status;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GetTSDiabetesResponderImpl implements GetTSDiabetesResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTSDiabetesResponderImpl.class);

    @Autowired
    private ModuleService moduleService;

    @Autowired
    @Qualifier("ts-diabetes-objectMapper")
    private ObjectMapper objectMapper;

    @Override
    public GetTSDiabetesResponseType getTSDiabetes(String logicalAddress, GetTSDiabetesType parameters) {
        GetTSDiabetesResponseType response = new GetTSDiabetesResponseType();
        CertificateHolder certificate = null;

        String certificateId = parameters.getIntygsId();
        String personNummer = parameters.getPersonId() != null ? parameters.getPersonId().getExtension() : null;

        if (certificateId == null || certificateId.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing certificateId '.");
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "non-existing certificateId"));
            return response;
        }

        try {
            certificate = moduleService.getModuleContainer().getCertificate(certificateId, personNummer, false);
            if (personNummer != null && !certificate.getCivicRegistrationNumber().equals(personNummer)) {
                LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing nationalIdentityNumber '.");
                response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "Validation error: missing nationalIdentityNumber"));
                return response;
            }
            if (certificate.isDeletedByCareGiver()) {
                response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR,
                        String.format("Certificate '%s' has been deleted by care giver", certificateId)));
            } else {
                Utlatande utlatande = objectMapper.readValue(certificate.getDocument(), Utlatande.class);
                TSDiabetesIntyg tsDiabetesIntyg = InternalToTransportConverter.convert(utlatande);
                response.setIntyg(tsDiabetesIntyg);
                response.setMeta(createMetaData(certificate));
                if (certificate.isRevoked()) {
                    response.setResultat(ResultTypeUtil.infoResult(String.format("Certificate '%s' has been revoked", parameters.getIntygsId())));
                } else {
                    response.setResultat(ResultTypeUtil.okResult());
                }
            }
        } catch (InvalidCertificateException | IOException e) {
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.TECHNICAL_ERROR, e.getMessage()));
        }
        return response;
    }

    private IntygMeta createMetaData(CertificateHolder certificate) {
        IntygMeta intygMeta = new IntygMeta();
        intygMeta.setAdditionalInfo(certificate.getAdditionalInfo());
        intygMeta.setAvailable(certificate.isDeleted() ? "false" : "true");
        intygMeta.getStatus().addAll(convertToStatuses(certificate.getCertificateStates()));
        return intygMeta;
    }

    private Collection<? extends IntygStatus> convertToStatuses(List<CertificateStateHolder> certificateStates) {
        List<IntygStatus> statuses = new ArrayList<IntygStatus>();
        for (CertificateStateHolder csh : certificateStates) {
            statuses.add(convert(csh));
        }
        return statuses;
    }

    private IntygStatus convert(CertificateStateHolder source) {
        IntygStatus status = new IntygStatus();
        status.setTarget(source.getTarget());
        status.setTimestamp(source.getTimestamp().toString());
        status.setType(mapToStatus(source.getState()));
        return status;
    }

    private boolean isEmpty(String intygsId) {
        return intygsId == null || intygsId.isEmpty();
    }

    private Status mapToStatus(CertificateState state) {
        return Status.valueOf(state.name());
    }

}
