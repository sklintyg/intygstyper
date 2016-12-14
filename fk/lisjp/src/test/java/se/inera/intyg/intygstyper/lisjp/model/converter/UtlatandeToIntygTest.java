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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.lisjp.model.converter.UtlatandeToIntyg;
import se.inera.intyg.intygstyper.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.intygstyper.lisjp.model.internal.Sysselsattning;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public class UtlatandeToIntygTest {

    @Test
    public void testConvert() throws Exception {
        final String intygsId = "intygsid";
        final String textVersion = "textversion";
        final String enhetsId = "enhetsid";
        final String enhetsnamn = "enhetsnamn";
        final String patientPersonId = "pid";
        final String skapadAvFullstandigtNamn = "fullstÃ¤ndigt namn";
        final String skapadAvPersonId = "skapad av pid";
        final LocalDateTime signeringsdatum = LocalDateTime.now();
        final String arbetsplatsKod = "arbetsplatsKod";
        final String postadress = "postadress";
        final String postNummer = "postNummer";
        final String postOrt = "postOrt";
        final String epost = "epost";
        final String telefonNummer = "telefonNummer";
        final String vardgivarid = "vardgivarid";
        final String vardgivarNamn = "vardgivarNamn";
        final String forskrivarKod = "forskrivarKod";
        final String fornamn = "fornamn";
        final String efternamn = "efternamn";
        final String mellannamn = "mellannamn";
        final String patientPostadress = "patientPostadress";
        final String patientPostnummer = "patientPostnummer";
        final String patientPostort = "patientPostort";

        LisjpUtlatande utlatande = buildUtlatande(intygsId, textVersion, enhetsId, enhetsnamn, patientPersonId,
                skapadAvFullstandigtNamn, skapadAvPersonId, signeringsdatum, arbetsplatsKod, postadress, postNummer, postOrt, epost, telefonNummer,
                vardgivarid, vardgivarNamn, forskrivarKod, fornamn, efternamn, mellannamn, patientPostadress, patientPostnummer, patientPostort,
                null, null);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertEquals(textVersion, intyg.getVersion());
        assertEquals("LISJP", intyg.getTyp().getCode());
        assertEquals("b64ea353-e8f6-4832-b563-fc7d46f29548", intyg.getTyp().getCodeSystem());
        assertEquals("Läkarintyg för sjukpenning", intyg.getTyp().getDisplayName());
        assertEquals(signeringsdatum, intyg.getSigneringstidpunkt());
        assertNotNull(patientPersonId, intyg.getPatient().getPersonId().getRoot());
        assertEquals(patientPersonId, intyg.getPatient().getPersonId().getExtension());
        assertEquals(skapadAvFullstandigtNamn, intyg.getSkapadAv().getFullstandigtNamn());
        assertNotNull(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getRoot());
        assertEquals(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getRoot());
        assertEquals(enhetsId, intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertEquals(enhetsnamn, intyg.getSkapadAv().getEnhet().getEnhetsnamn());
        assertNotNull(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getRoot());
        assertEquals(arbetsplatsKod, intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension());
        assertEquals(postadress, intyg.getSkapadAv().getEnhet().getPostadress());
        assertEquals(postNummer, intyg.getSkapadAv().getEnhet().getPostnummer());
        assertEquals(postOrt, intyg.getSkapadAv().getEnhet().getPostort());
        assertEquals(epost, intyg.getSkapadAv().getEnhet().getEpost());
        assertEquals(telefonNummer, intyg.getSkapadAv().getEnhet().getTelefonnummer());
        assertNotNull(intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getRoot());
        assertEquals(vardgivarid, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getExtension());
        assertEquals(vardgivarNamn, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivarnamn());
        assertEquals(forskrivarKod, intyg.getSkapadAv().getForskrivarkod());
        assertEquals(fornamn, intyg.getPatient().getFornamn());
        assertEquals(efternamn, intyg.getPatient().getEfternamn());
        assertEquals(mellannamn, intyg.getPatient().getMellannamn());
        assertEquals(patientPostadress, intyg.getPatient().getPostadress());
        assertEquals(patientPostnummer, intyg.getPatient().getPostnummer());
        assertEquals(patientPostort, intyg.getPatient().getPostort());
        assertTrue(intyg.getRelation().isEmpty());
    }

    @Test
    public void testConvertWithRelation() {
        RelationKod relationKod = RelationKod.FRLANG;
        String relationIntygsId = "relationIntygsId";
        LisjpUtlatande utlatande = buildUtlatande(relationKod, relationIntygsId);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertNotNull(intyg.getRelation());
        assertEquals(1, intyg.getRelation().size());
        assertEquals(relationKod.value(), intyg.getRelation().get(0).getTyp().getCode());
        assertNotNull(intyg.getRelation().get(0).getTyp().getCodeSystem());
        assertEquals(relationIntygsId, intyg.getRelation().get(0).getIntygsId().getExtension());
        assertNotNull(intyg.getRelation().get(0).getIntygsId().getRoot());
    }

    @Test
    public void testConvertDoesNotAddSvarForDiagnosWithoutCode() {
        Diagnos diagnos = Diagnos.create(null, Diagnoskodverk.ICD_10_SE.name(), null, null);
        LisjpUtlatande utlatande = buildUtlatande().toBuilder().setDiagnoser(Arrays.asList(diagnos)).build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertTrue(intyg.getSvar().isEmpty());
    }

    @Test
    public void testConvertWithConcatToOvrigt() {
        LisjpUtlatande utlatande = buildUtlatande().toBuilder().setMotiveringTillInteBaseratPaUndersokning("Motivering!").build();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertTrue(intyg.getSvar().size() == 1);
        assertEquals("Motivering till varför utlåtandet inte baseras på undersökning av patienten: Motivering!",
                intyg.getSvar().get(0).getDelsvar().get(0).getContent().get(0));
    }

    @Test
    public void testConvertWithConcatToOvrigt2() {
        LisjpUtlatande utlatande = buildUtlatande().toBuilder().setMotiveringTillInteBaseratPaUndersokning("Motivering!").
                setMotiveringTillTidigtStartdatumForSjukskrivning("Motivering2!").setOvrigt("TheRealOvrigt").build();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertTrue(intyg.getSvar().size() == 1);
        assertEquals("Motivering till varför utlåtandet inte baseras på undersökning av patienten: Motivering!\n"
                + "Orsak för att starta perioden mer än 7 dagar bakåt i tiden: Motivering2!\nTheRealOvrigt",
                intyg.getSvar().get(0).getDelsvar().get(0).getContent().get(0));
    }

    @Test
    public void testConvertDoesNotAddSvarForSysselsattningWithoutType() {
        Sysselsattning sysselsattning = Sysselsattning.create(null);
        LisjpUtlatande utlatande = buildUtlatande().toBuilder().setSysselsattning(Arrays.asList(sysselsattning)).build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertTrue(intyg.getSvar().isEmpty());
    }

    private LisjpUtlatande buildUtlatande() {
        return buildUtlatande(null, null);
    }
    private LisjpUtlatande buildUtlatande(RelationKod relationKod, String relationIntygsId) {
        return buildUtlatande("intygsId", "textVersion", "enhetsId", "enhetsnamn", "patientPersonId",
                "skapadAvFullstandigtNamn", "skapadAvPersonId", LocalDateTime.now(), "arbetsplatsKod", "postadress", "postNummer", "postOrt",
                "epost", "telefonNummer", "vardgivarid", "vardgivarNamn", "forskrivarKod", "fornamn", "efternamn", "mellannamn", "patientPostadress",
                "patientPostnummer", "patientPostort", relationKod, relationIntygsId);
    }

    private LisjpUtlatande buildUtlatande(String intygsId, String textVersion, String enhetsId, String enhetsnamn,
            String patientPersonId, String skapadAvFullstandigtNamn, String skapadAvPersonId, LocalDateTime signeringsdatum, String arbetsplatsKod,
            String postadress, String postNummer, String postOrt, String epost, String telefonNummer, String vardgivarid, String vardgivarNamn,
            String forskrivarKod, String fornamn, String efternamn, String mellannamn, String patientPostadress, String patientPostnummer,
            String patientPostort, RelationKod relationKod, String relationIntygsId) {
        LisjpUtlatande.Builder template = LisjpUtlatande.builder();
        template.setId(intygsId);
        template.setTextVersion(textVersion);
        GrundData grundData = new GrundData();
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);
        vardenhet.setEnhetsnamn(enhetsnamn);
        vardenhet.setArbetsplatsKod(arbetsplatsKod);
        vardenhet.setPostadress(postadress);
        vardenhet.setPostnummer(postNummer);
        vardenhet.setPostort(postOrt);
        vardenhet.setEpost(epost);
        vardenhet.setTelefonnummer(telefonNummer);
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivarid);
        vardgivare.setVardgivarnamn(vardgivarNamn);
        vardenhet.setVardgivare(vardgivare);
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setFullstandigtNamn(skapadAvFullstandigtNamn);
        skapadAv.setPersonId(skapadAvPersonId);
        skapadAv.setForskrivarKod(forskrivarKod);
        grundData.setSkapadAv(skapadAv);
        Patient patient = new Patient();
        Personnummer personId = new Personnummer(patientPersonId);
        patient.setPersonId(personId);
        patient.setFornamn(fornamn);
        patient.setEfternamn(efternamn);
        patient.setMellannamn(mellannamn);
        patient.setPostadress(patientPostadress);
        patient.setPostnummer(patientPostnummer);
        patient.setPostort(patientPostort);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(signeringsdatum);
        if (relationKod != null) {
            Relation relation = new Relation();
            relation.setRelationIntygsId(relationIntygsId);
            relation.setRelationKod(relationKod);
            grundData.setRelation(relation);
        }
        template.setGrundData(grundData);

        return template.build();
    }
}
