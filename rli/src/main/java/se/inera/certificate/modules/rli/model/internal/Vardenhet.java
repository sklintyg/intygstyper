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

public class Vardenhet {

	private String enhetsId;
	
	private String enhetsNamn;
	
	private String postAddress;
	
	private String postNummer;
	
	private String postOrt;
	
	private String telefonNummer;
	
	private String ePost;
	
	private Vardgivare vardgivare;
	
	public Vardenhet() {
	
	}

	public String getEnhetsId() {
		return enhetsId;
	}

	public void setEnhetsId(String enhetsId) {
		this.enhetsId = enhetsId;
	}

	public String getEnhetsNamn() {
		return enhetsNamn;
	}

	public void setEnhetsNamn(String enhetsNamn) {
		this.enhetsNamn = enhetsNamn;
	}

	public String getPostAddress() {
		return postAddress;
	}

	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	public String getPostNummer() {
		return postNummer;
	}

	public void setPostNummer(String postNummer) {
		this.postNummer = postNummer;
	}

	public String getPostOrt() {
		return postOrt;
	}

	public void setPostOrt(String postOrt) {
		this.postOrt = postOrt;
	}

	public String getTelefonNummer() {
		return telefonNummer;
	}

	public void setTelefonNummer(String telefonNummer) {
		this.telefonNummer = telefonNummer;
	}

	public String getePost() {
		return ePost;
	}

	public void setePost(String ePost) {
		this.ePost = ePost;
	}

	public Vardgivare getVardgivare() {
		return vardgivare;
	}

	public void setVardgivare(Vardgivare vardgivare) {
		this.vardgivare = vardgivare;
	}
	
}
