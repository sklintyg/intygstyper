package se.inera.intyg.intygstyper.luae_na.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.intygstyper.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.luae_na.model.internal.LuaenaUtlatande;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public class InternalToTransportTest {

    @Test
    public void testInternalToTransportConversion() throws Exception {
        LuaenaUtlatande expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected);
        LuaenaUtlatande actual = TransportToInternal.convert(transport.getIntyg());

        assertEquals(expected, actual);
    }

    @Test
    public void convertDecorateSvarPaTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        LuaenaUtlatande utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertEquals(1, transport.getSvarPa().getReferensId().size());
        assertEquals(referensId, transport.getSvarPa().getReferensId().get(0));
    }

    @Test
    public void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        LuaenaUtlatande utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertTrue(transport.getSvarPa().getReferensId().isEmpty());
    }

    @Test
    public void convertDecorateSvarPaNoRelationTest() throws Exception {
        LuaenaUtlatande utlatande = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    public void convertDecorateSvarPaNotKompltTest() throws Exception {
        LuaenaUtlatande utlatande = getUtlatande(RelationKod.FRLANG, null, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    public static LuaenaUtlatande getUtlatande() {
        return getUtlatande(null, null, null);
    }

    public static LuaenaUtlatande getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) {
        LuaenaUtlatande.Builder utlatande = LuaenaUtlatande.builder();
        utlatande.setId("1234567");
        utlatande.setTextVersion("1.0");
        GrundData grundData = IntygTestDataBuilder.getGrundData();

        grundData.setSigneringsdatum(LocalDateTime.parse("2015-12-07T15:48:05"));

        if (relationKod != null) {
            Relation relation = new Relation();
            relation.setRelationKod(relationKod);
            relation.setMeddelandeId(relationMeddelandeId);
            relation.setReferensId(referensId);
            grundData.setRelation(relation);
        }
        utlatande.setGrundData(grundData);
        utlatande.setAnnatGrundForMU(new InternalDate("2015-12-07"));
        utlatande.setAnnatGrundForMUBeskrivning("Barndomsvän");
        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra", "Klämskada skuldra"))));
        utlatande.setAktivitetsbegransning("Kommer inte in i bilen");
        utlatande.setFormagaTrotsBegransning("Är bra på att dansa!");
        utlatande.setForslagTillAtgard("Ben & Jerrys och Breaking Bad");

        return utlatande.build();
    }
}
