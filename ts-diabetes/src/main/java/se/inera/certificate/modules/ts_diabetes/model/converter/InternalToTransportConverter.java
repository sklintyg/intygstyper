package se.inera.certificate.modules.ts_diabetes.model.converter;

import se.inera.certificate.modules.ts_diabetes.model.internal.Bedomning;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_diabetes.model.internal.Syn;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.schema.Constants;
import se.intygstjanster.ts.services.types.v1.II;
import se.intygstjanster.ts.services.v1.BedomningTypDiabetes;
import se.intygstjanster.ts.services.v1.Diabetes;
import se.intygstjanster.ts.services.v1.GrundData;
import se.intygstjanster.ts.services.v1.Hypoglykemier;
import se.intygstjanster.ts.services.v1.IntygsAvserTypDiabetes;
import se.intygstjanster.ts.services.v1.Korkortsbehorighet;
import se.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.intygstjanster.ts.services.v1.Patient;
import se.intygstjanster.ts.services.v1.SynfunktionDiabetes;
import se.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class InternalToTransportConverter {
	public static TSDiabetesIntyg convert(Utlatande utlatande) {
		TSDiabetesIntyg result = new TSDiabetesIntyg();
		
		result.setBedomning(readBedomning(utlatande.getBedomning()));
		result.setDiabetes(readDiabetes(utlatande.getDiabetes()));
		result.setGrundData(readGrundData(utlatande.getGrundData()));
		result.setHypoglykemier(readHypoglykemier(utlatande.getHypoglykemier()));
		//TODO:
		//result.setIdentitetStyrkt(readIdentitetStyrkt(utlatande.get));
		result.setIntygAvser(readIntygAvser(utlatande.getIntygAvser()));
		result.setIntygsId(utlatande.getId());
		result.setIntygsTyp(utlatande.getTyp());
		result.setSeparatOgonLakarintygKommerSkickas(utlatande.getSyn().getSeparatOgonlakarintyg());
		result.setSynfunktion(readSynfunktionDiabetes(utlatande.getSyn()));
		//TODO: 
		result.setUtgava("asdas");
		//TODO:
		result.setVersion("asdasd");
		return result;
	}

	private static SynfunktionDiabetes readSynfunktionDiabetes(Syn syn) {
		SynfunktionDiabetes result = new SynfunktionDiabetes();
		result.setHarDiplopi(syn.getDiplopi());
		result.setHarSynfaltsdefekt(syn.getSynfaltsprovningUtanAnmarkning() == false);
		//TODO:
		//result.setSynskarpaMedKorrektion(value);
		//result.setSynskarpaUtanKorrektion(value);
		return result;
	}

	private static IntygsAvserTypDiabetes readIntygAvser(IntygAvser intygAvser) {
		IntygsAvserTypDiabetes result = new IntygsAvserTypDiabetes();
		
		for(IntygAvserKategori kat : intygAvser.getKorkortstyp()){
			KorkortsbehorighetTsDiabetes bh = KorkortsbehorighetTsDiabetes.fromValue(Korkortsbehorighet.fromValue(kat.name()));
			result.getKorkortstyp().add(bh);
		}
		
		return result;
	}

	private static Hypoglykemier readHypoglykemier(se.inera.certificate.modules.ts_diabetes.model.internal.Hypoglykemier hypoglykemier) {
		Hypoglykemier result = new Hypoglykemier();
		result.setAllvarligForekomstBeskrivning(hypoglykemier.getAllvarligForekomstBeskrivning());
		result.setAllvarligForekomstTrafikBeskrivning(hypoglykemier.getAllvarligForekomstTrafikBeskrivning());
		result.setAllvarligForekomstVakenTidAr(hypoglykemier.getAllvarligForekomstVakenTidObservationstid().getDate());
		result.setGenomforEgenkontrollBlodsocker(hypoglykemier.getEgenkontrollBlodsocker());
		result.setHarAllvarligForekomst(hypoglykemier.getAllvarligForekomst());
		result.setHarAllvarligForekomstTrafiken(hypoglykemier.getAllvarligForekomstTrafiken());
		result.setHarAllvarligForekomstVakenTid(hypoglykemier.getAllvarligForekomstVakenTid());
		result.setHarKunskapOmAtgarder(hypoglykemier.getKunskapOmAtgarder());
		result.setHarTeckenNedsattHjarnfunktion(hypoglykemier.getTeckenNedsattHjarnfunktion());
		result.setSaknarFormagaKannaVarningstecken(hypoglykemier.getSaknarFormagaKannaVarningstecken());
		return result;
	}

	private static GrundData readGrundData(se.inera.certificate.model.common.internal.GrundData grundData) {
		GrundData result = new GrundData();
		result.setPatient(readPatient(grundData.getPatient()));
		return result;
	}

	private static Patient readPatient(
			se.inera.certificate.model.common.internal.Patient patient) {
		Patient result = new Patient();
		result.setEfternamn(patient.getEfternamn());
		result.setFornamn(patient.getFornamn());
		result.setFullstandigtNamn(patient.getFullstandigtNamn());
		
		II iid = new II();
		iid.setRoot(Constants.PERSON_ID_OID);
		iid.setExtension(patient.getPersonId());
		result.setPersonId(iid);
		
		result.setPostadress(patient.getPostadress());
		result.setPostnummer(patient.getPostnummer());
		result.setPostort(patient.getPostort());
		return result;
	}

	private static Diabetes readDiabetes(se.inera.certificate.modules.ts_diabetes.model.internal.Diabetes diabetes) {
		Diabetes result = new Diabetes();
		result.setAnnanBehandlingBeskrivning(diabetes.getAnnanBehandlingBeskrivning());
		result.setDebutArDiabetes(diabetes.getObservationsperiod());
		result.setHarBehandlingInsulin(diabetes.getInsulin());
		result.setHarBehandlingKost(diabetes.getEndastKost());
		result.setHarBehandlingTabletter(diabetes.getTabletter());
		result.setInsulinBehandlingSedanAr(diabetes.getInsulinBehandlingsperiod());
		return result;
	}

	private static BedomningTypDiabetes readBedomning(Bedomning bedomning) {
		BedomningTypDiabetes result = new BedomningTypDiabetes();
		result.setBehovAvLakareSpecialistKompetens(bedomning.getLakareSpecialKompetens());
		result.setKanInteTaStallning(bedomning.getKanInteTaStallning());
		result.setLamplighetInnehaBehorighetSpecial(bedomning.getLamplighetInnehaBehorighet());
		result.setOvrigKommentar(bedomning.getKommentarer());
		return result;
	}
}
