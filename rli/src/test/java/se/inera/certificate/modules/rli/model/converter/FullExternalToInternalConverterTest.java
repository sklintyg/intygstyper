package se.inera.certificate.modules.rli.model.converter;

import static org.junit.Assert.assertNotNull;
import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsKod;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomskannedomKod;
import se.inera.certificate.modules.rli.model.converter.ExternalToInternalConverter;
import se.inera.certificate.modules.rli.model.internal.mi.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.mi.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.mi.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.mi.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.mi.Patient;
import se.inera.certificate.modules.rli.model.internal.mi.Rekommendation;
import se.inera.certificate.modules.rli.model.internal.mi.Undersokning;
import se.inera.certificate.modules.rli.model.internal.mi.Utlatande;
import se.inera.certificate.modules.rli.model.internal.mi.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.mi.Vardgivare;

public class FullExternalToInternalConverterTest {

    private ApplicationContext ctx;

    private ExternalToInternalConverter converter;

    public FullExternalToInternalConverterTest() {
        this.ctx = new AnnotationConfigApplicationContext(SpringTestConfig.class);
    }

    @Before
    public void setUp() {
        this.converter = this.ctx.getBean("externalToInternalConverter", ExternalToInternalConverter.class);
    }

    @Test
    public void testConvertUtlatande() throws Exception {

        se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande = readUtlatandeFromJsonFile("/rli-example-3.json");

        Utlatande res = converter.convertUtlatandeFromExternalToInternal(extUtlatande);

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
        utlatande.setUtlatandeid("f6fb361a-e31d-48b8-8657-99b63912dd9b");
        LocalDateTime signeringsDatum = new LocalDateTime(2013, 8, 12, 11, 25);
        utlatande.setSigneringsdatum(signeringsDatum);
        LocalDateTime skickatDatum = new LocalDateTime(2013, 8, 12, 11, 25, 30);
        utlatande.setSkickatdatum(skickatDatum);
        utlatande.setTypAvUtlatande("RLI");
        utlatande.setKommentarer(Arrays.asList("Övriga upplysningar"));

        Arrangemang arr = new Arrangemang();
        arr.setArrangemangsdatum("2013-08-22");
        arr.setArrangemangstyp(ArrangemangsKod.RESA);
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
        pat.setPersonid("19121212-1212");
        pat.setFullstandigtNamn("Test Testsson");
        utlatande.setPatient(pat);

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("Testvårdgivaren");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setEnhetsid("1234-567");
        vardenhet.setEnhetsnamn("Tolvberga vårdcentral");
        vardenhet.setPostadress("Tolvstigen 12");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("Tolvberga");
        vardenhet.setTelefonnummer("012-345678");
        vardenhet.setEpost("ingen@alls.se");

        /** Used to set UtforsVid for second Aktivitet */
        Vardgivare vardgivare2 = new Vardgivare();
        vardgivare2.setVardgivarid("vardgivare_test");
        vardgivare2.setVardgivarnamn("Testvårdgivaren");

        Vardenhet vardenhet2 = new Vardenhet();
        vardenhet2.setVardgivare(vardgivare2);
        vardenhet2.setEnhetsid("vardenhet_test");
        vardenhet2.setEnhetsnamn("Tolvberga vårdcentral");
        vardenhet2.setPostadress("Tolvstigen 12");
        vardenhet2.setPostnummer("12345");
        vardenhet2.setPostort("Tolvberga");
        vardenhet2.setTelefonnummer("012-345678");
        vardenhet2.setEpost("ingen@alls.se");

        HoSPersonal hosPers = new HoSPersonal();
        hosPers.setFullstandigtNamn("Doktor Alban");
        hosPers.setPersonid("19101010-1010");
        hosPers.setBefattning("Specialistläkare");
        hosPers.setVardenhet(vardenhet);
        utlatande.setSkapadAv(hosPers);

        Rekommendation rekommendation = new Rekommendation();
        rekommendation.setRekommendationskod(RekommendationsKod.REK1);
        rekommendation.setSjukdomskannedom(SjukdomskannedomKod.SJK2);
        utlatande.setRekommendation(rekommendation);

        Undersokning undersokning = new Undersokning();
        undersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_SJUK);
        undersokning.setUndersokningsdatum("2013-08-12");
        // undersokning.setUndersokningsplats("Tolvberga vårdcentral");
        
        undersokning.setUtforsVid(vardenhet2);
        
        undersokning.setKomplikationstyrkt(KomplikationStyrkt.AV_PATIENT);
        undersokning.setForstaUndersokningsdatum("2010-01");
        undersokning.setForstaUndersokningsplats("Trestadens lasarett");
        utlatande.setUndersokning(undersokning);

        return utlatande;
    }

}
