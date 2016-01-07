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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.schemas.Constants;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.intygstyper.ts_bas.model.codes.IdKontrollKod;
import se.inera.intyg.intygstyper.ts_bas.model.codes.BefattningKod;
import se.inera.intyg.intygstyper.ts_bas.model.codes.KorkortsKod;
import se.inera.intyg.intygstyper.ts_bas.model.codes.UtlatandeKod;
import se.inera.intyg.intygstyper.ts_bas.model.internal.*;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Diabetes;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.*;
import se.inera.intygstjanster.ts.services.v1.Medvetandestorning;
import se.inera.intygstjanster.ts.services.v1.Sjukhusvard;
import se.inera.intygstjanster.ts.services.v1.Utvecklingsstorning;
/**
 * Convert from {@link se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande} to the external {@link TSBasIntyg}
 * model.
 *
 * @author erik
 *
 */
public final class InternalToTransport {

    private static final String SIGNERINGS_TIDSTAMPEL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private InternalToTransport() { }

    /**
     * Takes an internal Utlatande and converts it to the external model.
     *
     * @param source
     *            {@link se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande}
     *
     * @return {@link TSBasIntyg}, unless the source is null in which case a
     *         {@link se.inera.intyg.common.support.model.converter.util.ConverterException} is thrown
     *
     * @throws se.inera.intyg.common.support.model.converter.util.ConverterException
     */
    public static RegisterTSBasType convert(Utlatande source)
            throws ConverterException {
        LOG.trace("Converting internal model to transport");

        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }
        RegisterTSBasType registerTSBas = new RegisterTSBasType();
        TSBasIntyg utlatande = new TSBasIntyg();

        utlatande.setIntygsId(source.getId());
        utlatande.setGrundData(buildGrundData(source.getGrundData()));

        UtlatandeKod utlatandeKod = UtlatandeKod.getCurrentVersion();

        utlatande.setIntygsTyp(utlatandeKod.getCode());

        utlatande.setUtgava(utlatandeKod.getTsUtgava());
        utlatande.setVersion(utlatandeKod.getTsVersion());

        utlatande.setAlkoholNarkotikaLakemedel(buildAlkoholNarkotikaLakemedel(source.getNarkotikaLakemedel()));
        utlatande.setBedomning(buildBedomningTypBas(source.getBedomning()));
        utlatande.setDiabetes(buildDiabetesTypBas(source.getDiabetes()));
        utlatande.setHarKognitivStorning(source.getKognitivt().getSviktandeKognitivFunktion());
        utlatande.setHarNjurSjukdom(source.getNjurar().getNedsattNjurfunktion());
        utlatande.setHarPsykiskStorning(source.getPsykiskt().getPsykiskSjukdom());
        utlatande.setHarSomnVakenhetStorning(source.getSomnVakenhet().getTeckenSomnstorningar());
        utlatande.setHjartKarlSjukdomar(buildHjartKarlSjukdomar(source.getHjartKarl()));
        utlatande.setHorselBalanssinne(buildHorselBalanssinne(source.getHorselBalans()));
        utlatande.setIdentitetStyrkt(buildIdentitetStyrkt(source.getVardkontakt()));

        utlatande.setIntygAvser(buildIntygAvser(source.getIntygAvser()));

