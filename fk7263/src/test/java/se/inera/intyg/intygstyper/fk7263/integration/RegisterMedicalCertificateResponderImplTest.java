package se.inera.intyg.intygstyper.fk7263.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.intygstyper.fk7263.model.converter.TransportToInternal;
import se.inera.intyg.intygstyper.fk7263.model.converter.util.ConverterUtil;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.rest.Fk7263ModuleApi;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RegisterMedicalCertificateResponderImplTest {

    private CustomObjectMapper objectMapper = new CustomObjectMapper();
    private ConverterUtil converterUtil = new ConverterUtil();
    
    @Mock
    private ModuleEntryPoint moduleEntryPoint = mock(ModuleEntryPoint.class);

    @Mock
    private Fk7263ModuleApi moduleRestApi = mock(Fk7263ModuleApi.class);

    @Mock
    private ModuleContainerApi moduleContainer = mock(ModuleContainerApi.class);

    private RegisterMedicalCertificateType request;
    private String xml;
    private Utlatande utlatande;
    private CertificateHolder certificateHolder;
    

    @InjectMocks
    private RegisterMedicalCertificateResponderImpl responder = new RegisterMedicalCertificateResponderImpl();

    @Before
    public void initializeResponder() throws JAXBException {
        responder.initializeJaxbContext();
    }

    @Before
    public void prepareRequest() throws Exception {

        ClassPathResource file = new ClassPathResource(
                "RegisterMedicalCertificateResponderImplTest/fk7263.xml");

        JAXBContext context = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        JAXBElement<RegisterMedicalCertificateType> registerMedicalCertificate = context.createUnmarshaller().unmarshal(
                new StreamSource(file.getInputStream()), RegisterMedicalCertificateType.class);
        request = registerMedicalCertificate.getValue();

        xml = FileUtils.readFileToString(file.getFile());
        converterUtil.setObjectMapper(objectMapper);
        responder.setConverterUtil(converterUtil);
        utlatande = TransportToInternal.convert(request.getLakarutlatande());
        certificateHolder = converterUtil.toCertificateHolder(utlatande);
        certificateHolder.setOriginalCertificate(xml);
        when(moduleRestApi.getModuleContainer()).thenReturn(moduleContainer);
    }

    @Test
    public void testReceiveCertificate() throws Exception {

        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());
        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(certificateHolder);
    }

    @Test
    public void testReceiveCertificateWiretapped() throws Exception {

        responder.setWireTapped(true);
        certificateHolder.setWireTapped(true);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());
        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(certificateHolder);
    }

    @Test
    public void testWithExistingCertificate() throws Exception {
        Mockito.doThrow(new CertificateAlreadyExistsException(request.getLakarutlatande().getLakarutlatandeId())).when(moduleContainer).certificateReceived(certificateHolder);

        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);
        assertEquals(ResultCodeEnum.INFO, response.getResult().getResultCode());
    }

    @Test
    public void testWithInvalidCertificate() throws Exception {
        request.getLakarutlatande().setSkapadAvHosPersonal(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);
        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
    }
}
