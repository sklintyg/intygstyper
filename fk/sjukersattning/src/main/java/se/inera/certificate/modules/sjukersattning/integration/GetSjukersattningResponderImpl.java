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
package se.inera.certificate.modules.sjukersattning.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import riv.clinicalprocess.healthcond.certificate.getcertificateresponder._1.GetCertificateResponseType;
import riv.clinicalprocess.healthcond.certificate.getcertificateresponder._1.GetCertificateType;
import se.inera.certificate.modules.sjukersattning.model.converter.InternalToTransport;
import se.inera.certificate.modules.sjukersattning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukersattning.rest.SjukersattningModuleApi;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v2.ErrorIdType;

import com.google.common.base.Throwables;

public class GetSjukersattningResponderImpl implements GetCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSjukersattningResponderImpl.class);

    @Autowired
    private SjukersattningModuleApi moduleApi;

    @Autowired
    private ConverterUtil converterUtil;

    @Override
    public GetCertificateResponseType getCertificate(String logicalAddress, GetCertificateType request) {
        GetCertificateResponseType response = new GetCertificateResponseType();

        String certificateId = request.getIntygsId().getExtension();

        try {
            CertificateHolder certificate = moduleApi.getModuleContainer().getCertificate(certificateId, null, false);
            if (certificate.isDeletedByCareGiver()) {
                response.setResult(ResultUtil.errorResult(ErrorIdType.APPLICATION_ERROR,
                        String.format("Certificate '%s' has been deleted by care giver", certificateId)));
            } else {
                setCertificateBody(certificate, response);
                if (certificate.isRevoked()) {
                    response.setResult(ResultUtil.errorResult(ErrorIdType.REVOKED,
                            String.format("Certificate '%s' has been revoked", certificateId)));
                } else {
                    response.setResult(ResultUtil.okResult());
                }
            }
        } catch (InvalidCertificateException e) {
            response.setResult(ResultUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
        }
        return response;
    }

    protected void setCertificateBody(CertificateHolder certificate, GetCertificateResponseType response) {
        try {
            RegisterCertificateType jaxbObject = InternalToTransport.convert(converterUtil.fromJsonString(certificate.getDocument()));
            response.setIntyg(jaxbObject.getIntyg());
        } catch (Exception e) {
            LOGGER.error("Error while converting in getMedicalCertificate for id: {} with stacktrace: {}", certificate.getId(), e.getStackTrace());
            Throwables.propagate(e);
        }
    }

}
