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

package se.inera.intyg.intygstyper.ts_bas.integration;

import java.io.StringReader;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;

import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.integration.module.exception.MissingConsentException;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.*;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.util.logging.LogMarkers;
import se.inera.intyg.intygstyper.ts_parent.integration.ResultTypeUtil;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.*;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.*;

public class GetTSBasResponderImpl implements GetTSBasResponderInterface {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GetTSBasResponderImpl.class);

    @Autowired(required = false)
    private ModuleContainerApi moduleContainer;

    @Override
    public GetTSBasResponseType getTSBas(String logicalAddress, GetTSBasType request) {
        GetTSBasResponseType response = new GetTSBasResponseType();

        String certificateId = request.getIntygsId();
        Personnummer personNummer = request.getPersonId() != null ? new Personnummer(request.getPersonId().getExtension()) : null;

        if (Strings.isNullOrEmpty(certificateId)) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing certificateId '.");
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "non-existing certificateId"));
            return response;
        }

        CertificateHolder certificate = null;

        try {
            certificate = moduleContainer.getCertificate(certificateId, personNummer, false);
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
            TSBasIntyg tsBasIntyg = JAXB.unmarshal(new StringReader(certificate.getOriginalCertificate()), RegisterTSBasType.class).getIntyg();
            response.setIntyg(tsBasIntyg);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    private IntygMeta createCertificateMetaType(CertificateHolder certificate) {
        IntygMeta intygMeta = new IntygMeta();
        intygMeta.setAdditionalInfo(certificate.getAdditionalInfo());
        intygMeta.setAvailable(String.valueOf(!certificate.isDeleted()));
        intygMeta.getStatus().addAll(convertToStatuses(certificate.getCertificateStates()));
        return intygMeta;
    }

    private Collection<? extends IntygStatus> convertToStatuses(List<CertificateStateHolder> certificateStates) {
        List<IntygStatus> statuses = new ArrayList<>();
        for (CertificateStateHolder csh : certificateStates) {
            statuses.add(convert(csh));
        }
        return statuses;
    }

    private IntygStatus convert(CertificateStateHolder source) {
        IntygStatus status = new IntygStatus();
        status.setTarget(source.getTarget());
        status.setTimestamp(source.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        status.setType(mapToStatus(source.getState()));
        return status;
    }

    private Status mapToStatus(CertificateState state) {
        return Status.valueOf(state.name());
    }

}
