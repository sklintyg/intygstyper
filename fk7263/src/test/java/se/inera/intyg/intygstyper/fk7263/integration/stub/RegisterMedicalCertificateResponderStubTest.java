package se.inera.intyg.intygstyper.fk7263.integration.stub;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;

@RunWith(MockitoJUnitRunner.class)
public class RegisterMedicalCertificateResponderStubTest {
    
    private static JAXBContext jaxbContext;
    private RegisterMedicalCertificateType request;
    private AttributedURIType logicalAddress = new AttributedURIType();
    
    @Mock
    FkMedicalCertificatesStore store;

    @InjectMocks
    RegisterMedicalCertificateResponderStub stub;

    @BeforeClass
    public static void setUpOnce() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
    }
    
    @Before
    public  void setUp() throws Exception {
        logicalAddress.setValue("FK");
        // read request from file
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        request = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("fk7263/fk7263.xml").getInputStream()), RegisterMedicalCertificateType.class).getValue();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testName() throws Exception {

        request.getLakarutlatande().setLakarutlatandeId("id-1234567890");
        
        stub.registerMedicalCertificate(logicalAddress, request);
        
        verify(store).addCertificate(eq("id-1234567890"), any(Map.class));
    }
    
    @Test(expected=RuntimeException.class)
    public void testThrowsExceptionWhenIdIsError() throws Exception {

        request.getLakarutlatande().setLakarutlatandeId("error");
        
        stub.registerMedicalCertificate(logicalAddress, request);
    }
    
    @Test
    public void testValidation() throws Exception {
        // Invalid p-nr
        request.getLakarutlatande().getPatient().getPersonId().setExtension("121212-1212");
        
        RegisterMedicalCertificateResponseType response = stub.registerMedicalCertificate(logicalAddress, request);
        Assert.assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
    }
}
