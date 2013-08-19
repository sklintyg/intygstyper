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
