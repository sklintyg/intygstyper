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
package se.inera.certificate.modules.rli.model.codes;

public enum AktivitetsKod implements ICodeSystem {

	KLINISK_UNDERSOKNING("AV020","klinisk undersökning UNS","KVÅ"),
	OMVARDNADSATGARD("9632001","Omvårdnadsåtgärd","SNOMED-CT");
	
	private String code;
	private String description;
	private String codeSystemName;
	
	
	private AktivitetsKod(String code, String description, String codeSystemName) {
		this.code = code;
		this.description = description;
		this.codeSystemName = codeSystemName;
	}
	
	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getCodeSystem() {
		return null;
	}

	@Override
	public String getCodeSystemName() {
		return this.codeSystemName;
	}

	@Override
	public String getCodeSystemVersion() {
		return null;
	}
	
}
