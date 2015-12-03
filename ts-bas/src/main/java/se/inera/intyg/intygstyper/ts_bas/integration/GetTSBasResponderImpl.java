package se.inera.intyg.intygstyper.ts_bas.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.intyg.common.schemas.intygstjansten.ts.utils.ResultTypeUtil;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.integration.module.exception.MissingConsentException;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.util.logging.LogMarkers;
import se.inera.intyg.intygstyper.ts_bas.model.converter.InternalToTransport;
import se.inera.intyg.intygstyper.ts_bas.model.converter.util.ConverterUtil;
import se.inera.intyg.intygstyper.ts_bas.rest.TsBasModuleApi;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasResponderInterface;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasResponseType;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasType;
import se.inera.intygstjanster.ts.services.v1.*;

import com.google.common.base.Throwables;

public class GetTSBasResponderImpl implements GetTSBasResponderInterface {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GetTSBasResponderImpl.class);

    @Autowired
    @Qualifier("tsBasModelConverterUtil")
    private ConverterUtil converterUtil;

    @Autowired
    private TsBasModuleApi moduleApi;

    @Override
    public GetTSBasResponseType getTSBas(String logicalAddress, GetTSBasType request) {
        GetTSBasResponseType response = new GetTSBasResponseType();

        String certificateId = request.getIntygsId();
        Personnummer personNummer = request.getPersonId() != null ? new Personnummer(request.getPersonId().getExtension()) : null;

        if (certificateId == null || certificateId.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing certificateId '.");
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "non-existing certificateId"));
            return response;
        }

        CertificateHolder certificate = null;

        try {
            certificate = moduleApi.getModuleContainer().getCertificate(certificateId, personNummer, false);
            if (personNummer != null && !certificate.getCivicRegistrationNumber().equals(personNummer)) {
                response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "nationalIdentityNumber mismatch"));
                return response;
            }
            if (certificate.isDeletedByCareGiver()) {
                response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, String.format("Certificate '%s' has been deleted by care giver", certificateId)));
            } else {
                response.setMeta(createCertificateMetaType(certificate));
                attachCertificateDocument(certificate, response);
                if (certificate.isRevoked()) {
                    LOGGER.info("Certificate {} has been revoked", certificateId);
                    response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.REVOKED, String.format("Certificate '%s' has been revoked", certificateId)));
                } else {
                    response.setResultat(ResultTypeUtil.okResult());
                }
            }
        } catch (InvalidCertificateException | MissingConsentException e) {
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
        }

        return response;
    }

    private void attachCertificateDocument(CertificateHolder certificate, GetTSBasResponseType response) {
        try {
            TSBasIntyg tsBasIntyg = InternalToTransport.convert(converterUtil.fromJsonString(certificate.getDocument())).getIntyg();
            response.setIntyg(tsBasIntyg);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    private IntygMeta createCertificateMetaType(CertificateHolder certificate) {
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

    private Status mapToStatus(CertificateState state) {
        return Status.valueOf(state.name());
    }

}
