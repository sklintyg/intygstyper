package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertNotNull;
import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsTyp;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomsKannedom;
import se.inera.certificate.modules.rli.model.internal.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.Patient;
import se.inera.certificate.modules.rli.model.internal.Rekommendation;
import se.inera.certificate.modules.rli.model.internal.Undersokning;
import se.inera.certificate.modules.rli.model.internal.Utlatande;
import se.inera.certificate.modules.rli.model.internal.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.Vardgivare;

public class FullExternalToInternalConverterTest {

    private ApplicationContext ctx;

    private ExternalToInternalConverterImpl converter;

    public FullExternalToInternalConverterTest() {
        this.ctx = new AnnotationConfigApplicationContext(SpringTestConfig.class);
    }

    @Before
    public void setUp() {
        this.converter = this.ctx.getBean("externalToInternalConverter", ExternalToInternalConverterImpl.class);
    }

    @Test
    public void testConvertUtlatande() {

        se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande = readUtlatandeFromJsonFile("/rli-example-3.json");

        Utlatande res = converter.fromExternalToInternal(extUtlatande);

        Utlatande ref = buildInternalUtlatande();
        
        assertNotNull(res);
        
        assertLenientEquals(ref, res);
    }

    private se.inera.certificate.modules.rli.model.external.Utlatande readUtlatandeFromJsonFile(String fileName) {

        se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande = null;

        InputStream src = this.getClass().getResourceAsStream(fileName);

        try {
            CustomObjectMapper mapper = new CustomObjectMapper();
            extUtlatande = mapper.readValue(src, se.inera.certificate.modules.rli.model.external.Utlatande.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extUtlatande;
    }

    private Utlatande buildInternalUtlatande() {

        Utlatande utlatande = new Utlatande();
        utlatande.setUtlatandeId("f6fb361a-e31d-48b8-8657-99b63912dd9b");
        LocalDateTime signeringsDatum = new LocalDateTime(2013, 8, 12, 11, 25);
        utlatande.setSigneringsDatum(signeringsDatum);
        LocalDateTime skickatDatum = new LocalDateTime(2013, 8, 12, 11, 25, 30);
        utlatande.setSkickatDatum(skickatDatum);
        utlatande.setTypAvUtlatande("RLI");
        utlatande.setKommentarer(Arrays.asList("Övriga upplysningar"));

        Arrangemang arr = new Arrangemang();
        arr.setArrangemangdatum("2013-08-22");
        arr.setArrangemangstyp(ArrangemangsTyp.RESA);
        arr.setAvbestallningsdatum("2013-08");
        arr.setPlats("New York");
        arr.setBokningsreferens("12345678-90");
        arr.setBokningsdatum("2013-01-01");
        utlatande.setArrangemang(arr);

        Patient pat = new Patient();
        pat.setFornamn("Test");
        pat.setEfternamn("Testsson");
        pat.setPostadress("Teststigen 1");
        pat.setPostnummer("123 45");
        pat.setPostort("Teststaden");
        pat.setPersonId("19121212-1212");
        pat.setFullstandigtnamn("Test Testsson");
        utlatande.setPatient(pat);

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarId("vardgivareId");
        vardgivare.setVardgivarnamn("Testvårdgivaren");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setEnhetsId("1234-567");
        vardenhet.setEnhetsnamn("Tolvberga vårdcentral");
        vardenhet.setPostadress("Tolvstigen 12");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("Tolvberga");
        vardenhet.setTelefonnummer("012-345678");
        vardenhet.setePost("ingen@alls.se");

        HoSPersonal hosPers = new HoSPersonal();
        hosPers.setFullstandigtnamn("Doktor Alban");
        hosPers.setPersonId("19101010-1010");
        hosPers.setVardenhet(vardenhet);
        utlatande.setSkapadAv(hosPers);

        Rekommendation rekommendation = new Rekommendation();
        rekommendation.setRekommendationskod(RekommendationsKod.REK1);
        rekommendation.setSjukdomskannedom(SjukdomsKannedom.SJK2);
        utlatande.setRekommendation(rekommendation);

        Undersokning undersokning = new Undersokning();
        undersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_SJUK);
        undersokning.setUndersokningsdatum("2013-08-12");
        undersokning.setUndersokningsplats("Tolvberga vårdcentral");
        undersokning.setKomplikationstyrkt(KomplikationStyrkt.AV_PATIENT);
        undersokning.setForstaundersokningsdatum("2010-01");
        undersokning.setForstaundersokningsplats("Trestadens lasarett");
        utlatande.setUndersokning(undersokning);

        return utlatande;
    }

}
