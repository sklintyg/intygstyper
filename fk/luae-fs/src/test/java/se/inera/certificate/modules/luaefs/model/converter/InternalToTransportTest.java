package se.inera.certificate.modules.luaefs.model.converter;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.SVRLWriter;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.certificate.modules.fkparent.model.converter.RegisterCertificateTestValidator;
import se.inera.certificate.modules.fkparent.model.internal.Diagnos;
import se.inera.certificate.modules.luaefs.model.internal.LuaefsUtlatande;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.net.URL;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class InternalToTransportTest {

    @Test
    public void doSchematronValidationLuaefs() throws Exception {
        String xmlContents = Resources.toString(getResource("transport/luaefs-2.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator("aktivitetsersattning-fs.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    @Test
    public void testInternalToTransportConversion() throws Exception {
        LuaefsUtlatande expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected);
        LuaefsUtlatande actual = TransportToInternal.convert(transport.getIntyg());

        assertEquals(expected, actual);
    }

    @Test
    public void convertDecorateSvarPaTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        LuaefsUtlatande utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertEquals(1, transport.getSvarPa().getReferensId().size());
        assertEquals(referensId, transport.getSvarPa().getReferensId().get(0));
    }

    @Test
    public void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        LuaefsUtlatande utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertTrue(transport.getSvarPa().getReferensId().isEmpty());
    }

    @Test
    public void convertDecorateSvarPaNoRelationTest() throws Exception {
        LuaefsUtlatande utlatande = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    public void convertDecorateSvarPaNotKompltTest() throws Exception {
        LuaefsUtlatande utlatande = getUtlatande(RelationKod.FRLANG, null, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    public static LuaefsUtlatande getUtlatande() {
        return getUtlatande(null, null, null);
    }

    public static LuaefsUtlatande getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) {
        LuaefsUtlatande.Builder utlatande = LuaefsUtlatande.builder();
        utlatande.setId("1234567");
        utlatande.setTextVersion("1.0");
        GrundData grundData = IntygTestDataBuilder.getGrundData();

        grundData.setSigneringsdatum(new LocalDateTime("2015-12-07T15:48:05"));

        if (relationKod != null) {
            Relation relation = new Relation();
            relation.setRelationKod(relationKod);
            relation.setMeddelandeId(relationMeddelandeId);
            relation.setReferensId(referensId);
            grundData.setRelation(relation);
        }
        utlatande.setGrundData(grundData);

        utlatande.setAnnanGrundForMU(new InternalDate("2015-12-07"));
        utlatande.setAnnanGrundForMUBeskrivning("Barndomsvän");

        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra", "Klämskada skuldra"))));

        utlatande.setFunktionsnedsattningDebut("Skoldansen");
        utlatande.setFunktionsnedsattningPaverkan("Haltar när han dansar");

        return utlatande.build();
    }
}
