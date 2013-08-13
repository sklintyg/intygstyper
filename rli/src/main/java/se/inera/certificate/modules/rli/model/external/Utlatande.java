package se.inera.certificate.modules.rli.model.external;

import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Status;
import se.inera.certificate.model.Vardkontakt;

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

	private List<String> kommentars;

	private LocalDateTime signeringsDatum;

	private LocalDateTime skickatDatum;

	private Patient patient;

	private HosPersonal skapadAv;

	private List<Aktivitet> aktiviteter;

	private List<Observation> observations;

	private List<Vardkontakt> vardkontakter;

	private List<Referens> referenser;

	private Arrangemang arrangemang;

	private List<Status> status;

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

	public List<String> getKommentars() {
		return kommentars;
	}

	public void setKommentars(List<String> kommentars) {
		this.kommentars = kommentars;
	}

	public LocalDateTime getSigneringsDatum() {
		return signeringsDatum;
	}

	public void setSigneringsDatum(LocalDateTime signeringsDatum) {
		this.signeringsDatum = signeringsDatum;
	}

	public LocalDateTime getSkickatDatum() {
		return skickatDatum;
	}

	public void setSkickatDatum(LocalDateTime skickatDatum) {
		this.skickatDatum = skickatDatum;
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

	public List<Aktivitet> getAktiviteter() {
		return aktiviteter;
	}

	public void setAktiviteter(List<Aktivitet> aktiviteter) {
		this.aktiviteter = aktiviteter;
	}

	public List<Observation> getObservations() {
		return observations;
	}

	public void setObservations(List<Observation> observations) {
		this.observations = observations;
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

	public Arrangemang getArrangemang() {
		return arrangemang;
	}

	public void setArrangemang(Arrangemang arrangemang) {
		this.arrangemang = arrangemang;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}
}
