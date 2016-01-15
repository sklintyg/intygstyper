/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.ts_bas.model.codes.UtlatandeKod;
import se.inera.intyg.intygstyper.ts_bas.model.internal.*;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intygstjanster.ts.services.v1.*;
import se.inera.intygstjanster.ts.services.v1.Medvetandestorning;
import se.inera.intygstjanster.ts.services.v1.Sjukhusvard;
import se.inera.intygstjanster.ts.services.v1.Utvecklingsstorning;

public final class TransportToInternal {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private static final String VARDKONTAKT_TYP = "5880005";

    private static Utlatande internal;

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

        internal = new Utlatande();
        internal.setGrundData(convertGrundData(source.getGrundData()));
        internal.setId(source.getIntygsId());
        internal.setKommentar(source.getOvrigKommentar());
        internal.setTyp(UtlatandeKod.getCurrentVersion().getCode());

        internal.getPsykiskt().setPsykiskSjukdom(source.isHarPsykiskStorning());
        internal.getKognitivt().setSviktandeKognitivFunktion(source.isHarKognitivStorning());
        internal.getNjurar().setNedsattNjurfunktion(source.isHarNjurSjukdom());
        internal.getSomnVakenhet().setTeckenSomnstorningar(source.isHarSomnVakenhetStorning());
        internal.getNeurologi().setNeurologiskSjukdom(source.isNeurologiskaSjukdomar());

