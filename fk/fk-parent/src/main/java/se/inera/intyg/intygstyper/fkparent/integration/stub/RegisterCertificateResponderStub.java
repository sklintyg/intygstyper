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

package se.inera.intyg.intygstyper.fkparent.integration.stub;

import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.MAKULERAD;
import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.MAKULERAD_NEJ;
import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.PERSONNUMMER;

import java.util.HashMap;

import javax.xml.ws.WebServiceProvider;

import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.stub.MedicalCertificatesStore;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v2.ResultType;

@SchemaValidation
@WebServiceProvider(targetNamespace = "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2")
public final class RegisterCertificateResponderStub implements RegisterCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCertificateResponderStub.class);

    @Autowired(required = false)
    private MedicalCertificatesStore store;

    private boolean throwException = false;

    @Override
    public RegisterCertificateResponseType registerCertificate(String logicalAddress, RegisterCertificateType parameters) {
        LOGGER.debug("fk-parent RegisterCertificate responding");
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        if (throwException) {
            LOGGER.debug("Throwing fake exception");
            throw new RuntimeException();
        }
        ResultType resultType = new ResultType();

        try {
            HashMap<String, String> properties = new HashMap<>();
            Intyg intyg = parameters.getIntyg();
            String pnr = intyg.getPatient().getPersonId().getExtension();
            String certificateteId = intyg.getIntygsId().getExtension();
            properties.put(MAKULERAD, MAKULERAD_NEJ);
            properties.put(PERSONNUMMER, pnr);
            store.addCertificate(certificateteId, properties);
            resultType.setResultCode(ResultCodeType.OK);
        } catch (Exception e) {
            LOGGER.debug("fk-parent RegisterCertificate got exception: ", e);
            resultType.setResultCode(ResultCodeType.ERROR);
        }
        response.setResult(resultType);
        return response;
    }

    public boolean isThrowException() {
        return throwException;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

}
