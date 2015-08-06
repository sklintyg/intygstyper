/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.sjukpenning.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.integration.module.exception.InvalidCertificateException;
import se.inera.certificate.modules.sjukpenning.model.converter.InternalToTransport;
import se.inera.certificate.modules.sjukpenning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukpenning.rest.SjukpenningModuleApi;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.intygstjanster.fk.services.getsjukpenningresponder.v1.GetSjukpenningResponderInterface;
import se.inera.intygstjanster.fk.services.getsjukpenningresponder.v1.GetSjukpenningResponseType;
import se.inera.intygstjanster.fk.services.getsjukpenningresponder.v1.GetSjukpenningType;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningType;
import se.inera.intygstjanster.fk.services.v1.ResultatTyp;
import se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType;

import com.google.common.base.Throwables;

public class GetSjukpenningResponderImpl implements GetSjukpenningResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSjukpenningResponderImpl.class);

    @Autowired
    private SjukpenningModuleApi moduleApi;

    @Autowired
    private ConverterUtil converterUtil;

    @Override
    public GetSjukpenningResponseType getSjukpenning(String logicalAddress, GetSjukpenningType request) {

        GetSjukpenningResponseType response = new GetSjukpenningResponseType();

        String certificateId = request.getIntygsId();

        try {
            CertificateHolder certificate = moduleApi.getModuleContainer().getCertificate(certificateId, null, false);
            if (certificate.isDeletedByCareGiver()) {
                response.setResultat(errorResult(ErrorIdType.APPLICATION_ERROR,
                        String.format("Certificate '%s' has been deleted by care giver", certificateId)));
            } else {
                attachCertificateDocument(certificate, response);
                if (certificate.isRevoked()) {
                    response.setResultat(errorResult(ErrorIdType.REVOKED, String.format("Certificate '%s' has been revoked", certificateId)));
                } else {
                    response.setResultat(okResult());
                }
            }
        } catch (InvalidCertificateException e) {
            response.setResultat(errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
        }

        return response;
    }

    protected void attachCertificateDocument(CertificateHolder certificate, GetSjukpenningResponseType response) {
        try {
            RegisterSjukpenningType jaxbObject = InternalToTransport.convert(converterUtil.fromJsonString(certificate.getDocument()));
            response.setIntyg(jaxbObject.getIntyg());
        } catch (Exception e) {
            LOGGER.error("Error while converting in getSjukpenning for id: {} with stacktrace: {}", certificate.getId(), e.getStackTrace());
            Throwables.propagate(e);
        }
    }

    private ResultatTyp okResult() {
        // TODO
        return null;
    }

    private ResultatTyp infoResult(String message) {
        // TODO
        return null;
    }

    private ResultatTyp errorResult(ErrorIdType errorIdtype, String message) {
        // TODO
        return null;
    }

}
