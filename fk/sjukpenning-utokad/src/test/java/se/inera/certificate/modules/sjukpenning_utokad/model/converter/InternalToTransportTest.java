package se.inera.certificate.modules.sjukpenning_utokad.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.SVRLWriter;

import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.certificate.modules.fkparent.model.converter.RegisterCertificateTestValidator;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Diagnos;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Prognos;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Prognos.PrognosTyp;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Sjukskrivning;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Sysselsattning;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public class InternalToTransportTest {

    @Test
    public void doSchematronValidationSjukpenningUtokat() throws Exception {
        String xmlContents = Resources.toString(getResource("sjukpenning-utokat2.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator("sjukpenning-utokat.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    @Test
    public void testInternalToTransportConversion() throws Exception {
        SjukpenningUtokadUtlatande expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected);
        SjukpenningUtokadUtlatande actual = TransportToInternal.convert(transport.getIntyg());

        assertEquals(expected, actual);
    }

    public static SjukpenningUtokadUtlatande getUtlatande() {
        SjukpenningUtokadUtlatande.Builder utlatande = SjukpenningUtokadUtlatande.builder();
        utlatande.setId("1234567");
        utlatande.setTextVersion("1.0");
        GrundData grundData = IntygTestDataBuilder.getGrundData();

        grundData.setSigneringsdatum(new LocalDateTime("2015-12-07T15:48:05"));
        utlatande.setGrundData(grundData);

        utlatande.setTelefonkontaktMedPatienten(new InternalDate("2015-12-08"));
        utlatande.setAnnatGrundForMU(new InternalDate("2015-12-07"));
        utlatande.setAnnatGrundForMUBeskrivning("Barndomsvän");

        utlatande.setSysselsattning(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE));
        utlatande.setNuvarandeArbete("Smed");

        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra"))));

        utlatande.setFunktionsnedsattning("Haltar när han dansar");
        utlatande.setAktivitetsbegransning("Kommer inte in i bilen");

        utlatande.setSjukskrivningar(asList(
                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_3_4,
                        new InternalLocalDateInterval(new InternalDate("2015-12-07"), new InternalDate("2015-12-10"))),
                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(new InternalDate("2015-12-12"), new InternalDate("2015-12-14"))),
                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_1_4,
                        new InternalLocalDateInterval(new InternalDate("2015-12-15"), new InternalDate("2015-12-15")))
                        ));

        utlatande.setForsakringsmedicinsktBeslutsstod("Överskrider inte FMB");

        utlatande.setArbetstidsforlaggning(true);
        utlatande.setArbetstidsforlaggningMotivering("Kan bara jobba på nätterna");

        utlatande.setArbetsresor(true);

        utlatande.setFormagaTrotsBegransning("Är bra på att dansa!");

        utlatande.setPrognos(Prognos.create(PrognosTyp.PROGNOS_OKLAR, "Kan bara jobba på nätterna."));

        utlatande.setArbetslivsinriktadeAtgarder(asList(
                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING)));
        utlatande.setArbetslivsinriktadeAtgarderAktuelltBeskrivning("Jobbar bra om man inte stör honom");
        return utlatande.build();
    }
}
