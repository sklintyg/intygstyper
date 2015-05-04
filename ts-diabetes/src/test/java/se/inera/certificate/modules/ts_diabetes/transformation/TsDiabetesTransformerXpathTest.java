package se.inera.certificate.modules.ts_diabetes.transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.ENHET_ID_XPATH;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.ENHET_POSTADRESS_XPATH;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.ENHET_POSTNUMMER_XPATH;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.ENHET_VARDINRATTNINGENS_NAMN_XPATH;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.ID_KONTROLL_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.INTYG_AVSER_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.AKTIVITET_FOREKOMST_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.OBSERVATION_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.OBSERVATION_FOREKOMST_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.OBSERVATION_FOREKOMST_VARDE_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.OBSERVATION_VARDE_CODE_LATERALITET;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.OBSERVATION_TID_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.REKOMMENDATION_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.REKOMMENDATION_VARDE_CODE_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.REKOMMENDATION_VARDE_BOOL_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.VARDGIVARE_ID_XPATH;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.VARDGIVARE_NAMN_XPATH;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.booleanXPath;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.stringXPath;
import static se.inera.certificate.modules.ts_diabetes.transformation.XPathExpressions.dateXPath;

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

import se.inera.certificate.modules.ts_diabetes.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_parent.transformation.XslTransformer;
import se.inera.certificate.modules.ts_parent.transformation.test.BooleanXPathExpression;
import se.inera.certificate.modules.ts_parent.transformation.test.KorkortsKodToIntygAvserMapping;
import se.inera.certificate.modules.ts_parent.transformation.test.XPathEvaluator;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.Diabetes;
import se.inera.intygstjanster.ts.services.v1.Hypoglykemier;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.inera.intygstjanster.ts.services.v1.Patient;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.SynfunktionDiabetes;
import se.inera.intygstjanster.ts.services.v1.SynskarpaMedKorrektion;
import se.inera.intygstjanster.ts.services.v1.SynskarpaUtanKorrektion;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;
import se.inera.intygstjanster.ts.services.v1.Vardenhet;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v1.Utlatande;

public class TsDiabetesTransformerXpathTest {

    private static XslTransformer transformer;

    @BeforeClass
    public static void setup() {
        transformer = new XslTransformer("xsl/transform-ts-diabetes.xsl");
    }

    @Test
    public void testMaximaltIntyg() throws IOException, ParserConfigurationException, JAXBException, XPathExpressionException, SAXException,
            TransformerException {
        File file = new ClassPathResource("scenarios/transport/valid-maximal.xml").getFile();
        String xmlContent = FileUtils.readFileToString(file);
        TSDiabetesIntyg utlatande = JAXB.unmarshal(file, RegisterTSDiabetesType.class).getIntyg();
        String transformed = transformer.transform(xmlContent);
        performTest(transformed, utlatande);
    }

    @Test
    public void testMinimaltIntyg() throws IOException, ParserConfigurationException, JAXBException, XPathExpressionException, SAXException,
            TransformerException {
        File file = new ClassPathResource("scenarios/transport/valid-minimal.xml").getFile();
        String xmlContent = FileUtils.readFileToString(file);
        TSDiabetesIntyg utlatande = JAXB.unmarshal(file, RegisterTSDiabetesType.class).getIntyg();
        String transformed = transformer.transform(xmlContent);
        performTest(transformed, utlatande);
    }

