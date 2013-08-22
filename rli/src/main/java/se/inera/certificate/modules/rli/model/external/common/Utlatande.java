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
package se.inera.certificate.modules.rli.model.external.common;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Status;

public class Utlatande {
	private Id utlatandeId;
	private Kod typAvUtlatande;
	private List<String> kommentars;
	private LocalDateTime signeringsdatum;
	private LocalDateTime skickatdatum;
	private Patient patient;
	private HosPersonal skapadAv;
	private List<HosPersonal> harDeltagandeHosPersonals;
	private List<Vardkontakt> vardkontakts;
	private List<Referens> referens;
	private List<Aktivitet> aktivitets;
	private Bestallare bestallare;
	private List<Substansintag> substansintags;
	private Betalningsmottagare betalningsmottagare;
	private List<Rekommendation> rekommendations;
	private List<Observation> observations;
	private List<Status> status;

	public Id getUtlatandeId() {
		return utlatandeId;
	}

	public void setUtlatandeId(Id utlatandeId) {
		this.utlatandeId = utlatandeId;
	}

	public Kod getTypAvUtlatande() {
		return typAvUtlatande;
	}

	public void setTypAvUtlatande(Kod typAvUtlatande) {
		this.typAvUtlatande = typAvUtlatande;
	}

	public List<String> getKommentars() {
		if (kommentars == null){
			kommentars = new ArrayList<String>();
		}
		return this.kommentars;
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

	public List<HosPersonal> getHarDeltagandeHosPersonals() {
		if(harDeltagandeHosPersonals == null){
			harDeltagandeHosPersonals = new ArrayList<HosPersonal>();
		}
		return this.harDeltagandeHosPersonals;
	}

	public List<Vardkontakt> getVardkontakts() {
		return vardkontakts;
	}

	public void setVardkontakts(List<Vardkontakt> vardkontakts) {
		this.vardkontakts = vardkontakts;
	}

	public List<Referens> getReferens() {
		if (referens == null){
			referens = new ArrayList<Referens>();
		}
		return this.referens;
	}

	public List<Aktivitet> getAktivitets() {
		if (aktivitets == null){
			aktivitets = new ArrayList<Aktivitet>();
		}
		return this.aktivitets;
	}

	public Bestallare getBestallare() {
		return bestallare;
	}

	public void setBestallare(Bestallare bestallare) {
		this.bestallare = bestallare;
	}

	public List<Substansintag> getSubstansintags() {
		if (substansintags == null){
			substansintags = new ArrayList<Substansintag>();
		}
		return this.substansintags;
	}

	public Betalningsmottagare getBetalningsmottagare() {
		return betalningsmottagare;
	}

	public void setBetalningsmottagare(Betalningsmottagare betalningsmottagare) {
		this.betalningsmottagare = betalningsmottagare;
	}

	public List<Rekommendation> getRekommendations() {
		if (rekommendations == null){
			rekommendations = new ArrayList<Rekommendation>();
		}
		return this.rekommendations;
	}

	public List<Observation> getObservations() {
		if (observations == null){
			observations = new ArrayList<Observation>();
		}
		return this.observations;
	}

	public List<Status> getStatus() {
		if (status == null){
			status = new ArrayList<Status>();
		}
		return this.status;
	}

}
