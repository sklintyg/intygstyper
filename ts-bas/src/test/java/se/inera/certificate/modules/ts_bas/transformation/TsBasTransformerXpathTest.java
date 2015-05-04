package se.inera.certificate.modules.ts_bas.transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.AKTIVITET_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.AKTIVITET_FOREKOMST_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.ENHET_ID_XPATH;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.ENHET_POSTADRESS_XPATH;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.ENHET_POSTNUMMER_XPATH;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.ENHET_VARDINRATTNINGENS_NAMN_XPATH;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.ID_KONTROLL_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.INTYG_AVSER_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.OBSERVATION_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.OBSERVATION_FOREKOMST_CODE_LATERALITET;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.OBSERVATION_FOREKOMST_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.OBSERVATION_VARDE_CODE_LATERALITET;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.REKOMMENDATION_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.REKOMMENDATION_VARDE_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.VARDGIVARE_ID_XPATH;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.VARDGIVARE_NAMN_XPATH;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.booleanXPath;
import static se.inera.certificate.modules.ts_bas.transformation.XPathExpressions.stringXPath;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import se.inera.certificate.modules.ts_bas.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_parent.transformation.XslTransformer;
import se.inera.certificate.modules.ts_parent.transformation.test.BooleanXPathExpression;
import se.inera.certificate.modules.ts_parent.transformation.test.KorkortsKodToIntygAvserMapping;
import se.inera.certificate.modules.ts_parent.transformation.test.XPathEvaluator;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.AlkoholNarkotikaLakemedel;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypBas;
import se.inera.intygstjanster.ts.services.v1.HjartKarlSjukdomar;
import se.inera.intygstjanster.ts.services.v1.HorselBalanssinne;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsBas;
import se.inera.intygstjanster.ts.services.v1.Medvetandestorning;
import se.inera.intygstjanster.ts.services.v1.OvrigMedicinering;
import se.inera.intygstjanster.ts.services.v1.Patient;
import se.inera.intygstjanster.ts.services.v1.RorelseorganenFunktioner;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.SynskarpaMedKorrektion;
import se.inera.intygstjanster.ts.services.v1.SynskarpaUtanKorrektion;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;
import se.inera.intygstjanster.ts.services.v1.Utvecklingsstorning;
import se.inera.intygstjanster.ts.services.v1.Vardenhet;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v1.Utlatande;

public class TsBasTransformerXpathTest {

    private static XslTransformer transformer;

    @BeforeClass
    public static void setup() {
        transformer = new XslTransformer("xsl/transform-ts-bas.xsl");
    }

