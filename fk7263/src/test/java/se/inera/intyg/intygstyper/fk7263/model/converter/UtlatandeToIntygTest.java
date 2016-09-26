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

package se.inera.intyg.intygstyper.fk7263.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.intygstyper.fk7263.model.converter.UtlatandeToIntyg.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.*;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.fk7263.model.internal.PrognosBedomning;
import se.inera.intyg.intygstyper.fk7263.model.internal.Rehabilitering;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public class UtlatandeToIntygTest {

    @Test
    public void testConvert() throws Exception {
        final String intygsId = "intygsid";
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

        Utlatande utlatande = buildUtlatande(intygsId, enhetsId, enhetsnamn, patientPersonId,
                skapadAvFullstandigtNamn, skapadAvPersonId, signeringsdatum, arbetsplatsKod, postadress, postNummer, postOrt, epost, telefonNummer,
                vardgivarid, vardgivarNamn, forskrivarKod, fornamn, efternamn, mellannamn, patientPostadress, patientPostnummer, patientPostort,
                null, null);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertNotNull(intyg.getVersion());
        assertEquals(utlatande.getTyp().toUpperCase(), intyg.getTyp().getCode());
        assertEquals("f6fb361a-e31d-48b8-8657-99b63912dd9b", intyg.getTyp().getCodeSystem());
        assertNotNull(intyg.getTyp().getDisplayName());
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
        Utlatande utlatande = buildUtlatande(relationKod, relationIntygsId);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertNotNull(intyg.getRelation());
        assertEquals(1, intyg.getRelation().size());
        assertEquals(relationKod.value(), intyg.getRelation().get(0).getTyp().getCode());
        assertNotNull(intyg.getRelation().get(0).getTyp().getCodeSystem());
        assertEquals(relationIntygsId, intyg.getRelation().get(0).getIntygsId().getExtension());
        assertNotNull(intyg.getRelation().get(0).getIntygsId().getRoot());
    }

    @Test
    public void testSvar() {

        Utlatande utlatande = buildFullUtlatande();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        Map<String, List<Svar>> testSvar = intyg.getSvar().stream().collect(Collectors.groupingBy(Svar::getId));

        assertAll(intyg, testSvar);

        assertRehabilitering(testSvar);

        assertKontaktFk(testSvar);

        assertRessatTillArbetet(testSvar);

        assertAvstangningSmittskydd(testSvar);

        assertArbetsuppgifter(testSvar);

        assertAktivitetsBegransning(testSvar);

        assertOvrigaUpplysningar(testSvar);

        assertFunktionsnedsattning(testSvar);

        assertArbetsformagaPrognos(testSvar);

        assertSjukdomsForlopp(testSvar);

        assertDiagnosBeskrivning(testSvar);

        assertAtgard(testSvar);

        assertDiagnos(testSvar);

        assertSysselsattning(testSvar);

        assertGrundForMedicinsktUnderlag(testSvar);

        assertRekommendationKontakt(testSvar);

    }

    private Utlatande buildFullUtlatande() {
        InternalDate annanReferensDate = new InternalDate("2015-01-01");
        InternalDate from = new InternalDate("2015-01-01");
        InternalDate to = new InternalDate("2015-01-02");
        InternalDate journalDate = new InternalDate();
        Utlatande utlatande = buildUtlatande();
        utlatande.setAktivitetsbegransning("aktivitetsbegransning");
        utlatande.setAnnanAtgard("annan atgard");
        utlatande.setAnnanReferens(annanReferensDate);
        utlatande.setAnnanReferensBeskrivning("AnnanReferensBeskrivning");
        utlatande.setArbetsformagaPrognos("ArbetsFormagaPrognos");
        utlatande.setArbetsloshet(true);
        utlatande.setAtgardInomSjukvarden("AtgardInomSjukvarden");
        utlatande.setAvstangningSmittskydd(false);
        utlatande.setDiagnosBeskrivning("DiagnosBeskrivning");
        utlatande.setDiagnosBeskrivning1("HuvudDiagnosBeskrivning");
        utlatande.setDiagnosKod("DiagnosKod");
        utlatande.setDiagnosKodsystem1(Diagnoskodverk.ICD_10_SE.name());
        utlatande.setForaldrarledighet(true);
        utlatande.setFunktionsnedsattning("FunktionsNedsattning");
        utlatande.setGiltighet(new LocalDateInterval(LocalDate.now(), LocalDate.now().plusDays(4)));
        utlatande.setJournaluppgifter(journalDate);
        utlatande.setKommentar("Kommentar");
        utlatande.setKontaktMedFk(true);
        utlatande.setNuvarandeArbete(true);
        utlatande.setNuvarandeArbetsuppgifter("Arbetsuppgifter");
        utlatande.setPrognosBedomning(PrognosBedomning.arbetsformagaPrognosJaDelvis);
        utlatande.setRehabilitering(Rehabilitering.rehabiliteringEjAktuell);
        utlatande.setRekommendationKontaktArbetsformedlingen(true);
        utlatande.setRekommendationKontaktForetagshalsovarden(true);
        utlatande.setRekommendationOvrigtCheck(true);
        utlatande.setRekommendationOvrigt("RekommendationOvrigt");
        utlatande.setRessattTillArbeteAktuellt(true);
        utlatande.setSamsjuklighet(true);
        utlatande.setSjukdomsforlopp("sjukdomsforlopp");
        utlatande.setTelefonkontaktMedPatienten(from);
        utlatande.setTjanstgoringstid("Tjanstgoringstid");
        utlatande.setUndersokningAvPatienten(to);
        return utlatande;
    }

    private void assertAll(Intyg intyg, Map<String, List<Svar>> testSvar) {
        String[] svars = { GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1,
                DIAGNOS_SVAR_6,
                AKTIVITETSBEGRANSNING_SVAR_17,
                OVRIGA_UPPLYSNINGAR_SVAR_25,
                KONTAKT_MED_FK_SVAR_26, AVSTANGNING_SMITTSKYDD_SVAR_27,
                SYSSELSATTNING_SVAR_28, NUVARANDE_ARBETSUPPGIFTER_SVAR_29,
                RESSATT_TILL_ARBETE_AKTUELLT_SVAR_34,FUNKTIONSNEDSATTNING_SVAR_35,
                ARBETSFORMAGA_PROGNOS_SJUKSKRIVNING_LANGRE_AN_REKOMMENDERAD_MOTIVERING_SVAR_37,
                DIAGNOS_FRITEXT_SVAR_10001, SJUKDOMSFORLOPP_SVAR_10002, REKOMMENDATION_KONTAKT_SVAR_10003,
                ATGARD_INOM_SJUKVARDEN_SVAR_10004, REHABILITERING_SVAR_10005, ARBETSFORMAGA_PROGNOS_SVAR_10006 };
        assertEquals(21, intyg.getSvar().size());

        for (String svar : svars) {
            assertTrue(testSvar.containsKey(svar));
        }
    }

    private void assertRehabilitering(Map<String, List<Svar>> testSvar) {
        assertEquals("false", testSvar.get(REHABILITERING_SVAR_10005).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.REHABILITERING_DELSVAR_10005_1, testSvar.get(REHABILITERING_SVAR_10005).get(0).getDelsvar().get(0).getId());
    }

    private void assertKontaktFk(Map<String, List<Svar>> testSvar) {
        assertEquals("true", testSvar.get(KONTAKT_MED_FK_SVAR_26).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.KONTAKT_MED_FK_DELSVAR_26_1, testSvar.get(KONTAKT_MED_FK_SVAR_26).get(0).getDelsvar().get(0).getId());
    }

    private void assertRessatTillArbetet(Map<String, List<Svar>> testSvar) {
        assertEquals("true", testSvar.get(RESSATT_TILL_ARBETE_AKTUELLT_SVAR_34).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.RESSATT_TILL_ARBETE_AKTUELLT_DELSVAR_34_1, testSvar.get(RESSATT_TILL_ARBETE_AKTUELLT_SVAR_34).get(0).getDelsvar().get(0).getId());
    }

    private void assertAvstangningSmittskydd(Map<String, List<Svar>> testSvar) {
        assertEquals("false", testSvar.get(AVSTANGNING_SMITTSKYDD_SVAR_27).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.AVSTANGNING_SMITTSKYDD_DELSVAR_27_1, testSvar.get(AVSTANGNING_SMITTSKYDD_SVAR_27).get(0).getDelsvar().get(0).getId());
    }

    private void assertArbetsuppgifter(Map<String, List<Svar>> testSvar) {
        assertEquals("Arbetsuppgifter", testSvar.get(NUVARANDE_ARBETSUPPGIFTER_SVAR_29).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.NUVARANDE_ARBETSUPPGIFTER_DELSVAR_29_1, testSvar.get(NUVARANDE_ARBETSUPPGIFTER_SVAR_29).get(0).getDelsvar().get(0).getId());
    }

    private void assertAktivitetsBegransning(Map<String, List<Svar>> testSvar) {
        assertEquals("aktivitetsbegransning", testSvar.get(AKTIVITETSBEGRANSNING_SVAR_17).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.AKTIVITETSBEGRANSNING_DELSVAR_17_1, testSvar.get(AKTIVITETSBEGRANSNING_SVAR_17).get(0).getDelsvar().get(0).getId());
    }

    private void assertOvrigaUpplysningar(Map<String, List<Svar>> testSvar) {
        assertEquals("4b: AnnanReferensBeskrivning. Kommentar", testSvar.get(OVRIGA_UPPLYSNINGAR_SVAR_25).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.OVRIGA_UPPLYSNINGAR_DELSVAR_25_1, testSvar.get(OVRIGA_UPPLYSNINGAR_SVAR_25).get(0).getDelsvar().get(0).getId());
    }

    private void assertFunktionsnedsattning(Map<String, List<Svar>> testSvar) {
        assertEquals("FunktionsNedsattning", testSvar.get(FUNKTIONSNEDSATTNING_SVAR_35).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.FUNKTIONSNEDSATTNING_DELSVAR_35_1, testSvar.get(FUNKTIONSNEDSATTNING_SVAR_35).get(0).getDelsvar().get(0).getId());
    }

    private void assertArbetsformagaPrognos(Map<String, List<Svar>> testSvar) {
        assertEquals("ArbetsFormagaPrognos", testSvar.get(ARBETSFORMAGA_PROGNOS_SJUKSKRIVNING_LANGRE_AN_REKOMMENDERAD_MOTIVERING_SVAR_37).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.ARBETSFORMAGA_PROGNOS_SJUKSKRIVNING_LANGRE_AN_REKOMMENDERAD_MOTIVERING_DELSVAR_37_1, testSvar.get(ARBETSFORMAGA_PROGNOS_SJUKSKRIVNING_LANGRE_AN_REKOMMENDERAD_MOTIVERING_SVAR_37).get(0).getDelsvar().get(0).getId());
    }

    private void assertSjukdomsForlopp(Map<String, List<Svar>> testSvar) {
        assertEquals("sjukdomsforlopp", testSvar.get(SJUKDOMSFORLOPP_SVAR_10002).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.SJUKDOMSFORLOPP_DELSVAR_10002_1, testSvar.get(SJUKDOMSFORLOPP_SVAR_10002).get(0).getDelsvar().get(0).getId());
    }

    private void assertDiagnosBeskrivning(Map<String, List<Svar>> testSvar) {
        assertEquals("DiagnosBeskrivning", testSvar.get(DIAGNOS_FRITEXT_SVAR_10001).get(0).getDelsvar().get(0).getContent().get(0));
        assertEquals(UtlatandeToIntyg.DIAGNOS_FRITEXT_DELSVAR_10001_1, testSvar.get(DIAGNOS_FRITEXT_SVAR_10001).get(0).getDelsvar().get(0).getId());
    }

    @SuppressWarnings("unchecked")
    private void assertSysselsattning(Map<String, List<Svar>> testSvar) {
        assertEquals(3, testSvar.get(SYSSELSATTNING_SVAR_28).size());
        List<String> sysselsattningar = Arrays.asList("ARBETSSOKANDE", "FORALDRALEDIG", "NUVARANDE_ARBETE");
        for (Svar svar : testSvar.get(SYSSELSATTNING_SVAR_28)) {
            JAXBElement<CVType> cv = (JAXBElement<CVType>) svar.getDelsvar().get(0).getContent().get(0);
            assertEquals(UtlatandeToIntyg.TYP_AV_SYSSELSATTNING_CODE_SYSTEM, cv.getValue().getCodeSystem());
            sysselsattningar
                    .forEach(sysselsattning -> assertTrue(sysselsattningar.contains(cv.getValue().getCode())));
        }
    }

    @SuppressWarnings("unchecked")
    private void assertGrundForMedicinsktUnderlag(Map<String, List<Svar>> testSvar) {
        List<String> grunderForMu = Arrays.asList("TELEFONKONTAKT", "UNDERSOKNING", "ANNAT");
        assertEquals(3, testSvar.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).size());
        for (Svar svar : testSvar.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)) {
            JAXBElement<CVType> cv = (JAXBElement<CVType>) svar.getDelsvar().get(0).getContent().get(0);
            assertEquals(UtlatandeToIntyg.GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, cv.getValue().getCodeSystem());
            grunderForMu
            .forEach(grundForMu -> assertTrue(grunderForMu.contains(cv.getValue().getCode())));
        }
    }

    private void assertRekommendationKontakt(Map<String, List<Svar>> testSvar) {
        assertEquals(1, testSvar.get(REKOMMENDATION_KONTAKT_SVAR_10003).size());
        Map<String, List<Object>> rekDelsvar = testSvar.get(REKOMMENDATION_KONTAKT_SVAR_10003).get(0).getDelsvar().stream().collect(Collectors.toMap(Delsvar::getId, Delsvar::getContent));
        assertEquals(rekDelsvar.get(UtlatandeToIntyg.REKOMMENDATION_KONTAKT_DELSVAR_AF_10003_1).get(0), "true");
        assertEquals(rekDelsvar.get(UtlatandeToIntyg.REKOMMENDATION_KONTAKT_DELSVAR_FHV_10003_2).get(0), "true");
        assertEquals(rekDelsvar.get(UtlatandeToIntyg.REKOMMENDATION_KONTAKT_DELSVAR_OVRIGT_10003_3).get(0), "RekommendationOvrigt");
    }

    @SuppressWarnings("unchecked")
    private void assertDiagnos(Map<String, List<Svar>> testSvar) {
        assertEquals(1, testSvar.get(DIAGNOS_SVAR_6).size());
        assertEquals(1, testSvar.get(DIAGNOS_SVAR_6).get(0).getDelsvar().size());
        assertEquals(UtlatandeToIntyg.DIAGNOS_DELSVAR_6_2, testSvar.get(DIAGNOS_SVAR_6).get(0).getDelsvar().get(0).getId());
        JAXBElement<CVType> cv = (JAXBElement<CVType>) testSvar.get(DIAGNOS_SVAR_6).get(0).getDelsvar().get(0).getContent().get(0);
        assertEquals("DiagnosKod", cv.getValue().getCode());
        assertEquals(Diagnoskodverk.ICD_10_SE.getCodeSystem(), cv.getValue().getCodeSystem());
    }

    private void assertAtgard(Map<String, List<Svar>> testSvar) {
        assertEquals(1, testSvar.get(ATGARD_INOM_SJUKVARDEN_SVAR_10004).size());
        Map<String, List<Object>> rekDelsvar = testSvar.get(ATGARD_INOM_SJUKVARDEN_SVAR_10004).get(0).getDelsvar().stream().collect(Collectors.toMap(Delsvar::getId, Delsvar::getContent));
        assertEquals(rekDelsvar.get(UtlatandeToIntyg.ATGARD_INOM_SJUKVARDEN_DELSVAR_10004_1).get(0), "AtgardInomSjukvarden");
        assertEquals(rekDelsvar.get(UtlatandeToIntyg.ATGARD_INOM_SJUKVARDEN_DELSVAR_10004_2).get(0), "annan atgard");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddBehovAvSjukskrivningSvar() {
        InternalDate from25 = new InternalDate("2015-01-01");
        InternalDate to25 = new InternalDate("2015-01-02");
        InternalDate from50 = new InternalDate("2015-01-03");
        InternalDate to50 = new InternalDate("2015-01-04");
        InternalDate from75 = new InternalDate("2015-01-05");
        InternalDate to75 = new InternalDate("2015-01-06");
        InternalDate from100 = new InternalDate("2015-01-07");
        InternalDate to100 = new InternalDate("2015-01-08");
        Utlatande utlatande = buildUtlatande();
        utlatande.setNedsattMed25(new InternalLocalDateInterval(from25, to25));
        utlatande.setNedsattMed50(new InternalLocalDateInterval(from50, to50));
        utlatande.setNedsattMed75(new InternalLocalDateInterval(from75, to75));
        utlatande.setNedsattMed100(new InternalLocalDateInterval(from100, to100));

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        assertEquals(8, intyg.getSvar().size());
        assertEquals("32", intyg.getSvar().get(0).getId());
        assertEquals(2, intyg.getSvar().get(0).getDelsvar().size());
        assertEquals("32.1", intyg.getSvar().get(0).getDelsvar().get(0).getId());
        JAXBElement<CVType> cv = (JAXBElement<CVType>) intyg.getSvar().get(0).getDelsvar().get(0).getContent().get(0);
        assertEquals("HELT_NEDSATT", cv.getValue().getCode());
        assertEquals("KV_FKMU_0003", cv.getValue().getCodeSystem());
        assertEquals("100%", cv.getValue().getDisplayName());
        JAXBElement<DatePeriodType> dateperiod = (JAXBElement<DatePeriodType>) intyg.getSvar().get(0).getDelsvar().get(1).getContent().get(0);
        assertEquals(from100.asLocalDate(), dateperiod.getValue().getStart());
        assertEquals(to100.asLocalDate(), dateperiod.getValue().getEnd());
        assertEquals("32", intyg.getSvar().get(1).getId());
        assertEquals(2, intyg.getSvar().get(1).getDelsvar().size());
        assertEquals("32.1", intyg.getSvar().get(1).getDelsvar().get(0).getId());
        cv = (JAXBElement<CVType>) intyg.getSvar().get(1).getDelsvar().get(0).getContent().get(0);
        assertEquals("TRE_FJARDEDEL", cv.getValue().getCode());
        assertEquals("KV_FKMU_0003", cv.getValue().getCodeSystem());
        assertEquals("75%", cv.getValue().getDisplayName());
        dateperiod = (JAXBElement<DatePeriodType>) intyg.getSvar().get(1).getDelsvar().get(1).getContent().get(0);
        assertEquals(from75.asLocalDate(), dateperiod.getValue().getStart());
        assertEquals(to75.asLocalDate(), dateperiod.getValue().getEnd());
        assertEquals("32", intyg.getSvar().get(2).getId());
        assertEquals(2, intyg.getSvar().get(2).getDelsvar().size());
        assertEquals("32.1", intyg.getSvar().get(2).getDelsvar().get(0).getId());
        cv = (JAXBElement<CVType>) intyg.getSvar().get(2).getDelsvar().get(0).getContent().get(0);
        assertEquals("HALFTEN", cv.getValue().getCode());
        assertEquals("KV_FKMU_0003", cv.getValue().getCodeSystem());
        assertEquals("50%", cv.getValue().getDisplayName());
        dateperiod = (JAXBElement<DatePeriodType>) intyg.getSvar().get(2).getDelsvar().get(1).getContent().get(0);
        assertEquals(from50.asLocalDate(), dateperiod.getValue().getStart());
        assertEquals(to50.asLocalDate(), dateperiod.getValue().getEnd());
        assertEquals("32", intyg.getSvar().get(3).getId());
        assertEquals(2, intyg.getSvar().get(3).getDelsvar().size());
        assertEquals("32.1", intyg.getSvar().get(3).getDelsvar().get(0).getId());
        cv = (JAXBElement<CVType>) intyg.getSvar().get(3).getDelsvar().get(0).getContent().get(0);
        assertEquals("EN_FJARDEDEL", cv.getValue().getCode());
        assertEquals("KV_FKMU_0003", cv.getValue().getCodeSystem());
        assertEquals("25%", cv.getValue().getDisplayName());
        dateperiod = (JAXBElement<DatePeriodType>) intyg.getSvar().get(3).getDelsvar().get(1).getContent().get(0);
        assertEquals(from25.asLocalDate(), dateperiod.getValue().getStart());
        assertEquals(to25.asLocalDate(), dateperiod.getValue().getEnd());
    }

    private Utlatande buildUtlatande() {
        return buildUtlatande(null, null);
    }

    private Utlatande buildUtlatande(RelationKod relationKod, String relationIntygsId) {
        return buildUtlatande("intygsId", "enhetsId", "enhetsnamn", "patientPersonId",
                "skapadAvFullstandigtNamn", "skapadAvPersonId", LocalDateTime.now(), "arbetsplatsKod", "postadress", "postNummer", "postOrt",
                "epost", "telefonNummer", "vardgivarid", "vardgivarNamn", "forskrivarKod", "fornamn", "efternamn", "mellannamn", "patientPostadress",
                "patientPostnummer", "patientPostort", relationKod, relationIntygsId);
    }

    private Utlatande buildUtlatande(String intygsId, String enhetsId, String enhetsnamn,
            String patientPersonId, String skapadAvFullstandigtNamn, String skapadAvPersonId, LocalDateTime signeringsdatum, String arbetsplatsKod,
            String postadress, String postNummer, String postOrt, String epost, String telefonNummer, String vardgivarid, String vardgivarNamn,
            String forskrivarKod, String fornamn, String efternamn, String mellannamn, String patientPostadress, String patientPostnummer,
            String patientPostort, RelationKod relationKod, String relationIntygsId) {
        Utlatande utlatande = new Utlatande();
        utlatande.setId(intygsId);
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
        utlatande.setGrundData(grundData);
        return utlatande;
    }
}
