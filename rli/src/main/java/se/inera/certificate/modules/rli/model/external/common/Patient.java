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

public class Patient {

	private Id personId;
	private List<String> fornamns;
	private List<String> efternamns;
	private List<String> mellannamns;
	private String adress;
	private List<PatientRelation> patientRelations;
	private List<Arbetsuppgift> arbetsuppgifts;
	private List<Sysselsattning> sysselsattnings;

	public Id getPersonId() {
		return personId;
	}

	public void setPersonId(Id personId) {
		this.personId = personId;
	}

	public List<String> getFornamns() {
		return fornamns;
	}

	public void setFornamns(List<String> fornamns) {
		this.fornamns = fornamns;
	}

	public List<String> getEfternamns() {
		return efternamns;
	}

	public void setEfternamns(List<String> efternamns) {
		this.efternamns = efternamns;
	}

	public List<String> getMellannamns() {
		return mellannamns;
	}

	public void setMellannamns(List<String> mellannamns) {
		this.mellannamns = mellannamns;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public List<PatientRelation> getPatientRelations() {
		return patientRelations;
	}

	public void setPatientRelations(List<PatientRelation> patientRelations) {
		this.patientRelations = patientRelations;
	}

	public List<Arbetsuppgift> getArbetsuppgifts() {
		return arbetsuppgifts;
	}

	public void setArbetsuppgifts(List<Arbetsuppgift> arbetsuppgifts) {
		this.arbetsuppgifts = arbetsuppgifts;
	}

	public List<Sysselsattning> getSysselsattnings() {
		return sysselsattnings;
	}

	public void setSysselsattnings(List<Sysselsattning> sysselsattnings) {
		this.sysselsattnings = sysselsattnings;
	}
}
