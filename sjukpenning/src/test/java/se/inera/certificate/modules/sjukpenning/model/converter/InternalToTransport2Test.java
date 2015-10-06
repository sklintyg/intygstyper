package se.inera.certificate.modules.sjukpenning.model.converter;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.common.internal.*;
import se.inera.certificate.modules.sjukpenning.integration.RegisterSjukpenningValidator;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

import com.google.common.base.Charsets;
import com.helger.schematron.svrl.SVRLWriter;

public class InternalToTransport2Test {

    @Test
    public void doSchematronValidation() throws Exception {
        String xmlContents = xmlToString(InternalToTransport2.convert(getUtlatande()));
        RegisterSjukpenningValidator validator = new RegisterSjukpenningValidator();
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));
        System.out.println(SVRLWriter.createXMLString(result));
        //assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    private SjukpenningUtlatande getUtlatande() {
        SjukpenningUtlatande utlatande = new SjukpenningUtlatande();
        utlatande.setId("1234567");
        utlatande.setGrundData(getGrundData());
        utlatande.setUndersokningAvPatienten(new InternalDate(new LocalDate()));
        utlatande.setArbetsloshet(true);
        utlatande.setDiagnosKod1("S47");
        utlatande.setDiagnosBeskrivning1("Klämskada skuldra");
        utlatande.setFunktionsnedsattning("Kan inte smida");
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setPlaneradBehandling("Mer medicin");
        utlatande.setNedsattMed100(new InternalLocalDateInterval(new InternalDate(new LocalDate("2015-08-26")), new InternalDate(new LocalDate("2015-09-30"))));
        utlatande.setRessattTillArbeteAktuellt(false);
        utlatande.setRekommendationOverSocialstyrelsensBeslutsstod(false);
        utlatande.setVadPatientenKanGora("Dansa");
        utlatande.setPrognosNarPatientKanAterga("Aldrig");
        utlatande.setKommentar("Trevlig kille");
        utlatande.setKontaktMedFk(false);
        return utlatande;
    }

    public GrundData getGrundData() {
        GrundData grundData = new GrundData();
        grundData.setSigneringsdatum(new LocalDateTime());
        grundData.setSkapadAv(getHosPersonal());
        grundData.setPatient(getPatient());
        return grundData;
    }

    private Patient getPatient() {
        Patient patient = new Patient();
        patient.setEfternamn("Olsson");
        patient.setFornamn("Olivia");
        patient.setPersonId("19270310-4321");
        patient.setPostadress("Pgatan 2");
        patient.setPostnummer("100 20");
        patient.setPostort("Stadby gärde");
        return patient;
    }

    private HoSPersonal getHosPersonal() {
        HoSPersonal personal = new HoSPersonal();
        personal.setVardenhet(getVardenhet());
        personal.setForskrivarKod("09874321");
        personal.setFullstandigtNamn("Karl Karlsson");
        personal.setPersonId("19650708-1234");
        return personal;
    }

    private Vardenhet getVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarnamn("VG1");
        vardgivare.setVardgivarid("12345678");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setTelefonnummer("0812341234");
        vardenhet.setArbetsplatsKod("45312");
        vardenhet.setEnhetsid("123456789");
        vardenhet.setEnhetsnamn("VE1");
        vardenhet.setEpost("ve1@vg1.se");
        vardenhet.setPostadress("Enhetsg. 1");
        vardenhet.setPostnummer("100 10");
        vardenhet.setPostort("Stadby");
        return vardenhet;
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