    @Test
    public void testMaximaltIntyg() throws IOException, ParserConfigurationException, JAXBException, XPathExpressionException, SAXException,
            TransformerException {
        File file = new ClassPathResource("scenarios/transport/valid-maximal.xml").getFile();
        String xmlContent = FileUtils.readFileToString(file);
        TSBasIntyg utlatande = JAXB.unmarshal(file, RegisterTSBasType.class).getIntyg();
        String transformed = transformer.transform(xmlContent);

        // Create an xPath evaluator that operates on the transport model.
        XPathEvaluator xPath = createXPathEvaluator(transformed);

        // Check utlatande against xpath
        assertEquals("UtlatandeTyp", UtlatandeKod.getCurrentVersion().getTypForTransportConvertion(),
                xPath.evaluate(XPathExpressions.TYP_AV_UTLATANDE_XPATH));

        assertEquals("Utlatande-utgåva", utlatande.getUtgava(), xPath.evaluate(XPathExpressions.TS_UTGAVA_XPATH));

        assertEquals("Utlatande-utgåva", utlatande.getVersion(), xPath.evaluate(XPathExpressions.TS_VERSION_XPATH));

        // Patient
        Patient patient = utlatande.getGrundData().getPatient();

        assertEquals("Patient förnamn", patient.getFornamn(), xPath.evaluate(XPathExpressions.INVANARE_FORNAMN_XPATH));

        assertEquals("Patient efternamn", patient.getEfternamn(), xPath.evaluate(XPathExpressions.INVANARE_EFTERNAMN_XPATH));

        assertEquals("Patient personnummer", patient.getPersonId().getExtension(), xPath.evaluate(XPathExpressions.INVANARE_PERSONNUMMER_XPATH));

        assertEquals("Patient postadress", patient.getPostadress(), xPath.evaluate(XPathExpressions.INVANARE_POSTADRESS_XPATH));

        assertEquals("Patient postnummer", patient.getPostnummer(), xPath.evaluate(XPathExpressions.INVANARE_POSTNUMMER_XPATH));

        assertEquals("Patient postnummer", patient.getPostort(), xPath.evaluate(XPathExpressions.INVANARE_POSTORT_XPATH));

        // Signeringsdatum
        assertEquals("Signeringsdatum", utlatande.getGrundData().getSigneringsTidstampel(), xPath.evaluate(XPathExpressions.SIGNERINGSDATUM_XPATH));

        // Skapad Av
        SkapadAv skapadAv = utlatande.getGrundData().getSkapadAv();

        assertEquals("Skapad av - befattningar", skapadAv.getBefattningar().get(0), xPath.evaluate(XPathExpressions.SKAPAD_AV_BEFATTNING_XPATH));

        assertEquals("Skapad av - fullständigt namn", skapadAv.getFullstandigtNamn(),
                xPath.evaluate(XPathExpressions.SKAPAD_AV_NAMNFORTYDLIGANDE_XPATH));

        assertEquals("Skapad av - hsa-id", skapadAv.getPersonId().getExtension(), xPath.evaluate(XPathExpressions.SKAPAD_AV_HSAID_XPATH));

        // TODO is this correct?!
        // assertEquals("Skapad av - specialitet", skapadAv.getSpecialiteter().get(0),
        // xPath.evaluate(XPathExpressions.SKAPAD_AV_SPECIALISTKOMPETENS_BESKRVNING_XPATH));

        // Vardenhet
        Vardenhet vardenhet = skapadAv.getVardenhet();
        assertEquals("Enhet - enhetsid", vardenhet.getEnhetsId().getExtension(), xPath.evaluate(ENHET_ID_XPATH));

        assertEquals("Enhet - enhetsnamn", vardenhet.getEnhetsnamn(), xPath.evaluate(ENHET_VARDINRATTNINGENS_NAMN_XPATH));

        assertEquals("Enhet - postadress", vardenhet.getPostadress(), xPath.evaluate(ENHET_POSTADRESS_XPATH));

        assertEquals("Enhet - postnummer", vardenhet.getPostnummer(), xPath.evaluate(ENHET_POSTNUMMER_XPATH));

        assertEquals("Enhet - postort", vardenhet.getPostort(), xPath.evaluate(XPathExpressions.ENHET_POSTORT_XPATH));

        assertEquals("Enhet - postort", vardenhet.getTelefonnummer(), xPath.evaluate(XPathExpressions.ENHET_TELEFONNUMMER_XPATH));

        // Vardgivare
        assertEquals("Enhet - vardgivare - id", vardenhet.getVardgivare().getVardgivarid().getExtension(),
                xPath.evaluate(VARDGIVARE_ID_XPATH));

        assertEquals("Enhet - vardgivare - id", vardenhet.getVardgivare().getVardgivarnamn(), xPath.evaluate(VARDGIVARE_NAMN_XPATH));

        // IntygAvser
        for (KorkortsbehorighetTsBas t : utlatande.getIntygAvser().getKorkortstyp()) {
            KorkortsKodToIntygAvserMapping k = KorkortsKodToIntygAvserMapping.valueOf(t.name());
            assertTrue(xPath.evaluate(booleanXPath(INTYG_AVSER_TEMPLATE, k.getIntygAvser())));
        }

        // ID-kontroll
        assertTrue(xPath.evaluate(booleanXPath(ID_KONTROLL_TEMPLATE,
                utlatande.getIdentitetStyrkt().getIdkontroll().value())));

        // Aktiviteter
        boolean harGlasStyrkaOver8Dioptrier = utlatande.getSynfunktion().isHarGlasStyrkaOver8Dioptrier();
        assertEquals("8 dioptrier", harGlasStyrkaOver8Dioptrier,
                xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT17")));

        boolean harVardInsats = utlatande.getAlkoholNarkotikaLakemedel().isHarVardinsats();
        assertEquals("Har vårdinsats för missbruk", harVardInsats,
                xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT15")));

        boolean provtagningBehovs = utlatande.getAlkoholNarkotikaLakemedel().isHarVardinsatsProvtagningBehov();
        assertEquals("provtagning narkotika", provtagningBehovs,
                xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT14")));

        assertEquals("Sjukhusvård eller läkarkontakt", utlatande.getSjukhusvard().isHarSjukhusvardEllerLakarkontakt(),
                xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT19")));

        assertEquals("Sjukhusvård eller läkarkontakt - datum", utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktDatum(),
                xPath.evaluate(XPathExpressions.VARD_PA_SJUKHUS_TID_XPATH));

        assertEquals("Sjukhusvård eller läkarkontakt - plats", utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktVardinrattning(),
                xPath.evaluate(XPathExpressions.VARD_PA_SJUKHUS_VARDINRATTNING_XPATH));

        assertEquals("Sjukhusvård eller läkarkontakt - anledning", utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktAnledning(),
                xPath.evaluate(stringXPath(AKTIVITET_BESKRIVNING_TEMPLATE, "AKT19")));

        // Observationer
        // Synobservationer
        boolean synfaltsdefekter = utlatande.getSynfunktion().isHarSynfaltsdefekt();
        assertEquals("Synfältsdefekter", synfaltsdefekter, xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.4")));

        boolean nattblindhet = utlatande.getSynfunktion().isHarNattblindhet();
        assertEquals("Nattblindhet", nattblindhet, xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.6")));

        boolean progressivOgonsjukdom = utlatande.getSynfunktion().isHarProgressivOgonsjukdom();
        assertEquals("Progressiv ögonsjukdom", progressivOgonsjukdom, xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS1")));

        boolean diplopi = utlatande.getSynfunktion().isHarDiplopi();
        assertEquals("Diplopi", diplopi, xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2")));

        boolean nystagmus = utlatande.getSynfunktion().isHarNystagmus();
        assertEquals("Nystagmus", nystagmus, xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H55.9")));

        SynskarpaUtanKorrektion utanKorr = utlatande.getSynfunktion().getSynskarpaUtanKorrektion();

        SynskarpaMedKorrektion medKorr = utlatande.getSynfunktion().getSynskarpaMedKorrektion();

        assertEquals("Ej korrigerad synskärpa höger", String.valueOf(utanKorr.getHogerOga()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "420050001", "24028007")));

        assertEquals("Korrigerad synskärpa höger", String.valueOf(medKorr.getHogerOga()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "397535007", "24028007")));

        assertEquals("Ej korrigerad synskärpa vänster", String.valueOf(utanKorr.getVansterOga()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "420050001", "7771000")));

        assertEquals("Korrigerad synskärpa vänster", String.valueOf(medKorr.getVansterOga()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "397535007", "7771000")));

        assertEquals("Ej korrigerad synskärpa binokulärt", String.valueOf(utanKorr.getBinokulart()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "420050001", "51440002")));

        assertEquals("Korrigerad synskärpa binokulärt", String.valueOf(medKorr.getBinokulart()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "397535007", "51440002")));

        assertEquals("Kontaktlinser höger", medKorr.isHarKontaktlinsHogerOga(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_CODE_LATERALITET, "285049007", "24028007")));

        assertEquals("Kontaktlinser vänster", medKorr.isHarKontaktlinsVansterOga(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_CODE_LATERALITET, "285049007", "7771000")));

        // Hörsel & balans
        HorselBalanssinne horselBalans = utlatande.getHorselBalanssinne();
        assertEquals("Balansrubbningar", horselBalans.isHarBalansrubbningYrsel(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS2")));

        assertEquals("Svårt uppfatta samtal 4 meter", horselBalans.isHarSvartUppfattaSamtal4Meter(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS3")));

        // Rörelseorganens förmåga
        RorelseorganenFunktioner rorelse = utlatande.getRorelseorganensFunktioner();

        assertEquals("Har rörelsebegränsning", rorelse.isHarRorelsebegransning(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS4")));

        assertEquals("Rörelsebegränsning beskrivning", rorelse.getRorelsebegransningBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS4")));

        assertEquals("Otillräcklig rörelseförmåga passagerare", rorelse.isHarOtillrackligRorelseformagaPassagerare(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS5")));

        // Hjärt & kärlsjukdom
        HjartKarlSjukdomar hjartKarl = utlatande.getHjartKarlSjukdomar();

        assertEquals("Risk försämrad hjärnfunktion", hjartKarl.isHarRiskForsamradHjarnFunktion(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS6")));

        assertEquals("Tecken på hjärnskada", hjartKarl.isHarHjarnskadaICNS(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS8")));

        assertEquals("Riskfaktorer för stroke", hjartKarl.isHarRiskfaktorerStroke(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS7")));

        assertEquals("Riskfaktorer stroke - beskrivning", hjartKarl.getRiskfaktorerStrokeBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS7")));

        // Diabetes
        DiabetesTypBas diabetes = utlatande.getDiabetes();

        assertEquals("Har diabetes", diabetes.isHarDiabetes(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "73211009")));

        if (diabetes.getDiabetesTyp().name().equals("TYP1")) {
            assertEquals("Diabetes typ1", diabetes.getDiabetesTyp().name(),
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E10")));
        }
        else if (diabetes.getDiabetesTyp().name().equals("TYP2")) {
            assertEquals("Diabetes typ2", diabetes.getDiabetesTyp().name(),
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E11")));
        }

        assertEquals("Diabetes kostbehandling", diabetes.isHarBehandlingKost() != null ? diabetes.isHarBehandlingKost() : false,
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS9")));

        assertEquals("Diabetes insulinbehandling", diabetes.isHarBehandlingInsulin() != null ? diabetes.isHarBehandlingInsulin() : false,
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170747006")));

        assertEquals("Diabetes tablettbehandling", diabetes.isHarBehandlingTabletter() != null ? diabetes.isHarBehandlingTabletter() : false,
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170746002")));

        // Neurologi
        assertEquals("Tecken på neurologisk sjukdom", utlatande.isNeurologiskaSjukdomar(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "407624006")));

        // Medvetandestörning
        Medvetandestorning medvetande = utlatande.getMedvetandestorning();
        assertEquals("Har medvetandestörning", medvetande.isHarMedvetandestorning(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "G40.9")));

        assertEquals("Medvetandestörning - beskrivning", medvetande.getMedvetandestorningBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "G40.9")));

        assertEquals("Har medvetandestörning", utlatande.isHarNjurSjukdom(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS11")));

        // Sviktande kognitiv förmåga
        assertEquals("kognitiv förmåga", utlatande.isHarKognitivStorning(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS12")));

        // Somn vakenhet
        assertEquals("Somn vakenhet", utlatande.isHarSomnVakenhetStorning(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS13")));

        // alkohol och narkotika
        AlkoholNarkotikaLakemedel alkNark = utlatande.getAlkoholNarkotikaLakemedel();

        assertEquals("Tecken på missbruk", alkNark.isHarTeckenMissbruk(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS14")));

        assertEquals("Läkemedelsbruk", alkNark.isHarLakarordineratLakemedelsbruk(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS15")));

        assertEquals("Läkemedelsbruk - beskrivning", alkNark.getLakarordineratLakemedelOchDos(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS15")));

        // Psykisk sjukdom
        assertEquals("Läkemedelsbruk", utlatande.isHarPsykiskStorning(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS16")));

        // Utvecklingsstörning
        Utvecklingsstorning utvecklingsstorning = utlatande.getUtvecklingsstorning();
        assertEquals("Har psykisk utvecklingsstörning", utvecklingsstorning.isHarPsykiskUtvecklingsstorning(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "129104009")));

        assertEquals("Har ADHD eller damp", utvecklingsstorning.isHarAndrayndrom(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS17")));

        // Stadigvarande medicinering
        OvrigMedicinering medicinering = utlatande.getOvrigMedicinering();
        assertEquals("Har stadigvarande medicinering", medicinering.isHarStadigvarandeMedicinering(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS18")));

        assertEquals("Har stadigvarande medicinering- beskrivning", medicinering.getStadigvarandeMedicineringBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS18")));

        // Rekommendationer
        assertTrue("Rekommendation REK8", xPath.evaluate(new BooleanXPathExpression("utlatande/p:rekommendation/p:rekommendationskod/@code = 'REK8'")));

        for (KorkortsbehorighetTsBas t : utlatande.getBedomning().getKorkortstyp()) {
            KorkortsKodToIntygAvserMapping k = KorkortsKodToIntygAvserMapping.valueOf(t.name());
            assertTrue(String.format("Rekommendationsvärde %s", k.getRekommendation()),
                    xPath.evaluate(booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, k.getRekommendation())));
        }
        if (utlatande.getBedomning().isKanInteTaStallning()) {
            assertEquals("Rekommendationsvärde Kan inte ta ställning (VAR11)", KorkortsKodToIntygAvserMapping.KANINTETASTALLNING.getRekommendation(),
                    xPath.evaluate(stringXPath(REKOMMENDATION_VARDE_TEMPLATE, KorkortsKodToIntygAvserMapping.KANINTETASTALLNING.getRekommendation())));
        }

        if (utlatande.getBedomning().getBehovAvLakareSpecialistKompetens() != null) {
            assertEquals(String.format("REK9 med beskrivning  %s", utlatande.getBedomning().getBehovAvLakareSpecialistKompetens()), utlatande.getBedomning().getBehovAvLakareSpecialistKompetens(),
                    xPath.evaluate(stringXPath(REKOMMENDATION_BESKRIVNING_TEMPLATE, "REK9")));
        }

    }

    private XPathEvaluator createXPathEvaluator(String xml) throws ParserConfigurationException,
            JAXBException, SAXException, IOException, TransformerException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        namespaces.bindDefaultNamespaceUri("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1");
        namespaces.bindNamespaceUri("ns2", "urn:riv:clinicalprocess:healthcond:certificate:types:1");
        namespaces.bindNamespaceUri("p", "urn:riv:clinicalprocess:healthcond:certificate:1");
        namespaces.bindNamespaceUri("p2", "urn:riv:clinicalprocess:healthcond:certificate:ts-bas:1");
        xPath.setNamespaceContext(namespaces);
        Node document = generateDocumentFor(xml);

        return new XPathEvaluator(xPath, document);
    }

    private Node generateDocumentFor(String xml) throws ParserConfigurationException, JAXBException, SAXException, IOException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Node node = parser.newDocument();

        InputStream is = new ByteArrayInputStream(xml.getBytes());

        RegisterCertificateType register = JAXB.unmarshal(is, RegisterCertificateType.class);

        JAXBElement<Utlatande> jaxbElement = new JAXBElement<Utlatande>(new QName("ns3:utlatande"), Utlatande.class, register.getUtlatande());
        JAXBContext context = JAXBContext.newInstance(Utlatande.class);
        context.createMarshaller().marshal(jaxbElement, node);

        // // Output the Document
        // TransformerFactory tf = TransformerFactory.newInstance();
        // Transformer t = tf.newTransformer();
        // DOMSource source = new DOMSource(node);
        // StreamResult result = new StreamResult(System.out);
        // t.transform(source, result);
        return node;
    }
}