        utlatande.setMedvetandestorning(buildMedvetandestorning(source.getMedvetandestorning()));
        utlatande.setNeurologiskaSjukdomar(source.getNeurologi().getNeurologiskSjukdom());
        utlatande.setOvrigKommentar(source.getKommentar());
        utlatande.setOvrigMedicinering(buildOvrigMedicinering(source.getMedicinering()));
        utlatande.setRorelseorganensFunktioner(buildRorelseorganensFunktioner(source.getFunktionsnedsattning()));
        utlatande.setSjukhusvard(buildSjukhusvard(source.getSjukhusvard()));
        utlatande.setSynfunktion(buildSynfunktionBas(source.getSyn()));
        utlatande.setUtvecklingsstorning(buildUtvecklingsstorning(source.getUtvecklingsstorning()));
        registerTSBas.setIntyg(utlatande);
        return registerTSBas;
    }

    private static AlkoholNarkotikaLakemedel buildAlkoholNarkotikaLakemedel(NarkotikaLakemedel source) {
        AlkoholNarkotikaLakemedel ret = new AlkoholNarkotikaLakemedel();
        ret.setHarLakarordineratLakemedelsbruk(source.getLakarordineratLakemedelsbruk());
        ret.setHarTeckenMissbruk(source.getTeckenMissbruk());
        ret.setHarVardinsats(source.getForemalForVardinsats());
        ret.setHarVardinsatsProvtagningBehov(source.getProvtagningBehovs());
        ret.setLakarordineratLakemedelOchDos(source.getLakemedelOchDos());
        return ret;
    }

    private static BedomningTypBas buildBedomningTypBas(Bedomning source) {
        BedomningTypBas bedomning = new BedomningTypBas();
        bedomning.setBehovAvLakareSpecialistKompetens(source.getLakareSpecialKompetens());
        bedomning.setKanInteTaStallning(source.getKanInteTaStallning());
        bedomning.getKorkortstyp().addAll(convertToKorkortsbehorighetTsBas(source.getKorkortstyp()));
        return bedomning;
    }

    private static Collection<? extends KorkortsbehorighetTsBas> convertToKorkortsbehorighetTsBas(Set<BedomningKorkortstyp> source) {
        List<KorkortsbehorighetTsBas> behorigheter = new ArrayList<KorkortsbehorighetTsBas>();
        for (BedomningKorkortstyp typ : source) {
            behorigheter.add(KorkortsbehorighetTsBas.valueOf(KorkortsKod.valueOf(typ.name()).getCode()));
        }
        return behorigheter;
    }

    private static DiabetesTypBas buildDiabetesTypBas(Diabetes source) {
        DiabetesTypBas diabetes = new DiabetesTypBas();
        diabetes.setHarDiabetes(source.getHarDiabetes());
        if (!source.getHarDiabetes()) {
            return diabetes;
        }
        diabetes.setHarBehandlingInsulin(source.getInsulin());
        diabetes.setHarBehandlingKost(source.getKost());
        diabetes.setHarBehandlingTabletter(source.getTabletter());
        if (source.getDiabetesTyp().equals("DIABETES_TYP_1")) {
            diabetes.setDiabetesTyp(DiabetesTypVarden.fromValue("TYP1"));
        } else if (source.getDiabetesTyp().equals("DIABETES_TYP_2")) {
            diabetes.setDiabetesTyp(DiabetesTypVarden.fromValue("TYP2"));
        }
        return diabetes;
    }

    private static HjartKarlSjukdomar buildHjartKarlSjukdomar(HjartKarl source) {
        HjartKarlSjukdomar hjartKarl = new HjartKarlSjukdomar();
        hjartKarl.setHarHjarnskadaICNS(source.getHjarnskadaEfterTrauma());
        hjartKarl.setHarRiskForsamradHjarnFunktion(source.getHjartKarlSjukdom());
        hjartKarl.setHarRiskfaktorerStroke(source.getRiskfaktorerStroke());
        hjartKarl.setRiskfaktorerStrokeBeskrivning(source.getBeskrivningRiskfaktorer());
        return hjartKarl;
    }

    private static HorselBalanssinne buildHorselBalanssinne(HorselBalans source) {
        HorselBalanssinne horselBalans = new HorselBalanssinne();
        horselBalans.setHarBalansrubbningYrsel(source.getBalansrubbningar());
        horselBalans.setHarSvartUppfattaSamtal4Meter(source.getSvartUppfattaSamtal4Meter());
        return horselBalans;
    }

    private static IdentitetStyrkt buildIdentitetStyrkt(se.inera.intyg.intygstyper.ts_bas.model.internal.Vardkontakt source) throws ConverterException {
        if (source.getIdkontroll() == null) {
            throw new ConverterException("Idkontroll was null");
        }
        IdentitetStyrkt identitetStyrkt = new IdentitetStyrkt();
        identitetStyrkt.setIdkontroll(IdentifieringsVarden.valueOf(IdKontrollKod.valueOf(source.getIdkontroll()).getCode()));
        return identitetStyrkt;
    }

    private static IntygsAvserTypBas buildIntygAvser(IntygAvser source) {

        IntygsAvserTypBas intygAvser = new IntygsAvserTypBas();
        for (IntygAvserKategori kat : source.getKorkortstyp()) {
            intygAvser.getKorkortstyp().add(KorkortsbehorighetTsBas.valueOf(KorkortsKod.valueOf(kat.name()).getCode()));
        }
        return intygAvser;
    }

    private static Medvetandestorning buildMedvetandestorning(se.inera.intyg.intygstyper.ts_bas.model.internal.Medvetandestorning source) {
        Medvetandestorning medvetandestorning = new Medvetandestorning();
        medvetandestorning.setHarMedvetandestorning(source.getMedvetandestorning());
        medvetandestorning.setMedvetandestorningBeskrivning(source.getBeskrivning());
        return medvetandestorning;
    }

    private static OvrigMedicinering buildOvrigMedicinering(Medicinering source) {
        OvrigMedicinering medicinering = new OvrigMedicinering();
        medicinering.setHarStadigvarandeMedicinering(source.getStadigvarandeMedicinering());
        medicinering.setStadigvarandeMedicineringBeskrivning(source.getBeskrivning());
        return medicinering;
    }

    private static RorelseorganenFunktioner buildRorelseorganensFunktioner(Funktionsnedsattning source) {
        RorelseorganenFunktioner funktioner = new RorelseorganenFunktioner();
        funktioner.setHarOtillrackligRorelseformagaPassagerare(source.getOtillrackligRorelseformaga());
        funktioner.setHarRorelsebegransning(source.getFunktionsnedsattning());
        funktioner.setRorelsebegransningBeskrivning(source.getBeskrivning());
        return funktioner;
    }

    private static Sjukhusvard buildSjukhusvard(se.inera.intyg.intygstyper.ts_bas.model.internal.Sjukhusvard source) {
        Sjukhusvard sjukhusvard = new Sjukhusvard();
        sjukhusvard.setHarSjukhusvardEllerLakarkontakt(source.getSjukhusEllerLakarkontakt());
        sjukhusvard.setSjukhusvardEllerLakarkontaktAnledning(source.getAnledning());
        sjukhusvard.setSjukhusvardEllerLakarkontaktDatum(source.getTidpunkt());
        sjukhusvard.setSjukhusvardEllerLakarkontaktVardinrattning(source.getVardinrattning());
        return sjukhusvard;
    }

    private static SynfunktionBas buildSynfunktionBas(Syn source) {
        SynfunktionBas syn = new SynfunktionBas();
        syn.setHarDiplopi(source.getDiplopi());
        syn.setHarGlasStyrkaOver8Dioptrier(source.getKorrektionsglasensStyrka());
        syn.setHarNattblindhet(source.getNattblindhet());
        syn.setHarNystagmus(source.getNystagmus());
        syn.setHarProgressivOgonsjukdom(source.getProgressivOgonsjukdom());
        syn.setHarSynfaltsdefekt(source.getSynfaltsdefekter());

        syn.setSynskarpaMedKorrektion(buildSynskarpaMedKorrektion(source.getHogerOga().getMedKorrektion(), source.getVansterOga().getMedKorrektion(),
                source.getBinokulart().getMedKorrektion(), source.getHogerOga().getKontaktlins(), source.getVansterOga().getKontaktlins()));

        syn.setSynskarpaUtanKorrektion(buildSynskarpaUtanKorrektion(source.getHogerOga().getUtanKorrektion(), source.getVansterOga()
                .getUtanKorrektion(), source.getBinokulart().getUtanKorrektion()));
        return syn;
    }

    private static SynskarpaUtanKorrektion buildSynskarpaUtanKorrektion(Double hoger, Double vanster, Double binokulart) {
        if (hoger == null && vanster == null && binokulart == null) {
            return null;
        }
        SynskarpaUtanKorrektion synUtanKorrektion = new SynskarpaUtanKorrektion();
        synUtanKorrektion.setHogerOga(hoger);
        synUtanKorrektion.setVansterOga(vanster);
        synUtanKorrektion.setBinokulart(binokulart);
        return synUtanKorrektion;
    }

    private static SynskarpaMedKorrektion buildSynskarpaMedKorrektion(Double hoger, Double vanster, Double binokulart, Boolean kontaktlinsHoger,
            Boolean kontaktlinsVanster) {
        if (hoger == null && vanster == null && binokulart == null && kontaktlinsHoger == null && kontaktlinsVanster == null) {
            return null;
        }
        SynskarpaMedKorrektion synMedKorrektion = new SynskarpaMedKorrektion();
        if (hoger != null) {
            synMedKorrektion.setHogerOga(hoger);
        }
        if (vanster != null) {
            synMedKorrektion.setVansterOga(vanster);
        }
        if (binokulart != null) {
            synMedKorrektion.setBinokulart(binokulart);
        }
        if (kontaktlinsHoger != null) {
            synMedKorrektion.setHarKontaktlinsHogerOga(kontaktlinsHoger);
        }
        if (kontaktlinsVanster != null) {
            synMedKorrektion.setHarKontaktlinsVansterOga(kontaktlinsVanster);
        }
        return synMedKorrektion;
    }

    private static Utvecklingsstorning buildUtvecklingsstorning(
            se.inera.intyg.intygstyper.ts_bas.model.internal.Utvecklingsstorning source) {
        Utvecklingsstorning utvecklingsstorning = new Utvecklingsstorning();
        utvecklingsstorning.setHarAndrayndrom(source.getHarSyndrom());
        utvecklingsstorning.setHarPsykiskUtvecklingsstorning(source.getPsykiskUtvecklingsstorning());
        return utvecklingsstorning;
    }

    // Convert GrundData //
    private static GrundData buildGrundData(se.inera.intyg.common.support.model.common.internal.GrundData source) {
        GrundData grundData = new GrundData();
        grundData.setPatient(buildPatient(source.getPatient()));
        grundData.setSigneringsTidstampel(source.getSigneringsdatum().toString(SIGNERINGS_TIDSTAMPEL_FORMAT));
        grundData.setSkapadAv(buildSkapadAv(source.getSkapadAv()));
        return grundData;
    }

    private static Patient buildPatient(se.inera.intyg.common.support.model.common.internal.Patient source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.setFornamn(source.getFornamn());
        patient.setFullstandigtNamn(StringUtils.join(ArrayUtils.toArray(source.getFornamn(), source.getEfternamn()), " "));
        patient.setPersonId(buildII(Constants.PERSON_ID_OID, source.getPersonId().getPersonnummer()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());
        return patient;
    }

    private static SkapadAv buildSkapadAv(HoSPersonal source) {
        SkapadAv skapadAv = new SkapadAv();
        skapadAv.setAtLakare(source.getBefattningar().contains(BefattningKod.LAKARE_EJ_LEG_AT.getNamn()));
        if (!source.getBefattningar().isEmpty()) {
            skapadAv.getBefattningar().addAll(source.getBefattningar());
        }
        skapadAv.setFullstandigtNamn(source.getFullstandigtNamn());
        skapadAv.setPersonId(buildII(Constants.HSA_ID_OID, source.getPersonId()));
        skapadAv.setVardenhet(buildVardenhet(source.getVardenhet()));
        skapadAv.getSpecialiteter().addAll(source.getSpecialiteter());
        return skapadAv;
    }

    private static Vardenhet buildVardenhet(se.inera.intyg.common.support.model.common.internal.Vardenhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsId(buildII(Constants.HSA_ID_OID, source.getEnhetsid()));
        vardenhet.setEnhetsnamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(buildVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare buildVardgivare(se.inera.intyg.common.support.model.common.internal.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(buildII(Constants.HSA_ID_OID, source.getVardgivarid()));
        vardgivare.setVardgivarnamn(source.getVardgivarnamn());
        return vardgivare;
    }

    private static II buildII(String root, String extension) {
        II ii = new II();
        ii.setExtension(extension);
        ii.setRoot(root);
        return ii;
    }
}
