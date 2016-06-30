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

import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.VARDKONTAKT_TYP;
import static se.inera.intyg.intygstyper.ts_parent.model.converter.TransportToInternalUtil.getTextVersion;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.intygstyper.ts_bas.model.internal.*;
import se.inera.intyg.intygstyper.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.intygstyper.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.intygstyper.ts_parent.model.converter.TransportToInternalUtil;
import se.inera.intygstjanster.ts.services.v1.*;
import se.inera.intygstjanster.ts.services.v1.Medvetandestorning;
import se.inera.intygstjanster.ts.services.v1.Sjukhusvard;
import se.inera.intygstjanster.ts.services.v1.Utvecklingsstorning;

public final class TransportToInternal {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private TransportToInternal() {
    }

    /**
     * Takes an utlatande on the transport format and converts it to the internal model.
     *
     * @param source
     *            {@link se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande}
     *
     * @return {@link Utlatande}, unless the source is null in which case a
     *         {@link se.inera.intyg.common.support.model.converter.util.ConverterException} is thrown
     *
     * @throws se.inera.intyg.common.support.model.converter.util.ConverterException
     */
    public static Utlatande convert(TSBasIntyg source) throws ConverterException {
        LOG.trace("Converting transport model to internal");

        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        Utlatande internal = new Utlatande();
        internal.setGrundData(TransportToInternalUtil.buildGrundData(source.getGrundData()));
        internal.setId(source.getIntygsId());
        internal.setTextVersion(getTextVersion(source.getVersion(), source.getUtgava()));
        internal.setKommentar(source.getOvrigKommentar());
        internal.setTyp(TsBasEntryPoint.MODULE_ID);

        internal.getPsykiskt().setPsykiskSjukdom(source.isHarPsykiskStorning());
        internal.getKognitivt().setSviktandeKognitivFunktion(source.isHarKognitivStorning());
        internal.getNjurar().setNedsattNjurfunktion(source.isHarNjurSjukdom());
        internal.getSomnVakenhet().setTeckenSomnstorningar(source.isHarSomnVakenhetStorning());
        internal.getNeurologi().setNeurologiskSjukdom(source.isNeurologiskaSjukdomar());

        buildAlkoholNarkotikaLakemedel(internal, source.getAlkoholNarkotikaLakemedel());
        buildBedomning(internal, source.getBedomning());
        buildDiabetes(internal, source.getDiabetes());
        buildHjartKarl(internal, source.getHjartKarlSjukdomar());
        buildHorselBalans(internal, source.getHorselBalanssinne());
        buildIdentitetStyrkt(internal, source.getIdentitetStyrkt());
        buildIntygAvser(internal, source.getIntygAvser());
        buildMedvetandeStorning(internal, source.getMedvetandestorning());
        buildMedicinering(internal, source.getOvrigMedicinering());
        buildFunktionsnedsattning(internal, source.getRorelseorganensFunktioner());
        buildSjukhusvard(internal, source.getSjukhusvard());
        buildSynfunktioner(internal, source.getSynfunktion());
        buildUvecklingsstorning(internal, source.getUtvecklingsstorning());
        return internal;
    }

    private static void buildAlkoholNarkotikaLakemedel(Utlatande internal, AlkoholNarkotikaLakemedel source) {
        internal.getNarkotikaLakemedel().setForemalForVardinsats(source.isHarVardinsats());
        internal.getNarkotikaLakemedel().setLakarordineratLakemedelsbruk(source.isHarLakarordineratLakemedelsbruk());
        internal.getNarkotikaLakemedel().setLakemedelOchDos(source.getLakarordineratLakemedelOchDos());
        internal.getNarkotikaLakemedel().setProvtagningBehovs(source.isHarVardinsatsProvtagningBehov());
        internal.getNarkotikaLakemedel().setTeckenMissbruk(source.isHarTeckenMissbruk());
    }

