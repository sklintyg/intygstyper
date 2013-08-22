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

import se.inera.certificate.model.Id;

public class Enhet {

	private Id enhetsId;
	private Id arbetsplatskod;
	private String enhetsnamn;
	private String postadress;
	private String postnummer;
	private String postort;
	private String telefonnummer;
	private String epost;
	private Vardgivare vardgivare;

	public Id getEnhetsId() {
		return enhetsId;
	}

	public void setEnhetsId(Id enhetsId) {
		this.enhetsId = enhetsId;
	}

	public Id getArbetsplatskod() {
		return arbetsplatskod;
	}

	public void setArbetsplatskod(Id arbetsplatskod) {
		this.arbetsplatskod = arbetsplatskod;
	}

	public String getEnhetsnamn() {
		return enhetsnamn;
	}

	public void setEnhetsnamn(String enhetsnamn) {
		this.enhetsnamn = enhetsnamn;
	}

	public String getPostadress() {
		return postadress;
	}

	public void setPostadress(String postadress) {
		this.postadress = postadress;
	}

	public String getPostnummer() {
		return postnummer;
	}

	public void setPostnummer(String postnummer) {
		this.postnummer = postnummer;
	}

	public String getPostort() {
		return postort;
	}

	public void setPostort(String postort) {
		this.postort = postort;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	public String getEpost() {
		return epost;
	}

	public void setEpost(String epost) {
		this.epost = epost;
	}

	public Vardgivare getVardgivare() {
		return vardgivare;
	}

	public void setVardgivare(Vardgivare vardgivare) {
		this.vardgivare = vardgivare;
	}
}
