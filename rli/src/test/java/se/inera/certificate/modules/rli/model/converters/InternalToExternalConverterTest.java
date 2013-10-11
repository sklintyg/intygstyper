package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;
import static se.inera.certificate.modules.rli.model.codes.HSpersonalTyp.HSA_ID;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsTyp;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomsKannedom;
import se.inera.certificate.modules.rli.model.codes.UtforarTypKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Utlatande;

import se.inera.certificate.modules.rli.model.edit.Arrangemang;
import se.inera.certificate.modules.rli.model.edit.Graviditet;
import se.inera.certificate.modules.rli.model.edit.HoSPersonal;
import se.inera.certificate.modules.rli.model.edit.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.edit.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.edit.Patient;
import se.inera.certificate.modules.rli.model.edit.Rekommendation;
import se.inera.certificate.modules.rli.model.edit.Undersokning;
import se.inera.certificate.modules.rli.model.edit.Utforare;
import se.inera.certificate.modules.rli.model.edit.Vardenhet;
import se.inera.certificate.modules.rli.model.edit.Vardgivare;

/**
 * Unit test for InternalToExternalConverter
 * 
 * @author erik
 * 
 */
public class InternalToExternalConverterTest {

    private InternalToExternalConverterImpl converter;

    @Before
    public void setUp() throws Exception {
        this.converter = new InternalToExternalConverterImpl();
    }

    @Test
    public void testIntHosPersonalToExt() {
        HoSPersonal intHsp = buildHosPersonal();
        HosPersonal extHsp = converter.convertHoSPersonal(intHsp);

        assertNotNull(extHsp);
        assertEquals("Johnathan Swift", extHsp.getNamn());
        assertEquals("Vårdenhetsnamn", extHsp.getVardenhet().getNamn());
        assertEquals("VårdenhetsID", extHsp.getVardenhet().getId().getExtension());
        assertEquals("epost@vardenhet.se", extHsp.getVardenhet().getEpost());
        assertEquals("Vårdenhetsvägen", extHsp.getVardenhet().getPostadress());
        assertEquals("101010", extHsp.getVardenhet().getPostnummer());
        assertEquals("Stockholm", extHsp.getVardenhet().getPostort());
        assertEquals("08-7777777", extHsp.getVardenhet().getTelefonnummer());

        assertEquals("VårdgivarID", extHsp.getVardenhet().getVardgivare().getId().getExtension());
        assertEquals("Vårdgivarnamn", extHsp.getVardenhet().getVardgivare().getNamn());
    }

    private HoSPersonal buildHosPersonal() {
        HoSPersonal hsp = new HoSPersonal();
        hsp.setFullstandigtNamn("Johnathan Swift");
        hsp.setPersonid("19121212-1212");
        hsp.setVardenhet(buildVardenhet());
        return hsp;

    }

