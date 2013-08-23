package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import se.inera.certificate.common.v1.PartialDateInterval;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.common.Enhet;
import se.inera.certificate.modules.rli.model.external.common.HosPersonal;
import se.inera.certificate.modules.rli.model.external.common.Patient;
import se.inera.certificate.modules.rli.model.external.common.Vardgivare;
import se.inera.certificate.modules.rli.model.internal.HoSPersonal;

/**
 * Unit test for the ExternalToInteralConverter. This test is Spring configured.
 * 
 * @author Niklas Pettersson, R2M
 * 
 */
public class ExternalToInternalConverterTest {

    private ApplicationContext ctx;

    private ExternalToInternalConverterImpl converter;

    public ExternalToInternalConverterTest() {
        this.ctx = new AnnotationConfigApplicationContext(SpringTestConfig.class);
    }

    @Before
    public void setUp() {
        this.converter = this.ctx.getBean("externalToInternalConverter", ExternalToInternalConverterImpl.class);
    }

    @Test
    public void testConvertUtlatande() {

        Utlatande extUtlatande = readUtlatandeFromJsonFile("/rli-example-1-external.json");

        se.inera.certificate.modules.rli.model.internal.Utlatande res = converter.fromExternalToInternal(extUtlatande);

        assertNotNull(res);

        assertNotNull(res.getUtlatandeId());
        assertNotNull(res.getSigneringsDatum());
        assertNotNull(res.getSkickatDatum());
        assertNotNull(res.getTypAvUtlatande());

        assertNotNull(res.getArrangemang());
        assertNotNull(res.getPatient());
        assertNotNull(res.getSkapatAv());
        assertNotNull(res.getUndersokning());
        assertNotNull(res.getRekommendation());
    }

    private Utlatande readUtlatandeFromJsonFile(String fileName) {

        Utlatande extUtlatande = null;

        InputStream src = this.getClass().getResourceAsStream(fileName);

        try {
            CustomObjectMapper mapper = new CustomObjectMapper();
            extUtlatande = mapper.readValue(src, Utlatande.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extUtlatande;
    }

    @Test
    public void testConvertToIntPatient() {

        Patient extPatient = buildPatient();

        se.inera.certificate.modules.rli.model.internal.Patient res = converter.convertToIntPatient(extPatient);

        assertNotNull(res);

        assertEquals("19121212-1212", res.getPersonId());
        assertEquals("Abel Baker", res.getForNamn());
        assertEquals("Smith Doe", res.getEfterNamn());
        assertEquals("Abel Baker Smith Doe", res.getFullstandigtNamn());
        assertNotNull(res.getPostAdress());

    }

    private Patient buildPatient() {

        Patient pat = new Patient();

        pat.setPersonId(new Id("PersonId", "19121212-1212"));
        pat.getFornamns().addAll(Arrays.asList("Abel", "Baker"));
        pat.getEfternamns().addAll(Arrays.asList("Smith", "Doe"));
        pat.setAdress("Testgatan 123, 123 45 Teststaden");

        return pat;
    }

    @Test
    public void testConvertToIntArrangemang() {

        Arrangemang extArr = buildArrangemang();
        se.inera.certificate.modules.rli.model.internal.Arrangemang res = converter.convertToIntArrangemang(extArr);

        assertNotNull(res);
        assertNotNull(res.getArrangemangsTyp());
        assertNotNull(res.getBokningsReferens());
        assertNotNull(res.getBokningsDatum());
        assertNotNull(res.getAvbestallningsDatum());
        assertNotNull(res.getArrangemangStartDatum());
    }

    private Arrangemang buildArrangemang() {

        Arrangemang arr = new Arrangemang();
        arr.setArrangemangstyp(new Kod("420008001"));
        arr.setBokningsreferens("1234567-890");
        arr.setPlats("Långtbortistan");
        arr.setBokningsdatum(TestUtils.constructPartial(2013, 8, 6));
        arr.setAvbestallningsdatum(TestUtils.constructPartial(2013, 8, 22));

        PartialDateInterval arrTid = new PartialDateInterval();
        arrTid.setFrom(TestUtils.constructPartial(2013, 9, 1));
        arrTid.setTom(TestUtils.constructPartial(2013, 9, 16));

        arr.setArrangemangstid(arrTid);

        return arr;
    }

    @Test
    public void testConvertToIntHoSPersonal() {

        HosPersonal extHoSPersonal = buildHoSPersonal();
        HoSPersonal res = converter.convertToIntHoSPersonal(extHoSPersonal);

        assertNotNull(res);
        assertNotNull(res.getFullstandigtNamn());
        assertNotNull(res.getPersonId());

    }

    private HosPersonal buildHoSPersonal() {

        HosPersonal hosPers = new HosPersonal();

        hosPers.setPersonalId(new Id("19101010-1010"));
        hosPers.setFullstandigtNamn("Börje Dengroth");
        hosPers.setForskrivarkod("12345-67");

        Enhet vardenhet = buildVardenhet();
        hosPers.setEnhet(vardenhet);

        return hosPers;
    }

    @Test
    public void testConvertToIntVardenhet() {

        Enhet extVardenhet = buildVardenhet();
        se.inera.certificate.modules.rli.model.internal.Vardenhet res = converter.convertToIntVardenhet(extVardenhet);

        assertNotNull(res);

        assertNotNull(res.getEnhetsId());
        assertNotNull(res.getEnhetsNamn());
        assertNotNull(res.getPostAddress());
        assertNotNull(res.getPostNummer());
        assertNotNull(res.getPostOrt());
        assertNotNull(res.getePost());
        assertNotNull(res.getTelefonNummer());
        assertNotNull(res.getVardgivare());
    }

    private Enhet buildVardenhet() {

        Enhet vardenhet = new Enhet();

        vardenhet.setEnhetsId(new Id("123-456"));
        vardenhet.setArbetsplatskod(new Id("1234-56"));
        vardenhet.setEnhetsnamn("Tolvberga Vårdcentral");
        vardenhet.setPostadress("Nollstigen 12");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("Tolvberga");
        vardenhet.setTelefonnummer("012-345678");
        vardenhet.setEpost("ingen@alls.nu");

        Vardgivare vardgivare = buildVardgivare();
        vardenhet.setVardgivare(vardgivare);

        return vardenhet;
    }

    private Vardgivare buildVardgivare() {

        Vardgivare vardgivare = new Vardgivare();

        vardgivare.setVardgivareId(new Id("1234567"));
        vardgivare.setVardgivarnamn("Landstinget");

        return vardgivare;

    }

}
