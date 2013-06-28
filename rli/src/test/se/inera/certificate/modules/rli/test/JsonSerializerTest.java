package se.inera.certificate.modules.rli.test;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;

import com.ctc.wstx.dtd.LargePrefixedNameSet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.certificate.integration.v1.ArrangemangType;
import se.inera.certificate.integration.v1.BedomningType;
import se.inera.certificate.integration.v1.BedomningskodType;
import se.inera.certificate.integration.v1.HosPersonalType;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.integration.v1.ObservationType;
import se.inera.certificate.integration.v1.PatientType;
import se.inera.certificate.integration.v1.VardenhetType;
import se.inera.certificate.integration.v1.VardgivareType;
import se.inera.certificate.modules.rli.json.RLICustomObjectMapper;

public class JsonSerializerTest {

	@Ignore
	@Test
	public void doSerializing() throws JsonProcessingException {
		
		Lakarutlatande utlatande = buildLakarutlatande();
		case3(utlatande);
		
		RLICustomObjectMapper mapper = new RLICustomObjectMapper();
		
		String json = mapper.writeValueAsString(utlatande);
		
		assertNotNull(json);
		
		System.out.println(json);
	}
	
	private Lakarutlatande buildLakarutlatande() {
		
		Lakarutlatande lk = new Lakarutlatande();
		lk.setId(UUID.randomUUID().toString());
		
		PatientType pat = new PatientType();
		pat.setId("19121212-1212");
		pat.setFornamn("Tolva");
		pat.setEfternamn("Tolvansson");
		pat.setPostadress("Tolvgatan 12");
		pat.setPostnummer("12345");
		pat.setPostort("Tolvberga");
		
		lk.setPatient(pat);
		
		ArrangemangType arr = new ArrangemangType();
		arr.setDestination("Långtbortistan");
		arr.setBokningsreferens("ABC123");
		arr.setBokningsdatum(LocalDate.now().minusDays(8));
		arr.setArrangemangstyp("resa");
		arr.setAvbestallningsdatum(LocalDate.now().minusDays(2));
		arr.setArrangemangstid(LocalDate.now().plusDays(2));
		
		lk.setArrangemang(arr);
		
		VardgivareType vardgivare = new VardgivareType();
		vardgivare.setId("BBB222");
		vardgivare.setId("Landstinget");
						
		VardenhetType venh = new VardenhetType();
		venh.setVardgivare(vardgivare);
		venh.setNamn("Tolvberga vårdcentral");
		venh.setPostadress("Trettongången 13");
		venh.setPostnummer("12345");
		venh.setPostort("Tolvberga");
		venh.setId("AAA1111");
				
		HosPersonalType pers = new HosPersonalType();
		pers.setVardenhet(venh);
		pers.setNamn("Börje Dengroth");
		pers.setBefattning("Leg läkare");
		pers.setPersonalkategori("läkare");
		pers.setId("112345");
		
		lk.setSkapadAv(pers);
		
		return lk;
	}
	
	private void case1(Lakarutlatande lk) {
		
		// pat sjuk ej resbar
		BedomningType b1 = new BedomningType();
		b1.setBedomningskod(BedomningskodType.RESENAR_SJUK_KAN_EJ_RESA);
		lk.getBedomnings().add(b1);
		
		// pat har ej kronisk sjukdom
		BedomningType b2 = new BedomningType();
		b2.setBedomningskod(BedomningskodType.KOMPLIKATION_OKAND);
		lk.getBedomnings().add(b2);
	}
	
	private void case2(Lakarutlatande lk) {
		
		// pat sjuk ej resbar
		BedomningType b1 = new BedomningType();
		b1.setBedomningskod(BedomningskodType.RESENAR_SJUK_KAN_EJ_RESA);
		lk.getBedomnings().add(b1);
		
		// pat har kronisk sjukdom
		BedomningType b2 = new BedomningType();
		b2.setBedomningskod(BedomningskodType.KOMPLIKATION_KRONISK);
		lk.getBedomnings().add(b2);
		
		// sjukdomens tillstånd har förvärrats på ej förutsägbart sätt
		BedomningType b3 = new BedomningType();
		b3.setBedomningskod(BedomningskodType.KOMPLIKATION_TILLSTAND_FORVARRATS_EJ_FORUTSAGBART);
		lk.getBedomnings().add(b3);
	}
	
	private void case3(Lakarutlatande lk) {
		
		// pat sjuk ej resbar
		BedomningType b1 = new BedomningType();
		b1.setBedomningskod(BedomningskodType.RESENAR_SJUK_KAN_EJ_RESA);
		lk.getBedomnings().add(b1);
		
		// pat har konstig sjukdom
		BedomningType b2 = new BedomningType();
		b2.setBedomningskod(BedomningskodType.KOMPLIKATION_ANNAN);
		b2.setBeskrivning("Pat lider av intermitent aerofobi i kombination med anal magnetism.");
		lk.getBedomnings().add(b2);
	}
	
	private void case4(Lakarutlatande lk) {
		
		// pat sjuk ej resbar
		BedomningType b1 = new BedomningType();
		b1.setBedomningskod(BedomningskodType.RESENAR_SJUK_KAN_EJ_RESA);
		lk.getBedomnings().add(b1);
		
		// pat har konstig sjukdom
		BedomningType b2 = new BedomningType();
		b2.setBedomningskod(BedomningskodType.KOMPLIKATION_ANNAN);
		b2.setBeskrivning("Pat lider av stokastisk aerofobi i kombination med anal magnetism.");
		lk.getBedomnings().add(b2);
	}
	
	
	
}
