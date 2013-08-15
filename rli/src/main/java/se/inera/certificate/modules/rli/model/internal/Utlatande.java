package se.inera.certificate.modules.rli.model.internal;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class Utlatande {

	private String utlatandeId;
	
	private String typAvUtlatande;
	
	private List<String> kommentarer;
	
	private LocalDateTime signeringsDatum;
	
	private LocalDateTime skickatDatum;
	
	private LocalDate giltighetsPeriodStart;
	
	private LocalDate giltighetsPeriodSlut;
	
	private OrsakAvbokning orsakForAvbokning;
	
	private Utfardare utfardare;
	
	private Patient patient;
		
	private Arrangemang arrangemang;
		
	private Undersokning undersokning;
	
	private List<Status> status;
	
	public Utlatande() {
		
	}

	public String getUtlatandeId() {
		return utlatandeId;
	}

	public void setUtlatandeId(String utlatandeId) {
		this.utlatandeId = utlatandeId;
	}

	public String getTypAvUtlatande() {
		return typAvUtlatande;
	}

	public void setTypAvUtlatande(String typAvUtlatande) {
		this.typAvUtlatande = typAvUtlatande;
	}

	public List<String> getKommentarer() {
		return kommentarer;
	}

	public void setKommentarer(List<String> kommentarer) {
		this.kommentarer = kommentarer;
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

	public LocalDate getGiltighetsPeriodStart() {
		return giltighetsPeriodStart;
	}

	public void setGiltighetsPeriodStart(LocalDate giltighetsPeriodStart) {
		this.giltighetsPeriodStart = giltighetsPeriodStart;
	}

	public LocalDate getGiltighetsPeriodSlut() {
		return giltighetsPeriodSlut;
	}

	public void setGiltighetsPeriodSlut(LocalDate giltighetsPeriodSlut) {
		this.giltighetsPeriodSlut = giltighetsPeriodSlut;
	}

	public OrsakAvbokning getOrsakForAvbokning() {
		return orsakForAvbokning;
	}

	public void setOrsakForAvbokning(OrsakAvbokning orsakForAvbokning) {
		this.orsakForAvbokning = orsakForAvbokning;
	}

	public Utfardare getUtfardare() {
		return utfardare;
	}

	public void setUtfardare(Utfardare utfardare) {
		this.utfardare = utfardare;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Arrangemang getArrangemang() {
		return arrangemang;
	}

	public void setArrangemang(Arrangemang arrangemang) {
		this.arrangemang = arrangemang;
	}

	public Undersokning getUndersokning() {
		return undersokning;
	}

	public void setUndersokning(Undersokning undersokning) {
		this.undersokning = undersokning;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}
	
}