    private static void buildBedomning(Utlatande internal, BedomningTypBas source) {
        internal.getBedomning().setKanInteTaStallning(source.isKanInteTaStallning());
        internal.getBedomning().setLakareSpecialKompetens(source.getBehovAvLakareSpecialistKompetens());
        if (CollectionUtils.isNotEmpty(source.getKorkortstyp())) {
            internal.getBedomning().getKorkortstyp().addAll(convertBedomningKorkortstyp(source.getKorkortstyp()));
        }
    }

    private static List<BedomningKorkortstyp> convertBedomningKorkortstyp(List<KorkortsbehorighetTsBas> source) {
        List<BedomningKorkortstyp> korkortsTyper = new ArrayList<BedomningKorkortstyp>();
        for (KorkortsbehorighetTsBas it : source) {
            korkortsTyper.add(BedomningKorkortstyp.valueOf(it.value().value()));
        }
        return korkortsTyper;
    }

    private static void buildDiabetes(Utlatande internal, DiabetesTypBas source) {
        internal.getDiabetes().setHarDiabetes(source.isHarDiabetes());
        if (source.isHarDiabetes()) {
            if (source.getDiabetesTyp() != null) {
                internal.getDiabetes().setDiabetesTyp(TransportToInternalUtil.convertDiabetesTyp(source.getDiabetesTyp()).name());
            }
            internal.getDiabetes().setInsulin(source.isHarBehandlingInsulin());
            internal.getDiabetes().setKost(source.isHarBehandlingKost());
            internal.getDiabetes().setTabletter(source.isHarBehandlingTabletter());
        }
    }

    private static void buildHjartKarl(Utlatande internal, HjartKarlSjukdomar source) {
        internal.getHjartKarl().setBeskrivningRiskfaktorer(source.getRiskfaktorerStrokeBeskrivning());
        internal.getHjartKarl().setHjarnskadaEfterTrauma(source.isHarHjarnskadaICNS());
        internal.getHjartKarl().setHjartKarlSjukdom(source.isHarRiskForsamradHjarnFunktion());
        internal.getHjartKarl().setRiskfaktorerStroke(source.isHarRiskfaktorerStroke());
    }

    private static void buildHorselBalans(Utlatande internal, HorselBalanssinne source) {
        internal.getHorselBalans().setBalansrubbningar(source.isHarBalansrubbningYrsel());
        internal.getHorselBalans().setSvartUppfattaSamtal4Meter(source.isHarSvartUppfattaSamtal4Meter());
    }

    private static void buildIdentitetStyrkt(Utlatande internal, IdentitetStyrkt source) {
        internal.getVardkontakt().setTyp(VARDKONTAKT_TYP);
        internal.getVardkontakt().setIdkontroll(IdKontrollKod.fromCode(source.getIdkontroll().value()).name());
    }

    private static void buildIntygAvser(Utlatande internal, IntygsAvserTypBas source) {
        internal.getIntygAvser().getKorkortstyp().addAll(convertIntygAvsergKorkortstyp(source.getKorkortstyp()));
    }

    private static List<IntygAvserKategori> convertIntygAvsergKorkortstyp(List<KorkortsbehorighetTsBas> source) {
        List<IntygAvserKategori> korkortsTyper = new ArrayList<IntygAvserKategori>();
        for (KorkortsbehorighetTsBas it : source) {
            korkortsTyper.add(IntygAvserKategori.valueOf(it.value().value()));
        }
        return korkortsTyper;
    }

    private static void buildMedvetandeStorning(Utlatande internal, Medvetandestorning source) {
        internal.getMedvetandestorning().setBeskrivning(source.getMedvetandestorningBeskrivning());
        internal.getMedvetandestorning().setMedvetandestorning(source.isHarMedvetandestorning());
    }

    private static void buildMedicinering(Utlatande internal, OvrigMedicinering source) {
        internal.getMedicinering().setBeskrivning(source.getStadigvarandeMedicineringBeskrivning());
        internal.getMedicinering().setStadigvarandeMedicinering(source.isHarStadigvarandeMedicinering());
    }

