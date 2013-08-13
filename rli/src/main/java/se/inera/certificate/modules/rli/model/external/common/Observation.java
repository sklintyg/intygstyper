package se.inera.certificate.modules.rli.model.external.common;

import java.util.List;

import org.joda.time.Partial;

public class Observation {

	private Kod observationskategori;
	private Kod observationskod;
	private Partial observationstid;
	private String beskrivning;
	private PartialDateInterval observationsperiod;
	private List<PhysicalQuantity> vardes;
	private Boolean forekomst;
	private Boolean patientenInstammer;
	private Utforarroll utforsAv;
	private List<Prognos> prognos;

	public Kod getObservationskategori() {
		return observationskategori;
	}

	public void setObservationskategori(Kod observationskategori) {
		this.observationskategori = observationskategori;
	}

	public Kod getObservationskod() {
		return observationskod;
	}

	public void setObservationskod(Kod observationskod) {
		this.observationskod = observationskod;
	}

	public Partial getObservationstid() {
		return observationstid;
	}

	public void setObservationstid(Partial observationstid) {
		this.observationstid = observationstid;
	}

	public String getBeskrivning() {
		return beskrivning;
	}

	public void setBeskrivning(String beskrivning) {
		this.beskrivning = beskrivning;
	}

	public PartialDateInterval getObservationsperiod() {
		return observationsperiod;
	}

	public void setObservationsperiod(PartialDateInterval observationsperiod) {
		this.observationsperiod = observationsperiod;
	}

	public List<PhysicalQuantity> getVardes() {
		return vardes;
	}

	public void setVardes(List<PhysicalQuantity> vardes) {
		this.vardes = vardes;
	}

	public Boolean getForekomst() {
		return forekomst;
	}

	public void setForekomst(Boolean forekomst) {
		this.forekomst = forekomst;
	}

	public Boolean getPatientenInstammer() {
		return patientenInstammer;
	}

	public void setPatientenInstammer(Boolean patientenInstammer) {
		this.patientenInstammer = patientenInstammer;
	}

	public Utforarroll getUtforsAv() {
		return utforsAv;
	}

	public void setUtforsAv(Utforarroll utforsAv) {
		this.utforsAv = utforsAv;
	}

	public List<Prognos> getPrognos() {
		return prognos;
	}

	public void setPrognos(List<Prognos> prognos) {
		this.prognos = prognos;
	}
}
