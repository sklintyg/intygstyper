package se.inera.certificate.modules.ts_bas.model.converter.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;

@ContextConfiguration(locations = ("/ts-bas-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class ConverterUtilTest {

    @Autowired
    @Qualifier("tsBasModelConverterUtil")
    private ConverterUtil converterUtil;

    private String jsonString = "{\"id\":\"7cc59f3e-ed60-4f79-8f3c-a863a8e43c50\",\"typ\":\"TS_BAS_U06_V06\",\"grundData\":{\"signeringsdatum\":\"2015-03-17T11:26:24.000\",\"skapadAv\":{\"personId\":\"IFV1239877878-1049\",\"fullstandigtNamn\":\"Jan Nilsson\",\"befattningar\":[\"\"],\"specialiteter\":[\"\"],\"vardenhet\":{\"enhetsid\":\"IFV1239877878-1042\",\"enhetsnamn\":\"WebCert-Enhet1\",\"postadress\":\"Storgatan 1\",\"postnummer\":\"12345\",\"postort\":\"Småmåla\",\"telefonnummer\":\"0101234567890\",\"vardgivare\":{\"vardgivarid\":\"IFV1239877878-1041\",\"vardgivarnamn\":\"WebCert-Vårdgivare1\"}}},\"patient\":{\"personId\":\"19121212-1212\",\"fullstandigtNamn\":\"Tolvan Tolvansson\",\"fornamn\":\"Tolvan\",\"efternamn\":\"Tolvansson\",\"postadress\":\"Svensson, Storgatan 1, PL 1234\",\"postnummer\":\"12345\",\"postort\":\"Småmåla\",\"samordningsNummer\":false}},\"vardkontakt\":{\"typ\":\"5880005\",\"idkontroll\":\"ID_KORT\"},\"intygAvser\":{\"korkortstyp\":[{\"type\":\"C1\",\"selected\":true},{\"type\":\"C1E\",\"selected\":false},{\"type\":\"C\",\"selected\":false},{\"type\":\"CE\",\"selected\":false},{\"type\":\"D1\",\"selected\":false},{\"type\":\"D1E\",\"selected\":false},{\"type\":\"D\",\"selected\":false},{\"type\":\"DE\",\"selected\":false},{\"type\":\"TAXI\",\"selected\":false},{\"type\":\"ANNAT\",\"selected\":false}]},\"syn\":{\"synfaltsdefekter\":false,\"nattblindhet\":false,\"progressivOgonsjukdom\":false,\"diplopi\":false,\"nystagmus\":false,\"hogerOga\":{\"utanKorrektion\":1.0,\"medKorrektion\":1.0},\"vansterOga\":{\"utanKorrektion\":1.0,\"medKorrektion\":1.0},\"binokulart\":{\"utanKorrektion\":1.0,\"medKorrektion\":1.1}},\"horselBalans\":{\"balansrubbningar\":false},\"funktionsnedsattning\":{\"funktionsnedsattning\":false},\"hjartKarl\":{\"hjartKarlSjukdom\":false,\"hjarnskadaEfterTrauma\":false,\"riskfaktorerStroke\":false},\"diabetes\":{\"harDiabetes\":false},\"neurologi\":{\"neurologiskSjukdom\":false},\"medvetandestorning\":{\"medvetandestorning\":false},\"njurar\":{\"nedsattNjurfunktion\":false},\"kognitivt\":{\"sviktandeKognitivFunktion\":false},\"somnVakenhet\":{\"teckenSomnstorningar\":false},\"narkotikaLakemedel\":{\"teckenMissbruk\":false,\"foremalForVardinsats\":false,\"lakarordineratLakemedelsbruk\":false},\"psykiskt\":{\"psykiskSjukdom\":false},\"utvecklingsstorning\":{\"psykiskUtvecklingsstorning\":false,\"harSyndrom\":false},\"sjukhusvard\":{\"sjukhusEllerLakarkontakt\":false},\"medicinering\":{\"stadigvarandeMedicinering\":false},\"bedomning\":{\"korkortstyp\":[{\"type\":\"C1\",\"selected\":true},{\"type\":\"C1E\",\"selected\":false},{\"type\":\"C\",\"selected\":false},{\"type\":\"CE\",\"selected\":false},{\"type\":\"D1\",\"selected\":false},{\"type\":\"D1E\",\"selected\":false},{\"type\":\"D\",\"selected\":false},{\"type\":\"DE\",\"selected\":false},{\"type\":\"TAXI\",\"selected\":false},{\"type\":\"ANNAT\",\"selected\":false}]}}";

    @Test
    public void testFromJsonString() throws ModuleException {
        Utlatande utlatande = converterUtil.fromJsonString(jsonString);
        assertNotNull(utlatande);
    }

    @Test
    public void testConvertFromJsonString() throws ModuleException {
        CertificateHolder holder = converterUtil.toCertificateHolder(jsonString);
        Assert.assertEquals("7cc59f3e-ed60-4f79-8f3c-a863a8e43c50", holder.getId());
        Assert.assertEquals("IFV1239877878-1042", holder.getCareUnitId());
        Assert.assertEquals("IFV1239877878-1041", holder.getCareGiverId());
        Assert.assertEquals(jsonString, holder.getDocument());
    }

    @Test
    public void testConvertFromUtlatande() throws ModuleException {
        Utlatande utlatande = converterUtil.fromJsonString(jsonString);
        String formattedJson = converterUtil.toJsonString(utlatande);
        CertificateHolder holder = converterUtil.toCertificateHolder(utlatande);
        Assert.assertEquals("7cc59f3e-ed60-4f79-8f3c-a863a8e43c50", holder.getId());
        Assert.assertEquals("IFV1239877878-1042", holder.getCareUnitId());
        Assert.assertEquals("IFV1239877878-1041", holder.getCareGiverId());
        Assert.assertEquals(formattedJson, holder.getDocument());
    }
}
