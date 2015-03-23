package se.inera.certificate.modules.ts_diabetes.model.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import se.inera.certificate.model.common.internal.GrundData;
import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.common.internal.Patient;
import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.common.internal.Vardgivare;
import se.inera.certificate.modules.ts_diabetes.model.codes.IdKontrollKod;
import se.inera.certificate.modules.ts_diabetes.model.internal.Bedomning;
import se.inera.certificate.modules.ts_diabetes.model.internal.BedomningKorkortstyp;
import se.inera.certificate.modules.ts_diabetes.model.internal.Diabetes;
import se.inera.certificate.modules.ts_diabetes.model.internal.Hypoglykemier;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_diabetes.model.internal.Syn;
import se.inera.certificate.modules.ts_diabetes.model.internal.Synskarpevarden;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.model.internal.Vardkontakt;
import se.inera.certificate.schema.adapter.InternalDateAdapter;
import se.inera.intygstjanster.ts.services.v1.BedomningTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypVarden;
import se.inera.intygstjanster.ts.services.v1.IdentitetStyrkt;
import se.inera.intygstjanster.ts.services.v1.IntygsAvserTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.SynfunktionDiabetes;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class TransportToInternalConverter {
    private static final String VARDKONTAKT_TYP = "5880005";
    private static final String TYPE = "TS_DIABETES_U06_V02";
    
    public static final Map<DiabetesTypVarden, String> typVardenMap;
    
    static {
        Map<DiabetesTypVarden, String> tempMap = new HashMap<>();
        tempMap.put(DiabetesTypVarden.TYP_2, "DIABETES_TYP_2");
        tempMap.put(DiabetesTypVarden.TYP_1, "DIABETES_TYP_1");
        
        typVardenMap = Collections.unmodifiableMap(tempMap);
    }
    
    public static Utlatande convert(TSDiabetesIntyg transport){
        Utlatande result = new Utlatande();
        
        result.setId(transport.getIntygsId());
        result.setTyp(TYPE);
        result.setGrundData(readGrundData(transport.getGrundData()));
        readDiabetes(result.getDiabetes(), transport.getDiabetes());
        readHypoglykemier(result.getHypoglykemier(), transport.getHypoglykemier());
        readSyn(result.getSyn(), transport.getSynfunktion());
        readBedomning(result.getBedomning(), transport.getBedomning());
        readIntygAvser(result.getIntygAvser(), transport.getIntygAvser());
        result.setVardkontakt(readVardkontakt(transport.getIdentitetStyrkt()));
        result.setKommentar(transport.getOvrigKommentar());
        return result;
    }

    private static Vardkontakt readVardkontakt(IdentitetStyrkt identitetStyrkt) {
        Vardkontakt result = new Vardkontakt();
        //TODO: Vart kommer denna ifrån?
        result.setTyp(VARDKONTAKT_TYP);
        result.setIdkontroll(IdKontrollKod.fromCode(identitetStyrkt.getIdkontroll().value()).name());
        return result;
    }

    private static void readIntygAvser(IntygAvser intygAvser, IntygsAvserTypDiabetes intygAvser2) {
        for(KorkortsbehorighetTsDiabetes kbh : intygAvser2.getKorkortstyp()){
            intygAvser.getKorkortstyp().add(IntygAvserKategori.valueOf(kbh.name().replaceAll("_", "")));
        }
    }

    private static void readBedomning(Bedomning bedomning, BedomningTypDiabetes bedomning2) {
        bedomning.setKanInteTaStallning(bedomning2.isKanInteTaStallning());
        bedomning.setKommentarer(bedomning2.getOvrigKommentar());
        bedomning.setLakareSpecialKompetens(bedomning2.getBehovAvLakareSpecialistKompetens());
        bedomning.setLamplighetInnehaBehorighet(bedomning2.isLamplighetInnehaBehorighetSpecial());
        
        for (KorkortsbehorighetTsDiabetes kbh : bedomning2.getKorkortstyp()) {
            bedomning.getKorkortstyp().add(BedomningKorkortstyp.valueOf(kbh.toString().replaceAll("_", "")));
        }
    }

    private static void readSyn(Syn syn, SynfunktionDiabetes synfunktion) {
        syn.setBinokulart(readBinokulart(synfunktion));
        syn.setDiplopi(synfunktion.isHarDiplopi());
        syn.setHoger(readHoger(synfunktion));
        syn.setVanster(readVanster(synfunktion));
        syn.setSynfaltsprovning(synfunktion.isFinnsSynfaltsprovning());
        syn.setProvningOgatsRorlighet(synfunktion.isFinnsProvningOgatsRorlighet());
        syn.setSynfaltsprovningUtanAnmarkning(synfunktion.isSynfaltsprovningUtanAnmarkning());
    }

    private static Synskarpevarden readVanster(SynfunktionDiabetes synfunktion) {
        Synskarpevarden result = new Synskarpevarden();
        result.setMedKorrektion(synfunktion.getSynskarpaMedKorrektion().getVansterOga());
        result.setUtanKorrektion(synfunktion.getSynskarpaUtanKorrektion().getVansterOga());
        return result;
    }

    private static Synskarpevarden readHoger(SynfunktionDiabetes synfunktion) {
        Synskarpevarden result = new Synskarpevarden();
        result.setMedKorrektion(synfunktion.getSynskarpaMedKorrektion().getHogerOga());
        result.setUtanKorrektion(synfunktion.getSynskarpaUtanKorrektion().getHogerOga());
        return result;
    }

    private static Synskarpevarden readBinokulart(SynfunktionDiabetes synfunktion) {
        Synskarpevarden result = new Synskarpevarden();
        result.setMedKorrektion(synfunktion.getSynskarpaMedKorrektion().getBinokulart());
        result.setUtanKorrektion(synfunktion.getSynskarpaUtanKorrektion().getBinokulart());
        return result;
    }

    private static void readHypoglykemier(Hypoglykemier hypoglykemier, se.inera.intygstjanster.ts.services.v1.Hypoglykemier hypoglykemier2) {
        hypoglykemier.setAllvarligForekomst(hypoglykemier2.isHarAllvarligForekomst());
        hypoglykemier.setAllvarligForekomstBeskrivning(hypoglykemier2.getAllvarligForekomstBeskrivning());
        hypoglykemier.setAllvarligForekomstTrafikBeskrivning(hypoglykemier2.getAllvarligForekomstTrafikBeskrivning());
        hypoglykemier.setAllvarligForekomstTrafiken(hypoglykemier2.isHarAllvarligForekomstTrafiken());
        hypoglykemier.setAllvarligForekomstVakenTid(hypoglykemier2.isHarAllvarligForekomstVakenTid());
        
        hypoglykemier.setAllvarligForekomstVakenTidObservationstid(InternalDateAdapter.parseInternalDate(hypoglykemier2.getAllvarligForekomstVakenTidAr()));
        hypoglykemier.setEgenkontrollBlodsocker(hypoglykemier2.isGenomforEgenkontrollBlodsocker());
        hypoglykemier.setKunskapOmAtgarder(hypoglykemier2.isHarKunskapOmAtgarder());
        hypoglykemier.setSaknarFormagaKannaVarningstecken(hypoglykemier2.isSaknarFormagaKannaVarningstecken());
        hypoglykemier.setTeckenNedsattHjarnfunktion(hypoglykemier2.isHarTeckenNedsattHjarnfunktion());
    }

    private static void readDiabetes(Diabetes diabetes, se.inera.intygstjanster.ts.services.v1.Diabetes diabetes2) {
        diabetes.setAnnanBehandlingBeskrivning(diabetes2.getAnnanBehandlingBeskrivning());
        diabetes.setDiabetestyp(typVardenMap.get(diabetes2.getDiabetesTyp().get(0)));
        diabetes.setEndastKost(diabetes2.isHarBehandlingKost());
        diabetes.setInsulin(diabetes2.isHarBehandlingInsulin());
        diabetes.setInsulinBehandlingsperiod(diabetes2.getInsulinBehandlingSedanAr());
        diabetes.setObservationsperiod(diabetes2.getDebutArDiabetes());
        diabetes.setTabletter(diabetes2.isHarBehandlingTabletter());
    }

    private static GrundData readGrundData(se.inera.intygstjanster.ts.services.v1.GrundData grundData) {
        GrundData result = new GrundData();
        result.setPatient(readPatient(grundData.getPatient()));
        result.setSigneringsdatum(grundData.getSigneringsTidstampel() != null ? LocalDateTime.parse(grundData.getSigneringsTidstampel()) : null);
        result.setSkapadAv(readSkapadAv(grundData.getSkapadAv()));
        return result;
    }

    private static HoSPersonal readSkapadAv(SkapadAv skapadAv) {
        HoSPersonal result = new HoSPersonal();
        //result.setForskrivarKod(skapadAv.get);
        result.setFullstandigtNamn(skapadAv.getFullstandigtNamn());
        result.setPersonId(skapadAv.getPersonId().getExtension());
        result.setVardenhet(readVardenhet(skapadAv.getVardenhet()));
        return result;
    }

    private static Vardenhet readVardenhet(se.inera.intygstjanster.ts.services.v1.Vardenhet vardenhet) {
        Vardenhet result = new Vardenhet();
        //result.setArbetsplatsKod(vardenhet.get);
        result.setEnhetsid(vardenhet.getEnhetsId().getExtension());
        result.setEnhetsnamn(vardenhet.getEnhetsnamn());
        //result.setEpost(epost);
        result.setPostadress(vardenhet.getPostadress());
        result.setPostnummer(vardenhet.getPostnummer());
        result.setPostort(vardenhet.getPostort());
        result.setTelefonnummer(vardenhet.getTelefonnummer());
        result.setVardgivare(readVardgivare(vardenhet.getVardgivare()));
        return result;
    }

    private static Vardgivare readVardgivare(se.inera.intygstjanster.ts.services.v1.Vardgivare vardgivare) {
        Vardgivare result = new Vardgivare();
        result.setVardgivarid(vardgivare.getVardgivarid().getExtension());
        result.setVardgivarnamn(vardgivare.getVardgivarnamn());
        return result;
    }

    private static Patient readPatient(se.inera.intygstjanster.ts.services.v1.Patient patient) {
        Patient result = new Patient();
        result.setEfternamn(patient.getEfternamn());
        result.setFornamn(patient.getFornamn());
        result.setFullstandigtNamn(patient.getFullstandigtNamn());
        result.setPersonId(patient.getPersonId().getExtension());
        result.setPostadress(patient.getPostadress());
        result.setPostnummer(patient.getPostnummer());
        result.setPostort(patient.getPostort());
        return result;
    }
}
