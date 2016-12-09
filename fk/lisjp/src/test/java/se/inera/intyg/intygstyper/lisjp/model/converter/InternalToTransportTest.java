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
package se.inera.intyg.intygstyper.lisjp.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;

import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;

import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.intygstyper.fkparent.integration.RegisterCertificateValidator;
import se.inera.intyg.intygstyper.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.intyg.intygstyper.fkparent.model.converter.RegisterCertificateTestValidator;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.lisjp.model.internal.*;
import se.inera.intyg.intygstyper.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.intygstyper.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public class InternalToTransportTest {

    @Test
    public void doSchematronValidationLisjp() throws Exception {
        String xmlContents = Resources.toString(getResource("transport/lisjp.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator("lisjp.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    @Test
    public void testInternalToTransportConversion() throws Exception {
        LisjpUtlatande expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected);
        LisjpUtlatande actual = TransportToInternal.convert(transport.getIntyg());

        assertEquals(expected, actual);
    }

    @Test(expected = ConverterException.class)
    public void testInternalToTransportSourceNull() throws Exception {
        InternalToTransport.convert(null);
    }

    @Test
    public void convertDecorateSvarPaTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        LisjpUtlatande utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertEquals(referensId, transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        LisjpUtlatande utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertNull(transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaNoRelationTest() throws Exception {
        LisjpUtlatande utlatande = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    public void convertDecorateSvarPaNotKompltTest() throws Exception {
        LisjpUtlatande utlatande = getUtlatande(RelationKod.FRLANG, null, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    public static LisjpUtlatande getUtlatande() {
        return getUtlatande(null, null, null);
    }

    public static LisjpUtlatande getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) {
        LisjpUtlatande.Builder utlatande = LisjpUtlatande.builder();
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

        utlatande.setTelefonkontaktMedPatienten(new InternalDate("2015-12-08"));
        utlatande.setAnnatGrundForMU(new InternalDate("2015-12-07"));
        utlatande.setAnnatGrundForMUBeskrivning("Barndomsvän");

        utlatande.setSysselsattning(Arrays.asList(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)));
        utlatande.setNuvarandeArbete("Smed");

        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra", "Klämskada skuldra"))));

        utlatande.setFunktionsnedsattning("Haltar när han dansar");
        utlatande.setAktivitetsbegransning("Kommer inte in i bilen");

        utlatande.setSjukskrivningar(asList(
                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_3_4,
                        new InternalLocalDateInterval(new InternalDate("2015-12-07"), new InternalDate("2015-12-10"))),
                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(new InternalDate("2015-12-12"), new InternalDate("2015-12-14"))),
                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_1_4,
                        new InternalLocalDateInterval(new InternalDate("2015-12-15"), new InternalDate("2015-12-15")))));

        utlatande.setForsakringsmedicinsktBeslutsstod("Överskrider inte FMB");

        utlatande.setArbetstidsforlaggning(true);
        utlatande.setArbetstidsforlaggningMotivering("Kan bara jobba på nätterna");

        utlatande.setArbetsresor(true);

        utlatande.setPrognos(Prognos.create(PrognosTyp.PROGNOS_OKLAR, null));

        utlatande.setArbetslivsinriktadeAtgarder(asList(
                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING)));

        utlatande.setArbetslivsinriktadeAtgarderBeskrivning("Jobbar bra om man inte stör honom");

        return utlatande.build();
    }
}
