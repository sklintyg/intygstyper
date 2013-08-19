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

import java.util.List;

import org.joda.time.LocalDateTime;

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
		return kommentars;
	}

	public void setKommentars(List<String> kommentars) {
		this.kommentars = kommentars;
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
		return harDeltagandeHosPersonals;
	}

	public void setHarDeltagandeHosPersonals(List<HosPersonal> harDeltagandeHosPersonals) {
		this.harDeltagandeHosPersonals = harDeltagandeHosPersonals;
	}

	public List<Vardkontakt> getVardkontakts() {
		return vardkontakts;
	}

	public void setVardkontakts(List<Vardkontakt> vardkontakts) {
		this.vardkontakts = vardkontakts;
	}

	public List<Referens> getReferens() {
		return referens;
	}

	public void setReferens(List<Referens> referens) {
		this.referens = referens;
	}

	public List<Aktivitet> getAktivitets() {
		return aktivitets;
	}

	public void setAktivitets(List<Aktivitet> aktivitets) {
		this.aktivitets = aktivitets;
	}

	public Bestallare getBestallare() {
		return bestallare;
	}

	public void setBestallare(Bestallare bestallare) {
		this.bestallare = bestallare;
	}

	public List<Substansintag> getSubstansintags() {
		return substansintags;
	}

	public void setSubstansintags(List<Substansintag> substansintags) {
		this.substansintags = substansintags;
	}

	public Betalningsmottagare getBetalningsmottagare() {
		return betalningsmottagare;
	}

	public void setBetalningsmottagare(Betalningsmottagare betalningsmottagare) {
		this.betalningsmottagare = betalningsmottagare;
	}

	public List<Rekommendation> getRekommendations() {
		return rekommendations;
	}

	public void setRekommendations(List<Rekommendation> rekommendations) {
		this.rekommendations = rekommendations;
	}

	public List<Observation> getObservations() {
		return observations;
	}

	public void setObservations(List<Observation> observations) {
		this.observations = observations;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}
}
