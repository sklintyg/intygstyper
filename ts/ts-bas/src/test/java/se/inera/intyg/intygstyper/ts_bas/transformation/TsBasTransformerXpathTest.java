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

package se.inera.intyg.intygstyper.ts_bas.transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.intygstyper.ts_bas.transformation.XPathExpressions.*;

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
import se.inera.intyg.intygstyper.ts_parent.transformation.test.BooleanXPathExpression;
import se.inera.intyg.intygstyper.ts_parent.transformation.test.XPathEvaluator;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.*;
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
        performTests(new ClassPathResource("scenarios/transport/valid-maximal.xml").getFile());
    }

    @Test
    public void testMinimaltIntyg() throws IOException, ParserConfigurationException, JAXBException, XPathExpressionException, SAXException,
            TransformerException {
        performTests(new ClassPathResource("scenarios/transport/valid-minimal.xml").getFile());

    }

    private void performTests(File file) throws ParserConfigurationException, JAXBException, SAXException, IOException, TransformerException,
            XPathExpressionException {
        String xmlContent = FileUtils.readFileToString(file);
        TSBasIntyg utlatande = JAXB.unmarshal(file, RegisterTSBasType.class).getIntyg();
        String transformed = transformer.transform(xmlContent);

        // Create an xPath evaluator that operates on the transport model.
        XPathEvaluator xPath = createXPathEvaluator(transformed);

        // Check utlatande against xpath
        assertEquals("UtlatandeTyp", "TSTRK1007", xPath.evaluate(XPathExpressions.TYP_AV_UTLATANDE_XPATH));

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
        for (KorkortsbehorighetTsBas t : utlatande.getIntygAvser().getKorkortstyp()) {
            assertTrue(xPath.evaluate(booleanXPath(INTYG_AVSER_TEMPLATE, IntygAvserKod.valueOf(t.value().value()).getCode())));
        }

        // ID-kontroll
        assertTrue(xPath.evaluate(booleanXPath(ID_KONTROLL_TEMPLATE,
                utlatande.getIdentitetStyrkt().getIdkontroll().value())));

        // Aktiviteter
        boolean harGlasStyrkaOver8Dioptrier = utlatande.getSynfunktion().isHarGlasStyrkaOver8Dioptrier() == null ? false : utlatande.getSynfunktion()
                .isHarGlasStyrkaOver8Dioptrier();
        assertEquals("8 dioptrier", harGlasStyrkaOver8Dioptrier,
                xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT17", harGlasStyrkaOver8Dioptrier)));

        boolean harVardInsats = utlatande.getAlkoholNarkotikaLakemedel().isHarVardinsats();
        assertTrue("Har vårdinsats för missbruk",
                xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT15", harVardInsats)));

        Boolean provtagningBehovs = utlatande.getAlkoholNarkotikaLakemedel().isHarVardinsatsProvtagningBehov();
        if (provtagningBehovs != null) {
            assertTrue("provtagning narkotika",
                    xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT14", provtagningBehovs)));
        }

        assertTrue("Sjukhusvård eller läkarkontakt",
                xPath.evaluate(booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT19", utlatande.getSjukhusvard().isHarSjukhusvardEllerLakarkontakt())));

        if (utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktDatum() != null) {
            assertEquals("Sjukhusvård eller läkarkontakt - datum", utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktDatum(),
                    xPath.evaluate(XPathExpressions.VARD_PA_SJUKHUS_TID_XPATH));
        }

        if (utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktVardinrattning() != null) {
            assertEquals("Sjukhusvård eller läkarkontakt - plats", utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktVardinrattning(),
                    xPath.evaluate(XPathExpressions.VARD_PA_SJUKHUS_VARDINRATTNING_XPATH));
        }

        if (utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktAnledning() != null) {
            assertEquals("Sjukhusvård eller läkarkontakt - anledning", utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktAnledning(),
                    xPath.evaluate(stringXPath(AKTIVITET_BESKRIVNING_TEMPLATE, "AKT19")));
        }

        // Observationer
        // Synobservationer
        boolean synfaltsdefekter = utlatande.getSynfunktion().isHarSynfaltsdefekt();
        assertTrue("Synfältsdefekter", xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.4", synfaltsdefekter)));

        boolean nattblindhet = utlatande.getSynfunktion().isHarNattblindhet();
        assertTrue("Nattblindhet", xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.6", nattblindhet)));

        boolean progressivOgonsjukdom = utlatande.getSynfunktion().isHarProgressivOgonsjukdom();
        assertTrue("Progressiv ögonsjukdom",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS1", progressivOgonsjukdom)));

        boolean diplopi = utlatande.getSynfunktion().isHarDiplopi();
        assertTrue("Diplopi", xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2", diplopi)));

        boolean nystagmus = utlatande.getSynfunktion().isHarNystagmus();
        assertTrue("Nystagmus", xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H55.9", nystagmus)));

        SynskarpaUtanKorrektion utanKorr = utlatande.getSynfunktion().getSynskarpaUtanKorrektion();

        SynskarpaMedKorrektion medKorr = utlatande.getSynfunktion().getSynskarpaMedKorrektion();

        assertEquals("Ej korrigerad synskärpa höger", String.valueOf(utanKorr.getHogerOga()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "420050001", "24028007")));

        assertEquals("Korrigerad synskärpa höger", medKorr.getHogerOga() == null ? "" : String.valueOf(medKorr.getHogerOga()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "397535007", "24028007")));

        assertEquals("Ej korrigerad synskärpa vänster", String.valueOf(utanKorr.getVansterOga()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "420050001", "7771000")));

        assertEquals("Korrigerad synskärpa vänster", medKorr.getVansterOga() == null ? "" : String.valueOf(medKorr.getVansterOga()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "397535007", "7771000")));

        assertEquals("Ej korrigerad synskärpa binokulärt", String.valueOf(utanKorr.getBinokulart()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "420050001", "51440002")));

        assertEquals("Korrigerad synskärpa binokulärt", medKorr.getBinokulart() == null ? "" : String.valueOf(medKorr.getBinokulart()),
                xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET,
                        "397535007", "51440002")));

        if (medKorr.isHarKontaktlinsHogerOga() != null) {
            assertEquals("Kontaktlinser höger",medKorr.isHarKontaktlinsHogerOga(),
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_CODE_LATERALITET, "285049007", "24028007")));
        }

        if (medKorr.isHarKontaktlinsVansterOga() != null) {
            assertEquals("Kontaktlinser vänster", medKorr.isHarKontaktlinsVansterOga(),
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_CODE_LATERALITET, "285049007", "7771000")));
        }

        // Hörsel & balans
        HorselBalanssinne horselBalans = utlatande.getHorselBalanssinne();
        assertTrue("Balansrubbningar",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS2", horselBalans.isHarBalansrubbningYrsel())));

        if (horselBalans.isHarSvartUppfattaSamtal4Meter() != null) {
            assertTrue("Svårt uppfatta samtal 4 meter",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS3", horselBalans.isHarSvartUppfattaSamtal4Meter())));
        }

        // Rörelseorganens förmåga
        RorelseorganenFunktioner rorelse = utlatande.getRorelseorganensFunktioner();

        assertTrue("Har rörelsebegränsning",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS4", rorelse.isHarRorelsebegransning())));

        assertEquals("Rörelsebegränsning beskrivning", rorelse.getRorelsebegransningBeskrivning() == null ? "" : rorelse.getRorelsebegransningBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS4", rorelse.getRorelsebegransningBeskrivning())));

        if (rorelse.isHarOtillrackligRorelseformagaPassagerare() != null) {
            assertTrue("Otillräcklig rörelseförmåga passagerare",
                    xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS5", rorelse.isHarOtillrackligRorelseformagaPassagerare())));
        }

        // Hjärt & kärlsjukdom
        HjartKarlSjukdomar hjartKarl = utlatande.getHjartKarlSjukdomar();

        assertTrue("Risk försämrad hjärnfunktion",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS6", hjartKarl.isHarRiskForsamradHjarnFunktion())));

        assertTrue("Tecken på hjärnskada",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS8", hjartKarl.isHarHjarnskadaICNS())));

        assertTrue("Riskfaktorer för stroke",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS7", hjartKarl.isHarRiskfaktorerStroke())));

        assertEquals("Riskfaktorer stroke - beskrivning", hjartKarl.getRiskfaktorerStrokeBeskrivning() == null ? "" : hjartKarl.getRiskfaktorerStrokeBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS7", hjartKarl.getRiskfaktorerStrokeBeskrivning())));

        // Diabetes
        DiabetesTypBas diabetes = utlatande.getDiabetes();

        assertTrue("Har diabetes",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "73211009", diabetes.isHarDiabetes())));

        if (diabetes.isHarDiabetes()) {
            if (diabetes.getDiabetesTyp().name().equals("TYP1")) {
                assertTrue("Diabetes typ1",
                        xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E10")));
            }
            else if (diabetes.getDiabetesTyp().name().equals("TYP2")) {
                assertTrue("Diabetes typ2",
                        xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E11")));
            }

            if (diabetes.isHarBehandlingKost() != null) {
                assertTrue(
                        "Diabetes kostbehandling",
                        xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS9",
                                diabetes.isHarBehandlingKost())));
            }

            if (diabetes.isHarBehandlingInsulin() != null) {
                assertTrue(
                        "Diabetes insulinbehandling",
                        xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170747006", diabetes.isHarBehandlingInsulin())));
            }

            if (diabetes.isHarBehandlingTabletter() != null) {
                assertTrue(
                        "Diabetes tablettbehandling",
                        xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170746002", diabetes.isHarBehandlingTabletter())));
            }
        }
        // Neurologi
        assertTrue("Tecken på neurologisk sjukdom",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "407624006", utlatande.isNeurologiskaSjukdomar())));

        // Medvetandestörning
        Medvetandestorning medvetande = utlatande.getMedvetandestorning();
        assertTrue("Har medvetandestörning",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "G40.9", medvetande.isHarMedvetandestorning())));

        assertEquals("Medvetandestörning - beskrivning", medvetande.getMedvetandestorningBeskrivning() == null ? "" : medvetande.getMedvetandestorningBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "G40.9", medvetande.getMedvetandestorningBeskrivning())));

        assertTrue("Har medvetandestörning",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS11", utlatande.isHarNjurSjukdom())));

        // Sviktande kognitiv förmåga
        assertTrue("kognitiv förmåga",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS12", utlatande.isHarKognitivStorning())));

        // Somn vakenhet
        assertTrue("Somn vakenhet",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS13", utlatande.isHarSomnVakenhetStorning())));

        // alkohol och narkotika
        AlkoholNarkotikaLakemedel alkNark = utlatande.getAlkoholNarkotikaLakemedel();

        assertTrue("Tecken på missbruk",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS14", alkNark.isHarTeckenMissbruk())));

        assertTrue("Läkemedelsbruk",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS15", alkNark.isHarLakarordineratLakemedelsbruk())));

        assertEquals("Läkemedelsbruk - beskrivning", alkNark.getLakarordineratLakemedelOchDos() == null ? "" : alkNark.getLakarordineratLakemedelOchDos(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS15")));

        // Psykisk sjukdom
        assertTrue("Läkemedelsbruk",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS16", utlatande.isHarPsykiskStorning())));

        // Utvecklingsstörning
        Utvecklingsstorning utvecklingsstorning = utlatande.getUtvecklingsstorning();
        assertTrue("Har psykisk utvecklingsstörning",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "129104009", utvecklingsstorning.isHarPsykiskUtvecklingsstorning())));

        assertTrue("Har ADHD eller damp",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS17", utvecklingsstorning.isHarAndrayndrom())));

        // Stadigvarande medicinering
        OvrigMedicinering medicinering = utlatande.getOvrigMedicinering();
        assertTrue("Har stadigvarande medicinering",
                xPath.evaluate(booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS18", medicinering.isHarStadigvarandeMedicinering())));

        assertEquals("Har stadigvarande medicinering- beskrivning", medicinering.getStadigvarandeMedicineringBeskrivning() == null ? "" : medicinering.getStadigvarandeMedicineringBeskrivning(),
                xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS18")));

        // Rekommendationer
        assertTrue("Rekommendation REK8",
                xPath.evaluate(new BooleanXPathExpression("utlatande/p:rekommendation/p:rekommendationskod/@code = 'REK8'")));

        for (KorkortsbehorighetTsBas t : utlatande.getBedomning().getKorkortstyp()) {
            KorkortsbehorighetKod k = KorkortsbehorighetKod.valueOf(t.value().value());
            assertTrue(String.format("Rekommendationsvärde %s", k.getCode()),
                    xPath.evaluate(booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, k.getCode())));
        }
        if (utlatande.getBedomning().isKanInteTaStallning() !=  null && utlatande.getBedomning().isKanInteTaStallning()) {
            assertTrue("Rekommendationsvärde Kan inte ta ställning (VAR11)",
                    xPath.evaluate(booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, KorkortsbehorighetKod.KANINTETEASTALLNING.getCode())));
        }

        if (utlatande.getBedomning().getBehovAvLakareSpecialistKompetens() != null) {
            assertEquals(String.format("REK9 med beskrivning  %s", utlatande.getBedomning().getBehovAvLakareSpecialistKompetens()), utlatande
                    .getBedomning().getBehovAvLakareSpecialistKompetens(),
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
