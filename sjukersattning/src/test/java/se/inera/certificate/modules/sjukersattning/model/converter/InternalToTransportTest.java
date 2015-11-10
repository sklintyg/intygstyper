package se.inera.certificate.modules.sjukersattning.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.modules.fkparent.model.converter.IntygGrundDataBuilder;
import se.inera.certificate.modules.fkparent.model.converter.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.converter.RespConstants;
import se.inera.certificate.modules.sjukersattning.integration.RegisterSjukersattningValidator;
import se.inera.certificate.modules.sjukersattning.model.internal.*;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

import com.google.common.base.Charsets;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.SVRLWriter;

public class InternalToTransportTest {

    @Test
    public void doSchematronValidation() throws Exception {
        String xmlContents = xmlToString(InternalToTransport.convert(getUtlatande()));

        RegisterCertificateValidator generalValidator = new RegisterCertificateValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterSjukersattningValidator validator = new RegisterSjukersattningValidator();
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    private SjukersattningUtlatande getUtlatande() {
        SjukersattningUtlatande utlatande = new SjukersattningUtlatande();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygGrundDataBuilder.getGrundData());
        utlatande.setUndersokningAvPatienten(new InternalDate(new LocalDate()));
        utlatande.setKannedomOmPatient(new InternalDate(new LocalDate()));
        utlatande.getUnderlag().add(Underlag.create(Underlag.UnderlagsTyp.OVRIGT, new InternalDate(new LocalDate()), false));
        utlatande.getDiagnoser().add(Diagnos.create("S47", "ICD-10-SE", "Klämskada skuldra"));
        utlatande.getDiagnoser().add(Diagnos.create("S48", "ICD-10-SE", "Klämskada arm"));
        utlatande.getAtgarder().add(BehandlingsAtgard.create("ABC", RespConstants.BEHANDLINGSATGARD_CODE_SYSTEM, "Kristallterapi"));
        utlatande.getFunktionsnedsattnings().add(Funktionsnedsattning.create(Funktionsnedsattning.Funktionsomrade.ANNAN_KROPPSLIG, "Kan inte smida"));
        utlatande.getFunktionsnedsattnings().add(Funktionsnedsattning.create(Funktionsnedsattning.Funktionsomrade.ANNAN_PSYKISK, "Lite ledsen"));
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setDiagnostisering("Helt galen");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setPlaneradBehandling("Mer medicin");
        utlatande.setAktivitetsFormaga("Dansa");
        utlatande.setPrognos("Aldrig");
        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(false);
        return utlatande;
    }

    private String xmlToString(RegisterCertificateType registerCertificate) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class);
        ObjectFactory objectFactory = new ObjectFactory();

        StringWriter stringWriter = new StringWriter();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(registerCertificate);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        String result = stringWriter.toString();
        System.out.println(result);
        return result;
    }

}
