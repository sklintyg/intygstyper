package se.inera.certificate.modules.rli.model.external;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.certificate.modules.rli.model.external.common.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.Bestallare;
import se.inera.certificate.modules.rli.model.external.common.Betalningsmottagare;
import se.inera.certificate.modules.rli.model.external.common.HosPersonal;
import se.inera.certificate.modules.rli.model.external.common.Id;
import se.inera.certificate.modules.rli.model.external.common.Kod;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.Patient;
import se.inera.certificate.modules.rli.model.external.common.Referens;
import se.inera.certificate.modules.rli.model.external.common.Rekommendation;
import se.inera.certificate.modules.rli.model.external.common.Status;
import se.inera.certificate.modules.rli.model.external.common.Substansintag;
import se.inera.certificate.modules.rli.model.external.common.Vardkontakt;

/**
 * The utlåtande used by RLI. This class is a copy of the common external model (defined in se.inera.certificate.model),
 * extending with:
 * <ul>
 * <li> {@link Arrangemang}
 * </ul>
 * 
 * @author Gustav Norbäcker, R2M
 */
public class Utlatande {

	private Id id;

	private Kod typ;

	private List<String> kommentarer;

	private LocalDateTime signeringsdatum;

	private LocalDateTime skickatdatum;

	private Patient patient;

	private HosPersonal skapadAv;

	private List<HosPersonal> harDeltagandeHosPersonal;

	private List<Vardkontakt> vardkontakter;

	private List<Referens> referenser;

	private List<Aktivitet> aktiviteter;

	private Bestallare bestallare;

	private List<Substansintag> substansintag;

	private Betalningsmottagare betalningsmottagare;

	private List<Rekommendation> rekommendationer;

	private List<Observation> observationer;

	private List<Status> status;

	private Arrangemang arrangemang;

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Kod getTyp() {
		return typ;
	}

	public void setTyp(Kod typ) {
		this.typ = typ;
	}

	public List<String> getKommentarer() {
		if (kommentarer == null){
			kommentarer = new ArrayList<String>();
		}
		return this.kommentarer;
	}

	public LocalDateTime getSigneringsdatum() {
		return signeringsdatum;
	}

	public void setSigneringsdatum(LocalDateTime signeringsdatum) {
		this.signeringsdatum = signeringsdatum;
	}

	public LocalDateTime getSkickatdatum() {
		return skickatdatum;
	}

	public void setSkickatdatum(LocalDateTime skickatdatum) {
		this.skickatdatum = skickatdatum;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public HosPersonal getSkapadAv() {
		return skapadAv;
	}

	public void setSkapadAv(HosPersonal skapadAv) {
		this.skapadAv = skapadAv;
	}

	public List<HosPersonal> getHarDeltagandeHosPersonal() {
		if (harDeltagandeHosPersonal == null){
			harDeltagandeHosPersonal = new ArrayList<HosPersonal>();
		}
		return this.harDeltagandeHosPersonal;
	}

	public List<Vardkontakt> getVardkontakter() {
		if (vardkontakter == null){
			vardkontakter = new ArrayList<Vardkontakt>();
		}
		return this.vardkontakter;
	}

	public List<Referens> getReferenser() {
		if (referenser == null){
			referenser = new ArrayList<Referens>();
		}
		return this.referenser;
	}

	public List<Aktivitet> getAktiviteter() {
		if (aktiviteter == null){
			aktiviteter = new ArrayList<Aktivitet>();
		}
		return this.aktiviteter;
	}

	public Bestallare getBestallare() {
		return bestallare;
	}

	public void setBestallare(Bestallare bestallare) {
		this.bestallare = bestallare;
	}

	public List<Substansintag> getSubstansintag() {
		if (substansintag == null){
			substansintag = new ArrayList<Substansintag>();
		}
		return this.substansintag;
	}

	public Betalningsmottagare getBetalningsmottagare() {
		return betalningsmottagare;
	}

	public void setBetalningsmottagare(Betalningsmottagare betalningsmottagare) {
		this.betalningsmottagare = betalningsmottagare;
	}

	public List<Rekommendation> getRekommendationer() {
		if (rekommendationer == null){
			rekommendationer = new ArrayList<Rekommendation>();
		}
		return this.rekommendationer;
	}

	public List<Observation> getObservationer() {
		if (observationer == null){
			observationer = new ArrayList<Observation>();
		}
		return this.observationer;
	}
	
	public List<Status> getStatus() {
		if (status == null){
			status = new ArrayList<Status>();
		}
		return this.status;
	}

	public Arrangemang getArrangemang() {
		return arrangemang;
	}

	public void setArrangemang(Arrangemang arrangemang) {
		this.arrangemang = arrangemang;
	}
}
