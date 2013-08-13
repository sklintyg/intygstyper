package se.inera.certificate.modules.rli.model.external;

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
		return kommentarer;
	}

	public void setKommentarer(List<String> kommentarer) {
		this.kommentarer = kommentarer;
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
		return harDeltagandeHosPersonal;
	}

	public void setHarDeltagandeHosPersonal(List<HosPersonal> harDeltagandeHosPersonal) {
		this.harDeltagandeHosPersonal = harDeltagandeHosPersonal;
	}

	public List<Vardkontakt> getVardkontakter() {
		return vardkontakter;
	}

	public void setVardkontakter(List<Vardkontakt> vardkontakter) {
		this.vardkontakter = vardkontakter;
	}

	public List<Referens> getReferenser() {
		return referenser;
	}

	public void setReferenser(List<Referens> referenser) {
		this.referenser = referenser;
	}

	public List<Aktivitet> getAktiviteter() {
		return aktiviteter;
	}

	public void setAktiviteter(List<Aktivitet> aktiviteter) {
		this.aktiviteter = aktiviteter;
	}

	public Bestallare getBestallare() {
		return bestallare;
	}

	public void setBestallare(Bestallare bestallare) {
		this.bestallare = bestallare;
	}

	public List<Substansintag> getSubstansintag() {
		return substansintag;
	}

	public void setSubstansintag(List<Substansintag> substansintag) {
		this.substansintag = substansintag;
	}

	public Betalningsmottagare getBetalningsmottagare() {
		return betalningsmottagare;
	}

	public void setBetalningsmottagare(Betalningsmottagare betalningsmottagare) {
		this.betalningsmottagare = betalningsmottagare;
	}

	public List<Rekommendation> getRekommendationer() {
		return rekommendationer;
	}

	public void setRekommendationer(List<Rekommendation> rekommendationer) {
		this.rekommendationer = rekommendationer;
	}

	public List<Observation> getObservationer() {
		return observationer;
	}

	public void setObservationer(List<Observation> observationer) {
		this.observationer = observationer;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}

	public Arrangemang getArrangemang() {
		return arrangemang;
	}

	public void setArrangemang(Arrangemang arrangemang) {
		this.arrangemang = arrangemang;
	}
}
