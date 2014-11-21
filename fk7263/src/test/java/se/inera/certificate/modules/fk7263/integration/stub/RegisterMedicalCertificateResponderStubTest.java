package se.inera.certificate.modules.fk7263.integration.stub;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

@RunWith(MockitoJUnitRunner.class)
public class RegisterMedicalCertificateResponderStubTest {
    
    @Mock
    FkMedicalCertificatesStore store;

    @InjectMocks
    RegisterMedicalCertificateResponderStub stub = new RegisterMedicalCertificateResponderStub() {
        @Override
        protected void validateTransport(RegisterMedicalCertificateType registerMedicalCertificate) {}
        protected void validateInternal(Utlatande utlatande) {}
    };

    @SuppressWarnings("unchecked")
    @Test
    public void testName() throws Exception {
        AttributedURIType logicalAddress = new AttributedURIType();
        logicalAddress.setValue("FK");
        // read request from file
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        RegisterMedicalCertificateType request = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("fk7263/fk7263.xml").getInputStream()), RegisterMedicalCertificateType.class).getValue();

        request.getLakarutlatande().setLakarutlatandeId("id-1234567890");
        
        stub.registerMedicalCertificate(logicalAddress, request);
        
        verify(store).addCertificate(eq("id-1234567890"), any(Map.class));
    }
}
