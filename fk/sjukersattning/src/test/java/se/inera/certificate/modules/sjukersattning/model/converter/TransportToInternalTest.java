package se.inera.certificate.modules.sjukersattning.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.helger.schematron.svrl.SVRLHelper;

import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.certificate.modules.sjukersattning.model.internal.Diagnos;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.model.internal.Tillaggsfraga;
import se.inera.certificate.modules.sjukersattning.model.internal.Underlag;
import se.inera.intyg.common.support.model.InternalDate;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public class TransportToInternalTest {

    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;
    private RegisterCertificateValidator validator = new RegisterCertificateValidator("sjukersattning.sch");

    @Before
    public void suitSetup() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class);
        objectFactory = new ObjectFactory();
    }

    @Test
    public void endToEnd() throws Exception {
        SjukersattningUtlatande originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        SjukersattningUtlatande convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());

        String xml = xmlToString(transportCertificate);
        SchematronOutputType valResult = validator.validateSchematron(new StreamSource(new StringReader(xml)));

        assertTrue(SVRLHelper.getAllFailedAssertions(valResult).size() == 0);
        assertEquals(originalUtlatande, convertedIntyg);
    }

    private String xmlToString(RegisterCertificateType registerCertificate) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(registerCertificate);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        return stringWriter.toString();
    }

    private static SjukersattningUtlatande getUtlatande() {
        SjukersattningUtlatande.Builder utlatande = SjukersattningUtlatande.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("1.0");
        utlatande.setUndersokningAvPatienten(new InternalDate(new LocalDate()));
        utlatande.setKannedomOmPatient(new InternalDate(new LocalDate()));
        utlatande.setUnderlagFinns(true);
        utlatande.setUnderlag(asList(Underlag.create(Underlag.UnderlagsTyp.OVRIGT, new InternalDate(new LocalDate()), "plats 1"),
                Underlag.create(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(new LocalDate().plusWeeks(2)), "plats 2")));
        utlatande.setSjukdomsforlopp("Snabbt");
        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra")), Diagnos.create("S48", "ICD_10_SE", "Klämskada arm")));
        utlatande.setDiagnosgrund("Ingen som vet");
        utlatande.setNyBedomningDiagnosgrund(true);
        utlatande.setFunktionsnedsattningIntellektuell("Bra");
        utlatande.setFunktionsnedsattningKommunikation("Tyst");
        utlatande.setFunktionsnedsattningKoncentration("Noll");
        utlatande.setFunktionsnedsattningPsykisk("Lite ledsen");
        utlatande.setFunktionsnedsattningSynHorselTal("Vitt");
        utlatande.setFunktionsnedsattningBalansKoordination("Tyst");
        utlatande.setFunktionsnedsattningAnnan("Kan inte smida");
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setAvslutadBehandling("Gammal medicin");
        utlatande.setPlaneradBehandling("Mer medicin");
        utlatande.setMedicinskaForutsattningarForArbete("Svårt");
        utlatande.setAktivitetsFormaga("Dansa");
        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(true);
        utlatande.setAnledningTillKontakt("Känner mig ensam");
        utlatande.setTillaggsfragor(asList(Tillaggsfraga.create("9001", "Svar text 1"), Tillaggsfraga.create("9002", "Svar text 2")));
        return utlatande.build();
    }

}