    private static void buildFunktionsnedsattning(Utlatande internal, RorelseorganenFunktioner source) {
        internal.getFunktionsnedsattning().setBeskrivning(source.getRorelsebegransningBeskrivning());
        internal.getFunktionsnedsattning().setFunktionsnedsattning(source.isHarRorelsebegransning());
        internal.getFunktionsnedsattning().setOtillrackligRorelseformaga(source.isHarOtillrackligRorelseformagaPassagerare());
    }

    private static void buildSjukhusvard(Utlatande internal, Sjukhusvard source) {
        internal.getSjukhusvard().setAnledning(source.getSjukhusvardEllerLakarkontaktAnledning());
        internal.getSjukhusvard().setSjukhusEllerLakarkontakt(source.isHarSjukhusvardEllerLakarkontakt());
        internal.getSjukhusvard().setTidpunkt(source.getSjukhusvardEllerLakarkontaktDatum());
        internal.getSjukhusvard().setVardinrattning(source.getSjukhusvardEllerLakarkontaktVardinrattning());
    }

    private static void buildSynfunktioner(Utlatande internal, SynfunktionBas source) {
        internal.getSyn().setBinokulart(buildBinokulart(source));
        internal.getSyn().setHogerOga(buildHoger(source));
        internal.getSyn().setVansterOga(buildVanster(source));

        internal.getSyn().setDiplopi(source.isHarDiplopi());
        internal.getSyn().setKorrektionsglasensStyrka(source.isHarGlasStyrkaOver8Dioptrier());
        internal.getSyn().setNattblindhet(source.isHarNattblindhet());
        internal.getSyn().setNystagmus(source.isHarNystagmus());
        internal.getSyn().setProgressivOgonsjukdom(source.isHarProgressivOgonsjukdom());
        internal.getSyn().setSynfaltsdefekter(source.isHarSynfaltsdefekt());
    }

    private static Synskarpevarden buildBinokulart(SynfunktionBas source) {
        Synskarpevarden binokulart = new Synskarpevarden();
        binokulart.setUtanKorrektion(source.getSynskarpaUtanKorrektion().getBinokulart());
        if (source.getSynskarpaMedKorrektion() != null && source.getSynskarpaMedKorrektion().getBinokulart() != null) {
            binokulart.setMedKorrektion(source.getSynskarpaMedKorrektion().getBinokulart());
        }
        return binokulart;
    }

    private static Synskarpevarden buildHoger(SynfunktionBas source) {
        Synskarpevarden hoger = new Synskarpevarden();
        hoger.setUtanKorrektion(source.getSynskarpaUtanKorrektion().getHogerOga());
        if (source.getSynskarpaMedKorrektion() != null) {
            if (source.getSynskarpaMedKorrektion().getHogerOga() != null) {
                hoger.setMedKorrektion(source.getSynskarpaMedKorrektion().getHogerOga());
            }
            if (source.getSynskarpaMedKorrektion().isHarKontaktlinsHogerOga() != null) {
                hoger.setKontaktlins(source.getSynskarpaMedKorrektion().isHarKontaktlinsHogerOga());
            }
        }
        return hoger;
    }

    private static Synskarpevarden buildVanster(SynfunktionBas source) {
        Synskarpevarden vanster = new Synskarpevarden();
        vanster.setUtanKorrektion(source.getSynskarpaUtanKorrektion().getVansterOga());
        if (source.getSynskarpaMedKorrektion() != null) {
            if (source.getSynskarpaMedKorrektion().getVansterOga() != null) {
                vanster.setMedKorrektion(source.getSynskarpaMedKorrektion().getVansterOga());
            }
            if (source.getSynskarpaMedKorrektion().isHarKontaktlinsVansterOga() != null) {
                vanster.setKontaktlins(source.getSynskarpaMedKorrektion().isHarKontaktlinsVansterOga());
            }
        }
        return vanster;
    }

    private static void buildUvecklingsstorning(Utlatande internal, Utvecklingsstorning source) {
        internal.getUtvecklingsstorning().setHarSyndrom(source.isHarAndrayndrom());
        internal.getUtvecklingsstorning().setPsykiskUtvecklingsstorning(source.isHarPsykiskUtvecklingsstorning());
    }
}
