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

package se.inera.intyg.intygstyper.ts_bas.model.converter;

import static se.inera.intyg.common.support.Constants.KV_ID_KONTROLL_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_INTYGET_AVSER_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_KORKORTSBEHORIGHET_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_UTLATANDETYP_INTYG_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotNull;
import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.*;
import static se.inera.intyg.intygstyper.ts_parent.model.converter.InternalToTransportUtil.getVersion;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil.SvarBuilder;
import se.inera.intyg.intygstyper.ts_bas.model.internal.*;
import se.inera.intyg.intygstyper.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.intygstyper.ts_parent.codes.*;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;

public final class UtlatandeToIntyg {

    private static final String DEFAULT_VERSION = "6.7";

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(Utlatande source) {
        Intyg intyg = InternalConverterUtil.getIntyg(source);
        complementArbetsplatskodIfMissing(intyg);
        intyg.setTyp(getTypAvIntyg(source));
        intyg.getSvar().addAll(getSvar(source));
        intyg.setVersion(getVersion(source).orElse(DEFAULT_VERSION));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(Utlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(TsBasEntryPoint.KV_UTLATANDETYP_INTYG_CODE);
        typAvIntyg.setCodeSystem(KV_UTLATANDETYP_INTYG_CODE_SYSTEM);
        typAvIntyg.setDisplayName(TsBasEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static void complementArbetsplatskodIfMissing(Intyg intyg) {
        if (StringUtils.isBlank(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension())) {
            intyg.getSkapadAv().getEnhet().getArbetsplatskod().setExtension(NOT_AVAILABLE);
        }
    }

    private static List<Svar> getSvar(Utlatande source) {
        List<Svar> svars = new ArrayList<>();

        if (source.getIntygAvser() != null) {
            int intygAvserInstans = 1;
            for (IntygAvserKategori korkortstyp : source.getIntygAvser().getKorkortstyp()) {
                IntygAvserKod intygAvser = IntygAvserKod.valueOf(korkortstyp.name());
                svars.add(aSvar(INTYG_AVSER_SVAR_ID_1, intygAvserInstans++)
                        .withDelsvar(INTYG_AVSER_DELSVAR_ID_1, aCV(KV_INTYGET_AVSER_CODE_SYSTEM, intygAvser.getCode(), intygAvser.getDescription()))
                        .build());
            }
        }

        if (source.getVardkontakt() != null && source.getVardkontakt().getIdkontroll() != null) {
            IdKontrollKod idKontroll = IdKontrollKod.valueOf(source.getVardkontakt().getIdkontroll());
            svars.add(aSvar(IDENTITET_STYRKT_GENOM_SVAR_ID_2)
                    .withDelsvar(IDENTITET_STYRKT_GENOM_ID_2,
                            aCV(KV_ID_KONTROLL_CODE_SYSTEM, idKontroll.getCode(), idKontroll.getDescription()))
                    .build());
        }

        buildSynSvar(source.getSyn(), svars);

        if (source.getHorselBalans() != null) {
            addIfNotNull(svars, BALANSRUBBNINGAR_YRSEL_SVAR_ID_10, BALANSRUBBNINGAR_YRSEL_DELSVAR_ID_10,
                    source.getHorselBalans().getBalansrubbningar());
            addIfNotNull(svars, UPPFATTA_SAMTALSTAMMA_SVAR_ID_11, UPPFATTA_SAMTALSTAMMA_DELSVAR_ID_11,
                    source.getHorselBalans().getSvartUppfattaSamtal4Meter());
        }

        buildFunktionsnedsattningSvar(source.getFunktionsnedsattning(), svars);
        buildHjartKarlSvar(source.getHjartKarl(), svars);
        buildDiabetesSvar(source.getDiabetes(), svars);

        if (source.getNeurologi() != null) {
            addIfNotNull(svars, TECKEN_NEUROLOGISK_SJUKDOM_SVAR_ID_20, TECKEN_NEUROLOGISK_SJUKDOM_DELSVAR_ID_20,
                    source.getNeurologi().getNeurologiskSjukdom());
        }

        buildMedvetandestorningSvar(source.getMedvetandestorning(), svars);

        if (source.getNjurar() != null) {
            addIfNotNull(svars, NEDSATT_NJURFUNKTION_SVAR_ID_22, NEDSATT_NJURFUNKTION_DELSVAR_ID_22, source.getNjurar().getNedsattNjurfunktion());
        }

        if (source.getKognitivt() != null) {
            addIfNotNull(svars, TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_SVAR_ID_23, TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_DELSVAR_ID_23,
                    source.getKognitivt().getSviktandeKognitivFunktion());
        }

        if (source.getSomnVakenhet() != null) {
            addIfNotNull(svars, TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID_24, TECKEN_SOMN_ELLER_VAKENHETSSTORNING_DELSVAR_ID_24,
                    source.getSomnVakenhet().getTeckenSomnstorningar());
        }

        buildNarkotikaLakemedelSvar(source.getNarkotikaLakemedel(), svars);

        if (source.getPsykiskt() != null) {
            addIfNotNull(svars, PSYKISK_SJUKDOM_STORNING_SVAR_ID_27, PSYKISK_SJUKDOM_STORNING_DELSVAR_ID_27,
                    source.getPsykiskt().getPsykiskSjukdom());
        }

        if (source.getUtvecklingsstorning() != null) {
            addIfNotNull(svars, PSYKISK_UTVECKLINGSSTORNING_SVAR_ID_28, PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID_28,
                    source.getUtvecklingsstorning().getPsykiskUtvecklingsstorning());
            addIfNotNull(svars, ADHD_ADD_DAMP_ASPERGERS_TOURETTES_SVAR_ID_29, ADHD_ADD_DAMP_ASPERGERS_TOURETTES_DELSVAR_ID_29,
                    source.getUtvecklingsstorning().getHarSyndrom());
        }

        buildSjukhusvardSvar(source.getSjukhusvard(), svars);
        buildMedicineringSvar(source.getMedicinering(), svars);
        addIfNotBlank(svars, OVRIGA_KOMMENTARER_SVAR_ID_32, OVRIGA_KOMMENTARER_DELSVARSVAR_ID_32, source.getKommentar());
        buildBedomningSvar(source.getBedomning(), svars);

        return svars;
    }

    private static void buildSynSvar(Syn source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        addIfNotNull(svars, SYNFALTSDEFEKTER_SVAR_ID_3, SYNFALTSDEFEKTER_DELSVAR_ID_3, source.getSynfaltsdefekter());
        addIfNotNull(svars, SEENDE_NEDSATT_BELYSNING_SVAR_ID_4, SEENDE_NEDSATT_BELYSNING_DELSVAR_ID_4, source.getNattblindhet());
        addIfNotNull(svars, PROGRESSIV_OGONSJUKDOM_SVAR_ID_5, PROGRESSIV_OGONSJUKDOM_DELSVAR_ID_5, source.getProgressivOgonsjukdom());
        addIfNotNull(svars, DUBBELSEENDE_SVAR_ID_6, DUBBELSEENDE_DELSVAR_ID_6, source.getDiplopi());
        addIfNotNull(svars, NYSTAGMUS_SVAR_ID_7, NYSTAGMUS_DELSVAR_ID_7, source.getNystagmus());

        SvarBuilder synskarpa = aSvar(SYNSKARPA_SVAR_ID_8);
        if (source.getHogerOga() != null) {
            if (source.getHogerOga().getUtanKorrektion() != null) {
                synskarpa.withDelsvar(HOGER_OGA_UTAN_KORREKTION_DELSVAR_ID_8, source.getHogerOga().getUtanKorrektion().toString());
            }
            if (source.getHogerOga().getMedKorrektion() != null) {
                synskarpa.withDelsvar(HOGER_OGA_MED_KORREKTION_DELSVAR_ID_8, source.getHogerOga().getMedKorrektion().toString());
            }
            if (source.getHogerOga().getKontaktlins() != null) {
                synskarpa.withDelsvar(KONTAKTLINSER_HOGER_OGA_DELSVAR_ID_8, source.getHogerOga().getKontaktlins().toString());
            }
        }
        if (source.getVansterOga() != null) {
            if (source.getVansterOga().getUtanKorrektion() != null) {
                synskarpa.withDelsvar(VANSTER_OGA_UTAN_KORREKTION_DELSVAR_ID_8, source.getVansterOga().getUtanKorrektion().toString());
            }
            if (source.getVansterOga().getMedKorrektion() != null) {
                synskarpa.withDelsvar(VANSTER_OGA_MED_KORREKTION_DELSVAR_ID_8, source.getVansterOga().getMedKorrektion().toString());
            }
            if (source.getVansterOga().getKontaktlins() != null) {
                synskarpa.withDelsvar(KONTAKTLINSER_VANSTER_OGA_DELSVAR_ID_8, source.getVansterOga().getKontaktlins().toString());
            }
        }
        if (source.getBinokulart() != null) {
            if (source.getBinokulart().getUtanKorrektion() != null) {
                synskarpa.withDelsvar(BINOKULART_UTAN_KORREKTION_DELSVAR_ID_8, source.getBinokulart().getUtanKorrektion().toString());
            }
            if (source.getBinokulart().getMedKorrektion() != null) {
                synskarpa.withDelsvar(BINOKULART_MED_KORREKTION_DELSVAR_ID_8, source.getBinokulart().getMedKorrektion().toString());
            }
        }
        if (CollectionUtils.isNotEmpty(synskarpa.delSvars)) {
            svars.add(synskarpa.build());
        }

        addIfNotNull(svars, UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID_9, UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_DELSVAR_ID_9,
                source.getKorrektionsglasensStyrka());
    }

    private static void buildFunktionsnedsattningSvar(Funktionsnedsattning source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        SvarBuilder funktionsnedsattning = aSvar(SJUKDOM_FUNKTIONSNEDSATTNING_SVAR_ID_12);
        if (source.getFunktionsnedsattning() != null) {
            funktionsnedsattning.withDelsvar(FOREKOMST_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID_12,
                    source.getFunktionsnedsattning().toString());
        }
        if (StringUtils.isNotBlank(source.getBeskrivning())) {
            funktionsnedsattning.withDelsvar(TYP_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID_12, source.getBeskrivning());
        }
        if (CollectionUtils.isNotEmpty(funktionsnedsattning.delSvars)) {
            svars.add(funktionsnedsattning.build());
        }
        addIfNotNull(svars, OTILLRACKLIG_RORELSEFORMAGA_SVAR_ID_13, OTILLRACKLIG_RORELSEFORMAGA_DELSVAR_ID_13,
                source.getOtillrackligRorelseformaga());
    }

    private static void buildHjartKarlSvar(HjartKarl source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        addIfNotNull(svars, HJART_ELLER_KARLSJUKDOM_SVAR_ID_14, HJART_ELLER_KARLSJUKDOM_DELSVAR_ID_14,
                source.getHjartKarlSjukdom());
        addIfNotNull(svars, TECKEN_PA_HJARNSKADA_SVAR_ID_15, TECKEN_PA_HJARNSKADA_DELSVAR_ID_15,
                source.getHjarnskadaEfterTrauma());

        if (source.getRiskfaktorerStroke() != null) {
            SvarBuilder riskfaktorerStroke = aSvar(RISKFAKTORER_STROKE_SVAR_ID_16);
            if (source.getRiskfaktorerStroke() != null) {
                riskfaktorerStroke.withDelsvar(FOREKOMST_RISKFAKTORER_STROKE_DELSVAR_ID_16,
                        source.getRiskfaktorerStroke().toString());
            }
            if (StringUtils.isNotBlank(source.getBeskrivningRiskfaktorer())) {
                riskfaktorerStroke.withDelsvar(TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID_16,
                        source.getBeskrivningRiskfaktorer());
            }
            if (CollectionUtils.isNotEmpty(riskfaktorerStroke.delSvars)) {
                svars.add(riskfaktorerStroke.build());
            }
        }
    }

    private static void buildDiabetesSvar(Diabetes source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        addIfNotNull(svars, HAR_DIABETES_SVAR_ID_17, HAR_DIABETES_DELSVAR_ID_17, source.getHarDiabetes());
        if (source.getDiabetesTyp() != null) {
            DiabetesKod diabetesKod = DiabetesKod.valueOf(source.getDiabetesTyp());
            svars.add(aSvar(TYP_AV_DIABETES_SVAR_ID_18)
                    .withDelsvar(TYP_AV_DIABETES_DELSVAR_ID_18,
                            aCV(Diagnoskodverk.ICD_10_SE.getCodeSystem(), diabetesKod.getCode(), diabetesKod.getDescription()))
                    .build());
        }
        SvarBuilder diabetesBehandling = aSvar(BEHANDLING_DIABETES_SVAR_ID_19);
        if (source.getKost() != null) {
            diabetesBehandling.withDelsvar(KOSTBEHANDLING_DELSVAR_ID_19, source.getKost().toString());
        }
        if (source.getTabletter() != null) {
            diabetesBehandling.withDelsvar(TABLETTBEHANDLING_DELSVAR_ID_19, source.getTabletter().toString());
        }
        if (source.getInsulin() != null) {
            diabetesBehandling.withDelsvar(INSULINBEHANDLING_DELSVAR_ID_19, source.getInsulin().toString());
        }
        if (CollectionUtils.isNotEmpty(diabetesBehandling.delSvars)) {
            svars.add(diabetesBehandling.build());
        }
    }

    private static void buildMedvetandestorningSvar(Medvetandestorning source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        SvarBuilder medvetandestorning = aSvar(MEDVETANDESTORNING_SVAR_ID_21);
        if (source.getMedvetandestorning() != null) {
            medvetandestorning.withDelsvar(FOREKOMST_MEDVETANDESTORNING_DELSVAR_ID_21,
                    source.getMedvetandestorning().toString());
        }
        if (StringUtils.isNotBlank(source.getBeskrivning())) {
            medvetandestorning.withDelsvar(TIDPUNKT_ORSAK_ANNAN_MEDVETANDESTORNING_DELSVAR_ID_21,
                    source.getBeskrivning());
        }
        if (CollectionUtils.isNotEmpty(medvetandestorning.delSvars)) {
            svars.add(medvetandestorning.build());
        }
    }

    private static void buildNarkotikaLakemedelSvar(NarkotikaLakemedel source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        SvarBuilder missbrukBeroende = aSvar(MISSBRUK_BEROENDE_SVAR_ID_25);
        if (source.getTeckenMissbruk() != null) {
            missbrukBeroende.withDelsvar(TECKEN_MISSBRUK_BEROENDE_DELSVAR_ID_25, source.getTeckenMissbruk().toString());
        }
        if (source.getForemalForVardinsats() != null) {
            missbrukBeroende.withDelsvar(VARDINSATSER_MISSBRUK_BEROENDE_DELSVAR_ID_25, source.getForemalForVardinsats().toString());
        }
        if (source.getProvtagningBehovs() != null) {
            missbrukBeroende.withDelsvar(PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID_25,
                    source.getProvtagningBehovs().toString());
        }
        if (CollectionUtils.isNotEmpty(missbrukBeroende.delSvars)) {
            svars.add(missbrukBeroende.build());
        }

        SvarBuilder lakemedel = aSvar(REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_SVAR_ID_26);
        if (source.getLakarordineratLakemedelsbruk() != null) {
            lakemedel.withDelsvar(REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_DELSVAR_ID_26,
                    source.getLakarordineratLakemedelsbruk().toString());
        }
        if (StringUtils.isNotBlank(source.getLakemedelOchDos())) {
            lakemedel.withDelsvar(LAKEMEDEL_ORDINERAD_DOS_DELSVAR_ID_26, source.getLakemedelOchDos());
        }
        if (CollectionUtils.isNotEmpty(lakemedel.delSvars)) {
            svars.add(lakemedel.build());
        }
    }

    private static void buildSjukhusvardSvar(Sjukhusvard source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        SvarBuilder sjukhusvard = aSvar(VARD_SJUKHUS_KONTAKT_LAKARE_SVAR_ID_30);
        if (source.getSjukhusEllerLakarkontakt() != null) {
            sjukhusvard.withDelsvar(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30,
                    source.getSjukhusEllerLakarkontakt().toString());
        }
        if (StringUtils.isNotBlank(source.getTidpunkt())) {
            sjukhusvard.withDelsvar(TIDPUNKT_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30, source.getTidpunkt());
        }
        if (StringUtils.isNotBlank(source.getVardinrattning())) {
            sjukhusvard.withDelsvar(PLATS_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30, source.getVardinrattning());
        }
        if (StringUtils.isNotBlank(source.getAnledning())) {
            sjukhusvard.withDelsvar(ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30, source.getAnledning());
        }
        if (CollectionUtils.isNotEmpty(sjukhusvard.delSvars)) {
            svars.add(sjukhusvard.build());
        }
    }

    private static void buildMedicineringSvar(Medicinering source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        SvarBuilder medicinering = aSvar(STADIGVARANDE_MEDICINERING_SVAR_ID_31);
        if (source.getStadigvarandeMedicinering() != null) {
            medicinering.withDelsvar(FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID_31,
                    source.getStadigvarandeMedicinering().toString());
        }
        if (StringUtils.isNotBlank(source.getBeskrivning())) {
            medicinering.withDelsvar(MEDICINER_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID_31, source.getBeskrivning());
        }
        if (CollectionUtils.isNotEmpty(medicinering.delSvars)) {
            svars.add(medicinering.build());
        }
    }

    private static void buildBedomningSvar(Bedomning source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        int behorighetInstans = 1;
        if (source.getKorkortstyp() != null) {
            for (BedomningKorkortstyp korkortstyp : source.getKorkortstyp()) {
                KorkortsbehorighetKod korkortsbehorighet = KorkortsbehorighetKod.valueOf(korkortstyp.name());
                svars.add(aSvar(UPPFYLLER_KRAV_FOR_BEHORIGHET_SVAR_ID_33, behorighetInstans++)
                        .withDelsvar(UPPFYLLER_KRAV_FOR_BEHORIGHET_DELSVAR_ID_33,
                                aCV(KV_KORKORTSBEHORIGHET_CODE_SYSTEM, korkortsbehorighet.getCode(), korkortsbehorighet.getDescription()))
                        .build());
            }
        }
        if (source.getKanInteTaStallning() != null && source.getKanInteTaStallning()) {
            svars.add(aSvar(UPPFYLLER_KRAV_FOR_BEHORIGHET_SVAR_ID_33, behorighetInstans++)
                    .withDelsvar(UPPFYLLER_KRAV_FOR_BEHORIGHET_DELSVAR_ID_33,
                            aCV(KV_KORKORTSBEHORIGHET_CODE_SYSTEM, KorkortsbehorighetKod.KANINTETEASTALLNING.getCode(),
                                    KorkortsbehorighetKod.KANINTETEASTALLNING.getDescription()))
                    .build());
        }
        addIfNotBlank(svars, BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_SVAR_ID_34, BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_DELSVAR_ID_34,
                source.getLakareSpecialKompetens());
    }
}
