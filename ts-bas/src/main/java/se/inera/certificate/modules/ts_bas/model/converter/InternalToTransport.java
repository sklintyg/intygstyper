/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.ts_bas.model.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.ts_bas.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_bas.model.internal.Bedomning;
import se.inera.certificate.modules.ts_bas.model.internal.BedomningKorkortstyp;
import se.inera.certificate.modules.ts_bas.model.internal.Diabetes;
import se.inera.certificate.modules.ts_bas.model.internal.Funktionsnedsattning;
import se.inera.certificate.modules.ts_bas.model.internal.HjartKarl;
import se.inera.certificate.modules.ts_bas.model.internal.HorselBalans;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_bas.model.internal.Medicinering;
import se.inera.certificate.modules.ts_bas.model.internal.NarkotikaLakemedel;
import se.inera.certificate.modules.ts_bas.model.internal.Syn;
import se.inera.certificate.schema.Constants;
import se.intygstjanster.ts.services.types.v1.II;
import se.intygstjanster.ts.services.v1.AlkoholNarkotikaLakemedel;
import se.intygstjanster.ts.services.v1.BedomningTypBas;
import se.intygstjanster.ts.services.v1.DiabetesTypBas;
import se.intygstjanster.ts.services.v1.DiabetesTypVarden;
import se.intygstjanster.ts.services.v1.GrundData;
import se.intygstjanster.ts.services.v1.HjartKarlSjukdomar;
import se.intygstjanster.ts.services.v1.HorselBalanssinne;
import se.intygstjanster.ts.services.v1.IdentifieringsVarden;
import se.intygstjanster.ts.services.v1.IdentitetStyrkt;
import se.intygstjanster.ts.services.v1.IntygsAvserTypBas;
import se.intygstjanster.ts.services.v1.KorkortsbehorighetTsBas;
import se.intygstjanster.ts.services.v1.Medvetandestorning;
import se.intygstjanster.ts.services.v1.OvrigMedicinering;
import se.intygstjanster.ts.services.v1.Patient;
import se.intygstjanster.ts.services.v1.RorelseorganenFunktioner;
import se.intygstjanster.ts.services.v1.Sjukhusvard;
import se.intygstjanster.ts.services.v1.SkapadAv;
import se.intygstjanster.ts.services.v1.SynfunktionBas;
import se.intygstjanster.ts.services.v1.SynskarpaMedKorrektion;
import se.intygstjanster.ts.services.v1.SynskarpaUtanKorrektion;
import se.intygstjanster.ts.services.v1.TSBasIntyg;
import se.intygstjanster.ts.services.v1.Utvecklingsstorning;
import se.intygstjanster.ts.services.v1.Vardenhet;
import se.intygstjanster.ts.services.v1.Vardgivare;

/**
 * Convert from {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande} to the external {@link Utlatande}
 * model.
 *
 * @author erik
 *
 */
