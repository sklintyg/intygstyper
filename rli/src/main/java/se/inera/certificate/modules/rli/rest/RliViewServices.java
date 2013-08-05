package se.inera.certificate.modules.rli.rest;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import se.inera.certificate.rli.view.model.Arrangemang;
import se.inera.certificate.rli.view.model.Datumintervall;
import se.inera.certificate.rli.view.model.HosPersonal;
import se.inera.certificate.rli.view.model.KomplikationStatusType;
import se.inera.certificate.rli.view.model.OrsakAvbokningType;
import se.inera.certificate.rli.view.model.Patient;
import se.inera.certificate.rli.view.model.ResenarGravid;
import se.inera.certificate.rli.view.model.Tillstand;
import se.inera.certificate.rli.view.model.Undersokning;
import se.inera.certificate.rli.view.model.Utfardare;
import se.inera.certificate.rli.view.model.Utlatande;
import se.inera.certificate.rli.view.model.Vardenhet;
import se.inera.certificate.rli.view.model.Vardgivare;

@Path("/view")
public class RliViewServices {

	@GET
	@Path("/utlatande/{utlatande-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Utlatande getUtlatande(@PathParam("utlatande-id") String utlatandeId) {

		return buildUtlatande(utlatandeId);
	}

	private Utlatande buildUtlatande(String utlatandeId) {
		
		Utlatande lk = new Utlatande();
		lk.setKommentar("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac mi a erat tristique pretium. Donec suscipit sollicitudin porta. Praesent tristique massa vel rhoncus eleifend. Donec dapibus id sapien interdum mollis. Quisque vitae faucibus diam. Proin non sodales orci. In congue molestie lectus, sed lobortis sapien condimentum sit amet. Aliquam condimentum, orci ac semper pretium, nisl ante viverra nulla, a lobortis sapien massa ac erat. Ut sed eros eu felis fermentum venenatis eget id sem.");
		lk.setSigneringsdatum(LocalDateTime.now());
		lk.setUtlatandeId(UUID.randomUUID().toString());
		lk.setTypAvUtlatande("RLI");
		
		Datumintervall giltigPeriod = new Datumintervall();
		giltigPeriod.setStartDatum(LocalDate.now());
		giltigPeriod.setSlutDatum(LocalDate.now().plusMonths(3));
		
		lk.setGiltighetsperiod(giltigPeriod);

		Patient pat = new Patient();
		pat.setPersonId("19121212-1212");
		pat.setFullstandigtNamn("Tio Elva Tolva Tolvansson");

		lk.setPatient(pat);

		Arrangemang arr = new Arrangemang();
		arr.setPlats("Långtbortistan");
		arr.setBokningsreferens("ABC2340978");
		arr.setBokningsdatum(LocalDate.now().minusDays(23));
		arr.setArrangemangstyp("RESA");
		arr.setAvbestallningsdatum(LocalDate.now().minusDays(2));

		Datumintervall arrTid = new Datumintervall();
		arrTid.setStartDatum(LocalDate.now().plusDays(5));
		arrTid.setSlutDatum(LocalDate.now().plusDays(19));
		arr.setArrangemangstid(arrTid);

		lk.setArrangemang(arr);
		
		Vardgivare vardgivare = new Vardgivare();
		vardgivare.setVardgivareId("BBB1122");
		vardgivare.setVardgivareNamn("Landstinget");

		Vardenhet venh = new Vardenhet();
		venh.setVardgivare(vardgivare);
		venh.setEnhetNamn("Tolvberga vårdcentral");
		venh.setEnhetId("KS23497D");
		venh.setPostadress("Trettongången 13");
		venh.setPostnummer("12345");
		venh.setPostort("Tolvberga");
				
		HosPersonal hosPerson = new HosPersonal();
		hosPerson.setFullstandigtNamn("Börje Dengroth");
		hosPerson.setBefattning("leg läkare");
		hosPerson.setPersonId("23467736");
		
		Utfardare utfardare = new Utfardare();
		utfardare.setHosPersonal(hosPerson);
		utfardare.setVardenhet(venh);
		
		lk.setUtfardare(utfardare);
				
		Tillstand tills = new Tillstand();
		tills.setOrsakAvbokning(OrsakAvbokningType.RESENAR_GRAVID_EJ_RESBAR);
		ResenarGravid resenarGravid = new ResenarGravid();
		resenarGravid.setBeraknadForlossning(LocalDate.now().plusMonths(8));
		tills.setResenarGravid(resenarGravid);
				
		lk.setTillstand(tills);
		
		Undersokning usok = new Undersokning();
		usok.setForstaundersokningDatum("2013-05-08");
		usok.setForstaundersokningPlats("Stockholm");
		usok.setUndersokningDatum(LocalDate.now().minusDays(2));
		usok.setUndersokningPlats("Borås");
		usok.setKomplikationStatus(KomplikationStatusType.KOMPLIKATION_KAND_FORVARRATS_FORUTSAGBART);
		usok.setKomplikationBeskrivning("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac mi a erat tristique pretium. Donec suscipit sollicitudin porta. Praesent tristique massa vel rhoncus eleifend. Donec dapibus id sapien interdum mollis. Quisque vitae faucibus diam. Proin non sodales orci. In congue molestie lectus, sed lobortis sapien condimentum sit amet. Aliquam condimentum, orci ac semper pretium, nisl ante viverra nulla, a lobortis sapien massa ac erat. Ut sed eros eu felis fermentum venenatis eget id sem.");
		
		lk.setUndersokning(usok);
		
		return lk;
	}
}
