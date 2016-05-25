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
package se.inera.intyg.intygstyper.fk7263.integration;

import java.io.StringReader;

import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Throwables;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.*;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.converter.ModelConverter;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.ResultTypeUtil;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.fk7263.rest.Fk7263ModuleApi;
import se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType;

/**
 * @author andreaskaltenbach
 */
public class GetMedicalCertificateForCareResponderImpl implements
        GetMedicalCertificateForCareResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetMedicalCertificateForCareResponderImpl.class);

    @Autowired
    private Fk7263ModuleApi moduleApi;

    @Override
    public GetMedicalCertificateForCareResponseType getMedicalCertificateForCare(String logicalAddress,
            GetMedicalCertificateForCareRequestType request) {

        GetMedicalCertificateForCareResponseType response = new GetMedicalCertificateForCareResponseType();

        String certificateId = request.getCertificateId();
        Personnummer nationalIdentityNumber = request.getNationalIdentityNumber() != null ? new Personnummer(request.getNationalIdentityNumber())
                : null;

        CertificateHolder certificate = null;

        try {
            certificate = moduleApi.getModuleContainer().getCertificate(certificateId, nationalIdentityNumber, false);
            if (nationalIdentityNumber != null && !certificate.getCivicRegistrationNumber().equals(nationalIdentityNumber)) {
                response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "nationalIdentityNumber mismatch"));
                return response;
            }
            if (certificate.isDeletedByCareGiver()) {
                response.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR,
                        String.format("Certificate '%s' has been deleted by care giver", certificateId)));
            } else {
                response.setMeta(ModelConverter.toCertificateMetaType(certificate));
                attachCertificateDocument(certificate, response);
                if (certificate.isRevoked()) {
                    response.setResult(
                            ResultTypeUtil.errorResult(ErrorIdType.REVOKED, String.format("Certificate '%s' has been revoked", certificateId)));
                } else {
                    response.setResult(ResultTypeUtil.okResult());
                }
            }
        } catch (InvalidCertificateException e) {
            response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
        }
        return response;

    }

    protected void attachCertificateDocument(CertificateHolder certificate, GetMedicalCertificateForCareResponseType response) {
        try {

            RegisterMedicalCertificateType jaxbObject = JAXB.unmarshal(new StringReader(certificate.getOriginalCertificate()),
                    RegisterMedicalCertificateType.class);
            response.setLakarutlatande(jaxbObject.getLakarutlatande());

        } catch (Exception e) {
            LOGGER.error("Error while converting in getMedicalCertificate for id: {} with stacktrace: {}", certificate.getId(), e.getStackTrace());
            Throwables.propagate(e);
        }
    }

}