public class InternalToTransport {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    /**
     * Takes an internal Utlatande and converts it to the external model.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande}
     *
     * @return {@link Utlatande}, unless the source is null in which case a
     *         {@link se.inera.certificate.model.converter.util.ConverterException} is thrown
     *
     * @throws se.inera.certificate.model.converter.util.ConverterException
     */
    public static TSBasIntyg convert(se.inera.certificate.modules.ts_bas.model.internal.Utlatande source)
            throws ConverterException {
        LOG.trace("Converting internal model to transport");

        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        TSBasIntyg utlatande = new TSBasIntyg();

        utlatande.setIntygsId(source.getId());
        utlatande.setGrundData(buildGrundData(source.getGrundData()));

        UtlatandeKod utlatandeKod = UtlatandeKod.valueOf(source.getTyp());

        utlatande.setIntygsTyp(utlatandeKod.getTypForTransportConvertion());

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
        return utlatande;
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
            behorigheter.add(KorkortsbehorighetTsBas.valueOf(mapToKorkortsbehorighetTsBas(typ.name())));
        }
        return behorigheter;
    }

    private static String mapToKorkortsbehorighetTsBas(String name) {
        switch(name) {
        case "C":
            return "C";
        case "C1":
            return "C_1";
        case "C1E":
            return "C_1_E";
        case "D1":
            return "D_1";
        case "D1E":
            return "D_1_E";
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

    private static DiabetesTypBas buildDiabetesTypBas(Diabetes source) {
        DiabetesTypBas diabetes = new DiabetesTypBas();
        diabetes.setHarBehandlingInsulin(source.getInsulin());
        diabetes.setHarBehandlingKost(source.getKost());
        diabetes.setHarBehandlingTabletter(source.getTabletter());
        diabetes.setHarDiabetes(source.getHarDiabetes());
        if (source.getDiabetesTyp().equals("DIABETES_TYP_1")) {
            diabetes.getDiabetesTyp().add(DiabetesTypVarden.fromValue("TYP1"));
        } else if (source.getDiabetesTyp().equals("DIABETES_TYP_2")) {
            diabetes.getDiabetesTyp().add(DiabetesTypVarden.fromValue("TYP2"));
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

    private static IdentitetStyrkt buildIdentitetStyrkt(se.inera.certificate.modules.ts_bas.model.internal.Vardkontakt source) {
        IdentitetStyrkt identitetStyrkt = new IdentitetStyrkt();
        identitetStyrkt.getIdkontroll().add(IdentifieringsVarden.valueOf(mapToIdentifieringsVarde(source.getIdkontroll())));
        return identitetStyrkt;
    }

    private static String mapToIdentifieringsVarde(String idkontroll) {
        switch (idkontroll) {
        case "ID_KORT":
            return "IDK_1";
        case "FORETAG_ELLER_TJANSTEKORT":
            return "IDK_2";
        case "KORKORT":
            return "IDK_3";
        case "PERS_KANNEDOM":
            return "IDK_4";
        case "FORSAKRAN_KAP18":
            return "IDK_5";
        case "PASS":
            return "IDK_6";
        default:
            return null;
        }
    }

    private static IntygsAvserTypBas buildIntygAvser(IntygAvser source) {
        
        IntygsAvserTypBas intygAvser = new IntygsAvserTypBas();
        for (IntygAvserKategori kat : source.getKorkortstyp()) {
            intygAvser.getKorkortstyp().add(KorkortsbehorighetTsBas.valueOf(mapToKorkortsbehorighetTsBas(kat.name())));
        }
        return intygAvser;
    }

    private static Medvetandestorning buildMedvetandestorning(se.inera.certificate.modules.ts_bas.model.internal.Medvetandestorning source) {
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

    private static Sjukhusvard buildSjukhusvard(se.inera.certificate.modules.ts_bas.model.internal.Sjukhusvard source) {
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
        SynskarpaUtanKorrektion synUtanKorrektion = new SynskarpaUtanKorrektion();
        synUtanKorrektion.setHogerOga(hoger);
        synUtanKorrektion.setVansterOga(vanster);
        synUtanKorrektion.setBinokulart(binokulart);
        return synUtanKorrektion;
    }

    private static SynskarpaMedKorrektion buildSynskarpaMedKorrektion(Double hoger, Double vanster, Double binokulart, boolean kontaktlinsHoger,
            boolean kontaktlinsVanster) {
        SynskarpaMedKorrektion synMedKorrektion = new SynskarpaMedKorrektion();
        synMedKorrektion.setHogerOga(hoger);
        synMedKorrektion.setVansterOga(vanster);
        synMedKorrektion.setBinokulart(binokulart);
        return synMedKorrektion;
    }

    private static Utvecklingsstorning buildUtvecklingsstorning(
            se.inera.certificate.modules.ts_bas.model.internal.Utvecklingsstorning source) {
        Utvecklingsstorning utvecklingsstorning = new Utvecklingsstorning();
        utvecklingsstorning.setHarAndrayndrom(source.getHarSyndrom());
        utvecklingsstorning.setHarPsykiskUtvecklingsstorning(source.getPsykiskUtvecklingsstorning());
        return utvecklingsstorning;
    }

    // Convert GrundData //
    private static GrundData buildGrundData(se.inera.certificate.model.common.internal.GrundData source) {
        GrundData grundData = new GrundData();
        grundData.setPatient(buildPatient(source.getPatient()));
        grundData.setSigneringsTidstampel(source.getSigneringsdatum().toString());
        grundData.setSkapadAv(buildSkapadAv(source.getSkapadAv()));
        return grundData;
    }

    private static Patient buildPatient(se.inera.certificate.model.common.internal.Patient source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.setFornamn(source.getFornamn());
        patient.setFullstandigtNamn(StringUtils.join(ArrayUtils.toArray(source.getFornamn(), source.getEfternamn()), " "));
        patient.setPersonId(buildII(Constants.PERSON_ID_OID, source.getPersonId()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());
        return patient;
    }

    private static SkapadAv buildSkapadAv(HoSPersonal source) {
        SkapadAv skapadAv = new SkapadAv();
        // TODO Find out how this actually looks in Befattningar
        skapadAv.setAtLakare(source.getBefattningar().contains("AT-läkare"));
        skapadAv.setBefattningar(StringUtils.join(source.getBefattningar(), ", "));
        skapadAv.setFullstandigtNamn(source.getFullstandigtNamn());
        skapadAv.setPersonId(buildII(Constants.HSA_ID_OID, source.getPersonId()));
        skapadAv.setVardenhet(buildVardenhet(source.getVardenhet()));
        skapadAv.setSpecialiteter(StringUtils.join(source.getSpecialiteter(), ", "));
        return skapadAv;
    }

    private static Vardenhet buildVardenhet(se.inera.certificate.model.common.internal.Vardenhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsId(buildII(Constants.HSA_ID_OID,source.getEnhetsid()));
        vardenhet.setEnhetsnamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(buildVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare buildVardgivare(se.inera.certificate.model.common.internal.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(buildII(Constants.HSA_ID_OID, source.getVardgivarid()));
        vardgivare.setVardgivarnamn(source.getVardgivarnamn());
        return vardgivare ;
    }

    private static II buildII(String root, String extension) {
        II ii = new II();
        ii.setExtension(extension);
        ii.setRoot(root);
        return ii;
    }
}