    private void performTest(String transformed, TSDiabetesIntyg utlatande) throws ParserConfigurationException, JAXBException, SAXException,
            IOException, TransformerException, XPathExpressionException {
        // Create an xPath evaluator that operates on the transport model.
        XPathEvaluator xPath = createXPathEvaluator(transformed);

        // Check utlatande against xpath
        assertEquals("UtlatandeTyp", UtlatandeKod.getCurrentVersion().getTypForTransportConvertion(),
                xPath.evaluate(XPathExpressions.TYP_AV_UTLATANDE_XPATH));

        // assertEquals("Utlatande-utgåva", utlatande.getUtgava(), xPath.evaluate(XPathExpressions.TS_UTGAVA_XPATH));

        assertEquals("Utlatande-version", utlatande.getVersion(), xPath.evaluate(XPathExpressions.TS_VERSION_XPATH));

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

        // assertEquals("Skapad av - befattningar", skapadAv.getBefattningar().get(0),
        // xPath.evaluate(XPathExpressions.SKAPAD_AV_BEFATTNING_XPATH));

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
        for (KorkortsbehorighetTsDiabetes t : utlatande.getIntygAvser().getKorkortstyp()) {
            KorkortsKodToIntygAvserMapping k = KorkortsKodToIntygAvserMapping.valueOf(t.name());
            assertTrue(xPath.evaluate(booleanXPath(INTYG_AVSER_TEMPLATE, k.getIntygAvser())));
        }

        // ID-kontroll
        assertTrue(xPath.evaluate(booleanXPath(ID_KONTROLL_TEMPLATE,
                utlatande.getIdentitetStyrkt().getIdkontroll().value())));

        // Aktiviteter
        assertEquals("Egenkontroll av blodsocker", utlatande.getHypoglykemier().isGenomforEgenkontrollBlodsocker(),
                xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "308113006", utlatande.getHypoglykemier()
                        .isGenomforEgenkontrollBlodsocker())));

        SynfunktionDiabetes synfunktion = utlatande.getSynfunktion();
        if (synfunktion != null) {
            assertEquals("Synfältsprövning", synfunktion.isFinnsSynfaltsprovning(),
                    xPath.evaluate(booleanXPath("utlatande/p:aktivitet/p:aktivitetskod/@code='86944008'", synfunktion.isFinnsSynfaltsprovning())));

            assertEquals("Prövning av ögats rörlighet", synfunktion.isFinnsProvningOgatsRorlighet(),
                    xPath.evaluate(booleanXPath("utlatande/p:aktivitet/p:aktivitetskod/@code='AKT18'", synfunktion.isFinnsProvningOgatsRorlighet())));
        }
        // Observationer
        // Hypoglykemier
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();

        assertEquals("Kunskap om åtgärder", hypoglykemier.isHarKunskapOmAtgarder(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS19", hypoglykemier.isHarKunskapOmAtgarder())));

        assertTrue("Hypoglykemi nedsatt hjärnfunktion",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS20", hypoglykemier.isHarTeckenNedsattHjarnfunktion())));

        assertEquals("Saknar känna förmåga varningstecken", hypoglykemier.isSaknarFormagaKannaVarningstecken(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS21", hypoglykemier.isSaknarFormagaKannaVarningstecken())));

        assertEquals("Allvarlig hypoglykemi senaste året", hypoglykemier.isHarAllvarligForekomst(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS22", hypoglykemier.isHarAllvarligForekomst())));
        assertEquals("Allvarlig hypoglykemi senaste året - beskrivning", hypoglykemier.getAllvarligForekomstBeskrivning() == null ? ""
                : hypoglykemier.getAllvarligForekomstBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS22")));

        assertEquals("Allvarlig förekomst i trafiken senaste året", hypoglykemier.isHarAllvarligForekomstTrafiken(),
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS23", hypoglykemier.isHarAllvarligForekomstTrafiken())));
        assertEquals("Allvarlig förekomst trafiken senaste året - beskrivning", hypoglykemier.getAllvarligForekomstTrafikBeskrivning() == null ? ""
                : hypoglykemier.getAllvarligForekomstTrafikBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS23")));

        if (hypoglykemier.isHarAllvarligForekomstVakenTid() != null) {
            assertEquals("Allvarlig förekomst vaken tid senaste året", hypoglykemier.isHarAllvarligForekomstVakenTid(),
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS24", hypoglykemier.isHarAllvarligForekomstVakenTid())));
            assertEquals("Allvarlig förekomst vaken tid senaste året - observationstid", hypoglykemier.getAllvarligForekomstVakenTidAr() == null ? ""
                    : hypoglykemier.getAllvarligForekomstVakenTidAr(),
                    xPath.evaluate(dateXPath(OBSERVATION_TID_TEMPLATE, "yyyy-MM-dd", "OBS24")));
        }
        // Syn
        if (synfunktion != null) {
            assertEquals("Synfältsprovning utan anmärkning", synfunktion.isSynfaltsprovningUtanAnmarkning(),
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS25", synfunktion.isSynfaltsprovningUtanAnmarkning())));

            assertEquals("Diplopi", synfunktion.isHarDiplopi(),
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2", synfunktion.isHarDiplopi())));

            SynskarpaUtanKorrektion utanKorr = synfunktion.getSynskarpaUtanKorrektion();

            SynskarpaMedKorrektion medKorr = synfunktion.getSynskarpaMedKorrektion();

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
        }
        // Diabetes
        Diabetes diabetes = utlatande.getDiabetes();

        if (diabetes.getDiabetesTyp().get(0).equals("TYP1")) {
            assertEquals("Diabetes typ1", diabetes.getDiabetesTyp().get(0).equals("TYP1"),
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E10", diabetes.getDiabetesTyp().get(0).equals("TYP1"))));
        }
        else if (diabetes.getDiabetesTyp().get(0).equals("TYP2")) {
            assertEquals("Diabetes typ2", diabetes.getDiabetesTyp().get(0).equals("TYP2"),
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E11", diabetes.getDiabetesTyp().get(0).equals("TYP2"))));
        }

        assertEquals(
                "Diabetes kostbehandling",
                diabetes.isHarBehandlingKost() != null ? diabetes.isHarBehandlingKost() : false,
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170745003",
                        diabetes.isHarBehandlingKost() != null ? diabetes.isHarBehandlingKost() : false)));

        assertEquals(
                "Diabetes insulinbehandling",
                diabetes.isHarBehandlingInsulin() != null ? diabetes.isHarBehandlingInsulin() : false,
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170747006",
                        diabetes.isHarBehandlingInsulin() != null ? diabetes.isHarBehandlingInsulin() : false)));

        assertEquals(
                "Diabetes tablettbehandling",
                diabetes.isHarBehandlingTabletter() != null ? diabetes.isHarBehandlingTabletter() : false,
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_VARDE_TEMPLATE, "170746002",
                        diabetes.isHarBehandlingTabletter() != null ? diabetes.isHarBehandlingTabletter() : false)));

        if (diabetes.getAnnanBehandlingBeskrivning() != null) {
            assertEquals("Diabetes annan behandling beskrivning", diabetes.getAnnanBehandlingBeskrivning(),
                    xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS10")));
        }

        // Rekommendationer
        assertTrue("Rekommendation REK8",
                xPath.evaluate(new BooleanXPathExpression("utlatande/p:rekommendation/p:rekommendationskod/@code = 'REK8'")));

        for (KorkortsbehorighetTsDiabetes t : utlatande.getBedomning().getKorkortstyp()) {
            KorkortsKodToIntygAvserMapping k = KorkortsKodToIntygAvserMapping.valueOf(t.name());
            assertTrue(String.format("Rekommendationsvärde %s", k.getRekommendation()),
                    xPath.evaluate(booleanXPath(REKOMMENDATION_VARDE_CODE_TEMPLATE, k.getRekommendation())));
        }
        if (utlatande.getBedomning().isKanInteTaStallning() != null) {
            assertEquals(
                    "Rekommendationsvärde Kan inte ta ställning (VAR11)",
                    KorkortsKodToIntygAvserMapping.KANINTETASTALLNING.getRekommendation(),
                    xPath.evaluate(stringXPath(REKOMMENDATION_VARDE_CODE_TEMPLATE,
                            KorkortsKodToIntygAvserMapping.KANINTETASTALLNING.getRekommendation())));
        }

        if (utlatande.getBedomning().getBehovAvLakareSpecialistKompetens() != null) {
            assertEquals(String.format("REK9 med beskrivning  %s", utlatande.getBedomning().getBehovAvLakareSpecialistKompetens()), utlatande
                    .getBedomning().getBehovAvLakareSpecialistKompetens(),
                    xPath.evaluate(stringXPath(REKOMMENDATION_BESKRIVNING_TEMPLATE, "REK9")));
        }

        if (utlatande.getBedomning().isLamplighetInnehaBehorighetSpecial() != null) {
            assertEquals("Rekommendation lämplighet inneha behörighet", utlatande.getBedomning().isLamplighetInnehaBehorighetSpecial(),
                    xPath.evaluate(booleanXPath(REKOMMENDATION_VARDE_BOOL_TEMPLATE, "REK10", utlatande.getBedomning().isLamplighetInnehaBehorighetSpecial())));
        }

        // Bilaga
        assertTrue(
                "Bilaga",
                xPath.evaluate(new BooleanXPathExpression(String.format("utlatande/p2:bilaga/p:forekomst='%s'",
                        utlatande.isSeparatOgonLakarintygKommerSkickas()))));
    }

    private XPathEvaluator createXPathEvaluator(String xml) throws ParserConfigurationException,
            JAXBException, SAXException, IOException, TransformerException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        namespaces.bindDefaultNamespaceUri("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1");
        namespaces.bindNamespaceUri("ns2", "urn:riv:clinicalprocess:healthcond:certificate:types:1");
        namespaces.bindNamespaceUri("p", "urn:riv:clinicalprocess:healthcond:certificate:1");
        namespaces.bindNamespaceUri("p2", "urn:riv:clinicalprocess:healthcond:certificate:ts-diabetes:1");
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
