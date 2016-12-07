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

import static se.inera.intyg.common.support.Constants.KV_UTLATANDETYP_INTYG_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.*;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.intygstyper.fk7263.model.internal.PrognosBedomning;
import se.inera.intyg.intygstyper.fk7263.model.internal.Rehabilitering;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.support.Fk7263EntryPoint;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;

public final class UtlatandeToIntyg {

    public static final String DIAGNOS_FRITEXT_DELSVAR_10001_1 = "10001.1";
    public static final String DIAGNOS_DELSVAR_6_2 = "6.2";
    public static final String DIAGNOS_SVAR_6 = "6";
    public static final String DIAGNOS_FRITEXT_SVAR_10001 = "10001";
    public static final String REHABILITERING_DELSVAR_10005_2 = "10005.2";
    public static final String REHABILITERING_DELSVAR_10005_1 = "10005.1";
    public static final String REHABILITERING_SVAR_10005 = "10005";
    public static final String ARBETSFORMAGA_PROGNOS_DELSVAR_10006_3 = "10006.3";
    public static final String ARBETSFORMAGA_PROGNOS_DELSVAR_10006_2 = "10006.2";
    public static final String ARBETSFORMAGA_PROGNOS_DELSVAR_10006_1 = "10006.1";
    public static final String ARBETSFORMAGA_PROGNOS_SVAR_10006 = "10006";
    public static final String ARBETSFORMAGA_PROGNOS_SJUKSKRIVNING_LANGRE_AN_REKOMMENDERAD_MOTIVERING_DELSVAR_37_1 = "37.1";
    public static final String ARBETSFORMAGA_PROGNOS_SJUKSKRIVNING_LANGRE_AN_REKOMMENDERAD_MOTIVERING_SVAR_37 = "37";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_35_1 = "35.1";
    public static final String FUNKTIONSNEDSATTNING_SVAR_35 = "35";
    public static final String RESSATT_TILL_ARBETE_AKTUELLT_DELSVAR_34_1 = "34.1";
    public static final String RESSATT_TILL_ARBETE_AKTUELLT_SVAR_34 = "34";
    public static final String NUVARANDE_ARBETSUPPGIFTER_DELSVAR_29_1 = "29.1";
    public static final String NUVARANDE_ARBETSUPPGIFTER_SVAR_29 = "29";
    public static final String SYSSELSATTNING_DELSVAR_28_1 = "28.1";
    public static final String SYSSELSATTNING_SVAR_28 = "28";
    public static final String AVSTANGNING_SMITTSKYDD_DELSVAR_27_1 = "27.1";
    public static final String AVSTANGNING_SMITTSKYDD_SVAR_27 = "27";
    public static final String KONTAKT_MED_FK_DELSVAR_26_1 = "26.1";
    public static final String KONTAKT_MED_FK_SVAR_26 = "26";
    public static final String OVRIGA_UPPLYSNINGAR_DELSVAR_25_1 = "25.1";
    public static final String OVRIGA_UPPLYSNINGAR_SVAR_25 = "25";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_17_1 = "17.1";
    public static final String AKTIVITETSBEGRANSNING_SVAR_17 = "17";
    public static final String REKOMMENDATION_KONTAKT_DELSVAR_OVRIGT_10003_3 = "10003.3";
    public static final String REKOMMENDATION_KONTAKT_DELSVAR_FHV_10003_2 = "10003.2";
    public static final String REKOMMENDATION_KONTAKT_DELSVAR_AF_10003_1 = "10003.1";
    public static final String REKOMMENDATION_KONTAKT_SVAR_10003 = "10003";
    public static final String SJUKDOMSFORLOPP_DELSVAR_10002_1 = "10002.1";
    public static final String SJUKDOMSFORLOPP_SVAR_10002 = "10002";
    public static final String ATGARD_INOM_SJUKVARDEN_SVAR_10004 = "10004";
    public static final String ATGARD_INOM_SJUKVARDEN_DELSVAR_10004_1 = "10004.1";
    public static final String ATGARD_INOM_SJUKVARDEN_DELSVAR_10004_2 = "10004.2";
    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32 = "32";
    public static final String BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32 = "32.1";
    public static final String BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32 = "32.2";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM = "KV_FKMU_0001";
    public static final String TYP_AV_SYSSELSATTNING_CODE_SYSTEM = "KV_FKMU_0002";
    public static final String SJUKSKRIVNING_CODE_SYSTEM = "KV_FKMU_0003";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM = "KV_FKMU_0004";
    public static final String UNDERLAG_CODE_SYSTEM = "KV_FKMU_0005";
    public static final String PROGNOS_CODE_SYSTEM = "KV_FKMU_0006";
    public static final String PROGNOS_DAGAR_TILL_ARBETE_CODE_SYSTEM = "KV_FKMU_0007";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1 = "1.2";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1 = "1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1 = "1.1";

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(Utlatande source) {
        Intyg intyg = getIntyg(source);
        intyg.setTyp(getTypAvIntyg(source));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(Utlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(KV_UTLATANDETYP_INTYG_CODE_SYSTEM);
        typAvIntyg.setDisplayName(Fk7263EntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(Utlatande source) {
        List<Svar> svars = new ArrayList<>();

        int sjukskrivningInstans = 1;
        if (source.getNedsattMed100() != null && source.getNedsattMed100().isValid()) {
            svars.add(createBehovAvSjukskrivningSvar(SjukskrivningsGrad.HELT_NEDSATT, sjukskrivningInstans++, source.getNedsattMed100()));
        }
        if (source.getNedsattMed75() != null && source.getNedsattMed75().isValid()) {
            svars.add(createBehovAvSjukskrivningSvar(SjukskrivningsGrad.NEDSATT_3_4, sjukskrivningInstans++, source.getNedsattMed75()));
        }
        if (source.getNedsattMed50() != null && source.getNedsattMed50().isValid()) {
            svars.add(createBehovAvSjukskrivningSvar(SjukskrivningsGrad.NEDSATT_HALFTEN, sjukskrivningInstans++, source.getNedsattMed50()));
        }
        if (source.getNedsattMed25() != null && source.getNedsattMed25().isValid()) {
            svars.add(createBehovAvSjukskrivningSvar(SjukskrivningsGrad.NEDSATT_1_4, sjukskrivningInstans++, source.getNedsattMed25()));
        }

        Svar svar = createAtgard(source);
        if (!svar.getDelsvar().isEmpty()) {
            svars.add(createAtgard(source));
        }

        if (source.getDiagnosKod() != null) {
            svars.add(createDiagnosSvar(source));
        }

        if (source.getDiagnosBeskrivning() != null) {
            svars.add(createDiagnosFritextSvar(source));
        }

        if (source.getPrognosBedomning() != null) {
            svars.add(createPrognosSvar(source));
        }

        if (source.getRehabilitering() != null) {
            svars.add(createRehabiliteringSvar(source));
        }

        int grundForMUInstans = 1;
        if (source.getTelefonkontaktMedPatienten() != null && source.getTelefonkontaktMedPatienten().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, ReferensTyp.TELEFONKONTAKT.transportId, ReferensTyp.TELEFONKONTAKT.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getTelefonkontaktMedPatienten().asLocalDate().toString())
                    .build());
        }
        if (source.getUndersokningAvPatienten() != null && source.getUndersokningAvPatienten().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, ReferensTyp.UNDERSOKNING.transportId, ReferensTyp.UNDERSOKNING.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getUndersokningAvPatienten().asLocalDate().toString())
                    .build());
        }
        if (source.getJournaluppgifter() != null && source.getJournaluppgifter().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, ReferensTyp.JOURNAL.transportId, ReferensTyp.JOURNAL.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getJournaluppgifter().asLocalDate().toString()).build());
        }
        if (source.getAnnanReferens() != null && source.getAnnanReferens().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, ReferensTyp.ANNAT.transportId, ReferensTyp.ANNAT.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getAnnanReferens().asLocalDate().toString()).build());
        }

        addIfNotBlank(svars, SJUKDOMSFORLOPP_SVAR_10002, SJUKDOMSFORLOPP_DELSVAR_10002_1, source.getSjukdomsforlopp());
        svars.add(createRekommendation(source, svars));

        addIfNotBlank(svars, AKTIVITETSBEGRANSNING_SVAR_17, AKTIVITETSBEGRANSNING_DELSVAR_17_1, source.getAktivitetsbegransning());
        addIfNotBlank(svars, OVRIGA_UPPLYSNINGAR_SVAR_25, OVRIGA_UPPLYSNINGAR_DELSVAR_25_1, buildOvrigaUpplysningar(source));
        svars.add(aSvar(KONTAKT_MED_FK_SVAR_26).withDelsvar(KONTAKT_MED_FK_DELSVAR_26_1, String.valueOf(source.isKontaktMedFk())).build());
        svars.add(aSvar(AVSTANGNING_SMITTSKYDD_SVAR_27)
                .withDelsvar(AVSTANGNING_SMITTSKYDD_DELSVAR_27_1, String.valueOf(source.isAvstangningSmittskydd())).build());
        if (source.isArbetsloshet()) {
            svars.add(aSvar(SYSSELSATTNING_SVAR_28).withDelsvar(SYSSELSATTNING_DELSVAR_28_1,
                    aCV(TYP_AV_SYSSELSATTNING_CODE_SYSTEM, Sysselsattning.ARBETSSOKANDE.getTransportId(), Sysselsattning.ARBETSSOKANDE.getLabel()))
                    .build());
        }
        if (source.isForaldrarledighet()) {
            svars.add(aSvar(SYSSELSATTNING_SVAR_28).withDelsvar(SYSSELSATTNING_DELSVAR_28_1, aCV(TYP_AV_SYSSELSATTNING_CODE_SYSTEM,
                    Sysselsattning.FORADLRARLEDIGHET_VARD_AV_BARN.getTransportId(), Sysselsattning.FORADLRARLEDIGHET_VARD_AV_BARN.getLabel()))
                    .build());
        }
        if (source.isNuvarandeArbete()) {
            svars.add(aSvar(SYSSELSATTNING_SVAR_28)
                    .withDelsvar(SYSSELSATTNING_DELSVAR_28_1, aCV(TYP_AV_SYSSELSATTNING_CODE_SYSTEM, Sysselsattning.NUVARANDE_ARBETE.getTransportId(),
                            Sysselsattning.NUVARANDE_ARBETE.getLabel()))
                    .build());
        }
        addIfNotBlank(svars, NUVARANDE_ARBETSUPPGIFTER_SVAR_29, NUVARANDE_ARBETSUPPGIFTER_DELSVAR_29_1, source.getNuvarandeArbetsuppgifter());
        svars.add(aSvar(RESSATT_TILL_ARBETE_AKTUELLT_SVAR_34)
                .withDelsvar(RESSATT_TILL_ARBETE_AKTUELLT_DELSVAR_34_1, String.valueOf(source.isRessattTillArbeteAktuellt())).build());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_SVAR_35, FUNKTIONSNEDSATTNING_DELSVAR_35_1, source.getFunktionsnedsattning());
        addIfNotBlank(svars, ARBETSFORMAGA_PROGNOS_SJUKSKRIVNING_LANGRE_AN_REKOMMENDERAD_MOTIVERING_SVAR_37,
                ARBETSFORMAGA_PROGNOS_SJUKSKRIVNING_LANGRE_AN_REKOMMENDERAD_MOTIVERING_DELSVAR_37_1, source.getArbetsformagaPrognos());

        return svars;
    }

    private static Svar createAtgard(Utlatande source) {
        SvarBuilder svarBuilder = aSvar(ATGARD_INOM_SJUKVARDEN_SVAR_10004);
        if (!isNullOrEmpty(source.getAtgardInomSjukvarden())) {
            svarBuilder = svarBuilder.withDelsvar(ATGARD_INOM_SJUKVARDEN_DELSVAR_10004_1, source.getAtgardInomSjukvarden());
        }

        if (!isNullOrEmpty(source.getAnnanAtgard())) {
            svarBuilder = svarBuilder.withDelsvar(ATGARD_INOM_SJUKVARDEN_DELSVAR_10004_2, source.getAnnanAtgard());
        }
        return svarBuilder.build();
    }

    private static Svar createRekommendation(Utlatande source, List<Svar> svars) {
        SvarBuilder svarBuilder = aSvar(REKOMMENDATION_KONTAKT_SVAR_10003)
                .withDelsvar(REKOMMENDATION_KONTAKT_DELSVAR_AF_10003_1, String.valueOf(source.isRekommendationKontaktArbetsformedlingen()))
                .withDelsvar(REKOMMENDATION_KONTAKT_DELSVAR_FHV_10003_2, String.valueOf(source.isRekommendationKontaktForetagshalsovarden()));
        if (!isNullOrEmpty(source.getRekommendationOvrigt())) {
            svarBuilder = svarBuilder.withDelsvar(REKOMMENDATION_KONTAKT_DELSVAR_OVRIGT_10003_3, source.getRekommendationOvrigt());
        }
        return svarBuilder.build();
    }

    private static String buildOvrigaUpplysningar(Utlatande source) {
        String annanRef = null;
        String prognosBedomning = null;
        String ovrigKommentar = null;
        String arbetstidsforlaggning = null;

        // Falt4b
        if (!isNullOrEmpty(source.getAnnanReferensBeskrivning())) {
            annanRef = "4b: " + source.getAnnanReferensBeskrivning();
        }
        // Falt10
        if (!isNullOrEmpty(source.getArbetsformagaPrognosGarInteAttBedomaBeskrivning())) {
            prognosBedomning = "10: " + source.getArbetsformagaPrognosGarInteAttBedomaBeskrivning();
        }
        if (!isNullOrEmpty(source.getKommentar())) {
            ovrigKommentar = source.getKommentar();
        }
        String ret = Joiner.on(". ").skipNulls().join(annanRef, arbetstidsforlaggning, prognosBedomning, ovrigKommentar);
        return !isNullOrEmpty(ret) ? ret : null;
    }

    private static Svar createPrognosSvar(Utlatande source) {
        PrognosBedomning bedomning = source.getPrognosBedomning();
        SvarBuilder svar = aSvar(ARBETSFORMAGA_PROGNOS_SVAR_10006);
        switch (bedomning) {
        case arbetsformagaPrognosJa:
            return svar.withDelsvar(ARBETSFORMAGA_PROGNOS_DELSVAR_10006_1, String.valueOf(true)).build();
        case arbetsformagaPrognosNej:
            return svar.withDelsvar(ARBETSFORMAGA_PROGNOS_DELSVAR_10006_1, String.valueOf(false)).build();
        case arbetsformagaPrognosJaDelvis:
            return svar.withDelsvar(ARBETSFORMAGA_PROGNOS_DELSVAR_10006_2, String.valueOf(true)).build();
        case arbetsformagaPrognosGarInteAttBedoma:
            return svar.withDelsvar(ARBETSFORMAGA_PROGNOS_DELSVAR_10006_3, String.valueOf(true)).build();
        default:
            return aSvar(ARBETSFORMAGA_PROGNOS_SVAR_10006).withDelsvar(ARBETSFORMAGA_PROGNOS_DELSVAR_10006_3, true).build();
        }
    }

    private static Svar createRehabiliteringSvar(Utlatande source) {
        Rehabilitering rehab = source.getRehabilitering();
        SvarBuilder svar = aSvar(REHABILITERING_SVAR_10005);
        switch (rehab) {
        case rehabiliteringAktuell:
            return svar.withDelsvar(REHABILITERING_DELSVAR_10005_1, String.valueOf(true)).build();
        case rehabiliteringEjAktuell:
            return svar.withDelsvar(REHABILITERING_DELSVAR_10005_1, String.valueOf(false)).build();
        case rehabiliteringGarInteAttBedoma:
            return svar.withDelsvar(REHABILITERING_DELSVAR_10005_2, String.valueOf(true)).build();
        default:
            return aSvar(REHABILITERING_SVAR_10005).withDelsvar(REHABILITERING_DELSVAR_10005_2, true).build();
        }
    }

    private static Svar createDiagnosFritextSvar(Utlatande source) {
        return aSvar(DIAGNOS_FRITEXT_SVAR_10001).withDelsvar(DIAGNOS_FRITEXT_DELSVAR_10001_1, source.getDiagnosBeskrivning()).build();
    }

    private static Svar createDiagnosSvar(Utlatande source) {
        SvarBuilder svarBuilder = aSvar(DIAGNOS_SVAR_6);
        if (source.getDiagnosKodsystem1() != null) {
            svarBuilder = svarBuilder.withDelsvar(DIAGNOS_DELSVAR_6_2,
                    aCV(Diagnoskodverk.valueOf(source.getDiagnosKodsystem1()).getCodeSystem(), source.getDiagnosKod(), null));
        }
        return svarBuilder.build();
    }

    private static Svar createBehovAvSjukskrivningSvar(SjukskrivningsGrad sjukskrivningsgrad, int instans, InternalLocalDateInterval interval) {
        return aSvar(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, instans).withDelsvar(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32,
                aCV(SJUKSKRIVNING_CODE_SYSTEM, sjukskrivningsgrad.getTransportId(), sjukskrivningsgrad.getLabel()))
                .withDelsvar(BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32,
                        aDatePeriod(interval.fromAsLocalDate(), interval.tomAsLocalDate()))
                .build();
    }

    private static boolean isNullOrEmpty(String source) {
        return source == null || source.isEmpty();
    }

    public enum ReferensTyp {
        UNDERSOKNING("UNDERSOKNING", "Min undersökning av patienten"),
        TELEFONKONTAKT("TELEFONKONTAKT", "Min telefonkontakt med patienten"),
        JOURNAL("JOURNALUPPGIFTER", "Journaluppgifter från den"),
        ANHORIGSBESKRIVNING("ANHORIG", "Anhörigs beskrivning av patienten"),
        ANNAT("ANNAT", "Annat");

        public final String transportId;
        public final String label;

        ReferensTyp(String transportId, String label) {
            this.transportId = transportId;
            this.label = label;
        }
    }

    private enum SjukskrivningsGrad {
        HELT_NEDSATT("HELT_NEDSATT", "100%"),
        NEDSATT_3_4("TRE_FJARDEDEL", "75%"),
        NEDSATT_HALFTEN("HALFTEN", "50%"),
        NEDSATT_1_4("EN_FJARDEDEL", "25%");

        private final String transportId;
        private final String label;

        SjukskrivningsGrad(String transportId, String label) {
            this.transportId = transportId;
            this.label = label;
        }

        public String getTransportId() {
            return transportId;
        }

        public String getLabel() {
            return label;
        }
    }
}
