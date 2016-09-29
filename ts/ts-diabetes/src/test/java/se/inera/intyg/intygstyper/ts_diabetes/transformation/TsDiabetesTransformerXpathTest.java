/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.ts_diabetes.transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.intygstyper.ts_diabetes.transformation.XPathExpressions.*;

import java.io.*;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.*;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.intygstyper.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.intygstyper.ts_parent.codes.KorkortsbehorighetKod;
import se.inera.intyg.intygstyper.ts_parent.transformation.test.*;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.*;
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
        performTest(new ClassPathResource("scenarios/transport/transform-valid-maximal.xml").getFile());
    }

    @Test
    public void testMinimaltIntyg() throws IOException, ParserConfigurationException, JAXBException, XPathExpressionException, SAXException,
            TransformerException {
        performTest(new ClassPathResource("scenarios/transport/transform-valid-minimal.xml").getFile());
    }

    private void performTest(File file) throws ParserConfigurationException, JAXBException, SAXException,
            IOException, TransformerException, XPathExpressionException {
        String xmlContent = FileUtils.readFileToString(file);
        TSDiabetesIntyg utlatande = JAXB.unmarshal(file, RegisterTSDiabetesType.class).getIntyg();
        String transformed = transformer.transform(xmlContent);
        // Create an xPath evaluator that operates on the transport model.
        XPathEvaluator xPath = createXPathEvaluator(transformed);

        // Check utlatande against xpath
        assertEquals("UtlatandeTyp", "TSTRK1031", xPath.evaluate(XPathExpressions.TYP_AV_UTLATANDE_XPATH));

        assertEquals("Utlatande-utgåva", utlatande.getUtgava(), xPath.evaluate(XPathExpressions.TS_UTGAVA_XPATH));

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

        if (!skapadAv.getBefattningar().isEmpty()) {
            assertEquals("Skapad av - befattningar", skapadAv.getBefattningar().get(0),
                    xPath.evaluate(XPathExpressions.SKAPAD_AV_BEFATTNING_XPATH));
        }

        assertEquals("Skapad av - fullständigt namn", skapadAv.getFullstandigtNamn(),
                xPath.evaluate(XPathExpressions.SKAPAD_AV_NAMNFORTYDLIGANDE_XPATH));

        assertEquals("Skapad av - hsa-id", skapadAv.getPersonId().getExtension(), xPath.evaluate(XPathExpressions.SKAPAD_AV_HSAID_XPATH));

        if (!skapadAv.getSpecialiteter().isEmpty()) {
            assertEquals("Skapad av - specialitet", skapadAv.getSpecialiteter().get(0),
                    xPath.evaluate(XPathExpressions.SKAPAD_AV_SPECIALISTKOMPETENS_BESKRVNING_XPATH));
        }

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
            assertTrue(xPath.evaluate(booleanXPath(INTYG_AVSER_TEMPLATE, IntygAvserKod.valueOf(t.value().value()).getCode())));
        }

        // ID-kontroll
        assertTrue(xPath.evaluate(booleanXPath(ID_KONTROLL_TEMPLATE,
                utlatande.getIdentitetStyrkt().getIdkontroll().value())));

        // Aktiviteter
        if (utlatande.getHypoglykemier() != null && utlatande.getHypoglykemier().isGenomforEgenkontrollBlodsocker() != null) {
            assertTrue("Egenkontroll av blodsocker",
                    xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "308113006", utlatande.getHypoglykemier()
                            .isGenomforEgenkontrollBlodsocker())));
        }

        SynfunktionDiabetes synfunktion = utlatande.getSynfunktion();
        if (synfunktion != null) {
            assertTrue("Synfältsprövning",
                    xPath.evaluate(booleanXPath("utlatande/p:aktivitet/p:aktivitetskod/@code='86944008'", synfunktion.isFinnsSynfaltsprovning())));

            assertTrue("Prövning av ögats rörlighet",
                    xPath.evaluate(booleanXPath("utlatande/p:aktivitet/p:aktivitetskod/@code='AKT18'", synfunktion.isFinnsProvningOgatsRorlighet())));
        }
        // Observationer
        // Hypoglykemier
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();

        assertTrue("Kunskap om åtgärder",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS19", hypoglykemier.isHarKunskapOmAtgarder())));

        assertTrue("Hypoglykemi nedsatt hjärnfunktion",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS20", hypoglykemier.isHarTeckenNedsattHjarnfunktion())));

        if (hypoglykemier.isHarTeckenNedsattHjarnfunktion()) {
            assertTrue("Saknar känna förmåga varningstecken",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS21", hypoglykemier.isSaknarFormagaKannaVarningstecken())));

            assertTrue("Allvarlig hypoglykemi senaste året",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS22", hypoglykemier.isHarAllvarligForekomst())));
            assertEquals("Allvarlig hypoglykemi senaste året - beskrivning", hypoglykemier.getAllvarligForekomstBeskrivning() == null ? ""
                    : hypoglykemier.getAllvarligForekomstBeskrivning(),
                    xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS22")));

            assertTrue("Allvarlig förekomst i trafiken senaste året",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS23", hypoglykemier.isHarAllvarligForekomstTrafiken())));
            assertEquals("Allvarlig förekomst trafiken senaste året - beskrivning", hypoglykemier.getAllvarligForekomstTrafikBeskrivning() == null ? ""
                    : hypoglykemier.getAllvarligForekomstTrafikBeskrivning(),
                    xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS23")));
        }

        if (hypoglykemier.isHarAllvarligForekomstVakenTid() != null) {
            assertTrue("Allvarlig förekomst vaken tid senaste året",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS24", hypoglykemier.isHarAllvarligForekomstVakenTid())));
            assertEquals("Allvarlig förekomst vaken tid senaste året - observationstid", hypoglykemier.getAllvarligForekomstVakenTidAr() == null ? ""
                    : hypoglykemier.getAllvarligForekomstVakenTidAr(),
                    xPath.evaluate(dateXPath(OBSERVATION_TID_TEMPLATE, "yyyy-MM-dd", "OBS24")));
        }
        // Syn
        if (synfunktion != null) {
            assertTrue("Synfältsprovning utan anmärkning",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS25", synfunktion.isSynfaltsprovningUtanAnmarkning())));

            assertTrue("Diplopi",
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
            assertTrue("Diabetes typ1",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E10", diabetes.getDiabetesTyp().get(0).equals("TYP1"))));
        }
        else if (diabetes.getDiabetesTyp().get(0).equals("TYP2")) {
            assertTrue("Diabetes typ2",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E11", diabetes.getDiabetesTyp().get(0).equals("TYP2"))));
            assertEquals("Diabetes typ2 från år", diabetes.getDebutArDiabetes(),
                    xPath.evaluate(new StringXPathExpression("utlatande/p:observation/p:observationsperiod[(parent::p:observation/p:observationskod/@code='E11')]")));
        }

        if (diabetes.isHarBehandlingKost() != null) {
            assertTrue(
                    "Diabetes kostbehandling",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170745003", diabetes.isHarBehandlingKost())));
        }

        if (diabetes.isHarBehandlingInsulin() != null) {
            assertTrue(
                    "Diabetes insulinbehandling",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170747006", diabetes.isHarBehandlingInsulin())));

            if (diabetes.getInsulinBehandlingSedanAr() != null) {
                assertEquals("Insulin sedan år", diabetes.getInsulinBehandlingSedanAr(),
                        xPath.evaluate(new StringXPathExpression("utlatande/p:observation/p:observationsperiod[(parent::p:observation/p:observationskod/@code='170747006')]")));
            }
        }


        if (diabetes.isHarBehandlingTabletter() != null) {
            assertTrue(
                    "Diabetes tablettbehandling",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_VARDE_TEMPLATE, "170746002", diabetes.isHarBehandlingTabletter())));
        }

        if (diabetes.getAnnanBehandlingBeskrivning() != null) {
            assertEquals("Diabetes annan behandling beskrivning", diabetes.getAnnanBehandlingBeskrivning(),
                    xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS10")));
        }

        // Rekommendationer
        assertTrue("Rekommendation REK8",
                xPath.evaluate(new BooleanXPathExpression("utlatande/p:rekommendation/p:rekommendationskod/@code = 'REK8'")));

        for (KorkortsbehorighetTsDiabetes t : utlatande.getBedomning().getKorkortstyp()) {
            KorkortsbehorighetKod k = KorkortsbehorighetKod.valueOf(t.value().value());
            assertTrue(String.format("Rekommendationsvärde %s", k.getCode()),
                    xPath.evaluate(booleanXPath(REKOMMENDATION_VARDE_CODE_TEMPLATE, k.getCode())));
        }
        if (utlatande.getBedomning().isKanInteTaStallning() != null) {
            assertTrue("Rekommendationsvärde Kan inte ta ställning (VAR11)",
                    xPath.evaluate(booleanXPath(REKOMMENDATION_VARDE_CODE_TEMPLATE,
                            KorkortsbehorighetKod.KANINTETEASTALLNING.getCode())));
        }

        if (utlatande.getBedomning().getBehovAvLakareSpecialistKompetens() != null) {
            assertEquals(String.format("REK9 med beskrivning  %s", utlatande.getBedomning().getBehovAvLakareSpecialistKompetens()), utlatande
                    .getBedomning().getBehovAvLakareSpecialistKompetens(),
                    xPath.evaluate(stringXPath(REKOMMENDATION_BESKRIVNING_TEMPLATE, "REK9")));
        }

        if (utlatande.getBedomning().isLamplighetInnehaBehorighetSpecial() != null) {
            assertTrue("Rekommendation lämplighet inneha behörighet",
                    xPath.evaluate(booleanXPath(REKOMMENDATION_VARDE_BOOL_TEMPLATE, "REK10", utlatande.getBedomning()
                            .isLamplighetInnehaBehorighetSpecial())));
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

        JAXBElement<Utlatande> jaxbElement = new JAXBElement<>(new QName("ns3:utlatande"), Utlatande.class, register.getUtlatande());
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
