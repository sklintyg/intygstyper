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

package se.inera.intyg.intygstyper.fk7263.integration.stub;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Map;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.common.support.stub.MedicalCertificatesStore;

@RunWith(MockitoJUnitRunner.class)
public class RegisterMedicalCertificateResponderStubTest {

    private static JAXBContext jaxbContext;
    private RegisterMedicalCertificateType request;
    private AttributedURIType logicalAddress = new AttributedURIType();

    @Mock
    MedicalCertificatesStore store;

    @InjectMocks
    RegisterMedicalCertificateResponderStub stub;

    @BeforeClass
    public static void setUpOnce() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
    }

    @Before
    public void setUp() throws Exception {
        logicalAddress.setValue("FK");
        // read request from file
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        request = unmarshaller
                .unmarshal(new StreamSource(new ClassPathResource("fk7263/fk7263.xml").getInputStream()), RegisterMedicalCertificateType.class)
                .getValue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testName() throws Exception {

        request.getLakarutlatande().setLakarutlatandeId("id-1234567890");

        stub.registerMedicalCertificate(logicalAddress, request);

        verify(store).addCertificate(eq("id-1234567890"), any(Map.class));
    }

    @Test(expected = RuntimeException.class)
    public void testThrowsExceptionWhenIdIsError() throws Exception {

        request.getLakarutlatande().setLakarutlatandeId("error");

        stub.registerMedicalCertificate(logicalAddress, request);
    }

    @Test
    public void testValidation() throws Exception {
        // Invalid p-nr
        request.getLakarutlatande().getPatient().getPersonId().setExtension("121212-1212");

        RegisterMedicalCertificateResponseType response = stub.registerMedicalCertificate(logicalAddress, request);
        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
    }
}