        buildAlkoholNarkotikaLakemedel(source.getAlkoholNarkotikaLakemedel());
        buildBedomning(source.getBedomning());
        buildDiabetes(source.getDiabetes());
        buildHjartKarl(source.getHjartKarlSjukdomar());
        buildHorselBalans(source.getHorselBalanssinne());
        buildIdentitetStyrkt(source.getIdentitetStyrkt());
        buildIntygAvser(source.getIntygAvser());
        buildMedvetandeStorning(source.getMedvetandestorning());
        buildMedicinering(source.getOvrigMedicinering());
        buildFunktionsnedsattning(source.getRorelseorganensFunktioner());
        buildSjukhusvard(source.getSjukhusvard());
        buildSynfunktioner(source.getSynfunktion());
        buildUvecklingsstorning(source.getUtvecklingsstorning());
        return internal;
    }

    private static void buildAlkoholNarkotikaLakemedel(AlkoholNarkotikaLakemedel source) {
        internal.getNarkotikaLakemedel().setForemalForVardinsats(source.isHarVardinsats());
        internal.getNarkotikaLakemedel().setLakarordineratLakemedelsbruk(source.isHarLakarordineratLakemedelsbruk());
        internal.getNarkotikaLakemedel().setLakemedelOchDos(source.getLakarordineratLakemedelOchDos());
        internal.getNarkotikaLakemedel().setProvtagningBehovs(source.isHarVardinsatsProvtagningBehov());
        internal.getNarkotikaLakemedel().setTeckenMissbruk(source.isHarTeckenMissbruk());
    }

    private static void buildBedomning(BedomningTypBas source) {
        internal.getBedomning().setKanInteTaStallning(source.isKanInteTaStallning());
        internal.getBedomning().setLakareSpecialKompetens(source.getBehovAvLakareSpecialistKompetens());
        internal.getBedomning().getKorkortstyp().addAll(convertBedomningKorkortstyp(source.getKorkortstyp()));
    }

    private static List<BedomningKorkortstyp> convertBedomningKorkortstyp(List<KorkortsbehorighetTsBas> source) {
        List<BedomningKorkortstyp> korkortsTyper = new ArrayList<BedomningKorkortstyp>();
        for (KorkortsbehorighetTsBas it : source) {
            korkortsTyper.add(BedomningKorkortstyp.valueOf(mapToKorkortsbehorighetTsBas(it.name())));
        }
        return korkortsTyper;
    }

    private static void buildDiabetes(DiabetesTypBas source) {
        internal.getDiabetes().setHarDiabetes(source.isHarDiabetes());
        if (source.isHarDiabetes()) {
            internal.getDiabetes().setDiabetesTyp(source.getDiabetesTyp().name().equals("TYP_1") ? "DIABETES_TYP_1" : "DIABETES_TYP_2");
            internal.getDiabetes().setInsulin(source.isHarBehandlingInsulin());
            internal.getDiabetes().setKost(source.isHarBehandlingKost());
            internal.getDiabetes().setTabletter(source.isHarBehandlingTabletter());
        }
    }

    private static void buildHjartKarl(HjartKarlSjukdomar source) {
        internal.getHjartKarl().setBeskrivningRiskfaktorer(source.getRiskfaktorerStrokeBeskrivning());
        internal.getHjartKarl().setHjarnskadaEfterTrauma(source.isHarHjarnskadaICNS());
        internal.getHjartKarl().setHjartKarlSjukdom(source.isHarRiskForsamradHjarnFunktion());
        internal.getHjartKarl().setRiskfaktorerStroke(source.isHarRiskfaktorerStroke());
    }

    private static void buildHorselBalans(HorselBalanssinne source) {
        internal.getHorselBalans().setBalansrubbningar(source.isHarBalansrubbningYrsel());
        internal.getHorselBalans().setSvartUppfattaSamtal4Meter(source.isHarSvartUppfattaSamtal4Meter());
    }

    private static void buildIdentitetStyrkt(IdentitetStyrkt source) {
        internal.getVardkontakt().setIdkontroll(mapToInternalIdKontroll(source.getIdkontroll().name()));
        // TODO What is this used for?
        internal.getVardkontakt().setTyp(VARDKONTAKT_TYP);
    }

    private static String mapToInternalIdKontroll(String idkontroll) {
        switch (idkontroll) {
        case "IDK_1":
            return "ID_KORT";
        case "IDK_2":
            return "FORETAG_ELLER_TJANSTEKORT";
        case "IDK_3":
            return "KORKORT";
        case "IDK_4":
            return "PERS_KANNEDOM";
        case "IDK_5":
            return "FORSAKRAN_KAP18";
        case "IDK_6":
            return "PASS";
        default:
            return null;
        }
    }

    private static void buildIntygAvser(IntygsAvserTypBas source) {
        internal.getIntygAvser().getKorkortstyp().addAll(convertIntygAvsergKorkortstyp(source.getKorkortstyp()));
    }

    private static List<IntygAvserKategori> convertIntygAvsergKorkortstyp(List<KorkortsbehorighetTsBas> source) {
        List<IntygAvserKategori> korkortsTyper = new ArrayList<IntygAvserKategori>();
        for (KorkortsbehorighetTsBas it : source) {
            korkortsTyper.add(IntygAvserKategori.valueOf(mapToKorkortsbehorighetTsBas(it.name())));
        }
        return korkortsTyper;
    }

    private static void buildMedvetandeStorning(Medvetandestorning source) {
        internal.getMedvetandestorning().setBeskrivning(source.getMedvetandestorningBeskrivning());
        internal.getMedvetandestorning().setMedvetandestorning(source.isHarMedvetandestorning());
    }

    private static void buildMedicinering(OvrigMedicinering source) {
        internal.getMedicinering().setBeskrivning(source.getStadigvarandeMedicineringBeskrivning());
        internal.getMedicinering().setStadigvarandeMedicinering(source.isHarStadigvarandeMedicinering());
    }

    private static void buildFunktionsnedsattning(RorelseorganenFunktioner source) {
        internal.getFunktionsnedsattning().setBeskrivning(source.getRorelsebegransningBeskrivning());
        internal.getFunktionsnedsattning().setFunktionsnedsattning(source.isHarRorelsebegransning());
        internal.getFunktionsnedsattning().setOtillrackligRorelseformaga(source.isHarOtillrackligRorelseformagaPassagerare());
    }

    private static void buildSjukhusvard(Sjukhusvard source) {
        internal.getSjukhusvard().setAnledning(source.getSjukhusvardEllerLakarkontaktAnledning());
        internal.getSjukhusvard().setSjukhusEllerLakarkontakt(source.isHarSjukhusvardEllerLakarkontakt());
        internal.getSjukhusvard().setTidpunkt(source.getSjukhusvardEllerLakarkontaktDatum());
        internal.getSjukhusvard().setVardinrattning(source.getSjukhusvardEllerLakarkontaktVardinrattning());
    }

    private static void buildSynfunktioner(SynfunktionBas source) {
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

    private static void buildUvecklingsstorning(Utvecklingsstorning source) {
        internal.getUtvecklingsstorning().setHarSyndrom(source.isHarAndrayndrom());
        internal.getUtvecklingsstorning().setPsykiskUtvecklingsstorning(source.isHarPsykiskUtvecklingsstorning());
    }

    // Grunddata
    private static GrundData convertGrundData(se.inera.intygstjanster.ts.services.v1.GrundData source) {
        GrundData grundData = new GrundData();
        grundData.setPatient(convertPatient(source.getPatient()));
        grundData.setSigneringsdatum(LocalDateTime.parse(source.getSigneringsTidstampel()));
        grundData.setSkapadAv(convertHoSPersonal(source.getSkapadAv()));
        return grundData;
    }

    private static HoSPersonal convertHoSPersonal(SkapadAv source) {
        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(source.getFullstandigtNamn());
        hosPersonal.setPersonId(source.getPersonId().getExtension());
        if (source.getBefattningar() != null) {
            hosPersonal.getBefattningar().addAll(source.getBefattningar());
        }
        if (source.getSpecialiteter() != null) {
            hosPersonal.getSpecialiteter().addAll(source.getSpecialiteter());
        }
        hosPersonal.setVardenhet(convertVardenhet(source.getVardenhet()));
        return hosPersonal;
    }

    private static Vardenhet convertVardenhet(se.inera.intygstjanster.ts.services.v1.Vardenhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(source.getEnhetsId().getExtension());
        vardenhet.setEnhetsnamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare convertVardgivare(se.inera.intygstjanster.ts.services.v1.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getVardgivarid().getExtension());
        vardgivare.setVardgivarnamn(source.getVardgivarnamn());
        return vardgivare;
    }

    private static Patient convertPatient(se.inera.intygstjanster.ts.services.v1.Patient source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.setFornamn(source.getFornamn());
        patient.setFullstandigtNamn(source.getFullstandigtNamn());
        patient.setPersonId(new Personnummer(source.getPersonId().getExtension()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());
        return patient;
    }

    private static String mapToKorkortsbehorighetTsBas(String name) {
        switch (name) {
        case "C":
            return "C";
        case "C_1":
            return "C1";
        case "C_1_E":
            return "C1E";
        case "D_1":
            return "D1";
        case "D_1_E":
            return "D1E";
        case "D":
            return "D";
        case "DE":
            return "DE";
        case "CE":
            return "CE";
        case "TAXI":
            return "TAXI";
        case "ANNAT":
            return "ANNAT";
        default:
            return null;
        }
    }
}