    private Vardenhet buildVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("VårdenhetsID");
        vardenhet.setEnhetsnamn("Vårdenhetsnamn");
        vardenhet.setEpost("epost@vardenhet.se");
        vardenhet.setPostadress("Vårdenhetsvägen");
        vardenhet.setPostnummer("101010");
        vardenhet.setPostort("Stockholm");
        vardenhet.setTelefonnummer("08-7777777");
        vardenhet.setVardgivare(buildVardgivare());
        return vardenhet;
    }

    private Vardgivare buildVardgivare() {
        Vardgivare vg = new Vardgivare();
        vg.setVardgivarid("VårdgivarID");
        vg.setVardgivarnamn("Vårdgivarnamn");
        return vg;
    }

    @Test
    public void testIntPatientToExt() {
        Patient intP = buildPatient();
        se.inera.certificate.model.Patient extP = converter.convertPatient(intP);

        assertNotNull(extP);
        assertEquals("Johan", extP.getFornamn().get(0));
        assertEquals("Johansson", extP.getEfternamn());
        assertEquals("Mutumba", extP.getMellannamn().get(0));
        assertEquals("Johan Mutumba Johansson", extP.getFullstandigtNamn());
        assertEquals("19121212-1212", extP.getId().getExtension());
        assertEquals("Genvägen", extP.getPostadress());
        assertEquals("010101", extP.getPostnummer());
        assertEquals("Skövde", extP.getPostort());

    }

    private Patient buildPatient() {
        Patient patient = new Patient();

        patient.setEfternamn("Johansson");
        patient.setFornamn("Johan");
        patient.setMellannamn("Mutumba");
        patient.setFullstandigtNamn("Johan Mutumba Johansson");
        patient.setPersonid("19121212-1212");
        patient.setPostadress("Genvägen");
        patient.setPostnummer("010101");
        patient.setPostort("Skövde");

        return patient;
    }

    @Test
    public void testIntArrangemangToExt() {
        Arrangemang intArr = buildArrangemang();
        se.inera.certificate.modules.rli.model.external.Arrangemang extArr = converter.convertArrangemang(intArr);

        assertNotNull(extArr);
        assertEquals("2001-03-03", extArr.getArrangemangstid().getFrom().toString("yyyy-MM-dd"));
        assertEquals(ArrangemangsTyp.RESA.getCode(), extArr.getArrangemangstyp().getCode());
        assertEquals("2001-02-02", extArr.getAvbestallningsdatum().toString("yyyy-MM-dd"));
        assertEquals("2001-01-01", extArr.getBokningsdatum().toString("yyyy-MM-dd"));
        assertEquals("Tegucigalpa", extArr.getPlats());
        assertEquals("Bokningsreferens", extArr.getBokningsreferens());

    }

    private Arrangemang buildArrangemang() {
        Arrangemang arr = new Arrangemang();

        arr.setArrangemangsdatum("2001-03-03");
        arr.setArrangemangstyp(ArrangemangsTyp.RESA);
        arr.setAvbestallningsdatum("2001-02-02");
        arr.setBokningsdatum("2001-01-01");
        arr.setBokningsreferens("Bokningsreferens");
        arr.setPlats("Tegucigalpa");

        return arr;

    }

    @Test
    public void testIntSjukUndersokningToExtObservation() {
        Undersokning undersokning = buildUndersokningSjuk();
        List<Observation> obs = converter.convertObservationer(undersokning);

        assertNotNull(obs);
        assertEquals(ObservationsKod.SJUKDOM.getCode(), obs.get(0).getObservationskod().getCode());
    }

    @Test
    public void testSjukUndersokningToExtAktivitet() {
        Undersokning undersokning = buildUndersokningSjuk();
        List<Aktivitet> akt = converter.convertAktiviteter(undersokning);
        Aktivitet facit = buildSjukAktivitetFacit();

        assertEquals(false, akt.isEmpty());

        assertNotNull(facit.getUtforsVid().getNamn());
        assertNotNull(akt.get(0).getUtforsVid().getNamn());

        // assertEquals(facit.getUtforsVid().getNamn(), akt.get(0).getUtforsVid().getNamn());

    }

    private Undersokning buildUndersokningSjuk() {
        Undersokning uSjuk = new Undersokning();
        uSjuk.setUndersokningsdatum("2011-01-01");
        uSjuk.setOrsakforavbokning(OrsakAvbokning.RESENAR_SJUK);
        uSjuk.setUndersokningsplats("ExternalVårdenhetsnamn");
        uSjuk.setKomplikationstyrkt(KomplikationStyrkt.AV_HOS_PERSONAL);
        uSjuk.setUtforsVid(buildVardenhet());
        uSjuk.setUtforsAv(buildUtforsAv());

        return uSjuk;
    }

    private Utforare buildUtforsAv() {
        HoSPersonal antasAv = new HoSPersonal();
        antasAv.setFullstandigtNamn("Dr Jekyll");
        antasAv.setPersonid("190101010101");
        antasAv.setVardenhet(buildVardenhet());

        Utforare utforsAv = new Utforare();
        utforsAv.setAntasAv(antasAv);
        utforsAv.setUtforartyp(UtforarTypKod.AV_HOS_PERSONAL.getCode());
        return utforsAv;
    }

    private Aktivitet buildSjukAktivitetFacit() {
        Aktivitet akt = new Aktivitet();
        akt.setAktivitetskod(buildAktivitetskod(AktivitetsKod.KLINISK_UNDERSOKNING));

        PartialInterval tid = new PartialInterval();
        tid.setFrom(PartialConverter.stringToPartial("2011-01-01"));
        tid.setTom(PartialConverter.stringToPartial("2011-01-01"));
        akt.setAktivitetstid(tid);
        akt.setUtforsVid(buildExternalVardenhet());
        return akt;
    }

    @Test
    public void testIntGravidUndersokningToExtObservation() {
        Undersokning undersokning = buildUndersokningGravid();
        List<Observation> obs = converter.convertObservationer(undersokning);

        assertEquals(2, obs.size());
        assertEquals(ObservationsKod.GRAVIDITET.getCode(), obs.get(1).getObservationskod().getCode());
    }

    @Test
    public void testGravidUndersokningToExtAktivitet() {
        Undersokning undersokning = buildUndersokningGravid();
        List<Aktivitet> converted = converter.convertAktiviteter(undersokning);
        List<Aktivitet> facit = buildGravidAktivitetFacit();

        assertEquals(false, converted.isEmpty());
        assertEquals(facit.get(0).getAktivitetskod().getCode(), converted.get(0).getAktivitetskod().getCode());
        assertEquals(facit.get(1).getAktivitetskod().getCode(), converted.get(1).getAktivitetskod().getCode());

        assertEquals("Hemma", converted.get(0).getPlats());
        /** Is it enough to assume the First Aktivitet only has Plats attribute? */
        assertEquals("ExternalVårdenhetsnamn", converted.get(0).getUtforsVid().getNamn());

    }

    private List<Aktivitet> buildGravidAktivitetFacit() {
        List<Aktivitet> akts = new ArrayList<Aktivitet>();

        /** First Aktivitet */
        Aktivitet akt1 = new Aktivitet();
        akt1.setAktivitetskod(buildAktivitetskod(AktivitetsKod.FORSTA_UNDERSOKNING));

        PartialInterval tid1 = new PartialInterval();
        tid1.setFrom(PartialConverter.stringToPartial("2010-01-01"));
        tid1.setTom(PartialConverter.stringToPartial("2010-01-01"));
        akt1.setAktivitetstid(tid1);
        akt1.setPlats("Hemma");
        // akt1.setUtforsVid(buildExternalVardenhet());
        akts.add(akt1);

        /** Second Aktivitet */
        Aktivitet akt2 = new Aktivitet();
        akt2.setAktivitetskod(buildAktivitetskod(AktivitetsKod.KLINISK_UNDERSOKNING));

        PartialInterval tid2 = new PartialInterval();
        tid2.setFrom(PartialConverter.stringToPartial("2010-12-31"));
        tid2.setTom(PartialConverter.stringToPartial("2010-12-31"));
        akt2.setAktivitetstid(tid2);
        akt2.setUtforsVid(buildExternalVardenhet());
        akts.add(akt2);

        return akts;
    }

    private Undersokning buildUndersokningGravid() {
        Undersokning uGravid = new Undersokning();

        uGravid.setOrsakforavbokning(OrsakAvbokning.RESENAR_GRAVID);

        /** Add Forstaundersokning */
        uGravid.setForstaUndersokningsdatum("2010-01-01");
        uGravid.setForstaUndersokningsplats("Hemma");
        uGravid.setGraviditet(buildGraviditet());

        /** Add second Undersokning */
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivare_test");
        vardgivare.setVardgivarnamn("Testvårdgivaren");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setEnhetsid("arbetsplatskod");
        vardenhet.setEpost("external@epost.se");
        vardenhet.setEnhetsnamn("ExternalVårdenhetsnamn");
        vardenhet.setPostadress("Vårdenhetsadress");
        vardenhet.setPostnummer("11111");
        vardenhet.setPostort("Solna");
        vardenhet.setTelefonnummer("1337");

        uGravid.setUtforsVid(vardenhet);
        uGravid.setUndersokningsdatum("2010-12-31");

        uGravid.setKomplikationstyrkt(KomplikationStyrkt.AV_HOS_PERSONAL);
        return uGravid;
    }

    private Graviditet buildGraviditet() {
        Graviditet grav = new Graviditet();
        grav.setBeraknatForlossningsdatum("2010-12-30");
        return grav;
    }

    private se.inera.certificate.model.Vardenhet buildExternalVardenhet() {
        se.inera.certificate.model.Vardenhet vardenhet = new se.inera.certificate.model.Vardenhet();
        vardenhet.setId(new Id(HSA_ID.getCodeSystem(), "arbetsplatskod"));
        vardenhet.setEpost("external@epost.se");
        vardenhet.setNamn("ExternalVårdenhetsnamn");
        vardenhet.setPostadress("Vårdenhetsadress");
        vardenhet.setPostnummer("11111");
        vardenhet.setPostort("Solna");
        vardenhet.setTelefonnummer("1337");
        vardenhet.setVardgivare(buildExternalVardgivare());
        return vardenhet;
    }

    private se.inera.certificate.model.Vardgivare buildExternalVardgivare() {
        se.inera.certificate.model.Vardgivare vg = new se.inera.certificate.model.Vardgivare();
        vg.setId(new Id(HSA_ID.getCodeSystem(), "VårdgivarID"));
        vg.setNamn("Vårdgivarnamn");
        return vg;
    }

    private Kod buildAktivitetskod(AktivitetsKod aktKod) {
        return new Kod(aktKod.getCodeSystem(), aktKod.getCodeSystemName(), aktKod.getCodeSystemVersion(),
                aktKod.getCode());
    }

    @Test
    public void testConvertFullInternalToExternal() {
        Utlatande extUtlatande = readUtlatandeFromJsonFile("/rli-sjuk-1-template.json");

        se.inera.certificate.modules.rli.model.edit.Utlatande intUtlatande = buildInternalUtlatande();
        
        Utlatande externalConverted = converter.convertUtlatandeFromInternalToExternal(intUtlatande);

        assertLenientEquals(extUtlatande, externalConverted);
    }

    private se.inera.certificate.modules.rli.model.edit.Utlatande buildInternalUtlatande() {
        se.inera.certificate.modules.rli.model.edit.Utlatande utlatande = new se.inera.certificate.modules.rli.model.edit.Utlatande();
        
        utlatande.setUtlatandeid("39f80245-9730-4d76-aaff-b04a2f3cfbe7");
        utlatande.setUtlatandeidroot("???");

        LocalDateTime signeringsDatum = new LocalDateTime(2013, 8, 12, 11, 25);
        utlatande.setSigneringsdatum(signeringsDatum);
        LocalDateTime skickatDatum = new LocalDateTime(2013, 8, 12, 11, 25, 30);
        utlatande.setSkickatdatum(skickatDatum);
        utlatande.setTypAvUtlatande("RLI");
        utlatande.setKommentarer(Arrays.asList("Övriga upplysningar"));

        Patient pat = new Patient();
        pat.setFornamn("Test");
        pat.setEfternamn("Testsson");
        pat.setPostadress("Teststigen 1");
        pat.setPostnummer("123 45");
        pat.setPostort("Teststaden");
        pat.setPersonid("19121212+1212");
        pat.setFullstandigtNamn("Test Testsson");
        utlatande.setPatient(pat);

        Arrangemang arr = new Arrangemang();
        arr.setArrangemangsdatum("2013-07-22");
        arr.setArrangemangslutdatum("2013-08-02");
        arr.setArrangemangstyp(ArrangemangsTyp.RESA);
        arr.setAvbestallningsdatum("2013-08");
        arr.setPlats("New York");
        arr.setBokningsreferens("12345678-90");
        arr.setBokningsdatum("2013-01-01");
        utlatande.setArrangemang(arr);

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivare_test");
        vardgivare.setVardgivarnamn("Testvårdgivaren");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setEnhetsid("vardenhet_test");
        vardenhet.setEnhetsnamn("Testenheten");
        vardenhet.setPostadress("Teststigen 12");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("Teststaden");
        vardenhet.setTelefonnummer("012-345678");
        vardenhet.setEpost("ingen@alls.se");

        HoSPersonal hosPers = new HoSPersonal();
        hosPers.setFullstandigtNamn("Doktor Doktor");
        hosPers.setPersonid("19101010+1010");
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
        undersokning.setKomplikationstyrkt(KomplikationStyrkt.AV_HOS_PERSONAL);
        undersokning.setForstaUndersokningsdatum("2010-01");
        undersokning.setForstaUndersokningsplats("Trestadens lasarett");

        undersokning.setUtforsVid(vardenhet);

        Vardgivare vardgivare2 = new Vardgivare();
        vardgivare2.setVardgivarid("vardgivare_test2");
        vardgivare2.setVardgivarnamn("Testvårdgivaren");

        Vardenhet vardenhet2 = new Vardenhet();
        vardenhet2.setVardgivare(vardgivare2);
        vardenhet2.setEnhetsid("vardenhet_test2");
        vardenhet2.setEnhetsnamn("Elixirkliniken, London");

        HoSPersonal antasAv = new HoSPersonal();
        antasAv.setFullstandigtNamn("Dr Jekyll");
        antasAv.setPersonid("190101010101");
        antasAv.setVardenhet(vardenhet2);

        Utforare utforsAv = new Utforare();
        utforsAv.setAntasAv(antasAv);
        utforsAv.setUtforartyp(UtforarTypKod.AV_HOS_PERSONAL.getCode());
        undersokning.setUtforsAv(utforsAv);
        utlatande.setUndersokning(undersokning);

        return utlatande;
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

}
