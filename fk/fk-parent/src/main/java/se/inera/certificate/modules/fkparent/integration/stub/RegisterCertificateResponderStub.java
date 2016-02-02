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

package se.inera.certificate.modules.fkparent.integration.stub;

import static se.inera.certificate.modules.fkparent.integration.stub.CertificatesStubStore.*;

import java.util.HashMap;

import javax.xml.ws.WebServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;

@WebServiceProvider(targetNamespace = "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2")
public final class RegisterCertificateResponderStub implements RegisterCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCertificateResponderStub.class);

    @Autowired
    private CertificatesStubStore store;

    @Override
    public RegisterCertificateResponseType registerCertificate(String logicalAddress, RegisterCertificateType parameters) {
        LOGGER.debug("fk-parent RegisterCertificate responding");
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        ResultType resultType = new ResultType();

//        HashMap<String, String> properties = new HashMap<>();
//        Intyg intyg = parameters.getIntyg();
//        String pnr = intyg.getPatient().getPersonId().getExtension();
//        String certificateteId = intyg.getIntygsId().getExtension();
//        properties.put(MAKULERAD, MAKULERAD_NEJ);
//        properties.put(PERSONNUMMER, pnr);
//        store.addCertificate(certificateteId, properties);
//
        resultType.setResultCode(ResultCodeType.OK);
        response.setResult(resultType);
        return response;
    }

}
